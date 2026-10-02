package it.example.modernjava.streams;

/**
 * PRIMA / DOPO: la classe DTO "tradizionale" ({@code ClienteLegacy}) e il record ({@code Cliente}).
 * <p>
 * I <b>record</b> sono definitivi da <b>Java 16</b> (JEP 395).
 * Un record e' una classe immutabile pensata per i dati: campi {@code final}, costruttore,
 * accessor con il nome del campo, {@code equals}, {@code hashCode}, {@code toString}.
 * Il codice che segue e' tutto quello che serve per un DTO.
 *
 * @param id    identificativo del cliente
 * @param nome  nome del cliente
 * @param eta   eta del cliente
 * @param citta citta di residenza
 */
public record Cliente(int id, String nome, int eta, String citta) {

    /**
     * I record possono avere metodi normali, oltre agli accessor generati.
     */
    public boolean maggiorenne() {
        return eta >= 18;
    }

    /**
     * Lo {@code switch} expression (<b>Java 14</b>, JEP 361) dentro un record: produce
     * direttamente un valore, senza variabile temporanea e senza {@code break}.
     */
    public String fasciaEta() {
        return switch (eta) {
            case 0 -> "neonato";
            case 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 -> "minorenne";
            default -> "maggioreenne";
        };
    }
}