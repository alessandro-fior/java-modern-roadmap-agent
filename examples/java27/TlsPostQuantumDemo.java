// JEP 527 - Post-Quantum Hybrid Key Exchange for TLS 1.3 - Java 27
// Definito in Java 27, nessuna preview: si attiva con una proprieta' di sistema.
//
// Questo file dimostra che la crittografia post-quantum e' gia' il DEFAULT in Java 27:
// non serve cambiare codice, basta verificare quali gruppi negoziati vengono usati.
//
// Esecuzione:
//   java examples/java27/TlsPostQuantumDemo.java
// (oppure: jshell examples/java27/TlsPostQuantumDemo.java)

import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.util.Arrays;

public class TlsPostQuantumDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("Java in uso: " + System.getProperty("java.version"));

        var factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket socket = (SSLSocket) factory.createSocket();

        try {
            SSLParameters parametri = socket.getSSLParameters();
            System.out.println("Protocolli abilitati : " + parametri.getProtocols().length);
            System.out.println("Algoritmi TLS         : " + parametri.getCipherSuites().length);

            // Il gruppo post-quantum X25519MLKEM768 e' fra i nomi offerti di default
            // nelle implementazioni Java 27.
            boolean postQuantumPresente = Arrays.stream(parametri.getNamedGroups())
                    .anyMatch(n -> n.toUpperCase().contains("MLKEM") || n.toUpperCase().contains("ML-KEM"));

            System.out.println("Gruppo post-quantum offerto di default: " + postQuantumPresente);

            System.out.println();
            System.out.println("Se vuoi disattivare la negoziazione post-quantum:");
            System.out.println("  java -Djdk.tls.namedGroups=X25519:secp256r1 ...");
        } finally {
            socket.close();
        }

        // Verifica opzionale su un server pubblico (richiede rete).
        if (args.length > 0) {
            String host = args[0];
            int porta = args.length > 1 ? Integer.parseInt(args[1]) : 443;

            try (SSLSocket s = (SSLSocket) factory.createSocket(host, porta)) {
                s.startHandshake();

                System.out.println();
                System.out.println("Connessione a " + host + ":" + porta);
                System.out.println("  protocollo negoziato : " + s.getSession().getProtocol());
                System.out.println("  cipher suite         : " + s.getSession().getCipherSuite());
            }
        }
    }
}