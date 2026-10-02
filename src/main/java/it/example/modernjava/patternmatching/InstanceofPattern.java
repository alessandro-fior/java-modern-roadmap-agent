package it.example.modernjava.patternmatching;

import java.util.Optional;

/**
 * PRIMA / DOPO: {@code instanceof} con pattern.
 * <p>
 * Il pattern matching per {@code instanceof} e' definitivo da <b>Java 16</b> (JEP 394).
 * Prima serviva un controllo e poi un cast; ora il cast è dentro il pattern.
 * <p>
 * Attenzione: {@code instanceof} non accetta {@code null} (darebbe {@code NullPointerException}),
 * e il pattern non puo' essere usato con {@code ||} tra due variabili diverse.
 */
public final class InstanceofPattern {

    private InstanceofPattern() {
    }

    /**
     * PRIMA
     */
    public static String prima(Object obj) {
        if (obj instanceof String) {
            String s = (String) obj;
            return "String di lunghezza " + s.length();
        }

        if (obj instanceof Integer) {
            Integer n = (Integer) obj;
            return "Intero pari? " + (n % 2 == 0);
        }

        return "Altro tipo: " + obj;
    }

    /**
     * DOPO: niente cast, niente variabile duplicata.
     */
    public static String dopo(Object obj) {
        if (obj instanceof String s) {
            return "String di lunghezza " + s.length();
        }

        if (obj instanceof Integer n) {
            return "Intero pari? " + (n % 2 == 0);
        }

        if (obj instanceof Optional<?> opt) {
            return "Optional (" + opt.isPresent() + ")";
        }

        return "Altro tipo: " + obj;
    }

    /**
     * Il pattern e' utile anche con i record: si accede ai componenti per nome.
     */
    public static String descrivi(Object obj) {
        if (obj instanceof Punto p) {
            return "Punto(" + p.x() + ", " + p.y() + ")";
        }

        return "Non e' un punto";
    }

    /**
     * Combinare piu' condizioni: si scrive un {@code if} per ogni tipo.
     * Con {@code ||} e lo stesso nome di variabile si puo' verificare piu' tipi insieme.
     */
    public static boolean numeroPrimo(Object obj) {
        return switch (obj) {
            case null -> false;
            case Integer n -> n > 1 && n % 2 != 0;
            default -> false;
        };
    }

    public record Punto(int x, int y) {
    }

    public static void main(String[] args) {
        Object[] valori = { "Java", 42, Optional.of("x"), 3.14, new Punto(1, 2) };

        for (Object v : valori) {
            System.out.println(v + "  ->  PRIMA: " + prima(v));
            System.out.println(v + "  ->  DOPO : " + dopo(v));
        }

        System.out.println("descrivi(new Punto(1,2)) -> " + descrivi(new Punto(1, 2)));
        System.out.println("numeroPrimo(7)           -> " + numeroPrimo(7));
        System.out.println("numeroPrimo(null)        -> " + numeroPrimo(null));
    }
}