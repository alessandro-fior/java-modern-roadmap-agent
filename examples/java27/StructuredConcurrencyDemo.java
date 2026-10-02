// JEP 533 - Structured Concurrency (Seventh Preview) - Java 27
//
// ATTENZIONE: API in PREVIEW. Serve --enable-preview sia in compilazione sia in esecuzione.
// Questo file NON fa parte della build Maven del progetto.
//
// Esecuzione:
//   java --enable-preview examples/java27/StructuredConcurrencyDemo.java
//
// Cosa fa: la concorrenza strutturata tratta un gruppo di task come UNA unita' di lavoro.
// Se uno dei sotto-task fallisce, gli altri vengono cancellati automaticamente; quando si
// esce dal blocco try-with-resources, tutti i thread dei sotto-task sono gia' terminati.
// Con ExecutorService questi legami devono essere gestiti a mano, e si perdono i thread.

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeUnit;

public class StructuredConcurrencyDemo {

    public static void main(String[] args) throws InterruptedException {

        // 1. Politica di default: se un sotto-task fallisce, lo scope viene cancellato
        //    e join() propaga l'eccezione.
        System.out.println("--- default: fall if any fails ---");
        try {
            caricaDueCose();
        } catch (ExecutionException e) {
            System.out.println("catturato: " + e.getCause());
        }

        // 2. Joiner allSuccessfulOrThrow(): tutti devono riuscire, e join() restituisce
        //    la lista dei risultati.
        System.out.println("--- allSuccessfulOrThrow ---");
        System.out.println(tutti() .stream().map(String::toUpperCase).toList());

        // 3. Joiner anySuccessfulOrThrow(): basta il primo che riesce, gli altri vengono
        //    cancellati. Utile con servizi ridondanti.
        System.out.println("--- anySuccessfulOrThrow ---");
        System.out.println(ilPrimoCheRiesce());

        // 4. Concorrenza massima: 20 task, ma ognuno dura poco.
        System.out.println("--- 20 task in parallelo ---");
        System.out.println(elaboraInParallelo(20));
    }

    private static void caricaDueCose() throws InterruptedException {
        try (var scope = StructuredTaskScope.open()) {
            var utente = scope.fork(() -> {
                sleep(50);
                return "utente-ok";
            });
            var ordine = scope.<String>fork(() -> {
                sleep(30);
                throw new IllegalStateException("ordine non trovato");
            });

            scope.join();  // appena un sotto-task fallisce, l'altro viene cancellato
            System.out.println(utente.get() + " / " + ordine.get());
        }
    }

    private static List<String> tutti() throws InterruptedException {
        try (var scope = StructuredTaskScope.open(
                StructuredTaskScope.Joiner.<String>allSuccessfulOrThrow())) {

            scope.fork(() -> {
                sleep(40);
                return "alpha";
            });
            scope.fork(() -> {
                sleep(10);
                return "beta";
            });
            scope.fork(() -> "gamma");

            return scope.join();
        }
    }

    private static String ilPrimoCheRiesce() throws InterruptedException {
        try (var scope = StructuredTaskScope.open(
                StructuredTaskScope.Joiner.<String>anySuccessfulOrThrow())) {

            scope.fork(() -> {
                sleep(80);
                return "servizio lento";
            });
            scope.fork(() -> {
                sleep(10);
                return "servizio veloce";
            });

            return scope.join();
        }
    }

    private static long elaboraInParallelo(int totale) throws InterruptedException {
        long inizio = System.nanoTime();

        try (var scope = StructuredTaskScope.open(
                StructuredTaskScope.Joiner.<Long>allSuccessfulOrThrow())) {

            for (int i = 0; i < totale; i++) {
                scope.fork(() -> {
                    sleep(50);
                    return 1L;
                });
            }

            return scope.join().stream().mapToLong(Long::longValue).sum();
        } finally {
            System.out.println("elapsed: " + (System.nanoTime() - inizio) / 1_000_000 + " ms per "
                    + totale + " task da 50 ms");
        }
    }

    private static void sleep(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}