# Documentazione

Il percorso completo, da Java 5 a Java 27. Ogni capitolo ha codice eseguibile e un
confronto **PRIMA / DOPO**.

Parti dalla [roadmap](00-roadmap.md).

## Indice

| # | Capitolo | Versione | Sorgenti |
|---|---|---|---|
| 00 | [Roadmap](00-roadmap.md) | — | — |
| 01 | [Generics](01-generics.md) | Java 5 | `generics/` |
| 02 | [Lambda e interfacce funzionali](02-lambda.md) | Java 8 | `lambda/` |
| 03 | [Stream API](03-stream.md) | Java 8 | `streams/` |
| 04 | [Optional](04-optional.md) | Java 8 | `optional/` |
| 05 | [`var`](05-var.md) | Java 10 | `vardecl/` |
| 06 | [Record](06-records.md) | Java 16 | `records/` |
| 07 | [Pattern Matching e `sealed`](07-pattern-matching.md) | Java 16 / 17 / 21 | `patternmatching/` |
| 08 | [Switch Expression](08-switch-expression.md) | Java 14 | `switchexpression/` |
| 09 | [Virtual Threads](09-virtual-threads.md) | Java 21 | `virtualthreads/` |
| 10 | [Nuovo `main` (Java 25)](10-new-main.md) | Java 25 | `newmain/`, `examples/newmain/` |
| 11 | [Java 27](11-java-27.md) | Java 27 | `examples/java27/` |
| 12 | [Mini progetto finale](12-mini-progetto.md) | — | `customeranalyzer/` |
| 13 | [Glossario e riferimenti](13-glossario.md) | — | — |

Tutti i percorsi `generics/`, `lambda/` e così via sono relativi a
`src/main/java/it/example/modernjava/`.

## Come studiare

1. Leggi il capitolo.
2. Apri il sorgente indicato in testa al capitolo.
3. Eseguilo: `mvn -q exec:java -Dexec.mainClass=it.example.modernjava.<package>.<Classe>`
4. Fai gli esercizi in fondo al capitolo.
5. Passa al successivo.

Ogni coppia di metodi `prima` / `dopo` produce lo stesso risultato, e i test lo verificano:
è il modo più rapido per capire che la sintassi è cambiata ma la semantica no.

## Due percorsi possibili

- **In ordine**, se parti da Java 8. È l'ordine consigliato: ogni capitolo presuppone
  solo quelli precedenti.
- **A piacere**, se una parte la conosci già. Ogni pagina è autosufficiente e dichiara la
  versione Java che ha introdotto la funzionalità.

Torna al [README](../README.md)
