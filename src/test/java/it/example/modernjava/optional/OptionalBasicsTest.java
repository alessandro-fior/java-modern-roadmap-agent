package it.example.modernjava.optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Optional (Java 8)")
class OptionalBasicsTest {

    @Test
    @DisplayName("il metodo 'prima' restituisce null quando l'indice non esiste")
    void primaRestituisceNull() {
        assertEquals("Mario", OptionalBasics.trovaNomePrima(0));
        assertNull(OptionalBasics.trovaNomePrima(9));
    }

    @Test
    @DisplayName("il metodo 'dopo' non restituisce mai null")
    void dopoNonRestituisceNull() {
        assertEquals(Optional.of("Mario"), OptionalBasics.trovaNome(0));
        assertEquals(Optional.empty(), OptionalBasics.trovaNome(9));
    }

    @Test
    @DisplayName("ifPresent esegue l'azione solo se il valore c'e'")
    void ifPresent() {
        var trovati = new StringBuilder();
        var mancanti = new StringBuilder();

        OptionalBasics.trovaNome(0).ifPresent(trovati::append);
        OptionalBasics.trovaNome(9).ifPresent(mancanti::append);

        assertEquals("Mario", trovati.toString());
        assertEquals("", mancanti.toString());
    }

    @Test
    @DisplayName("orElse ha un fallback fisso, orElseGet un fallback calcolato")
    void valoriDiDefault() {
        assertEquals("Mario", OptionalBasics.trovaNome(0).orElse("sconosciuto"));
        assertEquals("sconosciuto", OptionalBasics.trovaNome(9).orElse("sconosciuto"));
        assertEquals("sconosciuto", OptionalBasics.trovaNome(9).orElseGet(() -> "sconosciuto"));
        assertThrows(NoSuchElementException.class, () -> OptionalBasics.trovaNome(9).orElseThrow());
    }

    @Test
    @DisplayName("map trasforma il valore e propaga il vuoto")
    void map() {
        assertEquals(Optional.of(5), OptionalBasics.trovaNome(0).map(String::length));
        assertEquals(Optional.empty(), OptionalBasics.trovaNome(9).map(String::length));
    }

    @Test
    @DisplayName("filter scarta il valore se la condizione non e' soddisfatta")
    void filter() {
        assertTrue(OptionalBasics.trovaNome(0).filter(n -> n.length() > 10).isEmpty());
        assertEquals(Optional.of("Mario"), OptionalBasics.trovaNome(0).filter(n -> n.length() > 2));
    }

    @Test
    @DisplayName("map + filter si possono concatenare")
    void concatenazione() {
        Optional<Integer> risultato = OptionalBasics.trovaNome(0)
                .map(String::toUpperCase)
                .filter(n -> n.startsWith("MA"))
                .map(String::length);

        assertEquals(Optional.of(5), risultato);
    }

    @Test
    @DisplayName("OptionalInt evita il boxing dei primitivi")
    void optionalNonBoxable() {
        assertEquals(42, java.util.OptionalInt.of(42).getAsInt());
        assertTrue(java.util.OptionalInt.empty().isEmpty());
        assertFalse(java.util.OptionalInt.of(42).isEmpty());
    }
}