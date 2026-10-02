package it.example.modernjava.virtualthreads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * I <b>Virtual Threads</b>: definitivi da <b>Java 21</b> (JEP 444).
 * <p>
 * Un thread tradizionale del sistema operativo costa circa 1 MB di memoria di stack e viene
 * schedulato dall'OS: creare 1 milione di thread non e' realistico.
 * Un Virtual Thread e' un thread gestito dalla JVM, montato su una <b>platform thread</b> solo
 * mentre sta davvero eseguendo codice. Crearne 1 milione costa qualche megabyte.
 * <p>
 * Quindi: i Virtual Threads risolvono il problema dei programmi <b>I/O-bound</b>
 * (che passano il tempo ad aspettare la rete, un file, un database), non quello dei programmi
 * <b>CPU-bound</b> (che stanno sempre a calcolare: per quelli il limite resta la CPU).
 * Non sono automaticamente "piu' veloci": sono piu' economici in concorrenza.
 */
public final class VirtualThreadsBasics {

    private VirtualThreadsBasics() {
    }

    /**
     * Il modo piu' semplice: un metodo statico che avvia un Virtual Thread.
     */
    public static void singolo() {
        Thread.startVirtualThread(() -> {
            System.out.println("[virtual] Sto lavorando in un Virtual Thread");
            System.out.println("[virtual] nome thread    = " + Thread.currentThread().name());
            System.out.println("[virtual] e' virtuale?   = " + Thread.currentThread().isVirtual());
        });
    }

    /**
     * L'equivalente "vecchio stile" con un {@code Runnable}, utile per confrontare.
     * Da Java 21 esiste anche {@code Thread.ofVirtual().start(...)}.
     */
    public static void singoloConExecutor() {
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        try {
            executor.submit(() -> System.out.println("[executor] task su Virtual Thread"));
        } finally {
            executor.close(); // Java 19+: close() e' equivalente a shutdown() + awaitTermination()
        }
    }

    /**
     * PRIMA: {@code newFixedThreadPool(n)} con un pool limitato.
     * Con 1.000 task su un pool di 5 thread, si eseguono 5 task alla volta.
     */
    public static void poolTradizionale(int task) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        var contatore = new AtomicInteger();

        try {
            for (int i = 0; i < task; i++) {
                int id = i;
                pool.submit(() -> {
                    simulaAttesaIo();
                    contatore.incrementAndGet();
                    if (id % 250 == 0) {
                        System.out.println("[pool]   task " + id + " completata");
                    }
                });
            }
        } finally {
            pool.shutdown();
            pool.awaitTermination(1, TimeUnit.MINUTES);
        }

        System.out.println("[pool]   completati " + contatore.get() + "/" + task + " task su 5 thread");
    }

    /**
     * DOPO: {@code newVirtualThreadPerTaskExecutor()}, un Virtual Thread per task.
     * Tutti i task partono subito, e il codice e' identico a quello del pool.
     */
    public static void executorVirtuale(int task) throws InterruptedException {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < task; i++) {
                int id = i;
                executor.submit(() -> {
                    simulaAttesaIo();
                    if (id % 250 == 0) {
                        System.out.println("[virtual] task " + id + " completata su " + Thread.currentThread().name());
                    }
                });
            }
        } // qui close() attende la fine di tutti i task

        System.out.println("[virtual] completati " + task + "/" + task + " task su 1.000 Virtual Thread");
    }

    /**
     * Simula un'attesa di I/O: il thread e' bloccato, ma non consuma risorse della JVM.
     * In un programma vero qui ci sarebbe una chiamata HTTP, JDBC, o File I/O.
     */
    private static void simulaAttesaIo() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Confronto diretto: {@code Thread.ofVirtual()} + {@code StructuredTaskScope}.
     * Nota: {@code StructuredTaskScope} e' ancora in preview, quindi non viene usato qui
     * per non costringere a compilare con {@code --enable-preview}.
     */
    public static void confronti() throws InterruptedException {
        Thread piattaforma = Thread.ofPlatform()
                .name("piattaforma-1")
                .start(() -> System.out.println("piattaforma -> virtuale? " + Thread.currentThread().isVirtual()));

        Thread virtuale = Thread.ofVirtual()
                .name("virtuale-1")
                .start(() -> System.out.println("virtuale    -> virtuale? " + Thread.currentThread().isVirtual()));

        piattaforma.join();
        virtuale.join();
    }

    public static void main(String[] args) throws InterruptedException {
        singolo();
        singoloConExecutor();

        System.out.println();
        confronti();

        System.out.println();
        long inizio = System.nanoTime();
        poolTradizionale(1_000);
        long dopoPool = System.nanoTime();
        System.out.println("pool tradizionale  -> " + ((dopoPool - inizio) / 1_000_000) + " ms");

        inizio = System.nanoTime();
        executorVirtuale(1_000);
        long dopoVirtuale = System.nanoTime();
        System.out.println("virtual thread     -> " + ((dopoVirtuale - inizio) / 1_000_000) + " ms");
    }
}