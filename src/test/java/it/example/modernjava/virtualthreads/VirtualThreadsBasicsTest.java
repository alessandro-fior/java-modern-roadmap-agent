package it.example.modernjava.virtualthreads;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Virtual Threads (Java 21)")
class VirtualThreadsBasicsTest {

    private static final int TIMEOUT_SECONDI = 30;

    @Test
    @DisplayName("Thread.startVirtualThread crea un thread di istanza virtuale")
    void threadVirtuale() throws InterruptedException {
        var eseguito = new AtomicInteger();

        Thread virtuale = Thread.startVirtualThread(eseguito::incrementAndGet);
        Thread piattaforma = new Thread(eseguito::incrementAndGet);

        virtuale.join();
        piattaforma.join();

        assertTrue(virtuale.isVirtual());
        assertFalse(piattaforma.isVirtual());
        assertEquals(2, eseguito.get());
    }

    @Test
    @DisplayName("Thread.ofVirtual() permette di nominare il thread")
    void threadOfVirtual() throws InterruptedException {
        var nome = new AtomicReference<String>();

        Thread t = Thread.ofVirtual()
                .name("demo-1")
                .start(() -> nome.set(Thread.currentThread().name()));

        t.join();

        assertTrue(t.isVirtual());
        assertEquals("demo-1", nome.get());
    }

    @Test
    @DisplayName("newVirtualThreadPerTaskExecutor esegue tutti i task")
    void executorPerTask() {
        int totale = 100;
        var completati = new AtomicInteger();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < totale; i++) {
                executor.submit(completati::incrementAndGet);
            }
        } // close() attende la fine di tutti i task

        assertEquals(totale, completati.get());
    }

    @Test
    @DisplayName("500 virtual thread possono essere attivi contemporaneamente")
    void concorrenza() throws InterruptedException {
        int totale = 500;
        var completati = new AtomicInteger();
        var latch = new CountDownLatch(totale);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < totale; i++) {
                executor.submit(() -> {
                    completati.incrementAndGet();
                    latch.countDown();
                });
            }

            assertTrue(latch.await(TIMEOUT_SECONDI, TimeUnit.SECONDS), "timeout in attesa dei task");
        }

        assertEquals(totale, completati.get());
    }

    @Test
    @DisplayName("un pool di 5 platform thread non supera 5 task contemporanei")
    void poolTradizionale() throws InterruptedException {
        int totale = 50;
        var simultanei = new AtomicInteger();
        var massimo = new AtomicInteger();
        var latch = new CountDownLatch(totale);

        var executor = Executors.newFixedThreadPool(5);
        try {
            for (int i = 0; i < totale; i++) {
                executor.submit(() -> {
                    int attivi = simultanei.incrementAndGet();
                    massimo.updateAndGet(precedente -> Math.max(precedente, attivi));

                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        simultanei.decrementAndGet();
                        latch.countDown();
                    }
                });
            }

            assertTrue(latch.await(TIMEOUT_SECONDI, TimeUnit.SECONDS), "timeout in attesa dei task");
        } finally {
            executor.shutdown();
        }

        assertTrue(massimo.get() <= 5,
                "con 5 platform thread i task contemporanei non possono superare 5, trovati: " + massimo.get());
    }
}