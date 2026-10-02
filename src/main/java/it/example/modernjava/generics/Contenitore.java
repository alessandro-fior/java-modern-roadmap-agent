package it.example.modernjava.generics;

/**
 * PRIMA / DOPO: una classe generica.
 * <p>
 * I Generics sono una funzionalita di <b>Java 5</b>.
 * <p>
 * PRIMA: per ogni tipo di dato serviva una classe diversa ({@code ContenitoreStringa},
 * {@code ContenitoreIntero}), oppure si scriveva {@code Object} e si faceva cast ovunque.
 * DOPO: una sola classe {@code Contenitore<T>} va bene per ogni tipo.
 *
 * @param <T> tipo del contenuto
 */
public class Contenitore<T> {

    private T valore;

    public void set(T valore) {
        this.valore = valore;
    }

    public T get() {
        return valore;
    }

    /**
     * Il tipo esatto restituito e' utile quando T e' un tipo con piu' di un supertipo utile.
     * {@code T extends Comparable<T>} permette di chiamare {@code compareTo} in sicurezza.
     */
    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    public static void main(String[] args) {
        Contenitore<String> nomi = new Contenitore<>();
        nomi.set("Mario");
        System.out.println("Contenitore<String>  -> " + nomi.get());

        Contenitore<Integer> numeri = new Contenitore<>();
        numeri.set(42);
        System.out.println("Contenitore<Integer> -> " + numeri.get());

        System.out.println("max(\"Anna\", \"Mario\") -> " + Contenitore.max("Anna", "Mario"));
        System.out.println("max(3, 9)              -> " + Contenitore.max(3, 9));
    }
}