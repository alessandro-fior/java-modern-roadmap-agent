package it.example.modernjava.records;

/**
 * Record generico: si puo' scrivere {@code Risultato<String>}, {@code Risultato<Integer>}, ...
 * come per una classe generica qualsiasi.
 * <p>
 * E' il modo naturale di modellare un risultato che puo' essere riuscito oppure no:
 * con {@code null} il caso "non riuscito" e il caso "valore assente" si confondono.
 *
 * @param ok     indica se l'operazione e' andata a buon fine
 * @param valore valore prodotto, presente solo se {@code ok} e' true
 * @param <T>    tipo del valore
 */
public record Risultato<T>(boolean ok, T valore) {

    public static <T> Risultato<T> ok(T valore) {
        return new Risultato<>(true, valore);
    }

    public static <T> Risultato<T> errore() {
        return new Risultato<>(false, null);
    }
}
