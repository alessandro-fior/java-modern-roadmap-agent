# 13 — Glossario e riferimenti

## Glossario

**Agenzia di astrazione** — un'interfaccia con un solo metodo astratto (SAM). Java 8
consente di implementarla con una lambda. Con `@FunctionalInterface` il compilatore
verifica che ne resti esattamente uno.

**Arrow** — la sintassi `case X ->` introdotta con lo switch expression (Java 14). Al
contrario di `case X:` non ha bisogno di `break`, e rende lo switch un'espressione.

**Boxing / Unboxing** — la conversione automatica tra primitivo e wrapper
(`int` ↔ `Integer`). È implicita, ma ha un costo: per questo esistono `OptionalInt`,
`OptionalLong` e `OptionalDouble`.

**Campo statico finale** — una costante di classe. Siccome è `final`, il compilatore la
inlinea: non occupa memoria e non viene riletto a runtime.

**Cast** — la conversione esplicita di un tipo, `(String) obj`. Con i Generics è quasi
sempre inutile, e con i pattern matching è sparito.

**Chained** — si dice di una stream o di una catena di `Optional` quando più operazioni
sono collegate da un punto: `list.stream().filter(...).map(...).toList()`.

**Collector** — l'operazione terminale che trasforma uno stream in un risultato:
`Collectors.toList()`, `groupingBy`, `counting`, `joining`, `partitioningBy`.

**Compact source file** — un file `.java` con metodi e statement **non racchiusi in una
classe** (JEP 512, definitivo in Java 25). Non ha `package` né `class`.

**Contatore** — vedi `switch expression`.

**Default method** — un metodo con corpo in un'interfaccia (Java 8). Permette di aggiungere
comportamento a un'interfaccia senza rompere le implementazioni esistenti.

**Diamond `<>`** — la sintassi abbreviata per i parametri di tipo quando si possono
dedurre dal contesto: `new ArrayList<>()` al posto di `new ArrayList<String>()`.

**Effectively final** — una variabile locale che non viene mai riassegnata. Può essere
catturata da una lambda anche senza `final` esplicito. Se viene riassegnata, la lambda non
compila.

**Energizzazione** — vedi `boxing`.

**Ereditarietà dei record** — un record è `final`: non si può estendere. Si possono
però aggiungere metodi, e i record possono implementare interfacce.

**Espressione lambda** — un'implementazione anonima e compatta di un'interfaccia
funzionale: `(a, b) -> a + b`. Non è una scorciatoia di sintassi: è un modo di passare
comportamento.

**Guard** (`when`) — una condizione aggiuntiva su un caso dello switch con pattern. Se è
falsa si passa al caso successivo.

**Instance `main`** — un metodo `main` non `static`, che il launcher invoca su un'oggetto
creato per l'occasione (JEP 512, definitivo in Java 25).

**Inferenza di tipo** — la capacità del compilatore di dedurre un tipo. `var` ne chiede una
forma locale; i diamond, la lambda e il `var` nel pattern matching ne sfruttano altre.

**JEP** — *JDK Enhancement Proposal*, la proposta formale di una modifica alla piattaforma
Java. Passa attraverso preview prima di diventare definitiva.

**LTS** — *Long Term Support*. Una release con almeno cinque anni di aggiornamenti.
Java 21, 25 e le successive versioni `n` con `n % 2 == 1` sono LTS; Java 27 no.

**Method reference** — `Cliente::nome`, un riferimento a un metodo esistente usato dove
serve una lambda. Equivale a `c -> c.nome()`.

**Module import declaration** — `import module java.base;`, che importa tutte le classi
esportate da un modulo (JEP 511, definitivo in Java 25). Implicita nei compact source file.

**Non-sealed** — un sotto-tipo che riapre la gerarchia: altri tipi possono estenderlo. È
il contrario di `sealed` e di `final`.

**Optional** — un contenitore che può avere zero o un valore. Nato per i **tipi di
ritorno**: dentro un campo o un parametro è quasi sempre una scelta sbagliata.

**Pattern** — una forma che combina un test di tipo e, se il test passa, l'estrazione di
variabili. `instanceof String s`, `case Successo(String m)`.

**Platform thread** — un vero thread del sistema operativo, schedulato dall'OS. Costa circa
1 MB di stack. È ciò su cui vengono montati i virtual thread.

**Preview** — una feature non garantita, attivabile con `--enable-preview`. Cambia nelle
release successive e le classi compilate con preview girano solo sulla stessa versione.

**Record pattern** — la destrutturazione di un record in un pattern: i componenti diventano
variabili (JEP 441, definitivo in Java 21).

**Record** — una classe immutabile pensata per i dati, con campi, costruttore canonico,
accessor, `equals`, `hashCode` e `toString` generati (JEP 395, definitivo in Java 16).

**Sealed** — una classe o un'interfaccia che dichiara gli unici sotto-tipi ammessi
(JEP 409, definitivo in Java 17). È ciò che permette al compilatore di verificare che uno
switch su quella gerarchia sia completo.

**Stream** — una sequenza di operazioni su dati. Non memorizza nulla: le operazioni
intermedie sono lazy e senza un'operazione terminale non viene eseguito nulla.

**Type pattern** — un pattern che testa il tipo, come `case String s` o `obj instanceof
Integer n`.

**Unboxing** — vedi `boxing`.

**Varargs** — `String... args`, una sintassi comoda per un array come ultimo parametro. In
un `main` è la stessa cosa di `String[] args`.

**Virtual thread** — un thread gestito dalla JVM, montato su una platform thread solo mentre
esegue codice (JEP 444, definitivo in Java 21). Costa poco, quindi si possono creare
centinaia di migliaia.

**`yield`** — la parola chiave che restituisce il valore di un caso switch con blocco
(Java 13).

**`var`** — inferenza di tipo locale (JEP 286, definitivo in Java 10). Non è tipizzazione
dinamica: il tipo viene fissato al primo assegnamento.

---

## Cronologia essentials

| Versione | Data | Tipo | Le novità di questo percorso |
|---|---|---|---|
| Java 5 | 2004 | — | Generics, `enum`, `for-each`, autoboxing |
| Java 7 | 2011 | — | `switch` su `String`, try-with-resources |
| Java 8 | 2014 | LTS | Lambda, method reference, interfacce funzionali, Stream API, `Optional`, `default` methods |
| Java 9 | 2017 | — | `List.of`, module system, `var` in prova |
| Java 10 | 2018 | — | `var` |
| Java 11 | 2018 | LTS | `var` nei parametri, HTTP client, `String.strip` |
| Java 14 | 2020 | — | `switch` expression, `yield`, `record` in preview |
| Java 16 | 2021 | — | `record` definitivi, `instanceof` con pattern, `Stream.toList()` |
| Java 17 | 2021 | LTS | `sealed`, context record |
| Java 18 | 2022 | — | UTF-8 di default, `SimpleDateFormat` thread-safe |
| Java 19 | 2022 | — | Virtual thread in preview, pattern matching in preview |
| Java 21 | 2023 | LTS | `switch` con pattern, record pattern, virtual thread definitivi, pattern su `switch` |
| Java 25 | 2025 | LTS | Compact source file, `main` di istanza, `java.lang.IO`, import di modulo, flexible constructor bodies |
| Java 27 | 2026 | no LTS | G1 di default, TLS post-quantum, compact object headers, JFR redaction; preview su lazy constant, concorrenza strutturata, pattern su primitivi, PEM |

## Tabella riepilogativa dei JEP citati

| JEP | Titolo | Definitivo in |
|---|---|---|
| [286](https://openjdk.org/jeps/286) | Local Variable Type Inference | Java 10 |
| [361](https://openjdk.org/jeps/361) | Switch Expressions | Java 14 |
| [394](https://openjdk.org/jeps/394) | Pattern Matching for `instanceof` | Java 16 |
| [395](https://openjdk.org/jeps/395) | Records | Java 16 |
| [409](https://openjdk.org/jeps/409) | Sealed Classes | Java 17 |
| [440](https://openjdk.org/jeps/440) | Pattern Matching for `switch` | Java 21 |
| [441](https://openjdk.org/jeps/441) | Record Patterns | Java 21 |
| [444](https://openjdk.org/jeps/444) | Virtual Threads | Java 21 |
| [502](https://openjdk.org/jeps/502) | Stable Values (Preview) | preview in 25 |
| [505](https://openjdk.org/jeps/505) | Structured Concurrency (Fifth Preview) | preview in 25 |
| [506](https://openjdk.org/jeps/506) | Scoped Values | preview in 25 |
| [511](https://openjdk.org/jeps/511) | Module Import Declarations | Java 25 |
| [512](https://openjdk.org/jeps/512) | Compact Source Files and Instance Main Methods | Java 25 |
| [513](https://openjdk.org/jeps/513) | Flexible Constructor Bodies | Java 25 |
| [523](https://openjdk.org/jeps/523) | G1 Default Garbage Collector | Java 27 |
| [527](https://openjdk.org/jeps/527) | Post-Quantum Hybrid Key Exchange for TLS 1.3 | Java 27 |
| [531](https://openjdk.org/jeps/531) | Lazy Constants (Third Preview) | preview in 27 |
| [532](https://openjdk.org/jeps/532) | Primitive Types in Patterns (Fifth Preview) | preview in 27 |
| [533](https://openjdk.org/jeps/533) | Structured Concurrency (Seventh Preview) | preview in 27 |
| [534](https://openjdk.org/jeps/534) | Compact Object Headers by Default | Java 27 |
| [536](https://openjdk.org/jeps/536) | JFR In-Process Data Redaction | Java 27 |
| [537](https://openjdk.org/jeps/537) | Vector API (Twelfth Incubator) | incubatore in 27 |
| [538](https://openjdk.org/jeps/538) | PEM Encodings (Third Preview) | preview in 27 |

## Risorse ufficiali

- [Java SE 25 Documentation](https://docs.oracle.com/en/java/javase/25/docs/)
- [Java SE 27 Documentation](https://docs.oracle.com/en/java/javase/27/docs/)
- [JEP index completo](https://openjdk.org/jeps/0)
- [Java Language Specification (JLS)](https://docs.oracle.com/javase/specs/jls/se25/html/)
- [Java Virtual Machine Specification](https://docs.oracle.com/javase/specs/jvms/se25/html/)
- [Migration Guide (JDK 25)](https://docs.oracle.com/en/java/javase/25/migration/overview-jdk-25-support.html)
- [JDK 27 Release Notes](https://www.oracle.com/java/technologies/javase/25-relnote-issues.html)
- [OpenJDK](https://openjdk.org/)

## Libri consigliati

- *Effective Java* — Joshua Bloch. Il capitolo sugli `enum` e quello sui record valgono da
  soli il prezzo.
- *Java: A Beginner's Guide* — Herbie Schildt. Per chi arriva da zero.
- *Modern Java* — Josh Long, O'Reilly. Orientato a chi già lavora in Java.
- *The Elements of Java Style* — Joshua Bloch, Doug Lea. Sul perché oltre il come.

## Come usare questo repository

1. Clona e compila: `mvn clean test`.
2. Scegli un capitolo della [roadmap](00-roadmap.md).
3. Apri il sorgente collegato e **eseguilo**: `mvn -q exec:java -Dexec.mainClass=<classe>`.
4. Fai gli esercizi alla fine del capitolo prima di passare al successivo.
5. Confronta con la versione "PRIMA": ogni coppia di metodi `prima`/`dopo` produce lo
   stesso risultato, e i test lo verificano.

Tornare a [00 — Roadmap](00-roadmap.md) · Oppure leggi il [README](../README.md)
