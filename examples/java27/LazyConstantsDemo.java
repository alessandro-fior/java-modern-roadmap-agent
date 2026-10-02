// JEP 531 - Lazy Constants (Third Preview) - Java 27
//
// ATTENZIONE: API in PREVIEW. Serve --enable-preview sia in compilazione sia in esecuzione.
// Questo file NON fa parte della build Maven del progetto.
//
// Esecuzione:
//   java --enable-preview examples/java27/LazyConstantsDemo.java
//
// Cosa fa: una "lazy constant" e' un valore immutabile la cui inizializzazione avviene
// al primo accesso, non nel costruttore. A differenza di un campo non-final nullable,
// garantisce che il valore sia calcolato una e una sola volta, anche con piu' thread,
// e resta eleggibile per le ottimizzazioni di constant folding del JVM.

import java.util.List;
import java.util.Map;
import java.util.Set;

public class LazyConstantsDemo {

    // Una lazy constant: il Logger viene creato solo al primo get().
    private static final LazyConstant<String> IMPOSTAZIONE =
            LazyConstant.of(() -> "caricata da " + costruisci());

    private static final LazyConstant<String> CONFIGURAZIONE =
            LazyConstant.of(() -> "impostazioni lette da disco (costoso)");

    // Una lazy list: gli elementi vengono creati solo quando servono.
    private static final List<String> POOL = List.ofLazy(4, i -> "risorsa-" + i);

    // Una lazy map: le chiavi sono note, i valori sono calcolati alla prima richiesta.
    private static final Map<String, String> SERVIZI =
            Map.ofLazy(Set.of("utenti", "ordini"), chiave -> "servizio per " + chiave);

    public static void main(String[] args) {
        System.out.println("Il programma e' partito: nessuna costruzione costosa e' avvenuta.");
        System.out.println("primo accesso IMPOSTAZIONE  -> " + IMPOSTAZIONE.get());
        System.out.println("secondo accesso              -> " + IMPOSTAZIONE.get());

        System.out.println("CONFIGURAZIONE              -> " + CONFIGURAZIONE.get());

        System.out.println("POOL (tutti gli elementi)   ->");
        for (int i = 0; i < POOL.size(); i++) {
            System.out.println("   " + POOL.get(i));
        }

        System.out.println("SERVIZI                     ->");
        System.out.println("   " + SERVIZI.get("utenti"));
        System.out.println("   " + SERVIZI.get("ordini"));

        System.out.println("Il valore non cambia: le lazy constant sono immutabili dopo l'inizializzazione.");
    }

    private static String costruisci() {
        return "file di configurazione";
    }
}