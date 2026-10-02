# 12 — Mini progetto finale

Sorgenti: `src/main/java/it/example/modernjava/customeranalyzer/`
Test: `src/test/java/it/example/modernjava/customeranalyzer/CustomerAnalyzerTest.java`

```bash
mvn -q test
mvn -q compile exec:java -Dexec.mainClass=it.example.modernjava.customeranalyzer.CustomerAnalyzer
```

Il progetto è **Customer Analyzer**: una classe che analizza una lista di clienti e
restituisce un rapporto. Nessun framework, nessuna dipendenza, Java puro.

---

## Il dominio

```java
public record Cliente(int id, String nome, int eta, String citta) {

    public Cliente {
        if (id <= 0) {
            throw new IllegalArgumentException("id deve essere positivo: " + id);
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("il nome non puo' essere vuoto");
        }
        if (eta < 0 || eta > 130) {
            throw new IllegalArgumentException("eta' non plausibile: " + eta);
        }
    }

    public boolean maggiorenne() {
        return eta >= 18;
    }
}
```

Trentacinque righe. Un record con validazione nel costruttore compatto e due metodi
derivati. Non esiste un `Cliente` non valido: la garanzia è nel tipo, non in una
documentazione.

## L'analizzatore

```java
public final class CustomerAnalyzer {

    public static final List<Cliente> CLIENTI_ESEMPIO = List.of(
            new Cliente(1, "Mario", 25, "Milano"),
            new Cliente(2, "Luigi", 17, "Roma"),
            new Cliente(3, "Anna", 32, "Milano"),
            new Cliente(4, "Paolo", 45, "Napoli"),
            new Cliente(5, "Laura", 16, "Roma")
    );

    private CustomerAnalyzer() {
    }
}
```

Tre scelte progettuali, tutte deliberate:

1. **Classe `final` con costruttore privato.** Non ha stato: è un insieme di funzioni.
2. **Metodi tutti `static`.** Non c'è un oggetto analizzatore da creare: si entra
   dall'elenco dei dati e si esce da un valore.
3. **I dati entrano come argomento.** Il metodo `main` passa `CLIENTI_ESEMPIO`, ma
   qualunque test può passare la propria lista. È la differenza fra codice testabile e
   codice che si può solo eseguire.

## Cosa dimostra, capitolo per capitolo

### Record — `Cliente`

Immutabilità, validazione nel costruttore compatto, `equals` per valore. Il dominio è
immutabile perché un cliente non cambia durante un'analisi.

### Generics — `List<Cliente>`

Il tipo del dato è dichiarato una volta sola e propagato ovunque. Non c'è nessun `Object`
e nessun cast: se un elemento non fosse un `Cliente`, non compilerebbe.

### Stream — filtro ed estrazione

```java
public static List<Cliente> maggiorenni(List<Cliente> clienti) {
    return clienti.stream()
            .filter(Cliente::maggiorenne)
            .sorted(Comparator.comparing(Cliente::nome))
            .toList();
}
```

Tre righe. Il metodo dice cosa fa, non come lo fa. La versione con il ciclo `for` è
[`ClientiStream.maggiorenniPrima()`](../src/main/java/it/example/modernjava/streams/ClientiStream.java):
fa la stessa cosa, ma il lettore deve ricostruire l'intento.

### Method reference e lambda

```java
.filter(Cliente::maggiorenne)          // il metodo è già scritto: lo si punta
.sorted(Comparator.comparing(Cliente::nome))
.riepilogo.etaMedia().map(eta -> String.valueOf((double) eta))   // qui serve una lambda
```

La regola: method reference quando il corpo è già un metodo, lambda quando c'è una
trasformazione.

### `var`

```java
public static Riepilogo riepilogo(List<Cliente> clienti) {
    var listaMaggiorenni = maggiorenni(clienti);

    return new Riepilogo(
            clienti.size(),
            numeroMaggiorenni(clienti),
            clienti.size() - numeroMaggiorenni(clienti),
            etaMedia(clienti),
            nomi(listaMaggiorenni),
            numeroPerCitta(clienti)
    );
}
```

`var` dove il tipo è ovvio. Nei parametri del record `Riepilogo` il tipo è esplicito,
perché lì il tipo **è** l'informazione.

### `Optional`

```java
public static Optional<Double> etaMedia(List<Cliente> clienti) {
    return clienti.stream()
            .mapToInt(Cliente::eta)
            .average()
            .boxed();
}
```

`average()` restituisce `OptionalDouble`: su una lista vuota non c'è media, e
`Optional.empty()` lo dice senza inventare uno zero. Nel test:

```java
assertEquals(Optional.of(27.0), CustomerAnalyzer.etaMedia(CLIENTI));
assertTrue(CustomerAnalyzer.etaMedia(List.of()).isEmpty());
```

```java
public static Cliente perIdOppure(List<Cliente> clienti, int id) {
    return perId(clienti, id)
            .orElseThrow(() -> new NoSuchElementException("nessun cliente con id " + id));
}
```

Quando l'assenza è un **errore** e non un caso previsto, l'eccezione è la risposta giusta.

### `switch` expression

```java
public static String sintassi(List<Cliente> clienti) {
    return switch (clienti.size()) {
        case 0 -> "Nessun cliente caricato";
        case 1 -> "Un solo cliente";
        case 2, 3 -> "Poche schede";
        default -> "Base dati consistente (" + clienti.size() + " schede)";
    };
}
```

### Un record per il risultato

```java
public record Riepilogo(
        int totale,
        long maggiorenni,
        long minorenni,
        Optional<Double> etaMedia,
        List<String> nomiMaggiorenni,
        Map<String, Long> perCitta
) {
}
```

Il risultato di un calcolo è un **valore**, quindi è un record. Restituire sei valori
separati avrebbe richiesto una classe, un costruttore e `equals`.

---

## L'output

```text
=== CUSTOMER ANALYZER ===
Clienti caricati: 5
#1  Mario       25 anni  Milano
#2  Luigi       17 anni  Roma
#3  Anna        32 anni  Milano
#4  Paolo       45 anni  Napoli
#5  Laura       16 anni  Roma
Maggiorenni: 3
Minorenni: 2
Eta' media: 27.0
Nomi dei maggiorenni: [Anna, Mario, Paolo]
Clienti per citta': {Milano=2, Roma=2, Napoli=1}
Sintassi: Base dati consistente (5 schede)
Cliente con id 3: Cliente[id=3, nome=Anna, eta=32, citta=Milano]
Cliente con id 99: Optional.empty
Per nome 'mario': true
Eccezione attesa: nessun cliente con id 99
```

Notare `Cliente con id 99: Optional.empty`: il metodo che non trova nulla **non restituisce
`null`**, e nella riga di comando si vede il tipo.

## I test

`CustomerAnalyzerTest` ha 276 righe e usa `@Nested` per raggruppare le asserzioni per
capitolo. Le verifiche non riguardano il codice ma le **regole**:

```java
@Test
@DisplayName("la somma di maggiorenni e minorenni e' il totale")
void partizioneCompleta() {
    assertEquals(CLIENTI.size(),
            CustomerAnalyzer.maggiorenni(CLIENTI).size()
                    + CustomerAnalyzer.minorenni(CLIENTI).size());
}
```

```java
@Test
@DisplayName("il record Cliente: il costruttore compatto valida i dati")
void validazione() {
    assertThrows(IllegalArgumentException.class, () -> new Cliente(0, "Mario", 25, "Milano"));
    assertThrows(IllegalArgumentException.class, () -> new Cliente(1, "  ", 25, "Milano"));
}
```

```java
@Test
@DisplayName("su lista vuota il report non contiene una media fasulla")
void reportSuVuota() {
    String report = CustomerAnalyzer.report(List.of());

    assertTrue(report.contains("Clienti caricati: 0"));
    assertTrue(report.contains("Eta' media: n.d."));
}
```

L'ultimo è il test più importante del progetto: verifica che l'assenza di un dato non
venga camuffata da uno zero.

---

## Esercizi di estensione

1. Aggiungi `String email` a `Cliente`, con validazione nel costruttore, e un metodo
   `ClientiStream.estrai(Cliente::email)`. Ricorda di aggiornare `Riepilogo`.
2. Aggiungi a `CustomerAnalyzer` un metodo `perFasciaEta(List<Cliente>)` che restituisca
   una `Map<String, Long>` con le fasce per età, usando `Collectors.groupingBy` e una
   `switch` expression come funzione di classificazione.
3. Riscrivi `riepilogo` **senza** stream, solo con cicli. Quale versione è più leggibile?
   Argomenta la risposta: cambia se i dati sono 5 o 5.000.000?
4. Aggiungi un test che verifichi che `report(List.of())` non contenga mai la stringa
   `NaN`. Perché è un test utile?

## Prossimo capitolo

[13 — Glossario](13-glossario.md)