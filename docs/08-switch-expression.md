# 08 — Switch Expression

**Introdotto in Java 14** (JEP 361). Le arrow con pattern sono definitive in Java 21.

Sorgenti: `src/main/java/it/example/modernjava/switchexpression/`
Test: `src/test/java/it/example/modernjava/switchexpression/SwitchExpressionsTest.java`

---

## PRIMA

```java
String risultato;

switch (giorno) {
    case 1:
        risultato = "Lunedì";
        break;
    case 2:
        risultato = "Martedì";
        break;
    case 3:
        risultato = "Mercoledì";
        break;
    default:
        risultato = "Altro";
}

return risultato;
```

Tre problemi, tutti visibili nel codice:

1. **La variabile va dichiarata prima.** Lo `switch` non è un'espressione, quindi serve
   una variabile di appoggio inizializzata "poi".
2. **`break` obbligatorio.** Dimenticarlo è un errore di compilazione, ma rende il
   codice pieno di rumore.
3. **Il compilatore non verifica la completezza.** Se il tipo è un enum e domani aggiungi
   una costante, lo `switch` continua a compilare e a cadere nel `default`.

## DOPO

```java
var risultato = switch (giorno) {
    case 1 -> "Lunedì";
    case 2 -> "Martedì";
    case 3 -> "Mercoledì";
    default -> "Altro"
};
```

Lo `switch` **produce un valore**. Si assegna con `=`, non con `:`, e le arrow non hanno
bisogno di `break`.

## Più etichette per lo stesso risultato

```java
var tipo = switch (giorno) {
    case 6, 7 -> "Weekend";
    default   -> "Giornata lavorativa"
};
```

Prima serviva:

```java
case 6:
case 7:
    risultato = "Weekend";
    break;
```

## Blocchi con `yield`

Quando il corpo del caso è più di una riga, si usa `yield`:

```java
var risultato = switch (codice) {
    case "IT" -> {
        var nome = "Italia";
        yield "Codice paese " + nome + " (IT)";
    }
    case "FR" -> {
        var nome = "Francia";
        yield "Codice paese " + nome + " (FR)";
    }
    default -> {
        yield "Codice sconosciuto: " + codice;
    }
};
```

`yield` restituisce il valore del caso. Ricorda `return` dentro `switch` in Java 14-17:
dal Java 18 in poi `switch` può anche essere un'istruzione, e `yield` distingue i due casi.

Attenzione: in un costruttore, uno `switch` expression **non può accedere ai campi
dell'istanza**, perché non è chiaro se sia inizializzata. Se serve, sposta il codice in un
metodo `static` o in una factory.

## Lo switch su enum torna a essere verificato

Questo è il punto che si dimentica spesso. Prima di Java 21:

```java
enum Priorita { ALTA, MEDIA, BASSA }

switch (priorita) {          // Java 11: nessuna verifica di completezza
    case ALTA  -> 1;
    default    -> 0;
}
```

In Java 21+ lo `switch` su enum è esaustivo anche senza `default`. Se aggiungi `URGENTE`
all'enum e non lo gestisci, è un **errore di compilazione**:

```text
error: the switch expression does not cover all possible input values
```

Il compilatore torna a fare il lavoro che faceva ai vecchi tempi con le costanti `case`.
È il motivo per cui le arrow hanno riportato la sicurezza che il `switch` aveva perso con
i valori letterali.

## Combinare con i pattern (Java 21)

Lo switch expression con pattern matching copre anche le gerarchie `sealed`:

```java
var tariffa = switch (figura) {
    case Cerchio(double raggio)                       -> 5 + raggio;
    case Rettangolo(double base, double altezza)       -> base * altezza;
    case Triangolo(double b, double a, double i) when i > 10 -> "grande";
    default                                            -> 0;
};
```

Vedi [07 — Pattern Matching](07-pattern-matching.md).

## Quando lo switch expression è meglio

```java
// Dove sta il vantaggio
var etichetta = switch (stato) {         // il nome della variabile dice cosa è
    case APERTO   -> "Aperto";
    case CHIUSO  -> "Chiuso";
    default      -> "Sospeso"
};

// Caso in cui non lo è
String x;                                 // dichiarazione inutilizzata
switch (stato) {                          // switch come istruzione
    case APERTO:
        x = "aperto";
        break;
    default:
        x = "chiuso";
}
```

Se il risultato serve solo in un punto, lo `switch` expression è più chiaro. Se serve
davvero un `if`/`else` con effetti collaterali, usa `if`.

## Le regole

- Le costanti `case` devono essere **letterali o costanti `static final` compilate**.
  `case GIORNO + 1` non compila.
- In uno switch expression, tutti i casi devono **restituire un valore**. Non ci sono
  percorsi che "non restituiscono niente".
- `case null` è ammesso (Java 21+) e va messo **prima** del `default`.
- Le variabili usate nelle guard sono **incapsulate**: `when raggio > 10` non definisce
  `raggio` fuori dallo `switch`.

## Riepilogo delle versioni

| Funzionalità | Versione |
|---|---|
| switch su `String` | Java 7 |
| switch expression con arrow | Java 14 |
| completitudine verificata su `enum` | Java 21 |
| `yield` | Java 13 |
| pattern matching e record pattern | Java 21 |
| `case null` | Java 21 |

## Esercizio

1. Riscrivi il parsing di un valore numerico in stringa con uno switch expression.
2. Scrivi uno switch su `Optional<String>` che produca la stringa, "vuoto" o "assente",
   con `case null` e senza `default`.
3. Aggiungi una costante a un enum e osserva l'errore di compilazione che segnala la
   completezza mancante.

## Prossimo capitolo

[09 — Virtual Threads](09-virtual-threads.md)