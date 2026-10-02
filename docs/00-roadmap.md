# Roadmap: da Java 5 a Java 27

Questa è la mappa del percorso. Ogni capitolo ha una pagina propria in `docs/`, con
codice eseguibile e confronto **PRIMA / DOPO**.

## Il percorso in 12 passi

```text
Java classico
     ↓  [1]  Generics (Java 5)                                    → 01
     ↓  [2]  Lambda e interfacce funzionali (Java 8)              → 02
     ↓  [3]  Stream API (Java 8)                                  → 03
     ↓  [4]  Optional (Java 8)                                    → 04
     ↓  [5]  var (Java 10)                                        → 05
     ↓  [6]  Record (Java 16)                                     → 06
     ↓  [7]  Pattern matching e gerarchie sealed (Java 16/17/21)   → 07
     ↓  [8]  Switch expression (Java 14)                          → 08
     ↓  [9]  Virtual Threads (Java 21)                            → 09
     ↓ [10]  Nuovo modello del main (Java 25)                     → 10
     ↓ [11]  Java 27: preview, incubatore e JVM                   → 11
     ↓ [12]  Mini progetto: tutto insieme                         → 12
     ↓      Glossario e riferimenti                                → 13
```

## Le versioni che contano

| Versione | Novità da ricordare | Capitolo |
|---|---|---|
| Java 5 (2004) | Generics, Enum, Annotations, Autoboxing, for-each | [01](01-generics.md) |
| Java 7 (2011) | try-with-resources, diamond operator, String in `switch` | [08](08-switch-expression.md) |
| Java 8 (2014) | Lambda, Stream, `Optional`, metodi di default, Date/Time API | [02](02-lambda.md), [03](03-stream.md), [04](04-optional.md) |
| Java 9 (2017) | Moduli, JShell, `List.of` | [03](03-stream.md) |
| Java 10 (2018) | `var` (inferenza del tipo locale) | [05](05-var.md) |
| Java 11 (2018) | HTTP Client standard, `var` nei parametri lambda | [03](03-stream.md) |
| Java 14 (2020) | Switch Expressions | [08](08-switch-expression.md) |
| Java 16 (2021) | Record, `instanceof` con pattern definitivo, `Stream.toList()` | [06](06-records.md), [07](07-pattern-matching.md) |
| Java 17 (2021) | Sealed Classes, pattern matching per `switch` (preview) | [07](07-pattern-matching.md) |
| Java 21 (2023) | Virtual Threads, pattern matching per `switch`, record pattern, `SequencedCollection` | [09](09-virtual-threads.md) |
| Java 25 (2025) | **LTS**: compact source file e `main` semplificato, module import declarations, API `IO`, flexible constructor bodies | [10](10-new-main.md) |
| Java 26 (2026) | non-LTS, di passaggio | — |
| Java 27 (2026) | non-LTS: G1 predefinito, compact object headers, TLS post-quantum, tre preview | [11](11-java-27.md) |

## Da notare

- **Java 25 è LTS**, Java 27 non lo è. La versione di riferimento del progetto è **Java 25**.
- Il progetto copre un percorso selezionato, **non tutte le novità** di ogni release.
  Le versioni citate nella tabella introducono anche altre funzionalità: quelle elencate
  qui sono solo quelle trattate nei capitoli.
- Tra Java 5 e Java 27 ci sono 11 release intermedie. Molte introducono novità importanti
  (moduli, HTTP client, record pattern, scoped values) che qui compaiono solo come contesto.

## Come usare questa documentazione

Due percorsi possibili:

1. **In ordine**, se parti da Java 8 e vuoi arrivare al Java moderno. È l'ordine consigliato:
   ogni capitolo presuppone solo quelli precedenti.
2. **A piacere**, se hai già presente una parte e ti interessa solo quella. Ogni pagina
   è autosufficiente e indica la versione Java che ha introdotto la funzionalità.

Ogni capitolo contiene:

- una riga con la **versione Java** che ha introdotto la funzionalità;
- il codice **PRIMA** (come si scriveva);
- il codice **DOPO** (come si scrive oggi);
- il percorso del sorgente che implementa l'esempio, per poterlo eseguire.

## I dieci concetti di Java moderno

Se dovesse restare una cosa sola, resterei qui:

| # | Concetto | Versione | Una riga |
|---|---|---|---|
| 1 | Generics | 5 | il tipo diventa parte della firma, non un cast |
| 2 | Lambda | 8 | si passa comportamento, non solo dati |
| 3 | Method reference | 8 | quando il corpo è già un metodo esistente |
| 4 | Stream API | 8 | si descrive la trasformazione invece del procedimento |
| 5 | `Optional` | 8 | l'assenza di un valore entra nel tipo |
| 6 | `var` | 10 | il tipo si deduce quando è ovvio |
| 7 | Record | 16 | i DTO immutabili si scrivono in tre righe |
| 8 | Pattern matching | 16 / 21 | il cast e la gerarchia si esprimono una volta sola |
| 9 | Switch expression | 14 | lo `switch` produce un valore |
| 10 | Virtual Threads | 21 | la concorrenza I/O-bound costa poco |

## Indice dei capitoli

- [01 — Generics](01-generics.md)
- [02 — Lambda](02-lambda.md)
- [03 — Stream API](03-stream.md)
- [04 — Optional](04-optional.md)
- [05 — `var`](05-var.md)
- [06 — Record](06-records.md)
- [07 — Pattern Matching](07-pattern-matching.md)
- [08 — Switch Expression](08-switch-expression.md)
- [09 — Virtual Threads](09-virtual-threads.md)
- [10 — Nuovo `main` (Java 25)](10-new-main.md)
- [11 — Java 27](11-java-27.md)
- [12 — Mini progetto finale](12-mini-progetto.md)
- [13 — Glossario e riferimenti](13-glossario.md)