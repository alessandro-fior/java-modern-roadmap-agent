package it.example.modernjava.records;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Record (Java 16)")
class RecordsUsageTest {

    @Test
    @DisplayName("il record espone gli accessor con il nome del campo")
    void accessor() {
        var cliente = new ClienteRecord(100, "Mario");

        assertEquals(100, cliente.id());
        assertEquals("Mario", cliente.nome());
        assertEquals("100 - Mario", cliente.descrizione());
    }

    @Test
    @DisplayName("la classe tradizionale ha bisogno dei getter")
    void legacy() {
        var cliente = new ClienteLegacy(100, "Mario");

        assertEquals(100, cliente.getId());
        assertEquals("Mario", cliente.getNome());
    }

    @Test
    @DisplayName("equals e hashCode confrontano il valore")
    void uguaglianza() {
        var a = new ClienteRecord(100, "Mario");
        var b = new ClienteRecord(100, "Mario");
        var c = new ClienteRecord(101, "Mario");

        assertEquals(a, b);
        assertNotEquals(a, c);
        assertEquals(a.hashCode(), b.hashCode());

        var set = new HashSet<ClienteRecord>();
        set.add(a);
        set.add(b);
        assertEquals(1, set.size());
    }

    @Test
    @DisplayName("il costruttore compatto valida e normalizza")
    void costruttoreCompatto() {
        assertEquals("Mario", new ClienteRecord(1, "  Mario  ").nome());
        assertThrows(IllegalArgumentException.class, () -> new ClienteRecord(0, "Mario"));
        assertThrows(IllegalArgumentException.class, () -> new ClienteRecord(-5, "Mario"));
    }

    @Test
    @DisplayName("la factory statica produce un record valido")
    void factory() {
        assertEquals("Anna", ClienteRecord.di(7, "Anna").nome());
        assertEquals(7, ClienteRecord.di(7, "Anna").id());
    }

    @Test
    @DisplayName("i record generici funzionano come una classe generica")
    void generico() {
        Risultato<String> ok = Risultato.ok("valore");
        Risultato<Integer> errore = Risultato.errore();

        assertTrue(ok.ok());
        assertEquals("valore", ok.valore());
        assertFalse(errore.ok());
    }

    @Test
    @DisplayName("i record dentro una lista si ordinano e si filtrano")
    void inCollezioni() {
        List<ClienteRecord> clienti = List.of(
                new ClienteRecord(2, "Luigi"),
                new ClienteRecord(1, "Mario")
        );

        var nomi = clienti.stream()
                .map(ClienteRecord::nome)
                .sorted()
                .toList();

        assertEquals(List.of("Luigi", "Mario"), nomi);
    }

    @Test
    @DisplayName("toString mostra tutti i componenti")
    void toString() {
        var testo = new ClienteRecord(1, "Mario").toString();

        assertTrue(testo.contains("id=1"));
        assertTrue(testo.contains("nome=Mario"));
    }
}