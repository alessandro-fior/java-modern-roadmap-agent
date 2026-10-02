# 05 — `var`

**Introdotto in Java 10** (JEP 286). In Java 11 esteso ai parametri delle lambda.

Sorgenti: `src/main/java/it/example/modernjava/vardecl/`

---

## La cosa più importante da capire subito

`var` **non è tipizzazione dinamica**.

```java
var nome = "Mario";     // il tipo è String, per sempre
nome = 100;             // errore di COMPILAZIONE
```

Il tipo viene dedotto **una volta sola**, al primo assegnamento, e poi è fissato come se
lo avessi scritto tu. `var` è inferenza di tipo, non un `Object` travestito.

## PRIMA / DOPO

```java
// PRIMA
StringBuilder sb = new StringBuilder();
List<String> nomi = new ArrayList<>();

// DOPO
var sb = new StringBuilder();
var nomi = new ArrayList<String>();
```

Il guadagno è reale: in un file pieno di dichiarazioni verbose, la parte dichiarativa
diventa rumore che copre la logica.

## Tipi dedotti

```java
var nome = "Alessandro";           // String
var anni = 45;                     // int
var voti = 9.5;                    // double
var attivo = true;                 // boolean
var iniziali = List.of("A", "F");  // List<String>
var nomi = new ArrayList<String>();// ArrayList<String>
var byte[] dati = new byte[10];    // byte[]
```

Nota: `var nomi = new ArrayList<>();` non funziona con `var` da solo — `var` richiede
che l'inferenza sia possibile. Con i costruttori generici a diamond Java non deduce il
parametro da solo, quindi va scritto `new ArrayList<String>()`.

## Dove si può usare

| Sì | No |
|---|---|
| variabili locali | campi di classe |
| parametri dei metodi **concreti** (Java 11+) | parametri di metodi astratti o di interfaccia |
| componenti di array | tipo di ritorno di un metodo |
| | `var x = null` (nessuna informazione di tipo) |
| | `var` con un tipo esplicito a destra |

```java
// campo: non ammesso
private var nome;                    // ❌

// parametro di metodo concreto: ammesso dal Java 11
void stampa(var testo) { }          // ✅

// parametro di metodo di interfaccia: non ammesso
interface Stampa { void stampa(var testo); }   // ❌

// tipo di ritorno: NON ammesso
static var crea() { return "x"; }   // ❌
static String crea() { return "x"; }// ✅ serve il tipo esplicito

// inizializzazione a null: non ammesso
var nome = null;                     // ❌ nessuna informazione di tipo

// null con tipo noto: si scrive il tipo
String nome = null;                  // ✅
```

Nota sul parametro: `var` in un parametro vale solo per metodi con corpo, cioè implementati.
In un'interfaccia o in un metodo astratto non c'è un corpo da cui dedurre il tipo, quindi
il tipo deve essere dichiarato.

## Quando usarlo

### Sì: quando il tipo è evidente

```java
var cliente = new Cliente(100, "Mario", 45);   // Cliente, non serve scriverlo
var listaClienti = List.of(cliente);
var totaleEta = listaClienti.stream().mapToInt(Cliente::eta).sum();
```

### Sì: quando il tipo è lungo e ripetuto

```java
// PRIMA
Map<String, List<Cliente>> perCitta = new HashMap<>();
List<Optional<Cliente>> trovati = new ArrayList<>();

// DOPO
var perCitta = new HashMap<String, List<Cliente>>();
var trovati = new ArrayList<Optional<Cliente>>();
```

Nota che anche qui i parametri di tipo restano: `var` da solo non basta quando il
costruttore è generico e non deducibile.

### No: quando il tipo non è evidente

```java
// ❌ "statistiche" di che tipo? Non si capisce.
var statistiche = calcola();

// ✅
Map<String, Integer> statistiche = calcola();
```

### No: quando il tipo è già nella dichiarazione del metodo

```java
// Il nome del metodo dice già String: aggiungere var non aiuta
List<String> nomi = new ArrayList<>();   // ✅ più chiaro
var nomi = new ArrayList<String>();      // ⚠️ accettabile, ma nulla guadagnato
```

## L'argomento contrario

Chi critica `var` ha ragione su un punto: `var` sposta l'informazione dal codice al
compilatore. Se leggi un file senza eseguirlo, perdi il tipo.

La controargomento è altrettanto valida: le convenzioni Java sono già forti, e leggere
`var clienti = new HashMap<String, List<Cliente>>()` è comunque più semplice di leggere
la dichiarazione completa due volte.

**Regola concreta che funziona:** usa `var` quando il tipo è dedotto dal **costruttore**,
dal **metodo chiamato** o dal **valore letterale**. Non usarlo quando il tipo arriva da
un metodo che restituisce qualcosa di composito (`Optional`, `List`, `Map` con più di un
parametro di tipo).

## `var` non è DRY

```java
// Non è un DRY: var non è un tipo, è una keyword di inferenza.
void metodo1() { var lista = new ArrayList<String>(); }
void metodo2() { var lista = new ArrayList<String>(); }
```

Non puoi scrivere `var` in un'interfaccia e poi cambiarne il tipo in un'implementazione
senza rompere la firma.

## Esercizio

1. Riscrivi una classe con dieci dichiarazioni di campo locali usando `var` dove ha senso.
2. In quali di queste righe `var` renderebbe il codice peggiore?
   `var x = list.get(0);` · `var y = new Cliente(1, "A", 20);` ·
   `var z = mappa.get("chiave");`
3. Scrivi il caso in cui `var` cambia il significato del codice:
   `var i = 10;` e poi `i++;` — è un errore, ma è un errore di compilazione o no?

## Prossimo capitolo

[06 — Record](06-records.md)