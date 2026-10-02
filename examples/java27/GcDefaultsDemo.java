// JEP 523 - Make G1 the Default Garbage Collector in All Environments - Java 27
// Definito, nessuna preview, nessun codice richiesto: basta una flag.
//
// In Java 27 G1 e' il garbage collector predefinito anche su macchine con molta memoria
// o con un solo processore, dove prima era richiesto specificarlo esplicitamente.
//
// Esecuzione con il default di Java 27 (G1):
//   java -Xlog:gc examples/java27/GcDefaultsDemo.java
//   java -XX:+UseG1GC examples/java27/GcDefaultsDemo.java     <- stesso collector, specificato
// Confronto con un collector diverso (non equivalente, serve solo come riferimento):
//   java -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC examples/java27/GcDefaultsDemo.java

public class GcDefaultsDemo {

    public static void main(String[] args) {
        System.out.println("GC attivo:");
        for (var gc : java.lang.management.ManagementFactory.getGarbageCollectorMXBeans()) {
            System.out.println("  " + gc.getName() + " -> " + gc.getCollectionCount() + " raccolte");
        }

        // Ciclo di allocazione e raccolta, per vedere lavorare il collector.
        for (int round = 1; round <= 5; round++) {
            var dati = new byte[8 * 1024 * 1024]; // 8 MB
            for (int i = 0; i < dati.length; i += 4096) {
                dati[i] = (byte) i;
            }
            System.out.println("  round " + round + " allocati 8 MB, somma a campione "
                    + (dati.length / 4096));
        }

        System.gc();
        System.out.println("Fine.");
    }
}