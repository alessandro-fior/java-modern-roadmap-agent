// Compact source file - Java 25 (JEP 512)
// Pattern matching (Java 16) e switch pattern matching su gerarchia sealed (Java 21).
//
// Esecuzione:  java examples/newmain/PatternMatchingEsempio.java

sealed interface Risultato permits Successo, Fallimento {

    String messaggio();

    record Successo(String messaggio, Object valore) implements Risultato {
    }

    record Fallimento(String messaggio) implements Risultato {
    }
}

void main() {
    // 1. instanceof con pattern (Java 16): niente cast
    Object oggetto = "Java 25";

    if (oggetto instanceof String s) {
        IO.println("instanceof -> lunghezza " + s.length());
    }

    // 2. switch pattern matching su gerarchia sealed: nessun default, il compilatore
    //    sa che i casi sono completi
    List<Risultato> risultati = List.of(
            new Risultato.Successo("OK", 42),
            new Risultato.Fallimento("Errore")
    );
    risultati.forEach(r -> IO.println("switch     -> " + descrivi(r)));

    // 3. record pattern (Java 21): i componenti diventano variabili
    descriviTutto(new Risultato.Successo("OK", 42));
    descriviTutto(new Risultato.Fallimento("Errore"));

    // 4. switch con guard
    IO.println("guard      -> " + grandezza(new Risultato.Successo("OK", List.of(1, 2, 3, 4))));
}

String descrivi(Risultato risultato) {
    return switch (risultato) {
        case Successo s -> "Successo: " + s.messaggio() + " (" + s.valore() + ")";
        case Fallimento f -> "Fallimento: " + f.messaggio();
    };
}

void descriviTutto(Object oggetto) {
    switch (oggetto) {
        case Risultato.Successo(String messaggio, Object valore) ->
                IO.println("record     -> messaggio=" + messaggio + ", valore=" + valore);
        case Risultato.Fallimento(String messaggio) ->
                IO.println("record     -> messaggio=" + messaggio);
        default ->
                IO.println("record     -> altro tipo");
    }
}

String grandezza(Risultato risultato) {
    return switch (risultato) {
        case Successo(String messaggio, List<?> valori) when valori.size() > 2 ->
                "tanti risultati (" + valori.size() + ")";
        case Successo(String messaggio, Object valore) ->
                "un solo risultato: " + messaggio + " = " + valore;
        case Fallimento(String messaggio) ->
                "nessun risultato: " + messaggio;
    };
}