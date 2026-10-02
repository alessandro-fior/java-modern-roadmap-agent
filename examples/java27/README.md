# Esempi: Java 27

I file in questa cartella **non fanno parte della build Maven** e sono **isolati**,
per non imporre a chi clona il progetto una JDK 27 (che, ricordiamo, non è LTS) e per
non miscelare funzionalità definitive con funzionalità in preview.

> **Java 27 non è una release LTS.** Java 25 è LTS; 27 è una *feature release* con
> sei mesi di supporto. Per un progetto in produzione la scelta prevedibile resta Java 25.

## I nove JEP di Java 27

| JEP | Titolo | Stato |
|---|---|---|
| [523](https://openjdk.org/jeps/523) | Make G1 the Default Garbage Collector in All Environments | definitivo |
| [527](https://openjdk.org/jeps/527) | Post-Quantum Hybrid Key Exchange for TLS 1.3 | definitivo |
| [531](https://openjdk.org/jeps/531) | Lazy Constants (Third Preview) | **preview** |
| [532](https://openjdk.org/jeps/532) | Primitive Types in Patterns, instanceof, and switch (Fifth Preview) | **preview** |
| [533](https://openjdk.org/jeps/533) | Structured Concurrency (Seventh Preview) | **preview** |
| [534](https://openjdk.org/jeps/534) | Compact Object Headers by Default | definitivo |
| [536](https://openjdk.org/jeps/536) | JFR In-Process Data Redaction | definitivo |
| [537](https://openjdk.org/jeps/537) | Vector API (Twelfth Incubator) | **incubator** |
| [538](https://openjdk.org/jeps/538) | PEM Encodings of Cryptographic Objects (Third Preview) | **preview** |

È il set di JEP più piccolo dal JDK 20: cinque delle nove feature sono ancora in fase di
preview o incubazione.

## Requisiti

```bash
# JDK 27
java -version

# per gli esempi in preview
javac --release 27 --enable-preview Esempio.java
java  --enable-preview Esempio
```

Una API in preview richiede `--enable-preview` **sia in compilazione sia in esecuzione**,
e le classi compilate con preview girano **solo** su una JVM identica (stessa versione).

## Eseguire gli esempi

```bash
# JEP 531 - Lazy Constants (preview)
java --enable-preview examples/java27/LazyConstantsDemo.java

# JEP 533 - Structured Concurrency (preview)
java --enable-preview examples/java27/StructuredConcurrencyDemo.java

# JEP 532 - Pattern su tipi primitivi (preview)
java --enable-preview examples/java27/PrimitivePatternsDemo.java

# JEP 527 - TLS ibrido post-quantum (definitivo, nessuna preview)
java examples/java27/TlsPostQuantumDemo.java

# JEP 523 - G1 come collector predefinito (definitivo)
java -Xlog:gc examples/java27/GcDefaultsDemo.java

# JEP 534 / 536 - Compact Object Headers e JFR redaction (definitivo)
java examples/java27/ObjectHeadersDemo.java
```

## I file

| File | JEP | Preview? |
|---|---|---|
| `LazyConstantsDemo.java` | [531](https://openjdk.org/jeps/531) Lazy Constants | sì |
| `StructuredConcurrencyDemo.java` | [533](https://openjdk.org/jeps/533) Structured Concurrency | sì |
| `PrimitivePatternsDemo.java` | [532](https://openjdk.org/jeps/532) Pattern su primitivi | sì |
| `TlsPostQuantumDemo.java` | [527](https://openjdk.org/jeps/527) TLS post-quantum | no |
| `GcDefaultsDemo.java` | [523](https://openjdk.org/jeps/523) G1 predefinito | no |
| `ObjectHeadersDemo.java` | [534](https://openjdk.org/jeps/534) / [536](https://openjdk.org/jeps/536) | no |

## Cosa vale la pena sapere

### JEP 527 — TLS ibrido post-quantum

La negoziazione chiave ibrida (post-quantum + classica) è **attiva per default**: non
serve toccare il codice. Il client e il server concordano un segreto ibrido, quindi la
connessione resta protetta anche se una delle due parti viene intercettata in futuro.

Per disattivarla:

```bash
java -Djdk.tls.namedGroups=X25519:secp256r1 ...
```

Nota operativa: le chiavi post-quantum ML-KEM generate con JDK 27 sono codificate in
PKCS#8 con il formato `SEED` per default e **non** sono riconosciute dalle versioni
precedenti. Se passi chiavi tra versioni diverse, imposta
`jdk.mlkem.pkcs8.encoding=EXPAND_AND_ROUND` o `jdk.mlkem.pkcs8.encoding=EXPANDED`.

### JEP 523 — G1 predefinito

In Java 27 G1 è il collector predefinito **in tutti gli ambienti**, anche su macchine
con un solo processore o con memoria molto ampia (prima serviva `-XX:+UseG1GC`).
Non è un cambiamento di codice: è un cambio di default. Se misuri le prestazioni,
ricontrolla: i benchmark vanno rifatti.

### JEP 534 — Compact Object Headers

L'intestazione degli oggetti scende a 8 byte. Con milioni di oggetti vivi il risparmio
di memoria è reale, e di solito si accompagna a un miglioramento della cache locality.

### JEP 533 — Structured Concurrency

Settima preview. L'API è cambiata nel tempo: in JDK 27 `StructuredTaskScope` ha **tre**
parametri di tipo, i costruttori pubblici sono stati sostituiti da metodi factory statici,
`awaitAll()` è stato rimosso e `onTimeout()` è diventato `timeout()`. Non scrivere codice
di produzione su un'API in preview: le firme cambiano a ogni release.

### JEP 532 — Pattern su tipi primitivi

Quinta preview. Estende `instanceof` e `switch` a `int`, `long`, `double`, `boolean`
e ai loro wrapper.

## Documentazione

- [`docs/11-java-27.md`](../../docs/11-java-27.md) — riepilogo commentato di Java 27
- [Elenco completo dei JEP](https://openjdk.org/projects/jdk/27/)
- [Java SE Support Roadmap di Oracle](https://www.oracle.com/java/technologies/java-se-support-roadmap.html)