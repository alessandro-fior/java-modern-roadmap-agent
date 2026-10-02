# 06 — Record

** definitivi da Java 16** (JEP 395). Prima in preview in Java 14 e 15.

Sorgenti: `src/main/java/it/example/modernjava/records/`
Test: `src/test/java/it/example/modernjava/records/RecordsUsageTest.java`

---

## PRIMA

Una classe DTO Java 8/11. Campi `final`, costruttore, getter:

```java
public final class Cliente {

    private final int id;
    private final String nome;

    public Cliente(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
}
```

Se servono `equals`, `hashCode` e `toString`, aggiungono altre trenta righe. E sono le
righe che quasi nessuno scrive a mano, o che copia da Stack Overflow e non capisce.

## DOPO

```java
public record Cliente(int id, String nome) {
}
```

Sette righe diventano tre. Dal record nascono automaticamente:

- campi `private final`;
- il costruttore canonico;
- gli accessor `id()` e `nome()`;
- `equals` e `hashCode` **per valore**;
- `toString` leggibile.

## Uso

```java
var cliente = new Cliente(100, "Mario");

System.out.println(cliente.id());     // 100
System.out.println(cliente.nome());   // Mario
System.out.println(cliente);          // Cliente[id=100, nome=Mario]
```

**L'accessor si chiama come il campo**, non `getNome()`. È il dettaglio che distingue un
record da una classe JavaBean, e la ragione per cui un record non è un bean.

## Uguaglianza per valore

```java
var a = new Cliente(100, "Mario");
var b = new Cliente(100, "Mario");
var c = new Cliente(101, "Mario");

a.equals(b);   // true  ← comportamento corretto, non quello di default
a.equals(c);   // false
a == b;        // false (confronto per riferimento)

var set = new HashSet<Cliente>();
set.add(a);
set.add(b);
set.size();    // 1
```

Con una classe tradizionale, `a == b` è `false` e `a.equals(b)` pure, a meno di non aver
scritto `equals` a mano. Nei record è diverso per costruzione.

## Il costruttore compatto

Quando servono validazione o normalizzazione, si scrive il **costruttore compatto**:
stesso nome del record, senza lista di parametri.

```java
public record Cliente(int id, String nome) {

    public Cliente {                       // attenzione: senza parametri
        if (id <= 0) {
            throw new IllegalArgumentException("id deve essere positivo: " + id);
        }
        nome = nome == null ? "" : nome.strip();   // normalizzazione
    }
}
```

La validazione è dentro il tipo: non esiste un `Cliente` malformato.

## Record generici

```java
public record Risultato<T>(boolean ok, T valore) {

    public static <T> Risultato<T> ok(T valore)     { return new Risultato<>(true, valore); }
    public static <T> Risultato<T> errore()         { return new Risultato<>(false, null); }
}
```

```java
Risultato<String>  testo  = Risultato.ok("ok");
Risultato<Integer> numero = Risultato.errore();
```

## Metodi aggiuntivi

Un record è una classe: si possono aggiungere metodi normali, e sono il posto giusto per
la logica derivata.

```java
public record Cliente(int id, String nome, int eta, String citta) {

    public boolean maggiorenne() {
        return eta >= 18;
    }

    public String fasciaEta() {
        return switch (eta) {
            case 0 -> "neonato";
            default -> eta < 18 ? "minorenne" : "maggioreenne";
        };
    }
}
```

In un progetto reale è quasi sempre questo il vero motivo per cui esiste un record:
non il dato, ma i metodi che ci vivono sopra.

## Record come contenitori di collezioni

```java
record Pagina<T>(int numero, List<T> contenuto) {

    Pagina {
        contenuto = List.copyOf(contenuto);   // difensiva: la lista resta immutabile
    }

    boolean vuota() {
        return contenuto.isEmpty();
    }
}
```

Il costruttore compatto è il posto giusto per la **copia difensiva**: il record è
immutabile, ma se contiene una lista mutabile l'immutabilità è solo apparente.

## I limiti

```java
record Cliente extends Persona { }             // ❌ i record non si estendono
record Cliente(int id) { int altro; }           // ❌ campi extra non ammessi
public int id() { return 999; }                 // ❌ l'accessor è implicito
```

Un record:

- è sempre `final`;
- estende solo `java.lang.Record`;
- non ha campi instance oltre a quelli dichiarati nell'intestazione;
- non ha metodi `static` di inizializzazione extra impliciti;
- non può dichiarare una classe annidata non statica.

In pratica: un record è un valore, non un oggetto con comportamento. Se ti serve un
comportamento ricco, usa una classe.

## Quando NON usare un record

- **Entità JPA/Hibernate**: un record non è una classe con identità, e il proxy di
  persistenza non funziona con record immutabili come farebbe con un `@Entity` classico.
- **Classi con costruttori non banali e logica di validazione complessa**: si può, ma
  se il file è pieno di regole, un record non è più il posto giusto.
- **DTO che devono essere serializzati in un formato specifico** senza più modifiche:
  Jackson e simili funzionano, ma il nome dell'accessor cambia la forma del JSON.

## Record e `equals` con i record

`a.equals(b)` è `true` solo se sono lo stesso tipo **e** hanno gli stessi componenti:

```java
new Cliente(1, "Mario").equals(new ClienteLavoratore(1, "Mario"));  // false
```

Conta anche il tipo dinamico, non solo i campi.

## Esercizio

1. Riscrivi la classe `ClienteLegacy` come record. Quali righe sono sparite?
2. Aggiungi al record `Pagina<T>` un metodo `map(Function<T, R>)` che restituisce una
   nuova pagina con i contenuti trasformati.
3. Perché `record Cliente(int id)` non può avere un campo `private String cache;`?

## Prossimo capitolo

[07 — Pattern Matching](07-pattern-matching.md)