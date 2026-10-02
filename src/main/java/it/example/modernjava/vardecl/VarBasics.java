package it.example.modernjava.vardecl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code var}: inferenza del tipo locale, introdotta in <b>Java 10</b> (JEP 286).
 * <p>
 * {@code var} <b>non</b> e' tipizzazione dinamica. Il tipo viene dedotto dal compilatore
 * <b>una volta sola</b>, al primo assegnamento, e poi resta immutabile esattamente come
 * un tipo scritto esplicitamente.
 * <p>
 * {@code var} si puo' usare solo per: variabili locali, parametri dei metodi concreti
 * (non astratti, non di interfaccia) e componenti di array.
 * {@code var} <b>non</b> si puo' usare per: campi, tipo di ritorno di un metodo,
 * parametri di metodi astratti o di interfaccia, {@code null} senza informazioni di tipo,
 * e non ammette un tipo esplicito.
 */
public final class VarBasics {

    private VarBasics() {
    }

    /**
     * DOPO: il tipo e' ovvio, quindi si lascia dedurre.
     */
    public static void tipiOvvi() {
        var nome = "Alessandro";          // String
        var anni = 45;                    // int
        var voti = 9.5;                   // double
        var attivo = true;                // boolean
        var iniziali = List.of("A", "F"); // List<String>
        var nomi = new ArrayList<String>();

        nomi.add(nome);

        System.out.println("nome      -> " + nome);
        System.out.println("anni      -> " + anni);
        System.out.println("voti      -> " + voti);
        System.out.println("attivo    -> " + attivo);
        System.out.println("iniziali  -> " + iniziali);
        System.out.println("nomi      -> " + nomi);
    }

    /**
     * {@code var} non cambia niente: il tipo e' comunque fissato.
     */
    public static void nonEDinamico() {
        var nome = "Mario"; // String

        // nome = 100;        // <-- errore di COMPILAZIONE: incompatible types
        // var valore = null; // <-- non ammesso: nessuna informazione di tipo

        System.out.println("nome      -> " + nome + " (tipo String, bloccato dal compilatore)");
    }

    /**
     * Quando {@code var} aiuta: il tipo e' evidente dal costruttore o dal metodo chiamato.
     */
    public static void quandoUsarlo() {
        var cliente = new Cliente(100, "Mario", 45);
        var listaClienti = List.of(cliente);
        var totaleEta = listaClienti.stream()
                .mapToInt(Cliente::eta)
                .sum();

        System.out.println("cliente     -> " + cliente);
        System.out.println("totale eta' -> " + totaleEta);
    }

    /**
     * Quando {@code var} NON aiuta: il tipo non e' evidente e il nome della variabile non lo chiarisce.
     * In questo caso il tipo esplicito resta piu' leggibile.
     */
    public static void quandoNonUsarlo() {
        // var statistiche = calcola();  // "statistiche" di che tipo? meglio dichiararlo.
        Map<String, Integer> statistiche = new LinkedHashMap<>();
        statistiche.put("clienti", 3);
        statistiche.put("maggiorenni", 2);

        System.out.println("statistiche -> " + statistiche);
    }

    private record Cliente(int id, String nome, int eta) {
    }

    public static void main(String[] args) {
        tipiOvvi();
        nonEDinamico();
        quandoUsarlo();
        quandoNonUsarlo();
    }
}