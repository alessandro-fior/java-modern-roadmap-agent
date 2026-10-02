package it.example.modernjava.records;

import java.util.List;

/**
 * DOPO: la stessa classe DTO scritta come <b>record</b> (definitivo da <b>Java 16</b>, JEP 395).
 * <p>
 * Dal record nascono automaticamente: campi {@code private final}, costruttore canonico,
 * accessor {@code id()} e {@code nome()}, {@code equals}, {@code hashCode}, {@code toString}.
 * Non si possono aggiungere campi non dichiarati nell'intestazione.
 * <p>
 * Attenzione: l'accessore e' {@code nome()}, non {@code getNome()}. E un dettaglio, ma
 * e' quello che rende il record diverso da una classe JavaBean.
 *
 * @param id   identificativo
 * @param nome nome completo
 */
public record ClienteRecord(int id, String nome) {

    /**
     * Costruttore compatto: utile per normalizzare i dati in ingresso.
     */
    public ClienteRecord {
        if (id <= 0) {
            throw new IllegalArgumentException("id deve essere positivo: " + id);
        }
        nome = nome == null ? "" : nome.strip();
    }

    /**
     * I record possono avere metodi "derivati" e metodi statici.
     */
    public String descrizione() {
        return id + " - " + nome;
    }

    public static ClienteRecord di(int id, String nome) {
        return new ClienteRecord(id, nome);
    }
}

/**
 * Record generico: si puo' scrivere {@code Risultato<String>}, {@code Risultato<Integer>}, ...
 * come per una classe generica qualsiasi.
 *
 * @param ok    indica se l'operazione e' andata a buon fine
 * @param valore valore prodotto, presente solo se {@code ok} e' true
 * @param <T>   tipo del valore
 */
public record Risultato<T>(boolean ok, T valore) {

    public static <T> Risultato<T> ok(T valore) {
        return new Risultato<>(true, valore);
    }

    public static <T> Risultato<T> errore() {
        return new Risultato<>(false, null);
    }
}

/**
 * Record "compattato": senza il par {@code nome; nome()}, la lista dei campi va fra parentesi.
 */
record Pagina<T>(int numero, List<T> contenuto) {

    Pagina {
        contenuto = List.copyOf(contenuto); // difensiva: la lista resta immutabile
    }

    boolean vuota() {
        return contenuto.isEmpty();
    }
}