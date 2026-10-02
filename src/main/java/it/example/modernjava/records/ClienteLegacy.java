package it.example.modernjava.records;

/**
 * PRIMA: la classe DTO tradizionale.
 * <p>
 * Questa e' la versione "classica" di un DTO Java 8/11: campi privati {@code final},
 * costruttore, getter. Piu' {@code equals}, {@code hashCode} e {@code toString} se serve
 * davvero, altrimenti si resta con l'identita' di default e i confronti falliscono.
 * <p>
 * Confronta con {@link ClienteRecord}, che fa le stesse cose in tre righe.
 */
public final class ClienteLegacy {

    private final int id;
    private final String nome;

    public ClienteLegacy(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClienteLegacy altro)) {
            return false;
        }
        return id == altro.id && nome.equals(altro.nome);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, nome);
    }

    @Override
    public String toString() {
        return "ClienteLegacy[id=" + id + ", nome=" + nome + "]";
    }
}