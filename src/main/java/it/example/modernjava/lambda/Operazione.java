package it.example.modernjava.lambda;

/**
 * Una {@code @FunctionalInterface} scritta in casa: ha esattamente un metodo astratto.
 * <p>
 * Dichiarare {@code @FunctionalInterface} non e' obbligatorio, ma e' utile per due motivi:
 * il compilatore verifica che la regola sia rispettata, e la documentazione dice al
 * lettore "qui si puo' passare un comportamento".
 * <p>
 * I metodi {@code default} e {@code static} non contano: il contratto funzionale resta
 * {@link #applica(int, int)}.
 */
@FunctionalInterface
public interface Operazione {

    int applica(int a, int b);

    default String descrizione() {
        return "operazione su due interi";
    }

    static Operazione somma() {
        return (a, b) -> a + b;
    }

    default Operazione piu(int costante) {
        return (a, b) -> applica(a, b) + costante;
    }
}
