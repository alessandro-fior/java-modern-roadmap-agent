package it.example.modernjava.patternmatching;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Pattern matching (Java 16 instanceof, Java 21 switch e record pattern)")
class PatternMatchingTest {

    @Test
    @DisplayName("instanceof con pattern: risultato identico alla versione con cast")
    void instanceofCoincide() {
        Object[] valori = { "Java", 42, 3.14, new InstanceofPattern.Punto(1, 2), null };

        for (Object v : valori) {
            assertEquals(InstanceofPattern.prima(v), InstanceofPattern.dopo(v),
                    "differenza per il valore " + v);
        }
    }

    @Test
    @DisplayName("instanceof con pattern: si puo' aggiungere un tipo senza toccare gli altri")
    void instanceofEsteso() {
        assertEquals("Optional (true)", InstanceofPattern.dopo(Optional.of("x")));
        assertEquals("Optional (false)", InstanceofPattern.dopo(Optional.empty()));
        assertEquals("Punto(1, 2)", InstanceofPattern.descrivi(new InstanceofPattern.Punto(1, 2)));
    }

    @Test
    @DisplayName("instanceof con pattern: il tipo viene dedotto, senza cast")
    void instanceofPattern() {
        assertEquals("String di lunghezza 4", InstanceofPattern.dopo("Java"));
        assertEquals("Intero pari? true", InstanceofPattern.dopo(42));
        assertEquals("Altro tipo: 3.14", InstanceofPattern.dopo(3.14));
    }

    @Test
    @DisplayName("instanceof con null: il pattern non viene valutato, si puo' gestire a parte")
    void instanceofConNull() {
        assertEquals("Altro tipo: null", InstanceofPattern.dopo(null));
        assertFalse(InstanceofPattern.numeroPrimo(null));
        assertTrue(InstanceofPattern.numeroPrimo(7));
        assertFalse(InstanceofPattern.numeroPrimo(8));
    }

    @Test
    @DisplayName("record pattern: i componenti diventano variabili del caso")
    void recordPattern() {
        var successo = new Risultato.Successo("OK", 42);

        assertEquals("record pattern su Successo: OK = 42", SealedHierarchy.recordPattern(successo));
        assertEquals("record pattern su Fallimento: Errore",
                SealedHierarchy.recordPattern(new Risultato.Fallimento("Errore")));
    }

    @Test
    @DisplayName("record pattern: le guard raffinano il caso")
    void guard() {
        assertTrue(SealedHierarchy.recordPattern(Optional.of("vincitore")).contains("vincitore"));
        assertEquals("Optional vuoto", SealedHierarchy.recordPattern(Optional.empty()));
        assertEquals("null", SealedHierarchy.recordPattern(null));
        assertEquals("tipo non riconosciuto", SealedHierarchy.recordPattern("testo qualsiasi"));
    }

    @Test
    @DisplayName("switch su gerarchia sealed: nessun default necessario")
    void switchSuSealed() {
        var esempi = SealedHierarchy.esempi();

        assertEquals(3, esempi.size());

        for (Risultato r : esempi) {
            // Se i due metodi dessero risultati diversi, il compilatore del progetto
            // non se ne accorgerebbe: e' il test che garantisce l'equivalenza.
            assertEquals(SealedHierarchy.descriviPrima(r), SealedHierarchy.descriviDopo(r));
        }
    }

    @Test
    @DisplayName("switch con guard su una gerarchia chiusa")
    void switchConGuard() {
        assertEquals("Cerchio grande (r=150.0)", SealedHierarchy.descriviForma(new SealedHierarchy.Cerchio(150)));
        assertEquals("Cerchio piccolo (r=5.0)", SealedHierarchy.descriviForma(new SealedHierarchy.Cerchio(5)));
        assertEquals("Rettangolo 3.0x4.0",
                SealedHierarchy.descriviForma(new SealedHierarchy.Rettangolo(3, 4)));
    }

    @Test
    @DisplayName("i tre tipi permessi sono quelli dichiarati")
    void gerarchiaChiusa() {
        List<String> nomi = SealedHierarchy.esempi().stream()
                .map(r -> r.getClass().getSimpleName())
                .toList();

        assertEquals(List.of("Successo", "Fallimento", "InCorso"), nomi);
    }
}