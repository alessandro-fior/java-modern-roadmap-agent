package it.example.modernjava.patternmatching;

import java.util.Optional;

/**
 * PRIMA / DOPO: {@code instanceof} con pattern.
 * <p>
 * Il pattern matching per {@code instanceof} e' definitivo da <b>Java 16</b> (JEP 394).
 * Prima serviva un controllo e poi un cast; ora il cast è dentro il pattern.
 * <p>
 * Attenzione: {@code null instanceof Tipo} e' semplicemente {@code false}, non un
 * {@code NullPointerException}, e in quel caso il pattern <b>non</b> viene assegnato.
 * Inoltre un pattern non puo' essere usato nel ramo destro di un {@code ||} che ne nomina
 * un altro pattern: la variabile non esiste ancora li'.
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
     * Quando un solo tipo di controllo non basta, lo {@code switch} con pattern (Java 21)
     * gestisce il {@code null} con un caso esplicito, che {@code instanceof} non permetteva.
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