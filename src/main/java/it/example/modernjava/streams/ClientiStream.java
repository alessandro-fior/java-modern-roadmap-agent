package it.example.modernjava.streams;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * L'esempio centrale del progetto: una pipeline che mette insieme piu' concetti.
 * <p>
 * Qui sotto, nello stesso file:
 * <ul>
 *   <li>record (<b>Java 16</b>) -&gt; {@link Cliente}</li>
 *   <li>lambda (<b>Java 8</b>) e method reference</li>
 *   <li>stream (<b>Java 8</b>) con {@code filter}, {@code map}, {@code sorted}, {@code toList}</li>
 *   <li>{@code Optional} (<b>Java 8</b>)</li>
 *   <li>{@code var} (<b>Java 10</b>)</li>
 *   <li>{@code switch} expression (<b>Java 14</b>)</li>
 * </ul>
 * E' il punto in cui il Java "moderno" smette di essere un elenco di novita' e diventa
 * un modo naturale di scrivere codice.
 */
public final class ClientiStream {

    private static final List<Cliente> CLIENTI = List.of(
            new Cliente(1, "Mario", 25, "Milano"),
            new Cliente(2, "Luigi", 17, "Roma"),
            new Cliente(3, "Anna", 32, "Milano"),
            new Cliente(4, "Paolo", 45, "Napoli"),
            new Cliente(5, "Laura", 16, "Roma"),
            new Cliente(6, "Giulia", 51, "Torino")
    );

    private ClientiStream() {
    }

    public static List<Cliente> tutti() {
        return CLIENTI;
    }

    // ------------------------------------------------- 1. filtro dei maggiorenni

    /**
     * PRIMA: ciclo + {@code ArrayList} di appoggio.
     */
    public static List<Cliente> maggiorenniPrima() {
        List<Cliente> risultato = new ArrayList<>();

        for (Cliente c : CLIENTI) {
            if (c.eta() >= 18) {
                risultato.add(c);
            }
        }

        return risultato;
    }

    /**
     * DOPO: la stessa cosa in tre righe, e si vede subito l'intento.
     */
    public static List<Cliente> maggiorenni() {
        return CLIENTI.stream()
                .filter(Cliente::maggiorenne)
                .toList();
    }

    // ------------------------------------------------- 2. estrazione dei nomi

    /**
     * PRIMA
     */
    public static List<String> nomiPrima() {
        List<String> nomi = new ArrayList<>();

        for (Cliente c : CLIENTI) {
            nomi.add(c.nome());
        }

        return nomi;
    }

    /**
     * DOPO
     */
    public static List<String> nomi() {
        return CLIENTI.stream()
                .map(Cliente::nome)
                .toList();
    }

    // ------------------------------------------------- 3. ordinamento

    /**
     * DOPO: ordinamento alfabetico dei soli nomi dei maggiorenni.
     */
    public static List<String> nomiMaggiorenniOrdinati() {
        return CLIENTI.stream()
                .filter(Cliente::maggiorenne)
                .map(Cliente::nome)
                .sorted()
                .toList();
    }

    /**
     * DOPO: ordinamento per eta' decrescente, con {@code Comparator.comparingInt}.
     */
    public static List<Cliente> perEtaDecrescente() {
        return CLIENTI.stream()
                .sorted(Comparator.comparingInt(Cliente::eta).reversed())
                .toList();
    }

    // ------------------------------------------------- 4. conteggio e statistiche

    public static long numeroMaggiorenni() {
        return CLIENTI.stream()
                .filter(Cliente::maggiorenne)
                .count();
    }

    public static Optional<Integer> etaMedia() {
        if (CLIENTI.isEmpty()) {
            return Optional.empty();
        }

        int somma = CLIENTI.stream()
                .mapToInt(Cliente::eta)
                .sum();

        return Optional.of(somma / CLIENTI.size());
    }

    public static Optional<Cliente> piuAnziano() {
        return CLIENTI.stream()
                .max(Comparator.comparingInt(Cliente::eta));
    }

    // ------------------------------------------------- 5. raggruppamento

    public static Map<String, List<Cliente>> perCitta() {
        return CLIENTI.stream()
                .collect(Collectors.groupingBy(Cliente::citta));
    }

    public static Map<String, Long> numeroClientiPerCitta() {
        return CLIENTI.stream()
                .collect(Collectors.groupingBy(Cliente::citta, Collectors.counting()));
    }

    // ------------------------------------------------- 6. ricerca con Optional

    /**
     * Ricerca per id. Non esiste piu' il {@code null}: il risultato e' un {@link Optional}.
     */
    public static Optional<Cliente> perId(int id) {
        return CLIENTI.stream()
                .filter(c -> c.id() == id)
                .findFirst();
    }

    // ------------------------------------------------- 7. partizione

    public static Map<Boolean, List<Cliente>> partizionePerEta() {
        return CLIENTI.stream()
                .collect(Collectors.partitioningBy(Cliente::maggiorenne));
    }

    // ------------------------------------------------- 8. funzione di gruppo

    /**
     * Gli stream con una funzione ricevuta in ingresso si generalizzano in un colpo solo:
     * non serve scrivere {@code nomi()}, {@code ids()}, {@code eta()}, basta {@code estrai(...)}.
     */
    public static <T> List<T> estrai(Function<Cliente, T> estrazione) {
        return CLIENTI.stream()
                .map(esrazione)
                .toList();
    }

    // ------------------------------------------------------------------ main

    public static void main(String[] args) {
        System.out.println("1. tutti i clienti            -> " + tutti());
        System.out.println("2. maggiorenni (DOPO)         -> " + maggiorenni());
        System.out.println("   maggiorenni (PRIMA)        -> " + maggiorenniPrima());
        System.out.println("3. nomi (DOPO)                -> " + nomi());
        System.out.println("   nomi (PRIMA)               -> " + nomiPrima());
        System.out.println("4. nomi maggiorenni ordinati  -> " + nomiMaggiorenniOrdinati());
        System.out.println("   per eta' decrescente       -> " + perEtaDecrescente());
        System.out.println("5. numero maggiorenni         -> " + numeroMaggiorenni());
        System.out.println("   eta' media                 -> " + etaMedia());
        System.out.println("   piu' anziano               -> " + piuAnziano());
        System.out.println("6. per citta'                 -> " + perCitta());
        System.out.println("   clienti per citta'         -> " + numeroClientiPerCitta());
        System.out.println("7. ricerca id = 3             -> " + perId(3));
        System.out.println("   ricerca id = 99            -> " + perId(99));
        System.out.println("8. partizione per eta'        -> " + partizionePerEta());
        System.out.println("9. solo nomi (funzione)       -> " + estrai(Cliente::nome));
    }
}