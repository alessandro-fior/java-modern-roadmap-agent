package it.example.modernjava.generics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * I Generics sono una feature di compilazione: il compilatore garantisce la type safety,
 * quindi i test verificano soprattutto che le API restino coerenti.
 */
@DisplayName("Generics (Java 5)")
class GenericsTest {

    @Test
    @DisplayName("il raw type accetta qualsiasi oggetto")
    @SuppressWarnings("unchecked")
    void rawType() {
        var risultato = RawTypeVsGeneric.rawType();

        assertEquals(3, risultato.size());
        assertInstanceOf(String.class, risultato.get(0));
    }

    @Test
    @DisplayName("la lista generica contiene solo String")
    void listaGenerica() {
        var nomi = RawTypeVsGeneric.generic();

        assertEquals(List.of("Mario", "Luigi"), nomi);
    }

    @Test
    @DisplayName("Contenitore<String> e Contenitore<Integer> sono lo stesso tipo")
    void contenitore() {
        Contenitore<String> nomi = new Contenitore<>();
        Contenitore<Integer> numeri = new Contenitore<>();

        nomi.set("Mario");
        numeri.set(42);

        assertEquals("Mario", nomi.get());
        assertEquals(42, numeri.get());
    }

    @Test
    @DisplayName("il metodo statico generico max funziona su qualunque Comparable")
    void max() {
        assertEquals("Mario", Contenitore.max("Anna", "Mario"));
        assertEquals(9, Contenitore.max(3, 9));
        assertEquals(4.5, Contenitore.max(4.5, 2.0).doubleValue());
    }

    @Test
    @DisplayName("sommaNumeri accetta List<Integer> e List<Double>")
    void wildcardProducer() {
        assertEquals(6.0, Wildcards.sommaNumeri(List.of(1, 2, 3)));
        assertEquals(4.0, Wildcards.sommaNumeri(List.of(1.5, 2.5)));
    }

    @Test
    @DisplayName("un List<? super Integer> puo' essere un List<Number>")
    void wildcardConsumer() {
        List<Number> numeri = new ArrayList<>();
        Wildcards.aggiungiInteri(numeri);

        assertEquals(List.of(10, 20, 30), numeri);
    }

    @Test
    @DisplayName("List<?> puo' essere letto solo come Object")
    void wildcardIllimitato() {
        List<?> valori = new ArrayList<String>();
        valori.add("Mario");

        assertTrue(valori.get(0) instanceof String);
    }
}