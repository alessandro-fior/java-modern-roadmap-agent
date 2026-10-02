# 07 — Pattern Matching

Tre tappe:

- `instanceof` con pattern — definitivo da **Java 16** (JEP 394)
- `switch` con pattern matching — definitivo da **Java 21** (JEP 440)
- record pattern — definitivi da **Java 21** (JEP 441)
- gerarchie `sealed` — definitive da **Java 17** (JEP 409)

Sorgenti: `src/main/java/it/example/modernjava/patternmatching/`
Test: `src/test/java/it/example/modernjava/patternmatching/PatternMatchingTest.java`

---

## Parte 1 — `instanceof` con pattern (Java 16)

### PRIMA

```java
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.length());
}
```

Il tipo è scritto due volte. Se il cast è sbagliato, si scopre a runtime.

### DOPO

```java
if (obj instanceof String s) {
    System.out.println(s.length());
}
```

Meno cast, una sola espressione, e il compilatore garantisce che `s` sia un `String`.

### Il caso utile: la catena di `if`

```java
// PRIMA
public static String descrivi(Object obj) {
    if (obj instanceof String) {
        String s = (String) obj;
        return "String di lunghezza " + s.length();
    }
    if (obj instanceof Integer) {
        Integer n = (Integer) obj;
        return "Intero pari? " + (n % 2 == 0);
    }
    return "Altro tipo: " + obj;
}

// DOPO
public static String descrivi(Object obj) {
    if (obj instanceof String s) {
        return "String di lunghezza " + s.length();
    }
    if (obj instanceof Integer n) {
        return "Intero pari? " + (n % 2 == 0);
    }
    return "Altro tipo: " + obj;
}
```

### Le regole

```java
if (obj instanceof String s) { ... }     // ok
if (obj instanceof String s && s.length() > 2) { ... }   // ok, s è nel suo ambito
if (!(obj instanceof String s)) { ... }  // ok, ma s esiste solo nel ramo opposto
if (obj instanceof String s || s.length() > 2) { }        // ❌ s non esiste nel ramo destro
if (obj instanceof String s && other instanceof String t) { }  // ❌ nomi diversi
```

`instanceof` con `null` dà `null` come risultato (non eccezione), ma **il pattern non
viene valutato**: `null instanceof String s` è `false` e `s` non viene assegnato.

---

## Parte 2 — gerarchie `sealed` (Java 17)

`sealed` dice al compilatore: "questa gerarchia è chiusa, gli unici sotto-tipi sono
questi".

```java
public sealed interface Risultato permits Successo, Fallimento, InCorso {

    String messaggio();

    record Successo(String messaggio, Object valore) implements Risultato { }

    non-sealed record Fallimento(String messaggio) implements Risultato { }

    record InCorso(String messaggio, int percentuale) implements Risultato { }
}
```

Le tre parole chiave:

| Parola | Significato |
|---|---|
| `sealed` | chiuso: solo i tipi in `permits` |
| `final` | chiuso e non ulteriormente estendibile (è il default per i record) |
| `non-sealed` | riapre: i sotto-tipi sono liberi |

`permits` è obbligatorio solo per `sealed` e `non-sealed`: se un sotto-tipo è `final`
(esplicito o implicito come nei record), `sealed` e `final` non possono comparire
insieme.

```java
sealed interface Figura permits Cerchio, Rettangolo, Triangolo {}
record Cerchio(double raggio) implements Figura {}
record Rettangolo(double base, double altezza) implements Figura {}
record Triangolo(double base, double altezza, double ipotenusa) implements Figura {}
```

Il compilatore ora sa che la gerarchia è chiusa. Questo è ciò che rende possibile lo
`switch` esaustivo.

---

## Parte 3 — `switch` con pattern matching (Java 21)

### PRIMA

```java
public static String descrivi(Risultato risultato) {
    if (risultato instanceof Risultato.Successo successo) {
        return "Successo: " + successo.messaggio() + " (" + successo.valore() + ")";
    }
    if (risultato instanceof Risultato.Fallimento fallimento) {
        return "Fallimento: " + fallimento.messaggio();
    }
    Risultato.InCorso inCorso = (Risultato.InCorso) risultato;
    return "In corso: " + inCorso.messaggio() + " al " + inCorso.percentuale() + "%";
}
```

### DOPO

```java
public static String descrivi(Risultato risultato) {
    return switch (risultato) {
        case Risultato.Successo s   -> "Successo: " + s.messaggio() + " (" + s.valore() + ")";
        case Risultato.Fallimento f -> "Fallimento: " + f.messaggio();
        case Risultato.InCorso c    -> "In corso: " + c.messaggio() + " al " + c.percentuale() + "%";
    };
}
```

**Nessun `default`.** E non è un caso speciale: il compilatore verifica che i casi
coprano tutti i sotto-tipi permessi. Se domani aggiungi `Errore` a `permits` e non lo
gestisci, l'errore è **di compilazione**, non a runtime.

Questo è il salto vero rispetto ai vecchi `switch`: il compilatore conosce la gerarchia.

### Le guard

```java
public static String descriviForma(Record forma) {
    return switch (forma) {
        case Cerchio(double raggio) when raggio > 100  -> "Cerchio grande (r=" + raggio + ")";
        case Cerchio(double raggio)                     -> "Cerchio piccolo (r=" + raggio + ")";
        case Rettangolo(double base, double altezza)   -> "Rettangolo " + base + "x" + altezza;
    };
}
```

`when` (o `guard`) è una condizione aggiuntiva. Se è falsa si passa al caso successivo.
Anche qui nessun `default`: la gerarchia chiusa basta.

### Ordine di valutazione

Il primo caso che corrisponde **e** ha una `guard` vera vince. Quindi metti i casi
particolari prima di quelli generici.

### Pattern su tipi normali, non solo record

```java
public static String descrivi(Object obj) {
    return switch (obj) {
        case Integer n -> "un intero: " + n;
        case String s  -> "una stringa: " + s;
        case null      -> "null";
        default        -> "tipo sconosciuto";
    };
}
```

`case null` è utile: con `instanceof` il `null` non viene mai valutato e va gestito a
parte. Con lo switch si può scrivere esplicitamente.

---

## Parte 4 — record pattern (Java 21)

Il record pattern **destructura** il record: i componenti diventano variabili.

### PRIMA

```java
if (obj instanceof Risultato.Successo successo) {
    String messaggio = successo.messaggio();
    Object valore = successo.valore();
    System.out.println(messaggio + " = " + valore);
}
```

### DOPO

```java
return switch (obj) {
    case Risultato.Successo(String messaggio, Object valore) ->
            "record pattern su Successo: " + messaggio + " = " + valor;
    case Risultato.Fallimento(String messaggio) ->
            "record pattern su Fallimento: " + messaggio;
    case null -> "null";
    default   -> "tipo non riconosciuto";
};
```

È comodo con `Optional`, che è un record a tutti gli effetti:

```java
Optional<String> configurazione = Optional.of("produzione");

switch (configurazione) {
    case Optional<String> s when s.isPresent() -> System.out.println("presente: " + s.get());
    case Optional<?> v  when v.isEmpty()      -> System.out.println("assente");
    default                                    -> System.out.println("altro Optional");
}
```

### Pattern annidati

```java
case Risultato.Successo("utente", Optional<String> nome) -> ...
```

Il record pattern si può comporre, e ogni livello si destrutura.

---

## Gerarchie chiuse con `Enum` e `Object`

Lo `switch` con pattern è più ampio dello `switch` classico:

```java
// su un enum
switch (priorita) {
    case ALTA  -> ...;
    case MEDIA -> ...;
    case BASSA -> ...;
}

// su Object, con fallback
switch (obj) {
    case String s   -> ...;
    case null       -> ...;
    default         -> ...;
}

// su una gerarchia sealed, senza default
switch (risultato) {
    case Successo s   -> ...;
    case Fallimento f -> ...;
}
```

Quando serve il `default` e quando no:

- **`Object`, `Integer`, `String`, un'interfaccia non chiusa** → serve `default`.
- **`enum`** → il `default` è opzionale. In Java 21+ lo `switch` su enum è esaustivo
  anche senza `default`: se aggiungi una costante all'enum e non la gestisci, è un errore
  di compilazione. (Prima di Java 21 era obbligatorio.)
- **gerarchia `sealed` chiusa** → il `default` non serve.

---

## Il pattern matching su `Map.Entry`

```java
for (var entry : mappa.entrySet()) {
    if (mappa.get(entry.getKey()) instanceof Integer valore && valore > 10) {
        System.out.println(entry.getKey() + " = " + valore);
    }
}
```

## Quando non usare il pattern matching

```java
// Non serve: un cast è più chiaro
Cliente c = (Cliente) risultato;

// Non serve: due tipi diversi non richiedono pattern matching
if (obj instanceof Integer || obj instanceof Long) { ... }

// Overkill: un solo controllo su una classe nota
if (cliente != null) { ... }
```

---

## Riepilogo delle versioni

| Funzionalità | Versione | Stato |
|---|---|---|
| `instanceof` con pattern | Java 16 | definitivo |
| `switch` con pattern | Java 21 | definitivo |
| record pattern | Java 21 | definitivo |
| `sealed` | Java 17 | definitivo |
| pattern su tipi primitivi | Java 27 | **preview** (JEP 532) |
| primitive pattern in `switch` | Java 27 | **preview** (JEP 532) |

Il progetto usa solo le funzionalità definitive. L'esempio sulle primitive è isolato in
`examples/java27/PrimitivePatternsDemo.java` e richiede `--enable-preview`.

## Esercizio

1. Scrivi una gerarchia `sealed` `Veicolo` con `Auto`, `Moto` e `Bicicletta`, e uno
   `switch` che calcola la tariffa oraria senza `default`.
2. Aggiungi `Camion` alla gerarchia e verifica che il compilatore segnali il caso mancante.
3. Riscrivi una catena di tre `if` su `instanceof` usando un solo `switch` con record
   pattern.

## Prossimo capitolo

[08 — Switch Expression](08-switch-expression.md)