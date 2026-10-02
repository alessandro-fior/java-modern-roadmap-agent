package it.example.modernjava.lambda;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Le interfacce funzionali della libreria standard (<b>Java 8</b>, package {@code java.util.function}).
 * <p>
 * Una {@code @FunctionalInterface} ha esattamente un metodo astratto. Quando si dichiara
 * {@code @FunctionalInterface} il compilatore verifica la regola, e serve anche alla
 * documentazione: e' il " contratto" che il metodo accetta come comportamento.
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

/**
 * PRIMA / DOPO: passare comportamento con una sola classe annidata.
 */
public final class FunctionalInterfaces {

    private FunctionalInterfaces() {
    }

    public static void interfacceStandard() {
        Supplier<String> fornitore = () -> "ciao";
        Function<String, Integer> lunghezza = String::length;
        Predicate<String> nonVuoto = s -> !s.isEmpty();
        BiFunction<Integer, Integer, Integer> somma = Integer::sum;
        Comparator<String> perLunghezza = Comparator.comparingInt(String::length);

        System.out.println("Supplier     -> " + fornitore.get());
        System.out.println("Function     -> " + lunghezza.apply("Java 25"));
        System.out.println("Predicate    -> nonVuoto(\"\") = " + nonVuoto.test(""));
        System.out.println("BiFunction   -> " + somma.apply(2, 40));
        System.out.println("Comparator   -> " + perLunghezza.compare("ab", "abcd"));
    }

    public static void interfacciaCustom() {
        Operazione addizione = Operazione.somma();
        Operazione moltiplicazione = (a, b) -> a * b;

System.out.println("addizione       -> " + addizione.applica(20, 22));
        System.out.println("moltiplicazione -> " + moltiplicazione.applica(20, 2));
        System.out.println("addizione.piu(1)-> " + addizione.piu(1).applica(1, 2));
        System.out.println("descrizione     -> " + addizione.descrizione());
    }

    /**
     * DOPO: la logica si scrive inline, nel punto in cui serve, senza una classe dedicata.
     */
    static List<Integer> raddoppia(List<Integer> numeri) {
        return numeri.stream().map(n -> n * 2).toList();
    }

    public static void main(String[] args) {
        interfacceStandard();
        interfacciaCustom();
        System.out.println("raddoppia      -> " + raddoppia(List.of(1, 2, 3)));
    }
}