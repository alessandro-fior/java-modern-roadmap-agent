package it.example.modernjava.generics;

import java.util.ArrayList;
import java.util.List;

/**
 * PRIMA / DOPO: i wildcard.
 * <p>
 * I Generics sono una funzionalita di <b>Java 5</b>.
 * <p>
 * I wildcard servono quando il tipo esatto non e' noto ma e' noto il rapporto con un altro tipo.
 * La regola da ricordare e' una sola, <b>PECS</b>:
 * <pre>
 *     Producer Extends, Consumer Super
 * </pre>
 * {@code ? extends T} = posso LEGGERE (producer): il tipo concreto e' un sottotipo di T.
 * {@code ? super T}   = posso SCRIVERE (consumer): il tipo concreto e' un supertipo di T.
 */
public final class Wildcards {

    private Wildcards() {
    }

    /**
     * {@code List<?>}: il tipo esatto e' ignoto. Si puo' leggere, ma solo come {@code Object},
     * e non si puo' scrivere nulla (a parte {@code null}).
     */
    public static void wildcardIllimitato() {
        List<String> sorgente = new ArrayList<>();
        sorgente.add("Mario");
        sorgente.add("Luigi");

        List<?> valori = sorgente;

        // valori.add("Anna");   // <-- non compila: il tipo esatto e' ignoto

        Object primo = valori.get(0);
        System.out.println("List<?>              -> primo = " + primo);
        System.out.println("List<?>              -> size  = " + valori.size());
    }

    /**
     * {@code List<? extends Number>}: lista che SICURAMENTE contiene solo numeri.
     * Uso tipico: producer, cioe' la lista viene letta e filtrata.
     */
    public static double sommaNumeri(List<? extends Number> numeri) {
        double totale = 0;
        for (Number n : numeri) {
            totale += n.doubleValue();
        }
        // numeri.add(1);   // <-- non compila: produttore, non consumatore
        return totale;
    }

    /**
     * {@code List<? super Integer>}: lista che puo' ricevere Integer e qualsiasi suo supertipo.
     * Uso tipico: consumer, cioe' nella lista si scrivono valori e poi la si legge come Object.
     */
    public static void aggiungiInteri(List<? super Integer> destinazione) {
        destinazione.add(10);
        destinazione.add(20);
        destinazione.add(30);
        // La lista di destinazione potrebbe essere List<Number> o List<Object>:
        // per questo, dopo aver scritto, possiamo solo rileggere come Object.
        Object primo = destinazione.get(0);
        System.out.println("List<? super Integer> -> primo riletto come Object = " + primo);
    }

    public static void main(String[] args) {
        wildcardIllimitato();
        System.out.println("sommaNumeri(List<Integer>)  -> " + sommaNumeri(List.of(1, 2, 3)));
        System.out.println("sommaNumeri(List<Double>)   -> " + sommaNumeri(List.of(1.5, 2.5)));
        aggiungiInteri(new ArrayList<Number>());
    }
}