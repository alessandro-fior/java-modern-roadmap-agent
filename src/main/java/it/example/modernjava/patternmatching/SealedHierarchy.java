package it.example.modernjava.patternmatching;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Gerarchia <b>sealed</b>: definitiva da <b>Java 17</b> (JEP 409).
 * <p>
 * {@code sealed} dice al compilatore: "questa gerarchia e' chiusa, gli unici sotto-tipi
 * sono questi". Il compilatore puo' allora verificare che ogni {@code switch} sia completo
 * e, se manca un caso, segnalarlo come <b>errore di compilazione</b> e non a runtime.
 * <p>
 * {@code permits} e' obbligatorio per {@code sealed} non {@code non-sealed}. I sotto-tipi
 * possono essere {@code final} (che e' il default per i record), {@code sealed} a loro volta,
 * oppure {@code non-sealed} per riaprire la gerarchia.
 */
public sealed interface Risultato permits Successo, Fallimento, InCorso {

    String messaggio();

    /**
     * Record normale: chiuso per default, quindi puo' stare in un {@code permits} senza dirlo.
     */
    record Successo(String messaggio, Object valore) implements Risultato {
    }

    /**
     * Record {@code non-sealed}: puo' essere a sua volta esteso.
     */
    non-sealed record Fallimento(String messaggio) implements Risultato {
    }

    /**
     * Record normale, chiuso per default.
     */
    record InCorso(String messaggio, int percentuale) implements Risultato {
    }
}

/**
 * {@code record pattern}: definitivi da <b>Java 21</b> (JEP 441).
 * Permette di destrutturare un record direttamente nella firma del caso.
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