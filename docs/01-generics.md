# 01 — Generics

**Introdotto in Java 5** (JSR 14).

Sorgenti: `src/main/java/it/example/modernjava/generics/`
Test: `src/test/java/it/example/modernjava/generics/GenericsTest.java`

---

## Il problema

Prima dei Generics, una collezione non diceva nulla su cosa conteneva. Il tipo di ritorno
di `get()` era `Object`, e ogni lettura richiedeva un cast. Il cast è un atto di
fiducia: il compilatore non lo verifica, si accorge solo a runtime se era sbagliato.

## PRIMA — raw type

```java
List lista = new ArrayList();

lista.add("Mario");
lista.add("Luigi");
lista.add(42);            // nessun errore: la lista non ha un tipo

String nome = (String) lista.get(0);   // cast obbligatorio
```

Con un raw type, `lista.add(42)` compila. Il cast continua a funzionare finché i dati
sono al posto giusto, e si rompe con un `ClassCastException` a runtime.

## DOPO — tipo dichiarato

```java
List<String> nomi = new ArrayList<>();

nomi.add("Mario");
nomi.add("Luigi");
// nomi.add(42);   // errore di COMPILAZIONE

String nome = nomi.get(0);   // nessun cast
```

Il compilatore ha spostato il controllo dal runtime alla compilazione. I Generics non
sono zucchero sintattico: sono un controllo di tipo.

## Generics su una classe propria

```java
class Contenitore<T> {

    private T valore;

    public void set(T valore) { this.valore = valore; }
    public T get() { return valore; }
}
```

Una sola classe va bene per ogni tipo:

```java
Contenitore<String> nomi = new Contenitore<>();
Contenitore<Integer> numeri = new Contenitore<>();

nomi.set("Mario");
numeri.set(42);
```

Sorgente: `Contenitore.java`

Nota il **diamond operator** (`<>`): introdotto in Java 7, evita di ripetere i parametri
di tipo quando sono deducibili dal contesto.

## Il parametro di tipo con vincoli

```java
public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) >= 0 ? a : b;
}
```

`T extends Comparable<T>` è un limite: garantisce alla JVM che gli oggetti in `a` e `b`
supportino `compareTo`. Senza il limite il codice non compilerebbe, perché il
compilatore non saprebbe nulla su `T`.

`extends` qui non indica ereditarietà di classe: indica un limite superiore del tipo.

## I wildcard

Quando il tipo esatto non è noto, ma è noto il rapporto con un altro tipo, si usa `?`.

### `List<?>`

```java
List<?> valori = new ArrayList<String>();
Object primo = valori.get(0);   // posso solo leggere, come Object
// valori.add(42);              // non posso scrivere: il tipo esatto è ignoto
```

### `List<? extends Number>` — producer

```java
public static double sommaNumeri(List<? extends Number> numeri) {
    double totale = 0;
    for (Number n : numeri) {
        totale += n.doubleValue();
    }
    return totale;
}

sommaNumeri(List.of(1, 2, 3));       // List<Integer>
sommaNumeri(List.of(1.5, 2.5));      // List<Double>
```

Il metodo accetta una lista che contiene **solo** numeri. Si usa quando si **legge**.

### `List<? super Integer>` — consumer

```java
public static void aggiungiInteri(List<? super Integer> destinazione) {
    destinazione.add(10);
    destinazione.add(20);
}
```

La lista accetta `Integer` **e qualsiasi suo supertipo**: può essere una `List<Integer>`,
una `List<Number>` o una `List<Object>`. Ci si può scrivere, ma dopo si rilegge solo
come `Object`.

Sorgente: `Wildcards.java`

## PECS, in una riga

```text
Producer Extends, Consumer Super
```

- `? extends T`: posso **leggere** valori di tipo `T` (e suoi sottotipi).
- `? super T`: posso **scrivere** valori di tipo `T` (e suoi supertipi).

## Quando non usare i Generics

```java
public List<Object> qualsiasi() { ... }        // accetta e restituisce qualcosa
```

I Generics si usano quando il tipo della collezione è informazione utile per chi chiama.

## Due regole pratiche

1. **Il tipo esatto è noto? Allora scrivilo.** I wildcard servono quando il tipo non è
   noto *al chiamante*, non al chiamato.
2. **Non usare più di un wildcard per parametro** in una firma pubblica. Diventa difficile
   da usare: meglio `List<T>`, `Collection<? extends T>` o `Collection<? super T>`.

## Esercizio

1. Scrivi `static <T> List<T> duplica(List<T> lista)` che restituisce la lista con ogni
   elemento ripetuto due volte.
2. Scrivi `static <T extends Comparable<T>> Optional<T> maximo(List<T> lista)` che
   restituisce `Optional.empty()` su lista vuota. Combina Generics e `Optional`.
3. Perché `static <T> void metodo(T a, T b)` e `static void metodo(Object a, Object b)`
   non sono lo stesso metodo, nonostante l'*erasure* li produca identici?

## Prossimo capitolo

[02 — Lambda](02-lambda.md)