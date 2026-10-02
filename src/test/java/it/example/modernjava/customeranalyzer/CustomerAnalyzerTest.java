package it.example.modernjava.customeranalyzer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test del mini progetto finale.
 * <p>
 * {@link Cliente} e' un record: le sue validazioni stanno nel costruttore compatto,
 * quindi ogni istanza creata nei test e' gia' valida.
 */
class CustomerAnalyzerTest {

    private static final List<Cliente> CLIENTI = CustomerAnalyzer.CLIENTI_ESEMPIO;

    @Nested
    @DisplayName("1. Stampa di tutti i clienti")
    class Stampa {

        @Test
        @DisplayName("produce una riga per cliente")
        void unaRigaPerCliente() {
            String[] righe = CustomerAnalyzer.stampaTutti(CLIENTI).split(System.lineSeparator());

            assertEquals(CLIENTI.size(), righe.length);
        }

        @Test
        @DisplayName("una lista vuota produce una stringa vuota")
        void listaVuota() {
            assertEquals("", CustomerAnalyzer.stampaTutti(List.of()));
        }
    }

    @Nested
    @DisplayName("2-3. Filtro ed estrazione")
    class Filtro {

        @Test
        @DisplayName("i maggiorenni sono 3 e sono ordinati per nome")
        void maggiorenni() {
            var risultato = CustomerAnalyzer.maggiorenni(CLIENTI);

            assertEquals(3, risultato.size());
            assertEquals(List.of("Anna", "Mario", "Paolo"), risultato.stream().map(Cliente::nome).toList());
        }

        @Test
        @DisplayName("i minorenni sono 2")
        void minorenni() {
            var risultato = CustomerAnalyzer.minorenni(CLIENTI);

            assertEquals(2, risultato.size());
            assertEquals(List.of("Laura", "Luigi"), risultato.stream().map(Cliente::nome).toList());
        }

        @Test
        @DisplayName("la somma di maggiorenni e minorenni e' il totale")
        void partizioneCompleta() {
            assertEquals(CLIENTI.size(),
                    CustomerAnalyzer.maggiorenni(CLIENTI).size()
                            + CustomerAnalyzer.minorenni(CLIENTI).size());
        }

        @Test
        @DisplayName("l'estrazione dei nomi conserva l'ordine della lista")
        void nomi() {
            assertEquals(List.of("Mario", "Luigi", "Anna", "Paolo", "Laura"), CustomerAnalyzer.nomi(CLIENTI));
        }
    }

    @Nested
    @DisplayName("4. Ordinamento")
    class Ordinamento {

        @Test
        @DisplayName("per eta' decrescente poi per nome crescente")
        void perEtaPoiNome() {
            var risultato = CustomerAnalyzer.perEtaPoiNome(CLIENTI);

            assertEquals("Paolo", risultato.get(0).nome());
            assertEquals("Anna", risultato.get(1).nome());
            assertEquals("Mario", risultato.get(2).nome());
            assertEquals("Luigi", risultato.get(3).nome());
            assertEquals("Laura", risultato.get(4).nome());
        }
    }

    @Nested
    @DisplayName("5. Conteggi e statistiche")
    class Statistiche {

        @Test
        @DisplayName("numero di maggiorenni")
        void numeroMaggiorenni() {
            assertEquals(3L, CustomerAnalyzer.numeroMaggiorenni(CLIENTI));
        }

        @Test
        @DisplayName("raggruppamento per citta'")
        void perCitta() {
            var perCitta = CustomerAnalyzer.numeroPerCitta(CLIENTI);

            assertEquals(2L, perCitta.get("Milano"));
            assertEquals(2L, perCitta.get("Roma"));
            assertEquals(1L, perCitta.get("Napoli"));
            assertEquals(3, perCitta.size());
        }

        @Test
        @DisplayName("eta' media troncata a 27.0")
        void etaMedia() {
            // (25 + 17 + 32 + 45 + 16) / 5 = 135 / 5 = 27.0
            assertEquals(Optional.of(27.0), CustomerAnalyzer.etaMedia(CLIENTI));
        }

        @Test
        @DisplayName("su lista vuota l'eta' media e' Optional.empty(), non 0")
        void etaMediaSuVuota() {
            assertTrue(CustomerAnalyzer.etaMedia(List.of()).isEmpty());
        }
    }

    @Nested
    @DisplayName("6. Ricerca con Optional")
    class Ricerca {

        @Test
        @DisplayName("ricerca per id trovato")
        void perIdTrovato() {
            var cliente = CustomerAnalyzer.perId(CLIENTI, 3);

            assertTrue(cliente.isPresent());
            assertEquals("Anna", cliente.orElseThrow().nome());
        }

        @Test
        @DisplayName("ricerca per id inesistente: Optional vuoto, non null")
        void perIdNonTrovato() {
            Optional<Cliente> cliente = CustomerAnalyzer.perId(CLIENTI, 99);

            assertTrue(cliente.isEmpty());
            assertEquals("sconosciuto", cliente.map(Cliente::nome).orElse("sconosciuto"));
        }

        @Test
        @DisplayName("il risultato non e' mai null")
        void maiNull() {
            assertFalse(CustomerAnalyzer.perId(CLIENTI, -1) == null);
        }

        @Test
        @DisplayName("ricerca per nome case-insensitive")
        void perNome() {
            assertTrue(CustomerAnalyzer.perNome(CLIENTI, "MARIO").isPresent());
            assertTrue(CustomerAnalyzer.perNome(CLIENTI, "mario").isPresent());
            assertTrue(CustomerAnalyzer.perNome(CLIENTI, "Pippo").isEmpty());
        }

        @Test
        @DisplayName("orElseThrow quando l'id non esiste")
        void perIdOppure() {
            assertThrows(NoSuchElementException.class, () -> CustomerAnalyzer.perIdOppure(CLIENTI, 99));
        }
    }

    @Nested
    @DisplayName("7. Riepilogo e report")
    class Riepilogo {

        @Test
        @DisplayName("il riepilogo conta tutto correttamente")
        void conteggi() {
            var r = CustomerAnalyzer.riepilogo(CLIENTI);

            assertEquals(5, r.totale());
            assertEquals(3L, r.maggiorenni());
            assertEquals(2L, r.minorenni());
            assertEquals(Optional.of(27.0), r.etaMedia());
            assertEquals(List.of("Anna", "Mario", "Paolo"), r.nomiMaggiorenni());
        }

        @Test
        @DisplayName("il report contiene le sezioni attese")
        void report() {
            String report = CustomerAnalyzer.report(CLIENTI);

            assertTrue(report.contains("CUSTOMER ANALYZER"));
            assertTrue(report.contains("Clienti caricati: 5"));
            assertTrue(report.contains("Maggiorenni: 3"));
            assertTrue(report.contains("Minorenni: 2"));
            assertTrue(report.contains("Eta' media: 27.0"));
        }

        @Test
        @DisplayName("su lista vuota il report non contiene una media fasulla")
        void reportSuVuota() {
            String report = CustomerAnalyzer.report(List.of());

            assertTrue(report.contains("Clienti caricati: 0"));
            assertTrue(report.contains("Eta' media: n.d."));
        }
    }

    @Nested
    @DisplayName("8. Sintesi con switch expression")
    class Sintassi {

        @Test
        void nessuno() {
            assertEquals("Nessun cliente caricato", CustomerAnalyzer.sintassi(List.of()));
        }

        @Test
        void uno() {
            assertEquals("Un solo cliente", CustomerAnalyzer.sintassi(List.of(CLIENTI.get(0))));
        }

        @Test
        void molti() {
            assertEquals("Base dati consistente (5 schede)", CustomerAnalyzer.sintassi(CLIENTI));
        }
    }

    @Nested
    @DisplayName("Il record Cliente")
    class RecordCliente {

        @Test
        @DisplayName("equals confronta il valore, non il riferimento")
        void uguaglianza() {
            assertEquals(new Cliente(1, "Mario", 25, "Milano"), new Cliente(1, "Mario", 25, "Milano"));
        }

        @Test
        @DisplayName("l'accessor ha il nome del campo, non getNome()")
        void accessor() {
            assertEquals("Mario", new Cliente(1, "Mario", 25, "Milano").nome());
        }

        @Test
        @DisplayName("i metodi derivati funzionano")
        void derivati() {
            var adulto = new Cliente(1, "Mario", 25, "Milano");
            var ragazzo = new Cliente(2, "Luigi", 17, "Roma");

            assertTrue(adulto.maggiorenne());
            assertFalse(adulto.minorenne());
            assertTrue(ragazzo.minorenne());
        }

        @Test
        @DisplayName("il costruttore compatto valida i dati")
        void validazione() {
            assertThrows(IllegalArgumentException.class, () -> new Cliente(0, "Mario", 25, "Milano"));
            assertThrows(IllegalArgumentException.class, () -> new Cliente(1, "  ", 25, "Milano"));
            assertThrows(IllegalArgumentException.class, () -> new Cliente(1, "Mario", -1, "Milano"));
            assertThrows(IllegalArgumentException.class, () -> new Cliente(1, "Mario", 200, "Milano"));
        }

        @Test
        @DisplayName("toString e' leggibile")
        void toStringLeggibile() {
            assertTrue(new Cliente(1, "Mario", 25, "Milano").toString().contains("Mario"));
        }
    }
}