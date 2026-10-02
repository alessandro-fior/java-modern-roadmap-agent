# Esempi: compact source file (Java 25)

I file in questa cartella **non fanno parte della build Maven**. Sono
*compact source file*, introdotti da **Java 25** (JEP 512): file sorgente con metodi e
campi che **non** sono racchiusi in una dichiarazione di classe.

Il compilatore genera implicitamente una classe `final` nel **package senza nome**,
con i campi e i metodi scritti nel file come membri. In pratica:

- niente `package`
- niente `public class ... { }`
- niente `import` (tutte le classi di `java.base` sono importate automaticamente)
- serve un `main` lanciabile: `void main()`, `void main(String[] args)`,
  `static void main(String[] args)`, con o senza `public`

## Requisiti

**JDK 25 o superiore.** Con una JDK precedente questi file non compilano.

```bash
java -version   # deve essere 25 o successiva
```

## Come si eseguono

Non serve compilare: il *source-code launcher* della JDK compila in memoria e esegue.

```bash
# dal root del progetto
java examples/newmain/HelloWorld.java
java examples/newmain/Variabili.java
java examples/newmain/GenericsEStream.java
java examples/newmain/RecordEsempio.java
java examples/newmain/PatternMatchingEsempio.java
```

Con gli argomenti:

```bash
java examples/newmain/ConArgomenti.java Mario 42 Anna
```

Con input dall'utente:

```bash
echo "Mario" | java examples/newmain/LettoreInput.java
```

Oppure da un terminale interattivo, digitando le risposte quando vengono richieste:

```bash
java examples/newmain/LettoreInput.java
```

## I file

| File | Cosa mostra |
|---|---|
| `HelloWorld.java` | il minimo assoluto: 3 righe, nessuna classe |
| `Variabili.java` | campi della classe implicita, metodi, `var`, lambda |
| `GenericsEStream.java` | generics + stream + `Optional` senza alcun `import` |
| `RecordEsempio.java` | record e switch expression dichiarati nel file stesso |
| `PatternMatchingEsempio.java` | `instanceof` con pattern, switch su gerarchia sealed, record pattern |
| `ConArgomenti.java` | `main` di istanza **con** `String[] args` |
| `LettoreInput.java` | `java.lang.IO`: `IO.print`, `IO.println`, `IO.readln` |

## Attenzione

- La classe implicita non ha un nome utilizzabile nel codice: **non si può fare `new`**,
  perché il nome è un dettaglio interno del compilatore. Si usa `this`.
- La classe implicita non può estendere un'altra classe e non può implementare interfacce.
- Le classi dichiarate in un compact source file diventano classi **annidate**
  della classe implicita.
- I compact source file stanno nel package senza nome: **non possono importare classi
  del progetto** in modo semplice, quindi questi esempi sono autopoportanti.
- Per passare a un programma normale basta racchiudere il contenuto in
  `class ... { ... }`, aggiungere il package e gli import necessari.

## Documentazione

- [`docs/10-new-main.md`](../../docs/10-new-main.md) — il modello semplificato del `main`
- [JEP 512](https://openjdk.org/jeps/512) — Compact Source Files and Instance Main Methods
- [JEP 511](https://openjdk.org/jeps/511) — Module Import Declarations