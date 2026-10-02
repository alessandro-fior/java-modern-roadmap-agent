package it.example.modernjava.newmain;

import java.util.List;

/**
 * PRIMA: il {@code main} di sempre (<b>Java 8 e precedenti</b>).
 * <p>
 * Tre requisiti storici: {@code public}, {@code static}, {@code String[] args}.
 * Sono convenzioni del 1995, non esigenze tecniche, e servono solo perche' il launcher
 * doveva poter trovare il punto di ingresso con la minima assunzione possibile.
 *
 * @see NuovoMain
 */
public final class VecchioMain {

    private VecchioMain() {
    }

    public static void main(String[] args) {
        System.out.println("Ciao Java");

        if (args.length > 0) {
            System.out.println("Argomenti ricevuti: " + args.length);
        }
    }
}