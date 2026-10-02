package it.example.modernjava.optional;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * Quando <b>non</b> usare {@link Optional}.
 * <p>
 * {@code Optional} e' nato come <b>tipo di ritorno</b>. Usarlo in altri ruoli porta
 * quasi sempre a un codice piu' rumoroso, non piu' chiaro.
 * <table border="1">
 *   <caption>Uso corretto e uso da evitare</caption>
 *   <tr><th>Da fare</th><th>Da evitare</th></tr>
 *   <tr><td>metodo che puo' non trovare nulla</td><td>{@code Optional} come parametro di un metodo</td></tr>
 *   <tr><td>campo che rappresenta davvero un'assenza</td><td>{@code Optional} come tipo di un bean / entity</td></tr>
 *   <tr><td>campo non primitivo con {@code null} ammesso</td><td>{@code Optional<List<String>>}, {@code Optional<Map<..>>}</td></tr>
 *   <tr><td>catena breve di {@code map} / {@code orElseGet}</td><td>10 {@code ifPresent} di fila</td></tr>
 * </table>
 */
public final class OptionalAntiPattern {

    private final String nome; // puo' essere null: meglio String semplice

    private OptionalAntiPattern(String nome) {
        this.nome = nome;
    }

    public String nome() {
        return nome;
    }

    /**
     * PRIMO ERRORE: {@code Optional} come parametro.
     * Il chiamante deve comunque decidere cosa passare: {@link Optional#empty()} non aggiunge
     * informazione, sposta solo il problema.
     */
    public static void optionalComeParametro() {
        System.out.println("Meglio: String nome, con contratto chiaro sul null.");
    }

    /**
     * SECONDO ERRORE: {@code Optional} come campo di una classe non-record.
     * Se il record e' gia' la risposta giusta, si usa il record e nient'altro.
     */
    public record Profilo(String nome, String email) {
    }

    /**
     * TERZO ERRORE: incapsulare un {@code Optional} dentro un {@code Optional}.
     */
    public static Optional<Optional<String>> opzionaleDiOpzionale() {
        Optional<String> interno = Optional.of("valore");
        return Optional.of(interno);
    }

    /**
     * Caso limite da ricordare: per i primitivi esistono {@code OptionalInt},
     * {@code OptionalLong}, {@code OptionalDouble}, che evitano il boxing.
     */
    public static void tipiNonBoxable() {
        Optional<String> testo = Optional.of("abc");
        System.out.println("Optional<String>  -> " + testo);

        OptionalInt numero = OptionalInt.of(42);
        System.out.println("OptionalInt       -> " + numero);
        System.out.println("OptionalInt vuoto -> " + OptionalInt.empty());
    }

    public static void main(String[] args) {
        var istanza = new OptionalAntiPattern("Mario");
        System.out.println("istanza       -> " + istanza.nome());

        optionalComeParametro();
        System.out.println("Profilo       -> " + new Profilo("Anna", "anna@example.com"));
        System.out.println("doppio       -> " + opzionaleDiOpzionale());
        tipiNonBoxable();
    }
}