package it.example.modernjava.switchexpression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.example.modernjava.switchexpression.SwitchExpressions.StringPriorita;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Switch expression (Java 14)")
class SwitchExpressionsTest {

    @Test
    @DisplayName("lo switch expression produce gli stessi risultati dello switch classico")
    void coincideConIlVecchioSwitch() {
        for (int giorno = 1; giorno <= 7; giorno++) {
            assertEquals(
                    SwitchExpressions.giornoPrima(giorno),
                    SwitchExpressions.giornoDopo(giorno),
                    "differenza per il giorno " + giorno);
        }
    }

    @Test
    @DisplayName("i giorni nominati")
    void giorniNominati() {
        assertEquals("Lunedi", SwitchExpressions.giornoDopo(1));
        assertEquals("Martedi", SwitchExpressions.giornoDopo(2));
        assertEquals("Mercoledi", SwitchExpressions.giornoDopo(3));
        assertEquals("Altro", SwitchExpressions.giornoDopo(9));
    }

    @Test
    @DisplayName("piu' etichette per lo stesso risultato")
    void weekend() {
        assertEquals("Weekend", SwitchExpressions.tipoGiorno(6));
        assertEquals("Weekend", SwitchExpressions.tipoGiorno(7));
        assertEquals("Giornata lavorativa", SwitchExpressions.tipoGiorno(1));
    }

    @Test
    @DisplayName("lo switch expression si usa ovunque serva un valore")
    void codicePriorita() {
        assertEquals(1, SwitchExpressions.codicePriorita(StringPriorita.ALTA));
        assertEquals(2, SwitchExpressions.codicePriorita(StringPriorita.MEDIA));
        assertEquals(3, SwitchExpressions.codicePriorita(StringPriorita.BASSA));
    }

    @Test
    @DisplayName("le yield restituiscono il valore di un blocco")
    void yield() {
        assertEquals("Codice paese Italia (IT)", SwitchExpressions.descrizioneLunga("IT"));
        assertEquals("Codice paese Francia (FR)", SwitchExpressions.descrizioneLunga("FR"));
        assertEquals("Codice sconosciuto: XX", SwitchExpressions.descrizioneLunga("XX"));
    }

    @Test
    @DisplayName("su enum la completezza e' verificata dal compilatore")
    void prioritaDiDefault() {
        assertEquals(StringPriorita.ALTA, SwitchExpressions.prioritaDiDefault(1));
        assertEquals(StringPriorita.MEDIA, SwitchExpressions.prioritaDiDefault(2));
        assertEquals(StringPriorita.BASSA, SwitchExpressions.prioritaDiDefault(99));
    }

    @Test
    @DisplayName("i valori fuori dall'intervallo gestito finiscono nel default")
    void fuoriIntervallo() {
        assertEquals("Altro", SwitchExpressions.giornoDopo(-1));
        assertEquals("Altro", SwitchExpressions.giornoDopo(0));
        assertEquals("Altro", SwitchExpressions.giornoDopo(100));
    }
}