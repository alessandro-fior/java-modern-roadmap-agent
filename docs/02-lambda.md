# 02 — Lambda e interfacce funzionali

**Introdotto in Java 8** (JSR 335, JEP 12, 13, 14).

Sorgenti: `src/main/java/it/example/modernjava/lambda/`

---

## Il punto di partenza

```java
public static void main(String[] args) {
    var nomi = List.of("Mario", "Luigi", "Anna");

    for (String nome : nomi) {
        System.out.println(nome);
    }

    nomi.forEach(nome -> System.out.println(nome));   // lambda
    nomi.forEach(System.out::println);                // method reference
}
```

Tre modi di dire la stessa cosa. Il for-each non è sparito: se devi solo iterare, è la
scelta giusta e la più veloce. Le lambda servono quando devi **passare comportamento**
a un metodo.

## La differenza vera

Non è una scorciatoia di sintassi. Prima di Java 8, per passare comportamento a un
metodo serviva una classe:

```java
// PRIMA: classe anonima (o una classe dedicata)
Comparator<String> perLunghezza = new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return Integer.compare(a.length(), b.length());
    }
};

// DOPO: la lambda dice esattamente la stessa cosa
Comparator<String> perLunghezza = (a, b) -> Integer.compare(a.length(), b.length());
```

Il motivo per cui serviva una classe era il tipo statico: Java non aveva modo di dichiarare
"un valore che è una funzione". Una lambda introduce i **functional type**: ogni
interfaccia con un solo metodo astratto è un tipo ammesso come variabile, come parametro,
come valore di ritorno.

## Le quattro forme di una lambda

```java
// 1. corpo su una riga
nome -> System.out.println(nome)

// 2. con parametri espliciti (serve quando i tipi non si deducono)
(String nome, String cognome) -> nome + " " + cognome

// 3. corpo su più righe, con blocco
(String nome) -> {
    String maiuscolo = nome.toUpperCase();
    System.out.println(maiuscolo);
}

// 4. senza parametri
() -> System.out.println("Ciao")
```

Quando c'è un solo parametro le parentesi sono opzionali. Con due o più servono.

## I method reference

Quando il corpo della lambda è già un metodo che esiste, si scrive il metodo:

```java
nomi.forEach(nome -> System.out.println(nome));   // lambda
nomi.forEach(System.out::println);                // method reference: più leggibile
```

Le quattro forme:

| Forma | Significato | Esempio |
|---|---|---|
| `Tipo::metodo` | metodo di istanza su un oggetto noto | `System.out::println` |
| `Tipo::metodoStatico` | metodo statico | `Integer::parseInt` |
| `istanza::metodo` | metodo di istanza su `istanza` | `cliente::nome` |
| `Tipo::nuovo` | costruttore | `ArrayList::new` |

Il method reference è solo zucchero: il compilatore genera una lambda equivalente.
Si preferisce quando rende il codice più leggibile, non per performance.

## Le interfacce funzionali della libreria standard

`java.util.function` contiene le interfacce più usate. Sono quasi tutte **consumer**
(una entrada, un risultato), perché è il caso più comune.

```java
Supplier<String>   fornitore = () -> "ciao";              // T     get()
Function<S,T>      mappa     = String::length;             // S -> T  apply(S)
Predicate<T>       filtro    = s -> !s.isEmpty();          // T -> boolean  test(T)
Consumer<T>        consumatore= System.out::println;       // T -> void  accept(T)
BiFunction<S,T,R>  due        = Integer::sum;              // (S, T) -> R
Comparator<T>      ordinatore = Comparator.naturalOrder(); // T compare(T, T)
UnaryOperator<T>   unaria     = String::toUpperCase;      // T -> T
BinaryOperator<T>  binaria    = Integer::max;              // (T, T) -> T
```

Sono pensate per essere **accettate** dai metodi, non per essere estese. Infatti hanno un
metodo astratto e possono avere metodi `default` (come `andThen`, `and`, `or`, `negate`):

```java
Predicate<String> nonVuoto = s -> !s.isEmpty();
Predicate<Integer> pari = n -> n % 2 == 0;
Predicate<Integer> pariENonVuoto = pari.and(n -> n != 0);   // composizione
```

## Definire una propria interfaccia funzionale

```java
@FunctionalInterface
public interface Operazione {

    int applica(int a, int b);

    default Operazione piu(int costante) {
        return (a, b) -> applica(a, b) + costante;
    }

    static Operazione somma() {
        return (a, b) -> a + b;
    }
}
```

L'annotazione `@FunctionalInterface` non è obbligatoria, ma serve:

- il compilatore verifica che ci sia **esattamente un** metodo astratto;
- chi legge il codice sa subito che è una lambda.

I metodi `default` e `static` non contano: servono a condividere implementazione senza
forzare chi implementa a riscriverla.

## Attenzione alle trappole

### Le variabili catturate devono essere effettivamente finali

```java
int base = 10;

// base = 20;   // errore: dopo la dichiarazione non si può più cambiare

Function<Integer, Integer> piuBase = n -> n + base;   // ok
```

Prima di Java 8 serviva `final int base = 10;`. Dal 2014 basta non riassegnare la
variabile. Non serve copiare nulla: la JVM la tratta come `final`.

### `var` nei parametri lambda (Java 11)

```java
BiFunction<Integer, Integer, Integer> somma = (var a, var b) -> a + b;
```

Utile con funzioni generiche, dove la deduzione del tipo è lenta o produce `Object`.

### Le lambda non sono metodi

Non hanno un nome, non possono essere sovrascritte, non hanno overload. Sono valori.

## Le quattro forme di `Function`

| Forma | Tipo | Note |
|---|---|---|
| implementazione | `Function<T, R>` | il caso normale |
| referenziata | `Function<T, R>` da metodo | `this::metodo`, `Tipo::metodo` |
| costruttore | `Function<T, R>` | `ArrayList::new` |
| composizione | `f.andThen(g)` | prima `f`, poi `g` |

```java
Function<String, Integer> lunghezza = String::length;
Function<String, Integer> lunghezzaRaddoppiata = lunghezza.andThen(n -> n * 2);
```

## Quando non usare una lambda

Se il blocco è lungo e ha una logica propria, un metodo con nome è più leggibile:

```java
// ❌ la lambda fa tre cose diverse, nessuna spiegata
clienti.stream().map(c -> {
    if (c.eta() < 18) {
        return null;
    }
    c.aggiungiBonus(1000);
    return c.nome();
}).toList();

// ✅ il nome dice cosa fa
clienti.stream().filter(Client::maggiorenne)
              .map(this::assegnaBonusEAggiornaNome)
              .toList();
```

## Esercizio

1. Riscrivi `max` dell'esercizio del capitolo 1 usando una lambda e `BinaryOperator.max`.
2. Scrivi una factory `static <T> Supplier<List<T>> listaDi(T elemento)` e verifica che
   ogni chiamata produca una lista nuova.
3. Spiega perché non si può scrivere `Comparator<String> c = (String a, String b) -> 0;`
   come `(a, b) -> 0` in un contesto dove il tipo è già noto.

## Prossimo capitolo

[03 — Stream API](03-stream.md)