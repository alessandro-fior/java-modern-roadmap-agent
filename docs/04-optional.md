# 04 — Optional

**Introdotto in Java 8** (JSR 310).

Sorgenti: `src/main/java/it/example/modernjava/optional/`
Test: `src/test/java/it/example/modernjava/optional/OptionalBasicsTest.java`

---

## Il problema

`null` non è un valore: è l'assenza di un valore. Ma il compilatore non fa distinzione,
e quindi non può aiutarti.

```java
String nome = trovaNome();

if (nome != null) {              // e se qualcuno si dimentica questo if?
    System.out.println(nome);
}
```

I due guai sono noti:

1. **Il `NullPointerException`.** Ce n'è uno in ogni riga 3 della terza parte del codice
   Java che hai scritto.
2. **Il "null contract".** Metodi come `find`, `get`, `lookup` restituiscono `null`
   per dire "non trovato", e non c'è modo di saperlo guardando la firma.

## PRIMA / DOPO

```java
// PRIMA
String nome = trovaNome();
if (nome != null) {
    System.out.println(nome);
}

// DOPO
Optional<String> nome = trovaNome();
nome.ifPresent(System.out::println);
```

Con `Optional`, l'assenza è **dentro il tipo**. Non puoi dimenticartene: il compilatore
ti costringe a dichiarare cosa fai quando il valore non c'è.

```java
public static Optional<String> trovaNome(int indice) {
    if (indice >= 0 && indice < NOMI.size()) {
        return Optional.of(NOMI.get(indice));
    }
    return Optional.empty();
}
```

## Costruire

| Metodo | Quando usarlo | Se il valore è `null` |
|---|---|---|
| `Optional.of(v)` | sei sicuro che non è `null` | `NullPointerException` |
| `Optional.ofNullable(v)` | può essere `null` | `Optional.empty()` |
| `Optional.empty()` | non c'è | — |
| `Optional.ofNullable(o).orElseGet(() -> ...)` | solo se ti serve pigro | — |

La distinzione è voluta: se scrivi `Optional.of(x)` e `x` è `null`, vuol dire che c'è un
bug nel tuo codice, e vuoi saperlo subito.

## Consumare

```java
Optional<String> nome = trovaNome(0);

// se c'è, stampa
nome.ifPresent(System.out::println);

// se c'è, trasforma
nome.map(String::toUpperCase).ifPresent(System.out::println);

// filtra: scarta il valore se non soddisfa la condizione
nome.filter(n -> n.length() > 10).ifPresent(System.out::println);

// trasformare lo stesso Optional in un altro Optional
nome.flatMap(n -> Optional.ofNullable(n.isBlank() ? null : n));
```

## I valori di fallback

```java
// orElse: il fallback è valutato SEMPRE, anche quando non serve
Optional<String> nome = trovaNome(0);
String a = nome.orElse(calcolaFallback());     // calcolaFallback() gira comunque

// orElseGet: il fallback è valutato SOLO se serve
String b = nome.orElseGet(() -> calcolaFallback());
```

Se il fallback è costoso, `orElseGet` è la scelta giusta. Se il fallback è una costante,
`orElse` va benissimo e si legge meglio.

```java
// orElseThrow: se il valore manca, il programma non ha senso
String c = nome.orElseThrow();
String d = nome.orElseThrow(() -> new NoSuchElementException("nome non trovato"));
```

## Come scrivere i metodi

### Come restituire un Optional

```java
public Optional<Cliente> perId(int id) {
    return CLIENTI.stream()
            .filter(c -> c.id() == id)
            .findFirst();
}
```

Non scrivere mai:

```java
public Optional<Cliente> perId(int id) {
    if (condizione) {
        return Optional.of(cliente);
    }
    return null;              // MAI: staireturning null dentro un Optional
}
```

### Come NON accettare un Optional come parametro

```java
// ❌ male
public void saluta(Optional<String> nome) {
    String n = nome.orElse("mondo");
}

// ✅ bene: il parametro è il valore, e il default è nel corpo
public void saluta(String nome) {
    String n = nome == null ? "mondo" : nome;
}
```

Il chiamante di una `Optional` deve comunque decidere cosa passare. `Optional.empty()`
non aggiunge informazione: sposta solo il problema a chi chiama.

## Quando usare `Optional`

| Sì | No |
|---|---|
| tipo di ritorno di un metodo che può non trovare | come parametro di un metodo |
| campo non primitivo che può davvero essere assente | come tipo di un bean/entity |
| catena breve di `map` e `orElseGet` | `Optional<List<String>>`, `Optional<Map<...>>` |
| | dieci `ifPresent` di fila |
| | per "evitare il null" in un campo che è sempre valorizzato |

## Anti-pattern

### `Optional` come campo di una classe non-record

```java
class Cliente {
    private Optional<String> nickname;   // ❌
}
```

Se il campo è un semplice dato opzionale, un record è la risposta giusta e il `null`
di default è accettabile. Se il record non basta, `Optional` nel campo aggiunge un livello
di `Optional` a ogni lettura:

```java
record Cliente(int id, String nome) {
    Optional<String> nickname() {            // almeno non un campo Optional
        return Optional.ofNullable(nickname);
    }
}
```

### `Optional` dentro `Optional`

```java
Optional<Optional<String>> x = Optional.of(Optional.of("a"));   // ❌
```

### Tipi non boxable

Per i primitivi esistono `OptionalInt`, `OptionalLong`, `OptionalDouble`:

```java
OptionalInt numero = OptionalInt.of(42);   // niente boxing
OptionalInt vuoto = OptionalInt.empty();
```

## Il punto giusto

`Optional` è nato come **tipo di ritorno**. Nel progetto è usato in tre modi e non in
quattro:

```java
// 1. come tipo di ritorno
public static Optional<Cliente> perId(int id)

// 2. come espressione
var cliente = perId(id);
String nome = cliente.map(Cliente::nome).orElse("sconosciuto");

// 3. dentro una pipeline
var primoMaggiorenne = clienti.stream()
        .filter(Cliente::maggiorenne)
        .findFirst();
```

Non altrove.

## Esercizio

1. Scrivi `static Optional<Cliente> perNome(List<Cliente>, String)` che cerca per nome
   ignorando maiuscole/minuscole e gli spazi iniziali e finali.
2. Riscrivi `Optional.of(v)` in `Optional.ofNullable(v)` e spiega in una frase quale
   differenza di comportamento hai introdotto.
3. Perché `Optional.orElseThrow()` senza argomenti esiste? Quando conviene?

## Prossimo capitolo

[05 — `var`](05-var.md)