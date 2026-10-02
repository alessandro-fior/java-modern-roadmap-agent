package it.example.modernjava.patternmatching;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Esempi su una gerarchia <b>sealed</b> (definitiva da Java 17, JEP 409) e sul
 * <b>record pattern</b> (definitivo da Java 21, JEP 441).
 * <p>
 * Il tipo {@link Risultato} e' dichiarato in {@code Risultato.java}: in Java un tipo pubblico
 * di primo livello deve stare in un file con lo stesso nome. Qui ci sono i metodi che lo
 * descrivono e lo destrutturano.
 * <p>
 * Il record pattern permette di destrutturare il record direttamente nella firma del caso.
 * I componenti diventano variabili, quindi non serve un {@code instanceof} e un cast prima.
 */
public final class SealedHierarchy {

    private SealedHierarchy() {
    }

    public static List<Risultato> esempi() {
        return List.of(
                new Risultato.Successo("OK", 42),
                new Risultato.Fallimento("Errore"),
                new Risultato.InCorso("Caricamento", 60)
        );
    }

    /**
     * PRIMA: {@code instanceof} + cast, uno per tipo.
     */
    public static String descriviPrima(Risultato risultato) {
        if (risultato instanceof Risultato.Successo successo) {
            return "Successo: " + successo.messaggio() + " (" + successo.valore() + ")";
        }

        if (risultato instanceof Risultato.Fallimento fallimento) {
            return "Fallimento: " + fallimento.messaggio();
        }

        Risultato.InCorso inCorso = (Risultato.InCorso) risultato;
        return "In corso: " + inCorso.messaggio() + " al " + inCorso.percentuale() + "%";
    }

    /**
     * DOPO: {@code switch} pattern matching (<b>Java 21</b>, JEP 440) su una gerarchia sealed.
     * Non serve {@code default}: il compilatore sa che i casi sono completi.
     */
    public static String descriviDopo(Risultato risultato) {
        return switch (risultato) {
            case Risultato.Successo s -> "Successo: " + s.messaggio() + " (" + s.valore() + ")";
            case Risultato.Fallimento f -> "Fallimento: " + f.messaggio();
            case Risultato.InCorso c -> "In corso: " + c.messaggio() + " al " + c.percentuale() + "%";
        };
    }

    /**
     * {@code record pattern}: i componenti diventano direttamente variabili del caso.
     * {@link Optional} e' un record, quindi si puo' destrutturare anche lui.
     */
    public static String recordPattern(Object oggetto) {
        return switch (oggetto) {
            case Risultato.Successo(String messaggio, Object valore) ->
                    "record pattern su Successo: " + messaggio + " = " + valore;
            case Risultato.Fallimento(String messaggio) ->
                    "record pattern su Fallimento: " + messaggio;
            case Optional<String> opzionale when opzionale.isPresent() ->
                    "Optional<String> presente: " + opzionale.get();
            case Optional<?> opzionale when opzionale.isEmpty() ->
                    "Optional vuoto";
            case null ->
                    "null";
            default ->
                    "tipo non riconosciuto";
        };
    }

    /**
     * Le {@code guard} ({@code when}) permettono di raffinare un caso.
     * Anche qui la gerarchia chiusa rende inutile il {@code default}.
     */
    public static String descriviForma(Record forma) {
        return switch (forma) {
            case Cerchio(double raggio) when raggio > 100 -> "Cerchio grande (r=" + raggio + ")";
            case Cerchio(double raggio) -> "Cerchio piccolo (r=" + raggio + ")";
            case Rettangolo(double base, double altezza) -> "Rettangolo " + base + "x" + altezza;
        };
    }

    public record Cerchio(double raggio) {
    }

    public record Rettangolo(double base, double altezza) {
    }

    /**
     * {@code instanceof} con pattern su un valore che arriva da una {@code Map}:
     * evita sia il cast esplicito sia il confronto {@code != null} duplicato.
     */
    public static void lookup(Map<String, Integer> mappa, String chiave) {
        if (mappa.get(chiave) instanceof Integer valore && valore > 10) {
            System.out.println("lookup     -> " + chiave + " = " + valore + " (valore alto)");
        } else {
            System.out.println("lookup     -> " + chiave + " non presente o valore basso");
        }
    }

    public static void main(String[] args) {
        for (Risultato r : esempi()) {
            System.out.println("PRIMA -> " + descriviPrima(r));
            System.out.println("DOPO  -> " + descriviDopo(r));
        }

        for (Object o : esempi()) {
            System.out.println("record pattern -> " + recordPattern(o));
        }

        System.out.println("record pattern -> " + recordPattern(Optional.of("vincitore")));
        System.out.println("record pattern -> " + recordPattern(Optional.empty()));
        System.out.println("record pattern -> " + recordPattern(null));
        System.out.println("record pattern -> " + recordPattern("testo qualsiasi"));

        System.out.println("forma -> " + descriviForma(new Cerchio(150)));
        System.out.println("forma -> " + descriviForma(new Cerchio(5)));
        System.out.println("forma -> " + descriviForma(new Rettangolo(3, 4)));

        lookup(Map.of("a", 5, "b", 20), "a");
        lookup(Map.of("a", 5, "b", 20), "b");
    }
}