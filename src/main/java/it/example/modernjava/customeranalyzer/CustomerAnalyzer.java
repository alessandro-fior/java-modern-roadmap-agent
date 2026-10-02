package it.example.modernjava.customeranalyzer;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * "Customer Analyzer": il mini progetto finale.
 * <p>
 * Nessun framework, nessuna dipendenza: solo Java e la libreria standard.
 * Tutto quello che serve e' gia' stato introdotto prima:
 * <pre>
 *     record        (Java 16)
 *     generics      (Java 5)
 *     lambda        (Java 8)
 *     stream        (Java 8)
 *     method ref    (Java 8)
 *     Optional      (Java 8)
 *     var           (Java 10)
 *     switch expr   (Java 14)
 *     pattern match (Java 16 / 21)
 * </pre>
 * <p>
 * La classe e' stateless: i dati arrivano come argomento, il risultato e' un valore.
 * Questo la rende facile da testare (vedi {@code CustomerAnalyzerTest}).
 */
public final class CustomerAnalyzer {

    /** Dati di esempio: {@code List.of} produce gia' una lista immutabile. */
    public static final List<Cliente> CLIENTI_ESEMPIO = List.of(
            new Cliente(1, "Mario", 25, "Milano"),
            new Cliente(2, "Luigi", 17, "Roma"),
            new Cliente(3, "Anna", 32, "Milano"),
            new Cliente(4, "Paolo", 45, "Napoli"),
            new Cliente(5, "Laura", 16, "Roma")
    );

    private CustomerAnalyzer() {
    }

    private CustomerAnalyzer() {
    }

    // ------------------------------------------------------- 1. stampa di tutti i clienti

    public static String stampaTutti(List<Cliente> clienti) {
        return clienti.stream()
                .map(c -> "#%d  %-8s  %2d anni  %s"
                        .formatted(c.id(), c.nome(), c.eta(), c.citta()))
                .collect(Collectors.joining(System.lineSeparator()));
    }

    // ------------------------------------------------------- 2. filtro dei maggiorenni

    public static List<Cliente> maggiorenni(List<Cliente> clienti) {
        return clienti.stream()
                .filter(Cliente::maggiorenne)
                .sorted(Comparator.comparing(Cliente::nome))
                .toList();
    }

    public static List<Cliente> minorenni(List<Cliente> clienti) {
        return clienti.stream()
                .filter(Cliente::minorenne)
                .sorted(Comparator.comparing(Cliente::nome))
                .toList();
    }

    // ------------------------------------------------------- 3. estrazione dei nomi

    public static List<String> nomi(List<Cliente> clienti) {
        return clienti.stream()
                .map(Cliente::nome)
                .toList();
    }

    // ------------------------------------------------------- 4. ordinamento

    /**
     * Ordinamento multi-criterio: prima per eta' decrescente, poi per nome crescente.
     * {@code thenComparing} e' l'equivalente funzionale dei vecchi {@code if} annidati.
     */
    public static List<Cliente> perEtaPoiNome(List<Cliente> clienti) {
        return clienti.stream()
                .sorted(Comparator
                        .comparingInt(Cliente::eta)
                        .reversed()
                        .thenComparing(Cliente::nome))
                .toList();
    }

    // ------------------------------------------------------- 5. conteggio

    public static long numeroMaggiorenni(List<Cliente> clienti) {
        return clienti.stream()
                .filter(Cliente::maggiorenne)
                .count();
    }

    public static Map<String, Long> numeroPerCitta(List<Cliente> clienti) {
        return clienti.stream()
                .collect(Collectors.groupingBy(Cliente::citta, Collectors.counting()));
    }

    /**
     * Età media arrotondata per difetto. Restituisce {@code Optional.empty()} su lista vuota:
     * non esiste una media di zero elementi.
     */
    public static Optional<Double> etaMedia(List<Cliente> clienti) {
        return clienti.stream()
                .mapToInt(Cliente::eta)
                .average()
                .boxed();
    }

    // ------------------------------------------------------- 6. ricerca con Optional

    /**
     * Ricerca per id. Nessun {@code null} esce da questo metodo.
     */
    public static Optional<Cliente> perId(List<Cliente> clienti, int id) {
        return clienti.stream()
                .filter(c -> c.id() == id)
                .findFirst();
    }

    /**
     * Ricerca per nome esatto, case-insensitive.
     */
    public static Optional<Cliente> perNome(List<Cliente> clienti, String nome) {
        return clienti.stream()
                .filter(c -> c.nome().equalsIgnoreCase(nome))
                .findFirst();
    }

    /**
     * Versione con fallback esplicito: se il cliente non esiste il programma non ha senso,
     * quindi l'eccezione e' l'errore giusto.
     */
    public static Cliente perIdOppure(List<Cliente> clienti, int id) {
        return perId(clienti, id)
                .orElseThrow(() -> new NoSuchElementException("nessun cliente con id " + id));
    }

    // ------------------------------------------------------- 7. rapporto finale

    /**
     * Il record {@link Riepilogo} raccoglie i risultati: mostra come un record puo' essere
     * il contenitore naturale del risultato di un calcolo.
     */
    public record Riepilogo(
            int totale,
            long maggiorenni,
            long minorenni,
            Optional<Double> etaMedia,
            List<String> nomiMaggiorenni,
            Map<String, Long> perCitta
    ) {
    }

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

    /**
     * Report testuale completo: il metodo {@code main} del mini progetto non fa nient'altro.
     */
    public static String report(List<Cliente> clienti) {
        var riepilogo = riepilogo(clienti);
        var out = new StringBuilder();

        out.append("=== CUSTOMER ANALYZER ===").append(System.lineSeparator());
        out.append("Clienti caricati: ").append(riepilogo.totale()).append(System.lineSeparator());
        out.append(stampaTutti(clienti)).append(System.lineSeparator());
        out.append("Maggiorenni: ").append(riepilogo.maggiorenni()).append(System.lineSeparator());
        out.append("Minorenni: ").append(riepilogo.minorenni()).append(System.lineSeparator());
        out.append("Eta' media: ")
                .append(riepilogo.etaMedia().map(String::valueOf).orElse("n.d."))
                .append(System.lineSeparator());
        out.append("Nomi dei maggiorenni: ").append(riepilogo.nomiMaggiorenni()).append(System.lineSeparator());
        out.append("Clienti per citta': ").append(riepilogo.perCitta()).append(System.lineSeparator());

        return out.toString();
    }

    // ------------------------------------------------------- 8. sintesi con switch

    /**
     * Un ultimo tocco: {@code switch} expression (<b>Java 14</b>) per sintetizzare il quadro.
     */
    public static String sintassi(List<Cliente> clienti) {
        return switch (clienti.size()) {
            case 0 -> "Nessun cliente caricato";
            case 1 -> "Un solo cliente";
            case 2, 3 -> "Poche schede";
            default -> "Base dati consistente (" + clienti.size() + " schede)";
        };
    }

    public static void main(String[] args) {
        System.out.println(report(CLIENTI_ESEMPIO));
        System.out.println("Sintassi: " + sintassi(CLIENTI_ESEMPIO));

        System.out.println("Cliente con id 3: " + perId(CLIENTI_ESEMPIO, 3).orElse(null));
        System.out.println("Cliente con id 99: " + perId(CLIENTI_ESEMPIO, 99));
        System.out.println("Per nome 'mario': " + perNome(CLIENTI_ESEMPIO, "mario").isPresent());

        try {
            perIdOppure(CLIENTI_ESEMPIO, 99);
        } catch (NoSuchElementException e) {
            System.out.println("Eccezione attesa: " + e.getMessage());
        }
    }
}