package it.example.modernjava.newmain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Il nuovo modello del {@code main} non e' una funzione di libreria, quindi non ha molto
 * da asserire. Qui si verifica che i metodi di istanza siano invocabili come qualsiasi
 * altro metodo: e' cioe' il punto in cui il codice diventa testabile senza dover passare
 * dalla riga di comando.
 */
@DisplayName("Nuovo main (Java 25)")
class NuovoMainTest {

    @Test
    @DisplayName("il main di istanza e' invocabile come un normale metodo")
    void mainDiIstanza() {
        var istanza = new NuovoMain();

        // non stampa nulla: il metodo contiene solo output, nessuna asserzione.
        istanza.main();
    }

    @Test
    @DisplayName("i campi dell'istanza sono disponibili")
    void campiDellIstanza() {
        var istanza = new NuovoMain();

        assertEquals("Java 5 -> Java 8 -> Java 25", istanza.diario());
    }

    @Test
    @DisplayName("il record annidato mantiene l'accessor col nome del campo")
    void recordAnnidato() {
        var cliente = new NuovoMain.Cliente(100, "Alessandro", 45);

        assertEquals(100, cliente.id());
        assertEquals("Alessandro", cliente.nome());
        assertEquals(45, cliente.eta());
    }

    @Test
    @DisplayName("lo switch expression dentro il record")
    void fasciaEta() {
        assertEquals("neonato", new NuovoMain.Cliente(1, "A", 0).fasciaEta());
        assertEquals("minorenne", new NuovoMain.Cliente(2, "B", 10).fasciaEta());
        assertEquals("maggioreenne", new NuovoMain.Cliente(3, "C", 18).fasciaEta());
    }

    @Test
    @DisplayName("il main di istanza con String[] riceve gli argomenti")
    void mainConArgomenti() {
        new MainConArgomenti().main(new String[] { "10", "20", "testo", "30" });
        new MainConArgomenti().main(new String[0]);
    }

    @Test
    @DisplayName("il launcher del vecchio main funziona ancora")
    void vecchioMain() {
        VecchioMain.main(new String[] { "a", "b" });
        VecchioMain.main(new String[0]);
    }

    @Test
    @DisplayName("la lista creata con List.of e' immutabile")
    void listaImmutabile() {
        var nomi = List.of("Mario", "Luigi");

        assertEquals(2, nomi.size());
        assertTrue(nomi.contains("Mario"));
    }
}