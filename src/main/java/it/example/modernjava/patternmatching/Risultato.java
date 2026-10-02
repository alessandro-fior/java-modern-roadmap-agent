package it.example.modernjava.patternmatching;

/**
 * Gerarchia <b>sealed</b>: definitiva da <b>Java 17</b> (JEP 409).
 * <p>
 * {@code sealed} dice al compilatore: "questa gerarchia e' chiusa, gli unici sotto-tipi
 * sono questi". Il compilatore puo' allora verificare che ogni {@code switch} sia completo
 * e, se manca un caso, segnalarlo come <b>errore di compilazione</b> e non a runtime.
 * <p>
 * {@code permits} e' obbligatorio per {@code sealed} e {@code non-sealed}. I sotto-tipi
 * possono essere {@code final} (che e' il default per i record), {@code sealed} a loro volta,
 * oppure {@code non-sealed} per riaprire la gerarchia.
 * <p>
 * {@link #messaggio()} e' il contratto comune a tutti i sotto-tipi, ed e' quello che permette
 * di scrivere un {@code switch} senza conoscere i dettagli di ciascuno.
 */
public sealed interface Risultato permits Successo, Fallimento, InCorso {

    String messaggio();

    /**
     * Record normale: chiuso per default, quindi puo' stare in un {@code permits} senza dirlo.
     */
    record Successo(String messaggio, Object valore) implements Risultato {
    }

    /**
     * Record {@code non-sealed}: puo' essere a sua volta esteso da altri tipi.
     */
    non-sealed record Fallimento(String messaggio) implements Risultato {
    }

    /**
     * Record normale, chiuso per default.
     */
    record InCorso(String messaggio, int percentuale) implements Risultato {
    }
}
