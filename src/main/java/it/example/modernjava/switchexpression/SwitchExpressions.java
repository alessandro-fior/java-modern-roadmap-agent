package it.example.modernjava.switchexpression;

/**
 * Il {@code switch} come <b>espressione</b>: definitivo da <b>Java 14</b> (JEP 361).
 * <p>
 * PRIMA lo {@code switch} era solo un'istruzione: serviva una variabile dichiarata prima,
 * {@code case} con {@code break}, e il compilatore non sapeva se tutti i rami erano coperti.
 * DOPO lo {@code switch} e' un'espressione: produce un valore, si assegna con {@code =},
 * e con le {@code arrow} non serve il {@code break}.
 */
public final class SwitchExpressions {

    private SwitchExpressions() {
    }

    /**
     * PRIMA: switch classico con break.
     */
    public static String giornoPrima(int giorno) {
        String risultato;

        switch (giorno) {
            case 1:
                risultato = "Lunedi";
                break;
            case 2:
                risultato = "Martedi";
                break;
            case 3:
                risultato = "Mercoledi";
                break;
            default:
                risultato = "Altro";
        }

        return risultato;
    }

    /**
     * DOPO: switch expression con frecce. Il valore e' prodotto direttamente.
     */
    public static String giornoDopo(int giorno) {
        return switch (giorno) {
            case 1 -> "Lunedi";
            case 2 -> "Martedi";
            case 3 -> "Mercoledi";
            default -> "Altro";
        };
    }

    /**
     * DOPO: piu' etichette per lo stesso risultato, senza {@code case} ripetuti.
     */
    public static String tipoGiorno(int giorno) {
        return switch (giorno) {
            case 6, 7 -> "Weekend";
            default -> "Giornata lavorativa";
        };
    }

    /**
     * DOPO: lo switch expression si puo' usare ovunque serva un valore.
     * Attenzione: in un costruttore lo switch expression non puo' accedere ai campi dell'istanza.
     */
    public static int codicePriorita(StringPriorita priorita) {
        return switch (priorita) {
            case ALTA -> 1;
            case MEDIA -> 2;
            case BASSA -> 3;
        };
    }

    /**
     * Le {@code yield} servono quando il corpo del caso e' un blocco con piu' righe:
     * {@code yield} restituisce il valore del caso.
     */
    public static String descrizioneLunga(String codice) {
        return switch (codice) {
            case "IT" -> {
                var nome = "Italia";
                yield "Codice paese " + nome + " (IT)";
            }
            case "FR" -> {
                var nome = "Francia";
                yield "Codice paese " + nome + " (FR)";
            }
            default -> {
                yield "Codice sconosciuto: " + codice;
            }
        };
    }

    /**
     * Con {@code switch} su {@code enum} (Java 5) il compilatore non poteva verificare la
     * completezza. Con lo switch expression su enum, se aggiungi una costante e non gestisci
     * il caso, ottieni un errore di compilazione: siamo tornati alla sicurezza dei vecchi switch.
     */
    public static StringPriorita prioritaDiDefault(int urgenza) {
        return switch (urgenza) {
            case 1 -> StringPriorita.ALTA;
            case 2 -> StringPriorita.MEDIA;
            default -> StringPriorita.BASSA;
        };
    }

    public enum StringPriorita {
        ALTA, MEDIA, BASSA
    }

    public static void main(String[] args) {
        for (int g = 1; g <= 7; g++) {
            System.out.println(g
                    + " -> PRIMA: " + giornoPrima(g)
                    + " | DOPO: " + giornoDopo(g)
                    + " | " + tipoGiorno(g));
        }

        System.out.println("priorita ALTA -> " + codicePriorita(StringPriorita.ALTA));
        System.out.println("descrizione   -> " + descrizioneLunga("IT"));
        System.out.println("priorita 2    -> " + prioritaDiDefault(2));
    }
}