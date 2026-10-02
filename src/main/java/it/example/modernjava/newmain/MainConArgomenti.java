package it.example.modernjava.newmain;

/**
 * Il {@code main} di istanza <b>con</b> argomenti: definitivo da <b>Java 25</b> (JEP 512).
 * <p>
 * Se il programma ha bisogno degli argomenti della riga di comando, il metodo puo' restare
 * di istanza e dichiarare il solo parametro {@code String[] args}:
 * il launcher crea l'istanza e poi invoca {@code main} passando i parametri come elementi
 * dell'array, esattamente come fa con il {@code main} statico.
 */
public final class MainConArgomenti {

    void main(String[] args) {
        IO.println("Numero di argomenti: " + args.length);

        if (args.length == 0) {
            IO.println("Nessun argomento: passo un valore predefinito.");
        }

        for (int i = 0; i < args.length; i++) {
            IO.println("  args[" + i + "] = " + args[i]);
        }

        IO.println("Media numerica degli argomenti: " + mediaNumerica(args));
    }

    /**
     * Il calcolo ausiliario resta un normale metodo di istanza: la firma del {@code main}
     * non ha niente a che fare con il resto della classe.
     */
    private double mediaNumerica(String[] args) {
        double somma = 0;
        int conteggio = 0;

        for (String arg : args) {
            try {
                somma += Double.parseDouble(arg);
                conteggio++;
            } catch (NumberFormatException e) {
                // argomento non numerico: lo ignoriamo
            }
        }

        return conteggio == 0 ? 0 : somma / conteggio;
    }
}