// JEP 534 - Compact Object Headers by Default - Java 27
// JEP 536 - JFR In-Process Data Redaction - Java 27
//
// Entrambe sono definitive in Java 27 e non richiedono codice: sono impostazioni della JVM.
//
// 1) Compact Object Headers: riducono l'intestazione di ogni oggetto da 12/16 byte a 8 byte.
//    Con molti milioni di oggetti vivi il risparmio di memoria e' significativo.
//
// 2) JFR In-Process Data Redaction: nasconde automaticamente valori sensibili nelle
//    registrazioni JFR (chiavi, password, token). Si controlla con le opzioni
//    -XX:FlightRecorderOptions:redact-key e -XX:FlightRecorderOptions:redact-argument.
//
// Esecuzione:
//   java examples/java27/ObjectHeadersDemo.java
//   java -XX:+UnlockExperimentalVMOptions -XX:+UseCompactObjectHeaders examples/java27/ObjectHeadersDemo.java
//   java -XX:FlightRecorderOptions:redact-key=true examples/java27/ObjectHeadersDemo.java
//
// Suggerimento: misurare l'heap con  jcmd <pid> GC.heap_info

import com.sun.management.HotSpotDiagnosticMXBean;
import java.lang.management.ManagementFactory;

public class ObjectHeadersDemo {

    static final class Semplice {
        int a;
        int b;
    }

    public static void main(String[] args) {
        System.out.println("Compact Object Headers attivi : " + compactHeadersAttivi());

        // Con 5 milioni di oggetti vivi, 4 byte in meno per intestazione fanno differenza.
        var vivi = new Semplice[5_000_000];
        for (int i = 0; i < vivi.length; i++) {
            vivi[i] = new Semplice();
            vivi[i].a = i;
        }

        var runtime = Runtime.getRuntime();
        long usati = runtime.totalMemory() - runtime.freeMemory();

        System.out.printf("Oggetti vivi: %,d%n", vivi.length);
        System.out.printf("Heap in uso : %,.1f MB%n", usati / (1024.0 * 1024.0));
        System.out.println("Confronta il risultato con e senza Compact Object Headers.");
    }

    static boolean compactHeadersAttivi() {
        var bean = ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);

        return bean.getVMOption("UseCompactObjectHeaders").getValue();
    }
}