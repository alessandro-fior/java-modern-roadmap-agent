// Compact source file - Java 25 (JEP 512)
// Le classi di java.base sono importate automaticamente: List, Map, Optional
// e String funzionano senza alcun import.
//
// Esecuzione:  java examples/newmain/Variabili.java

final String NOME = "Alessandro";   // un campo e' un campo della classe implicita
int conteggio = 0;                  // una variabile globale, se serve

void main() {
    // Esempio 1: inferenza del tipo locale (var, Java 10)
    var nome = "Mario";
    var anni = 45;
    var nomi = List.of("Mario", "Luigi", "Anna");

    // nome = 100;   // non compila: var non e' tipizzazione dinamica

    IO.println("nome    -> " + nome);
    IO.println("anni    -> " + anni);
    IO.println("nomi    -> " + nomi);
    IO.println("NOME    -> " + NOME);

    // Esempio 2: metodi e campi della classe implicita sono direttamente disponibili
    conta("Mario");
    conta("Luigi");
    conta("Anna");
    IO.println("conteggio -> " + conta);

    // Esempio 3: lambda e method reference (Java 8)
    nomi.forEach(System.out::println);
    nomi.stream()
            .map(String::toUpperCase)
            .forEach(n -> IO.println("maiuscolo -> " + n));
}

void conta(String nome) {
    conteggio++;
    IO.println("contato " + nome);
}