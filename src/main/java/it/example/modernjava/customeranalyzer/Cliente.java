package it.example.modernjava.customeranalyzer;

/**
 * Il dominio del mini progetto: un cliente.
 * <p>
 * Record (<b>Java 16</b>): il DTO e' completo in tre righe, con {@code equals},
 * {@code hashCode} e {@code toString} corretti, e l'accessor si chiama {@code eta()}.
 *
 * @param id    identificativo univoco
 * @param nome  nome e cognome
 * @param eta   eta in anni
 * @param citta citta di residenza
 */
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

    public boolean minorenne() {
        return !maggiorenne();
    }
}