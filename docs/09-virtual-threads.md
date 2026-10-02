# 09 — Virtual Threads

** definitivi da Java 21** (JEP 444). Prima in preview da Java 19 (JEP 425).

Sorgenti: `src/main/java/it/example/modernjava/virtualthreads/`
Test: `src/test/java/it/example/modernjava/virtualthreads/VirtualThreadsBasicsTest.java`

---

## Il problema che risolvono

Un thread del sistema operativo costa, mediamente, **circa 1 MB di memoria di stack**,
e viene schedulato dall'OS. Su una macchina con 8 GB si possono creare al massimo qualche
migliaia di thread prima di esaurire la memoria.

Il modello "un thread per richiesta" è quindi scomodo: se un servizio riceve 10.000
richieste al secondo e ogni richiesta passa 200 ms di attesa su una chiamata HTTP, servono
2.000 thread contemporanei per non accodare. È tanto.

## La soluzione

Un **Virtual Thread** è un thread gestito dalla JVM, non dal sistema operativo. Viene
montato su una **platform thread** (un normale thread dell'OS) solo mentre sta davvero
eseguendo codice, e smontato quando si blocca.

```text
10.000 virtual thread          <- economici, qualche KB ciascuno
        ↓ montati su
20 platform thread             <- i veri thread dell'OS
        ↓ in esecuzione su
N core                         <- il limite reale della CPU
```

Creare un milione di virtual thread costa qualche megabyte. Creare un milione di thread
del sistema operativo è impossibile.

## Il caso d'uso: I/O-bound

| Tipo | Definizione | Limite reale | I virtual thread aiutano? |
|---|---|---|---|
| **I/O-bound** | passa il tempo ad aspettare (rete, DB, file) | il tempo di attesa | **sì, moltissimo** |
| **CPU-bound** | calcola sempre, senza mai bloccarsi | la CPU | **no** |

Un programma CPU-bound con 10.000 virtual thread non diventa più veloce: ci sono 8 core,
e il codice gira a 8 thread reali. La differenza è che non ti blocchi, ma il throughput
resta quello.

**Quindi: i Virtual Thread non sono automaticamente più veloci.** Sono più economici in
concorrenza. Su un carico I/O-bound, dove il tempo si spende ad aspettare, il salto è
reale e misurabile.

## Le tre forme

### 1. Il caso più semplice

```java
Thread.startVirtualThread(() -> {
    System.out.println("Sto lavorando in un Virtual Thread");
});
```

Equivalente moderno:

```java
Thread.ofVirtual().start(() -> { ... });
```

### 2. L'executor per task

```java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 10; i++) {
        int id = i;
        executor.submit(() -> System.out.println("Task " + id));
    }
}   // qui close() attende la fine di tutti i task
```

`newVirtualThreadPerTaskExecutor()` crea **un virtual thread per task**, non un pool.
Questa è la differenza rispetto a `newFixedThreadPool(n)`, dove i task si accodano su
`n` thread.

`try-with-resources` funziona perché `ExecutorService` implementa `AutoCloseable` da
Java 19. `close()` fa `shutdown()` e attende il termine.

### 3. Confrontare con un pool tradizionale

```java
// pool di 5 platform thread: 1.000 task, 5 alla volta
ExecutorService pool = Executors.newFixedThreadPool(5);

// 1.000 virtual thread: tutti insieme
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 1_000; i++) {
        executor.submit(() -> attesaDiIo());
    }
}
```

Sorgente: [`VirtualThreadsBasics.java`](../src/main/java/it/example/modernjava/virtualthreads/VirtualThreadsBasics.java)

## Cosa cambia per il codice

Quasi niente, e questo è il punto. Il codice resta uguale: è lo stesso `Runnable`, la
stessa `Callable`, lo stesso `ExecutorService`. Cambia solo quanto costa.

Le regole pratiche:

1. **Non usare `synchronized` sui blocchi lunghi.** Un blocco `synchronized` può bloccare
   una platform thread, e con i virtual thread blocchi il carrier.
2. **Attenzione a `ThreadLocal`.** Con milioni di virtual thread, milioni di `ThreadLocal`
   sono un problema di memoria. Valuta `ScopedValue` (Java 25, preview).
3. **Il `pool sizing` non serve più.** Con i virtual thread non c'è un numero ottimale di
   thread da dimensionare: se serve, si usa `Executors.newVirtualThreadPerTaskExecutor()`.
4. **Non fare I/O bloccante senza motivo in codice CPU-bound.** Se l'attesa è breve, il
   costo di montare e smontare il carrier non si ripaga.

## Cosa dicono i numeri

Con 1.000 task che simulano 20 ms di attesa I/O:

```text
newFixedThreadPool(5)                 -> ~4.000 ms   (5 alla volta)
newVirtualThreadPerTaskExecutor()     -> ~30 ms      (tutti insieme)
```

Il primo è lento perché il tempo di attesa si somma in serie. Il secondo è veloce perché
i task sono davvero paralleli: sono in attesa, nonoccupano CPU.

Se invece i task **calcolassero** per 20 ms, i due risulterebbero equivalenti: 8 core,
20 secondi di lavoro, e la differenza è marginale.

## Concorrenza strutturata

I Virtual Threads sono l'infrastruttura; manca il modo per **coord**nare gruppi di task
in modo sicuro. Questo è il compito della concorrenza strutturata, che in Java 27 è
ancora in preview (JEP 533) — vedi
[`examples/java27/StructuredConcurrencyDemo.java`](../examples/java27/StructuredConcurrencyDemo.java).

L'idea:

```java
try (var scope = StructuredTaskScope.open()) {
    var utente = scope.fork(() -> findUser());
    var ordine = scope.fork(() -> fetchOrder());

    scope.join();   // se uno fallisce, l'altro viene cancellato

    return new Response(utente.get(), ordine.get());
}   // qui tutti i thread sono già terminati: nessun thread leak
```

Con `ExecutorService` devi gestire a mano cancellazioni e propagazione degli errori, e
i thread "sfuggono" quando un task fallisce. Non usare l'API in preview in produzione:
l'API è cambiata diverse volte (JDK 21, 22, 23, 24, 25, 26, 27).

## Quando NON usarli

- Se il programma è CPU-bound, non guadagni niente.
- Se usi già un pool ben dimensionato e non hai problemi di memoria, il salto è minimo.
- Se sei in un framework che gestisce il thread pool (Spring MVC, per esempio), i
  virtual thread vanno abilitati lì.

## Esercizio

1. Scrivi un programma che simula 500 chiamate HTTP con `Thread.sleep(50)` e confronta
   `newFixedThreadPool(10)` con `newVirtualThreadPerTaskExecutor()`.
2. Cambia `Thread.sleep` con un calcolo pesante e osserva come i due tempi convergono.
   Perché?
3. Crea 100.000 virtual thread che fanno solo `Thread.sleep(1)` e misura la memoria
   con `Runtime.getRuntime()`.

## Prossimo capitolo

[10 — Nuovo `main` (Java 25)](10-new-main.md)