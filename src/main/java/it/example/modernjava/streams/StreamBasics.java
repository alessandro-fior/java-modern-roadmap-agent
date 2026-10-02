package it.example.modernjava.streams;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * PRIMA / DOPO: la Stream API.
 * <p>
 * Stream API e' una funzionalita di <b>Java 8</b> (JSR 335).
 * <p>
 * PRIMA: si scrive un ciclo {@code for}, si tiene una {@code List} di appoggio per i risultati,
 * e ogni operazione e' un blocco di codice.
 * DOPO: si descrive una <b>trasformazione</b> in una catena di operazioni.
 * <p>
 * Attenzione: uno stream non e' una lista e non memorizza nulla. E' una pipeline:
 * sorgente -&gt; operazioni intermedie (lazy) -&gt; operazione terminale (risultato).
 * Senza operazione terminale non viene eseguito nulla.
 */
public final class StreamBasics {

    private static final List<Integer> NUMERI = List.of(10, 20, 30, 40, 50);

    private StreamBasics() {
    }

    // ---------------------------------------------------------------- filter

    /**
     * PRIMA: filter
     */
    public static List<Integer> filterPrima() {
        List<Integer> risultato = new ArrayList<>();

        for (Integer n : NUMERI) {
            if (n > 25) {
                risultato.add(n);
            }
        }

        return risultato;
    }

    /**
     * DOPO: filter
     */
    public static List<Integer> filterDopo() {
        return NUMERI.stream()
                .filter(n -> n > 25)
                .toList();
    }

    // ------------------------------------------------------------------- map

    /**
     * PRIMA: map (trasformare ogni elemento)
     */
    public static List<String> mapPrima() {
        List<String> risultato = new ArrayList<>();

        for (Integer n : NUMERI) {
            risultato.add("n." + n);
        }

        return risultato;
    }

    /**
     * DOPO: map + method reference
     */
    public static List<String> mapDopo() {
        return NUMERI.stream()
                .map(n -> "n." + n)
                .toList();
    }

    // ------------------------------------------------- sorted / distinct / count

    public static List<Integer> ordinati() {
        return NUMERI.stream()
                .sorted()
                .toList();
    }

    public static List<Integer> ordinatiDecrescenti() {
        return NUMERI.stream()
                .sorted(Comparator.reverseOrder())
                .toList();
    }

    public static List<Integer> distinti() {
        return List.of(3, 1, 3, 2, 1, 3)
                .stream()
                .distinct()
                .toList();
    }

    /**
     * PRIMA: count
     */
    public static long countPrima() {
        int contatore = 0;

        for (Integer n : NUMERI) {
            if (n >= 30) {
                contatore++;
            }
        }

        return contatore;
    }

    /**
     * DOPO: count
     */
    public static long countDopo() {
        return NUMERI.stream()
                .filter(n -> n >= 30)
                .count();
    }

    // ------------------------------------------------------------- toList

    /**
     * {@code Stream.toList()} e' definitivo da <b>Java 16</b> (prima era {@code Collectors.toList()}).
     * Restituisce una lista <b>immutabile</b>: non si puo' ne' aggiungere ne' rimuovere elementi.
     */
    public static List<Integer> toListImmutabile() {
        List<Integer> numeri = NUMERI.stream()
                .filter(n -> n % 20 == 0)
                .toList();

        // numeri.add(999); // <-- UnsupportedOperationException a runtime

        return numeri;
    }

    // ------------------------------------------------------------- raggruppamenti

    /**
     * {@code Collectors.groupingBy} e {@code Collectors.toMap} restano il modo piu' diretto
     * per raggruppare e indicizzare.
     */
    public static Map<Integer, List<Integer>> pariEDispari() {
        return NUMERI.stream()
                .collect(java.util.stream.Collectors.groupingBy(n -> n % 2));
    }

    public static Optional<Integer> massimo() {
        return NUMERI.stream()
                .max(Comparator.naturalOrder());
    }

    // ------------------------------------------------------------------ main

    public static void main(String[] args) {
        System.out.println("sorgente          -> " + NUMERI);
        System.out.println("filter  PRIMA     -> " + filterPrima());
        System.out.println("filter  DOPO      -> " + filterDopo());
        System.out.println("map     PRIMA     -> " + mapPrima());
        System.out.println("map     DOPO      -> " + mapDopo());
        System.out.println("sorted            -> " + ordinati());
        System.out.println("sorted (decresc.) -> " + ordinatiDecrescenti());
        System.out.println("distinct          -> " + distinti());
        System.out.println("count   PRIMA     -> " + countPrima());
        System.out.println("count   DOPO      -> " + countDopo());
        System.out.println("toList            -> " + toListImmutabile());
        System.out.println("groupingBy        -> " + pariEDispari());
        System.out.println("max               -> " + massimo());
    }
}