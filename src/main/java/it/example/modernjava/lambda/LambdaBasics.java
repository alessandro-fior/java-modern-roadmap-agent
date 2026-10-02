package it.example.modernjava.lambda;

import java.util.List;

/**
 * PRIMA / DOPO: ciclo for, lambda e method reference.
 * <p>
 * Le espressioni lambda e i method reference sono una funzionalita di <b>Java 8</b> (JSR 335).
 * <p>
 * Messaggio chiave: una lambda non e' una scorciatoia di sintassi, e' un modo di passare
 * <b>comportamento</b> a un metodo. Il metodo non dice piu' "esegui questo codice", dice
 * "esegui questo comportamento", e il chiamante decide quale.
 */
public final class LambdaBasics {

    private LambdaBasics() {
    }

    /**
     * PRIMA (stile funzionale pre-Java 8): una classe anonima.
     * Un'interfaccia con un solo metodo (SAM) doveva essere implementata da una classe.
     */
    public static void anonymousClass(List<String> nomi) {
        Runnable stampa = new Runnable() {
            @Override
            public void run() {
                nomi.forEach(n -> System.out.println("anon-class  -> " + n));
            }
        };
        stampa.run();
    }

    /**
     * DOPO: la stessa cosa in una riga.
     */
    public static void lambda(List<String> nomi) {
        nomi.forEach(nome -> System.out.println("lambda      -> " + nome));
    }

    /**
     * DOPO: quando il corpo e' gia' un metodo esistente, si usa un method reference.
     * {@code System.out::println} e' "il metodo println di System.out", non un nuovo codice.
     */
    public static void methodReference(List<String> nomi) {
        nomi.forEach(System.out::println);
    }

    /**
     * Il for-each non e' sparito: e' la scelta giusta quando si vuole solo iterare.
     */
    public static void forEachClassico(List<String> nomi) {
        for (String nome : nomi) {
            System.out.println("for-each    -> " + nome);
        }
    }

    public static void main(String[] args) {
        var nomi = List.of("Mario", "Luigi", "Anna");

        forEachClassico(nomi);
        anonymousClass(nomi);
        lambda(nomi);
        methodReference(nomi);
    }
}