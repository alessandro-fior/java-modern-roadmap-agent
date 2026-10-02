# 03 — Stream API

**Introdotta in Java 8** (JSR 335). `Stream.toList()` è definitivo da **Java 16**.

Sorgenti: `src/main/java/it/example/modernjava/streams/`
Test: `src/test/java/it/example/modernjava/streams/ClientiStreamTest.java`

---

## PRIMA / DOPO

```java
var numeri = List.of(10, 20, 30, 40, 50);

// PRIMA
List<Integer> risultato = new ArrayList<>();
for (Integer n : numeri) {
    if (n > 25) {
        risultato.add(n);
    }
}

// DOPO
var risultato = numeri.stream()
        .filter(n -> n > 25)
        .toList();
```

La differenza non è la lunghezza. È **che cosa dice il codice**. Il primo dice
"per ogni elemento, se è maggiore di 25, aggiungilo". Il secondo dice "filtra quelli
maggiori di 25". Il primo descrive un procedimento, il secondo una trasformazione.

## Cosa è uno stream

Uno stream **non è una lista e non memorizza nulla**. È una pipeline:

```text
sorgente  →  operazioni intermedie (lazy)  →  operazione terminale
List         filter, map, sorted, ...       count, toList, forEach, reduce
```

Regole:

- le operazioni intermedie restituiscono un altro stream e **non eseguono nulla**;
- senza operazione terminale, non viene eseguito **niente**;
- uno stream si consuma **una volta sola**;
- le operazioni intermedie non modificano la sorgente.

```java
var stream = numeri.stream().filter(n -> n > 25);   // ancora nulla
System.out.println(stream);                        // stream esaurito -> stampa l'oggetto Stream
stream.forEach(...);                                // UnsupportedStateException
```

## Le operazioni principali

### filter — selezionare

```java
numeri.stream().filter(n -> n > 25).toList();              // [30, 40, 50]
clienti.stream().filter(Cliente::maggiorenne).toList();    // method reference
```

### map — trasformare

```java
numeri.stream().map(n -> "n." + n).toList();
clienti.stream().map(Cliente::nome).toList();
numeri.stream().mapToInt(Integer::intValue).toList();      // evita il boxing
```

### sorted — ordinare

```java
numeri.stream().sorted().toList();                                   // naturale
numeri.stream().sorted(Comparator.reverseOrder()).toList();           // decrescente
clienti.stream().sorted(Comparator.comparing(Cliente::nome)).toList();
```

Più criteri con `thenComparing`:

```java
clienti.stream()
        .sorted(Comparator.comparingInt(Cliente::eta).reversed()
                              .thenComparing(Cliente::nome))
        .toList();
```

### distinct — eliminare i duplicati

```java
List.of(3, 1, 3, 2, 1, 3).stream().distinct().toList();   // [3, 1, 2]
```

L'ordine è quello del primo incontro, non quello naturale.

### limit / skip

```java
numeri.stream().limit(3).toList();    // [10, 20, 30]
numeri.stream().skip(3).toList();     // [40, 50]
```

### count e reduce

```java
numeri.stream().filter(n -> n >= 30).count();      // 3
numeri.stream().mapToInt(Cliente::eta).sum();      // su numeri: 310
numeri.stream().map(Cliente::nome).reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);
```

### toList

```java
var numeri = stream.toList();
```

`Stream.toList()` è definitivo da **Java 16**. Prima si usava
`Collectors.toList()`. La differenza che conta:

```java
stream.collect(Collectors.toList());   // lista modificabile (o non garantito)
stream.toList();                       // lista SEMPRE immutabile
```

```java
var lista = numeri.stream().toList();
lista.add(999);   // UnsupportedOperationException
```

### findFirst e findAny

```java
clienti.stream().filter(Cliente::maggiorenne).findFirst();   // Optional<Cliente>
clienti.stream().filter(c -> c.id() == 99).findFirst();      // Optional.empty()
```

Restituiscono `Optional`: è il modo naturale in cui la Stream API comunica "può non
esserci niente".

## L'esempio centrale

Questo è il pezzo di codice che riassume il progetto:

```java
var clienti = List.of(
        new Cliente(1, "Mario", 25, "Milano"),
        new Cliente(2, "Luigi", 17, "Roma"),
        new Cliente(3, "Anna", 32, "Milano"),
        new Cliente(4, "Paolo", 45, "Napoli"),
        new Cliente(5, "Laura", 16, "Roma")
);

var maggiorenni = clienti.stream()
        .filter(c -> c.eta() >= 18)
        .sorted(Comparator.comparing(Cliente::nome))
        .toList();

maggiorenni.forEach(System.out::println);
```

In cinque righe: record, generics, lambda, stream, method reference, `var`, e una
conoscenza del dominio (cosa sia "maggiorenne").

Sorgente: [`ClientiStream.java`](../src/main/java/it/example/modernjava/streams/ClientiStream.java)

## Raggruppare e indicizzare

Per il raggruppamento `Collectors` resta lo strumento giusto:

```java
Map<String, List<Cliente>> perCitta =
        clienti.stream().collect(Collectors.groupingBy(Cliente::citta));

Map<String, Long> numeroPerCitta =
        clienti.stream().collect(Collectors.groupingBy(Cliente::citta, Collectors.counting()));

Map<Boolean, List<Cliente>> partizione =
        clienti.stream().collect(Collectors.partitioningBy(Cliente::maggiorenne));
```

`toMap` per costruire mappe:

```java
Map<Integer, String> perId = clienti.stream()
        .collect(Collectors.toMap(Cliente::id, Cliente::nome));
```

Attenzione: `toMap` lancia `IllegalStateException` se due chiavi collidono.
Se serve tenere l'ultima, c'è la variante con la funzione di merge:

```java
Collectors.toMap(Cliente::id, Cliente::nome, (a, b) -> b)
```

## Funzioni di gruppo: la generalizzazione che ti risparmia metodi

Osserva il pattern: `nomi()`, `ids()`, `eta()`, `citta()` — un metodo per ogni accessor.
Non serve. Basta passare la funzione:

```java
public static <T> List<T> estrai(Function<Cliente, T> estrazione) {
    return CLIENTI.stream().map(espressione -> estrazione.apply(espressione)).toList();
}

estrai(Cliente::nome);   // ["Mario", "Luigi", "Anna", "Paolo", "Laura"]
estrai(Cliente::id);     // [1, 2, 3, 4, 5]
estrai(c -> c.eta() * 2);// [50, 34, 64, 90, 32]
```

Questo è il punto in cui gli stream passano da curiosità a strumento.

## Stream sequenziali e paralleli

```java
clienti.stream()             // sequenziale
clienti.parallelStream()     // parallelo, su un ForkJoinPool comune
```

**Non trasformare tutto in parallelo.** I costi di coordinamento sono reali e per una lista
di cinque elementi il parallelo è più lento. Il parallelo ha senso solo con molti dati e
operazioni CPU-intensive, e solo se si misura.

Una cosa da sapere: `parallelStream()` usa il pool comune della JVM, condiviso da tutto
il processo. Se ti serve un pool dedicato, usa `submit()` su un `ForkJoinPool` dedicato.

## Quando NON usare uno stream

Gli stream sono meno leggibili in certi casi:

```java
// Meglio un ciclo: c'è una condizione complicata e un effetto collaterale
for (Cliente c : clienti) {
    if (c.eta() >= 18 && !c.isBlacklisted() && c.citta().equals("Milano")) {
        c.assegnaBonus(1000);
    }
}
```

Regola pratica: se per capire cosa fa il codice devi tenere dritte tre variabili mentali,
probabilmente un `if` è più chiaro.

```java
// Meglio un ciclo: si modifica la lista mentre la si scorre
for (int i = 0; i < lista.size(); i++) {
    lista.add(lista.get(i) * 2);      // ConcurrentModificationException con uno stream
}
```

## Esercizio

1. Riscrivi `sum` e `average` con `mapToInt` e `mapToDouble`, e confronta con
   `reduce`.
2. Data `List<List<Integer>>`, appiattisci tutto in una lista ordinata usando
   `flatMap`.
3. Trova il cliente con la spesa massima usando `max(Comparator.comparingInt(...))`,
   poi rifallo con `reduce`.
4. Quando `filter` su uno stream parallelo diventa un collo di bottiglia?

## Prossimo capitolo

[04 — Optional](04-optional.md)