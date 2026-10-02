package it.example.modernjava.optional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

/**
 * PRIMA / DOPO: il problema del {@code null}.
 * <p>
 * {@link Optional} e' definitivo da <b>Java 8</b> (JSR 310).
 * <p>
 * {@code null} non e' un valore: e' l'assenza di un valore, e il compilatore non puo' aiutarti
 * a distinguerlo da un valore reale. {@code Optional} mette l'assenza dentro il tipo,
 * cosi' il compilatore ti costringe a gestirla.
 */
public final class OptionalBasics {

    private static final List<String> NOMI = List.of("Mario", "Luigi", "Anna");

    private OptionalBasics() {
    }

    /**
     * PRIMA: il chiamante deve ricordarsi del controllo {@code != null}.
     */
    @SuppressWarnings("ReturnNull")
    public static String trovaNomePrima(int indice) {
        if (indice >= 0 && indice < NOMI.size()) {
            return NOMI.get(indice);
        }
        return null;
    }

    /**
     * DOPO: l'assenza e' parte del tipo di ritorno.
     */
    public static Optional<String> trovaNome(int indice) {
        if (indice >= 0 && indice < NOMI.size()) {
            return Optional.of(NOMI.get(indice));
        }
        return Optional.empty();
    }

    public static void ifPresent() {
        System.out.println("-- ifPresent: esegue solo se il valore c'e'");

        trovaNome(1).ifPresent(System.out::println);
        trovaNome(9).ifPresent(System.out::println); // non stampa nulla
    }

    public static void valoriDiDefault() {
        System.out.println("-- orElse / orElseGet / orElseThrow");

        // orElse: il valore di fallback viene valutato SEMPRE, anche quando non serve.
        String conOrElse = trovaNome(0).orElse("sconosciuto");
        System.out.println("orElse(0)      -> " + conOrElse);

        String conOrElseGet = trovaNome(9).orElseGet(() -> "sconosciuto");
        System.out.println("orElseGet(9)   -> " + conOrElseGet);

        // orElseThrow: se il valore manca, il programma non ha senso: si propaga un'eccezione.
        String conOrElseThrow = trovaNome(0).orElseThrow();
        System.out.println("orElseThrow(0) -> " + conOrElseThrow);
    }

    public static void mapEfilter() {
        System.out.println("-- map / filter");

        // map: trasforma il valore, mantenendo l'Optional (vuoto resta vuoto).
        Optional<Integer> lunghezza = trovaNome(1).map(String::length);
        System.out.println("map lunghezza  -> " + lunghezza);

        Optional<Integer> lunghezzaVuota = trovaNome(9).map(String::length);
        System.out.println("map su vuoto   -> " + lunghezzaVuota);

        // filter: tiene il valore solo se soddisfa la condizione.
        Optional<String> nomeLungo = trovaNome(0).filter(n -> n.length() > 10);
        System.out.println("filter corto   -> " + nomeLungo);

        Optional<String> nomeBreve = trovaNome(0).filter(n -> n.length() > 2);
        System.out.println("filter giusto  -> " + nomeBreve);
    }

    public static void eccezioneEsplicita() {
        System.out.println("-- orElseThrow con eccezione propria");

        String nome = trovaNome(9).orElseThrow(
                () -> new NoSuchElementException("nessun nome all'indice richiesto"));

        System.out.println(nome); // non raggiunto: l'eccezione viene lanciata prima
    }

    public static void catenaDiFunzioni() {
        System.out.println("-- catena di transformazioni");

        Function<Optional<String>, Optional<String>> maiuscolo =
                o -> o.map(String::toUpperCase);

        System.out.println("maiuscolo(0)   -> " + maiuscolo.apply(trovaNome(0)));
        System.out.println("maiuscolo(9)   -> " + maiuscolo.apply(trovaNome(9)));
    }

    public static void main(String[] args) {
        System.out.println("NOMI           -> " + NOMI);
        ifPresent();
        valoriDiDefault();
        mapEfilter();
        catenaDiFunzioni();

        try {
            eccezioneEsplicita();
        } catch (NoSuchElementException e) {
            System.out.println("eccezione      -> " + e.getMessage());
        }
    }
}