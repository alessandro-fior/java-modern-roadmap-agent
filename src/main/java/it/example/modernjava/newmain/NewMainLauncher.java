package it.example.modernjava.newmain;

/**
 * Punto di ingresso statico che delega ai {@code main} di istanza.
 * <p>
 * Due motivi per cui questo file esiste:
 * <ol>
 *   <li>raccoglie i demo in un unico comando: {@code mvn -q exec:java} / {@code java ... NewMainLauncher};</li>
 *   <li>mostra che {@code void main()} di istanza e {@code static void main(String[])} non possono
 *       convivere nella stessa classe (stessa firma), quindi servono due classi.</li>
 * </ol>
 */
public final class NewMainLauncher {

    private NewMainLauncher() {
    }

    public static void main(String[] args) {
        System.out.println("=== VecchioMain (public static void main(String[] args)) ===");
        VecchioMain.main(args);

        System.out.println();
        System.out.println("=== NuovoMain (void main() di istanza, Java 25) ===");
        var istanza = new NuovoMain();
        istanza.main();

        System.out.println();
        System.out.println("=== MainConArgomenti (void main(String[] args) di istanza) ===");
        new MainConArgomenti().main(args);
    }
}