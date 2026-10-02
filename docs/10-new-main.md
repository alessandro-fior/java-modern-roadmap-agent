# 10 — Il nuovo `main` (Java 25)

**JEP 512** — Compact Source Files and Instance Main Methods. **JEP 511** — Module
Import Declarations. **LTS**: Java 25, GA il 16 settembre 2025.

Sorgenti: `src/main/java/it/example/modernjava/newmain/`
Esempi: `examples/newmain/`

Questo è il capitolo più interessante del percorso, perché riguarda direttamente il primo
programma che chiunque scrive.

---

## Il vecchio main

```java
public class Main {

    public static void main(String[] args) {
        System.out.println("Ciao Java");
    }
}
```

Per decenni, ogni programma Java è iniziato con queste cinque cose obbligatorie:

- una classe pubblica;
- un metodo `public`;
- `static`;
- un parametro `String[] args`;
- `System.out.println`.

Nessuna delle tre è tecnicamente necessaria. Sono convenzioni del 1995: il launcher
doveva poter trovare il punto di ingresso assumendo il minimo possibile.

## Il nuovo modello

Con Java 25 il `main` può essere:

- **di istanza**, non `static`;
- **senza `public`**;
- **senza `String[] args`**.

```java
void main() {
    System.out.println("Ciao Java");
}
```

Oppure, nella forma più compatta possibile, un **compact source file**: un file con
metodi e campi **non racchiusi in una classe**.

```java
IO.println("Ciao Java 25");
```

Quel file è un programma completo. Non ha `package`, non ha `class`, non ha `import`.

## Come si esegue

### Compact source file

```bash
java examples/newmain/HelloWorld.java
```

Non serve compilare: il *source-code launcher* compila in memoria ed esegue. Il file deve
essere chiamato come la classe, e serve **JDK 25 o superiore**.

### `main` di istanza in una classe normale

```java
package it.example.modernjava.newmain;

public final class NuovoMain {

    void main() {
        IO.println("Ciao Java 25");
    }
}
```

```bash
java -cp target/classes it.example.modernjava.newmain.NuovoMain
```

Il launcher accetta un `main` di istanza: crea l'oggetto e invoca il metodo. In questo
progetto c'è anche [`NewMainLauncher.java`](../src/main/java/it/example/modernjava/newmain/NewMainLauncher.java),
per lanciare i demo da Maven o da un test.

## Cosa cambia davvero

### Le dodici forme di `main` ammesse

Da Java 25 sono ammesse dodici combinazioni: `static` o di istanza (2) × `public` o senza
modificatore (2) × `String[] args`, `String... args` o nessun parametro (3) = 12. Non serve
ricordarle: funzionano tutte e non c'è motivo di sceglierne una in particolare.

### Una classe non può avere due `main` con lo stesso parametro

```java
public final class Classe {
    void main() { }                      // di istanza, senza parametri
    static void main(String[] args) { }  // statico, con parametri: SI, sono firme diverse
}
```

Ma questo **non** compila:

```java
public final class Classe {
    void main(String[] args) { }
    static void main(String[] args) { }  // ❌ stessa firma, cambia solo static
}
```

Se ti serve un `main` di istanza con gli argomenti, mettilo in una classe separata — nel
progetto è [`MainConArgomenti.java`](../src/main/java/it/example/modernjava/newmain/MainConArgomenti.java).

### Il `main` di istanza ha accesso ai campi

```java
public final class NuovoMain {

    private final List<String> diario = List.of("Java 5", "Java 8", "Java 25");

    void main() {
        System.out.println(String.join(" -> ", diario));   // this è disponibile
    }
}
```

In un `main` statico il contesto è la classe; in un `main` di istanza è l'oggetto.

## Le altre novità di Java 25 che lo accompagnano

### `java.lang.IO` (JEP 512)

Cinque metodi statici per la console:

```java
IO.println();              // a capo
IO.println("testo");
IO.print("testo");         // senza a capo
IO.readln();               // legge una riga da System.in, null a fine input
IO.readln("Come ti chiami? ");   // prompt + lettura
```

Sta in `java.lang`, quindi è disponibile senza `import` in **qualsiasi** programma, non
solo nei compact source file. Funziona anche quando l'input non è una console reale,
per esempio in un test.

Attenzione: i metodi sono `static` ma **non** vengono importati automaticamente nei
compact source file: va scritto `IO.println(...)`.

```java
IO.print("Come ti chiami? ");
String nome = IO.readln();
```

### Module Import Declarations (JEP 511)

```java
import module java.base;    // importa tutte le classi di java.base
```

Nei compact source file è **implicito**: `List`, `Map`, `Optional`, `String` funzionano
senza alcun `import`.

### Flexible Constructor Bodies (JEP 513)

Prima di Java 25, un costruttore poteva solo assegnare campi e chiamare `super()`. Ora
può contenere codice arbitrario **prima** dell'inizializzazione dei campi:

```java
public Cliente(int id, String nome, int eta) {
    // qui si può calcolare e validare, PRIMA dell'inizializzazione dei campi
    if (id <= 0) {
        throw new IllegalArgumentException("id deve essere positivo");
    }
    this.id = id;
    this.nome = nome.strip();
    this.eta = eta;
}
```

È la sintesi dei due modi di scrivere i record:

```java
// PRIMA: costruttore "esplicito" con super(...) per primo
public Cliente(int id, String nome, int eta) {
    super(nome, eta);
    if (id <= 0) { throw ... }
    this.id = id;
}

// DOPO (record): costruttore compatto
public Cliente(int id, String nome, int eta) {
    if (id <= 0) { throw ... }
    nome = nome.strip();
    eta = eta >= 0 ? eta : 0;
}
```

### Altre feature di Java 25 (non trattate qui)

- **Scoped Values** (JEP 506, preview) — alternativa a `ThreadLocal`
- **Stable Values** (JEP 502, preview) — valore inizializzato una volta sola
- **Structured Concurrency** (JEP 505, preview)
- **Module Import Declarations** (JEP 511) — definitivo
- **UTF-8 come charset di default** per `java.io`/`java.util`

## Perché è una novità importante

Java era nato con l'idea che un programma fosse una lezione di programmazione orientata
a oggetti. Con JEP 512, un principiante può scrivere il primo programma senza imparare
prima:

- cosa sono le classi;
- cosa significa `static`;
- cosa sono gli array;
- cosa serve `String[]`.

E quando cresce, può aggiungere quei concetti uno alla volta, nello stesso file, senza
riscrivere il programma. La documentazione Oracle lo dice esplicitamente: i costrutti
"in the large" possono essere rimandati finché servono.

Per chi è esperto, il vantaggio è diverso: gli script e i programmi monouso diventano
scrivebili in cinque righe, senza il file di classe e il `public static void main`.

## Quando NON usarlo

- **Codice di produzione.** `public static void main(String[] args)` è la forma attesa
  dagli strumenti, dai launcher e dalle convenzioni. Non è deprecata, è solo verbosa.
- **Codice condiviso in team.** Un `void main()` non statico in una libreria può
  confondere chi legge.
- **Progetti con modularità.** I compact source file stanno nel package senza nome: non
  hanno accesso al module system.

## Esercizio

1. Scrivi `examples/newmain/HelloWorld.java` e lanciarlo con `java`.
2. Riscrivi il tuo vecchio `main` con `void main()` e senza argomenti. Cosa cambia nella
   lettura?
3. Aggiungi un campo di istanza al programma e stampalo dal `main`. Perché `this` esiste?
4. Con `IO.readln()`, scrivi un programma che chiede due numeri e stampa la somma.
   Gestisci l'input non numerico.
5. Cosa succede se un compact source file contiene un `main` che non è lanciabile
   (per esempio un metodo `private void main()`)? Perché?

## Prossimo capitolo

[11 — Java 27](11-java-27.md)