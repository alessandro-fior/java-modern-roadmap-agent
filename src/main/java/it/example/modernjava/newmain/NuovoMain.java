package it.example.modernjava.newmain;

import java.util.List;

/**
 * DOPO: il modello semplificato del {@code main}, definitivo da <b>Java 25</b> (JEP 512).
 * <p>
 * Con Java 25 un programma puo' avere:
 * <ul>
 *   <li>un metodo {@code main} <b>di istanza</b> (non {@code static});</li>
 *   <li>senza il modificatore {@code public};</li>
 *   <li>senza il parametro {@code String[] args}.</li>
 * </ul>
 * In questo caso il {@code main} e' un normale metodo di istanza dentro una classe normale,
 * in un package normale: si lancia con il comando consueto
 * {@code java -cp target/classes it.example.modernjava.newmain.NuovoMain}.
 * <p>
 * {@code java.lang.IO} e' la classe nuova introdotta da Java 25 per la console:
 * {@code IO.println}, {@code IO.print}, {@code IO.printf}, {@code IO.readln},
 * {@code IO.readlnPrompt}. Sta in {@code java.lang}, quindi e' disponibile senza import in
 * qualsiasi programma, e funziona anche quando l'input non e' una console reale
 * (per esempio in un test).
 */
public final class NuovoMain {

    /** Campo dell'istanza: in un {@code main} di istanza {@code this} esiste. */
    private final List<String> diario = List.of("Java 5", "Java 8", "Java 25");

    /**
     * Il nuovo punto di ingresso: niente {@code public}, niente {@code static}, niente {@code String[]}.
     * <p>
     * Nota: una classe <b>non puo' dichiarare due metodi {@code main} con lo stesso
     * parametro</b>, uno statico e uno di istanza. Quindi qui c'e' il solo {@code main}
     * di istanza; il punto di ingresso statico sta in {@link NewMainLauncher}.
     */
    void main() {
        IO.println("Ciao Java 25");

        // Esempio 1: una variabile locale con inferenza del tipo
        var nome = "Alessandro";
        IO.println("Ciao " + nome);

        // Esempio 2: una lista, stampata con un method reference
        var nomi = List.of("Mario", "Luigi", "Anna");
        IO.println("Invitati: " + nomi.size());
        nomi.forEach(System.out::println);

        // Esempio 3: generics + stream + var
        List<Integer> numeri = List.of(10, 20, 30, 40, 50);
        var risultato = numeri.stream()
                .filter(n -> n >= 30)
                .toList();
        IO.println("Numeri >= 30: " + risultato);

        // Esempio 4: record + accessor con il nome del campo
        var cliente = new Cliente(100, "Alessandro", 45);
        IO.println("Cliente: " + cliente);
        IO.println("Fascia eta': " + cliente.fasciaEta());

        // Esempio 5: i dati dell'istanza sono disponibili direttamente
        IO.println("Diario: " + String.join(" -> ", diario));
    }

    /**
     * Se servono gli argomenti della riga di comando, si dichiara esplicitamente
     * {@code String[] args}: il launcher li passa comunque, anche con un {@code main} di istanza.
     * Va pero' in una classe a parte, per non duplicare la firma di {@link #main()}.
     *
     * @see NewMainLauncher
     */
    public record Cliente(int id, String nome, int eta) {

        String fasciaEta() {
            return switch (eta) {
                case 0 -> "neonato";
                default -> eta < 18 ? "minorenne" : "maggioreenne";
            };
        }
    }
}