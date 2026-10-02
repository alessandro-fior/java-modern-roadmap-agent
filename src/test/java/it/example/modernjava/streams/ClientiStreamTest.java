package it.example.modernjava.streams;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test dell'esempio centrale: la pipeline su una lista di clienti.
 */
@DisplayName("Stream API: prima / dopo producono lo stesso risultato")
class ClientiStreamTest {

    private static final List<Cliente> ATTESI_ID = List.of(1, 2, 3, 4, 5, 6);

    @Test
    @DisplayName("1. filter: il ciclo e lo stream selezionano gli stessi elementi")
    void filterCoincide() {
        assertEquals(ClientiStream.maggiorenniPrima(), ClientiStream.maggiorenni());
        assertEquals(List.of(1, 3, 4, 6), ClientiStream.maggiorenni().stream().map(Cliente::id).toList());
    }

    @Test
    @DisplayName("2. map: il ciclo e lo stream estraggono gli stessi nomi")
    void mapCoincide() {
        assertEquals(ClientiStream.nomiPrima(), ClientiStream.nomi());
        assertEquals(6, ClientiStream.nomi().size());
    }

    @Test
    @DisplayName("3. sorted: i nomi dei maggiorenni sono in ordine alfabetico")
    void sorted() {
        assertEquals(
                List.of("Anna", "Giulia", "Mario", "Paolo"),
                ClientiStream.nomiMaggiorenniOrdinati());
    }

    @Test
    @DisplayName("4. distinct: i duplicati vengono rimossi")
    void distinct() {
        assertEquals(List.of(3, 1, 2), StreamBasics.distinti());
    }

    @Test
    @DisplayName("5. count: il conteggio manuale e quello dello stream coincidono")
    void countCoincide() {
        assertEquals(StreamBasics.countPrima(), StreamBasics.countDopo());
        assertEquals(3L, ClientiStream.numeroMaggiorenni());
    }

    @Test
    @DisplayName("6. toList: il risultato e' immutabile")
    void toListImmutabile() {
        var lista = StreamBasics.toListImmutabile();

        assertEquals(List.of(20, 40), lista);
        assertThrows(UnsupportedOperationException.class, () -> lista.add(999));
    }

    @Test
    @DisplayName("7. groupingBy: pari e dispari")
    void groupingBy() {
        Map<Integer, List<Integer>> atteso = Map.of(0, List.of(10, 20, 30, 40, 50));

        assertEquals(atteso, StreamBasics.pariEDispari());
    }

    @Test
    @DisplayName("8. max: il numero piu' grande")
    void max() {
        assertEquals(Optional.of(50), StreamBasics.massimo());
    }

    @Test
    @DisplayName("9. ricerca con Optional: presente e assente")
    void ricercaConOptional() {
        assertEquals("Anna", ClientiStream.perId(3).map(Cliente::nome).orElse("?"));
        assertTrue(ClientiStream.perId(99).isEmpty());
    }

    @Test
    @DisplayName("10. funzione di gruppo: una sola astrazione per tutti gli accessor")
    void funzioneDiGruppo() {
        assertEquals(ClientiStream.nomi(), ClientiStream.estrai(Cliente::nome));
        assertEquals(List.of(1, 2, 3, 4, 5, 6), ClientiStream.estrai(Cliente::id));
        assertEquals(ATTESI_ID, ClientiStream.estrai(Cliente::id));
    }

    @Test
    @DisplayName("11. partizione: due gruppi complementari")
    void partizione() {
        var partizione = ClientiStream.partizionePerEta();

        assertEquals(4, partizione.get(true).size());
        assertEquals(2, partizione.get(false).size());
    }

    @Test
    @DisplayName("12. eta' media arrotondata per difetto")
    void etaMedia() {
        // (25 + 17 + 32 + 45 + 16 + 51) / 6 = 186 / 6 = 31
        assertEquals(Optional.of(31), ClientiStream.etaMedia());
    }

    @Test
    @DisplayName("13. i record hanno uguaglianza per valore")
    void uguaglianzaRecord() {
        var a = new Cliente(1, "Mario", 25, "Milano");

        assertEquals(a, new Cliente(1, "Mario", 25, "Milano"));
        assertNotEquals(a, new Cliente(2, "Mario", 25, "Milano"));
    }

    @Test
    @DisplayName("14. il metodo derivato del record")
    void metodiDerivati() {
        assertEquals("maggioreenne", new Cliente(1, "Mario", 25, "Milano").fasciaEta());
        assertEquals("minorenne", new Cliente(2, "Luigi", 17, "Roma").fasciaEta());
        assertEquals("neonato", new Cliente(3, "Bimbo", 0, "Roma").fasciaEta());
    }
}