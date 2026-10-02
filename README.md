# Java Modern Roadmap

**Come si è evoluto il modo di scrivere Java, da Java 5 a Java 27 — con esempi eseguibili
"prima / dopo".**

Ogni capitolo mette fianco a fianco il codice che scrivevi prima e quello che scrivi oggi,
e ogni coppia di metodi `prima` / `dopo` è verificata da un test che dimostra che
producono lo stesso risultato.

Java puro, nessun framework applicativo, nessuna dipendenza runtime.

---

## Perché questo repository

Il linguaggio Java è cambiato parecchio dal 2014, e quasi tutto è arrivato in silenzio:
una sintassi più breve, un tipo che descrive l'assenza, un `switch` che produce un valore,
thread che costano pochi byte.

Il rischio è imparare le novità **a pezzi**, senza capire perché sono state introdotte e
come si combinano. Qui l'ordine è quello consigliato da Oracle, e ogni capitolo si chiude
con degli esercizi.

- Versione di riferimento: **Java 25 (LTS)**
- Copertura: da Java 5 a Java 27, con attenzione alle release che hanno cambiato il modo di
  scrivere il codice
- Le API in preview di Java 27 sono isolate e non fanno parte della build

## Requisiti

| | |
|---|---|
| **JDK 25** | obbligatorio per compilare ed eseguire gli esempi principali |
| **Maven 3.9+** | per la build e i test |
| **JDK 27** | facoltativo, serve solo per `examples/java27/` |

Verifica la versione attiva:

```bash
java -version
mvn -version
```

## Avvio rapido

```bash
git clone https://github.com/alessandro-fior/java-modern-roadmap-agent.git
cd java-modern-roadmap-agent
mvn clean test
```

Poi esegui un esempio qualsiasi:

```bash
mvn -q compile exec:java -Dexec.mainClass=it.example.modernjava.customeranalyzer.CustomerAnalyzer
```

Oppure, senza Maven:

```bash
mvn -q compile
java -cp target/classes it.example.modernjava.customeranalyzer.CustomerAnalyzer
```

## Struttura

```text
java-modern-roadmap-agent/
├── docs/                              percorso in 14 pagine
├── examples/
│   ├── newmain/                       compact source file, Java 25
│   └── java27/                        demo JDK 27 (alcune in preview)
├── src/main/java/it/example/modernjava/
│   ├── generics/                      Java 5
│   ├── lambda/                        Java 8
│   ├── streams/                       Java 8
│   ├── optional/                      Java 8
│   ├── vardecl/                       Java 10
│   ├── records/                       Java 16
│   ├── patternmatching/               Java 16, 17, 21
│   ├── switchexpression/              Java 14
│   ├── virtualthreads/                Java 21
│   ├── newmain/                       Java 25
│   └── customeranalyzer/              mini progetto finale
├── src/test/java/                     test JUnit 5
├── pom.xml
└── LICENSE                            MIT
```

## Il percorso

| Capitolo | Argomento | Versione |
|---|---|---|
| [00](docs/00-roadmap.md) | Roadmap | — |
| [01](docs/01-generics.md) | Generics | Java 5 |
| [02](docs/02-lambda.md) | Lambda e interfacce funzionali | Java 8 |
| [03](docs/03-stream.md) | Stream API | Java 8 |
| [04](docs/04-optional.md) | Optional | Java 8 |
| [05](docs/05-var.md) | `var` | Java 10 |
| [06](docs/06-records.md) | Record | Java 16 |
| [07](docs/07-pattern-matching.md) | Pattern matching e `sealed` | Java 16 / 17 / 21 |
| [08](docs/08-switch-expression.md) | Switch expression | Java 14 |
| [09](docs/09-virtual-threads.md) | Virtual Threads | Java 21 |
| [10](docs/10-new-main.md) | Il nuovo `main` | Java 25 |
| [11](docs/11-java-27.md) | Java 27 | Java 27 |
| [12](docs/12-mini-progetto.md) | Mini progetto finale | — |
| [13](docs/13-glossario.md) | Glossario e riferimenti | — |

### In una riga ciascuno

1. **Generics (5)** — il tipo diventa parte della firma, e il cast sparisce.
2. **Lambda (8)** — si passa comportamento, non solo dati.
3. **Stream (8)** — si descrive la trasformazione invece del procedimento.
4. **Optional (8)** — l'assenza di un valore entra dentro il tipo.
5. **`var` (10)** — il tipo si deduce quando è ovvio.
6. **Record (16)** — i DTO immutabili si scrivono in tre righe.
7. **Pattern matching (16/21)** — il cast e la gerarchia si esprimono una volta sola.
8. **Switch expression (14)** — lo `switch` produce un valore.
9. **Virtual Threads (21)** — la concorrenza I/O-bound costa pochi byte.
10. **Nuovo `main` (25)** — un programma Java può stare in cinque righe.

## Esegui ogni esempio

Le classi hanno tutte un `main`. Nessuna richiede configurazione:

```bash
mvn -q compile

# Java 5 — generics e wildcard
java -cp target/classes it.example.modernjava.generics.RawTypeVsGeneric
java -cp target/classes it.example.modernjava.generics.Contenitore
java -cp target/classes it.example.modernjava.generics.Wildcards

# Java 8 — lambda e stream
java -cp target/classes it.example.modernjava.lambda.LambdaBasics
java -cp target/classes it.example.modernjava.lambda.FunctionalInterfaces
java -cp target/classes it.example.modernjava.streams.StreamBasics
java -cp target/classes it.example.modernjava.streams.ClientiStream
java -cp target/classes it.example.modernjava.optional.OptionalBasics
java -cp target/classes it.example.modernjava.optional.OptionalAntiPattern

# Java 10 — var
java -cp target/classes it.example.modernjava.vardecl.VarBasics

# Java 14, 16, 17, 21 — record, pattern matching, switch, virtual thread
java -cp target/classes it.example.modernjava.records.RecordsUsage
java -cp target/classes it.example.modernjava.patternmatching.InstanceofPattern
java -cp target/classes it.example.modernjava.patternmatching.SealedHierarchy
java -cp target/classes it.example.modernjava.switchexpression.SwitchExpressions
java -cp target/classes it.example.modernjava.virtualthreads.VirtualThreadsBasics

# Java 25 — il nuovo main
java -cp target/classes it.example.modernjava.newmain.NewMainLauncher

# Mini progetto
java -cp target/classes it.example.modernjava.customeranalyzer.CustomerAnalyzer
```

### Compact source file (Java 25)

Questi file non hanno né classe né package: si eseguono direttamente.

```bash
java examples/newmain/HelloWorld.java
java examples/newmain/ConArgomenti.java Mario 42 Anna
echo "Mario" | java examples/newmain/LettoreInput.java
java examples/newmain/GenericsEStream.java
java examples/newmain/RecordEsempio.java
java examples/newmain/PatternMatchingEsempio.java
java examples/newmain/Variabili.java
```

Dettagli in [`examples/newmain/README.md`](examples/newmain/README.md).

### Demo Java 27 (richiedono JDK 27)

```bash
java examples/java27/GcDefaultsDemo.java
java examples/java27/TlsPostQuantumDemo.java
java examples/java27/ObjectHeadersDemo.java

# API in preview: serve --enable-preview
java --enable-preview examples/java27/LazyConstantsDemo.java
java --enable-preview examples/java27/StructuredConcurrencyDemo.java
java --enable-preview examples/java27/PrimitivePatternsDemo.java
```

Le API in preview cambiano fra le release e le classi compilate girano **solo** sulla
stessa versione. Dettagli in [`examples/java27/README.md`](examples/java27/README.md).

## Test

```bash
mvn test
```

| Suite | Copre |
|---|---|
| `GenericsTest` | type safety, wildcard, `max` generico |
| `ClientiStreamTest` | equivalenza ciclo / stream, `groupingBy`, `partizione` |
| `OptionalBasicsTest` | `map`, `filter`, `orElse*`, assenza di `null` |
| `RecordsUsageTest` | accessor, `equals` per valore, costruttore compatto |
| `PatternMatchingTest` | `instanceof` con pattern, `switch` su `sealed`, record pattern |
| `SwitchExpressionsTest` | equivalenza switch classico / expression, `yield`, enum |
| `VirtualThreadsBasicsTest` | `startVirtualThread`, `ofVirtual`, pool vs virtual thread |
| `NuovoMainTest` | `main` di istanza, `java.lang.IO`, record annidato |
| `CustomerAnalyzerTest` | l'intero mini progetto, capitolo per capitolo |

## Note su Java 25 e Java 27

- **Java 25 è LTS**, Java 27 no. Su una base di produzione prevedibile resta Java 25.
- G1 diventa il collector predefinito in **tutti** gli ambienti in Java 27: se hai
  benchmark storici, rifalli.
- La negoziazione chiave ibrida post-quantum per TLS 1.3 è attiva per default in Java 27.
- Le intestazioni degli oggetti scendono a 8 byte in Java 27.
- Le chiavi ML-KEM in PKCS#8 usano il formato `SEED` per default: per interoperabilità con
  versioni diverse usa `-Djdk.mlkem.pkcs8.encoding=EXPAND_AND_ROUND`.

## Licenza

MIT — vedi [LICENSE](LICENSE).
