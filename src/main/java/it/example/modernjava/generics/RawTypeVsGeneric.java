package it.example.modernjava.generics;

import java.util.ArrayList;
import java.util.List;

/**
 * PRIMA / DOPO: dai raw type ai Generics.
 * <p>
 * I Generics sono una funzionalita di <b>Java 5</b> (JSR 14).
 * <p>
 * Prima: la lista non dice nulla su cosa contiene, quindi ogni lettura richiede un cast
 * e un errore di cast arriva solo a runtime, quando il programma e' gia' in esecuzione.
 * Dopo: il tipo e' dichiarato una volta sola e il compilatore fa da solo il controllo.
 */
public final class RawTypeVsGeneric {

    private RawTypeVsGeneric() {
    }

    /**
     * PRIMA (stile pre-Java 5): raw type, nessuna type safety.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List rawType() {
        List lista = new ArrayList();

        lista.add("Mario");
        lista.add("Luigi");
        lista.add(42); // nessun errore di compilazione: si puo' aggiungere un Integer!

        // Il cast e' obbligatorio... e puo' fallire a runtime.
        String nome = (String) lista.get(0);
        System.out.println("PRIMA  -> nome letto con cast: " + nome);

        return lista;
    }

    /**
     * DOPO: la lista dichiara cosa contiene.
     */
    public static List<String> generic() {
        List<String> nomi = new ArrayList<>();

        nomi.add("Mario");
        nomi.add("Luigi");

        // nomi.add(42); // <-- non compila: incompatible types: int cannot be converted to String

        String nome = nomi.get(0); // nessun cast
        System.out.println("DOPO   -> nome letto senza cast: " + nome);

        return nomi;
    }

    /**
     * Perche' il raw type e' pericoloso: l'errore emerge solo a runtime.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void erroreDiRuntime() {
        List lista = new ArrayList();
        lista.add("Mario");

        Object primo = lista.get(0);

        System.out.println("Valore grezzo letto come Object: " + primo);
        // Con Generics questo codice non sarebbe nemmeno scrivibile:
        // List<String> nomi = new ArrayList<>();
        // Object primo = nomi.get(0);   // String primo -> errore di COMPILAZIONE
    }

    public static void main(String[] args) {
        rawType();
        generic();
        erroreDiRuntime();
    }
}