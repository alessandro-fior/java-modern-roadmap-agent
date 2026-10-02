// JEP 532 - Primitive Types in Patterns, instanceof, and switch (Fifth Preview) - Java 27
//
// ATTENZIONE: funzionalita in PREVIEW nel linguaggio. Serve --enable-preview.
// Questo file NON fa parte della build Maven del progetto.
//
// Esecuzione:
//   java --enable-preview examples/java27/PrimitivePatternsDemo.java
//
// Cosa fa: estende i pattern matching ai tipi primitivi. Con la versione definitiva
// (Java 16 / 21) si possono destrutturare solo reference e record; con questa preview
// si possono usare anche int, long, double, boolean e i loro wrapper nei pattern.
// E' la quinta preview: l'API e' ancora in movimento, per questo nel progetto
// principale si preferisce restare sulle funzionalita' definitive.

public class PrimitivePatternsDemo {

    sealed interface Risultato permits Ok, Errore {
    }

    record Ok(int valore) implements Risultato {
    }

    record Errore(String messaggio) implements Risultato {
    }

    public static void main(String[] args) {
        // 1. pattern su primitivo con instanceof
        descriviNumero(42);
        descriviNumero(-7);
        descriviNumero(3.5);

        // 2. pattern su primitivo dentro lo switch, insieme ai pattern sui reference
        for (Object o : new Object[] { 1, "due", 3.0, true, new Ok(4), new Errore("no") }) {
            System.out.println("switch -> " + descrivi(o));
        }
    }

    static void descriviNumero(Object o) {
        // Senza preview servirebbe: o instanceof Integer i -> ...
        if (o instanceof int valore) {
            System.out.println("int     -> " + (valore > 0 ? "positivo" : "negativo"));
        } else if (o instanceof double valore) {
            System.out.println("double  -> " + valore);
        } else {
            System.out.println("altro   -> " + o);
        }
    }

    static String descrivi(Object o) {
        return switch (o) {
            // pattern su primitivi (preview)
            case int n -> "un intero: " + n;
            case double d -> "un double: " + d;
            case boolean b -> "un booleano: " + b;
            // pattern su reference e record (definitivi dal Java 16 / 21)
            case String s -> "una stringa: " + s;
            case Ok(int valore) -> "un risultato ok: " + valore;
            case Errore(String messaggio) -> "un errore: " + messaggio;
            case null -> "null";
            default -> "tipo sconosciuto";
        };
    }
}