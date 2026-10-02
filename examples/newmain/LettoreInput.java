// Compact source file - Java 25 (JEP 512)
// java.lang.IO: la nuova classe per la console introdotta in Java 25.
//
// Esecuzione con input:  echo "Mario" | java examples/newmain/LettoreInput.java
//
// IO sta in java.lang, quindi non serve importarlo. I suoi metodi sono statici,
// ma nei compact source file NON sono importati automaticamente: si deve scrivere IO.readln(...).
// Nota: IO.readln() restituisce null quando lo standard input finisce.

void main() {
    IO.print("Come ti chiami? ");
    String nome = leggiRiga("nescun nome");

    IO.print("Quanti anni hai? ");
    int eta = leggiIntero();

    IO.println("Ciao " + nome + ", hai " + eta + " anni.");
    IO.println("Il maggiore di eta': " + Math.max(eta, 18));
}

String leggiRiga(String fallback) {
    String riga = IO.readln();
    return riga == null ? fallback : riga.strip();
}

int leggiIntero() {
    for (int tentativo = 1; tentativo <= 3; tentativo++) {
        String testo = leggiRiga("");
        try {
            return Integer.parseInt(testo);
        } catch (NumberFormatException e) {
            IO.println("  '" + testo + "' non e' un numero, riprova.");
        }
    }
    return 0;
}