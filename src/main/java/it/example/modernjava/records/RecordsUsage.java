package it.example.modernjava.records;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * PRIMA / DOPO: la classe DTO tradizionale ({@link ClienteLegacy}) e il record ({@link ClienteRecord}).
 * <p>
 * I <b>record</b> sono definitivi da <b>Java 16</b> (JEP 395), dopo due round di preview
 * (Java 14 e Java 15).
 * <p>
 * Un record descrive un <b>valore</b>: e' immutabile, ha identita' basata sui suoi campi ed e'
 * confrontabile con {@code equals} senza scrivere nulla.
 */
public final class RecordsUsage {

    private RecordsUsage() {
    }

    /**
     * PRIMA
     */
    public static void prima() {
        var cliente = new ClienteLegacy(100, "Mario");

        System.out.println("PRIMA  -> getId()   = " + cliente.getId());
        System.out.println("PRIMA  -> getNome() = " + cliente.getNome());
        System.out.println("PRIMA  -> toString  = " + cliente);
    }

    /**
     * DOPO: l'accessor ha il nome del campo, non il prefisso {@code get}.
     */
    public static void dopo() {
        var cliente = new ClienteRecord(100, "Mario");

        System.out.println("DOPO   -> id()      = " + cliente.id());
        System.out.println("DOPO   -> nome()    = " + cliente.nome());
        System.out.println("DOPO   -> toString  = " + cliente);
    }

    /**
     * {@code equals} e {@code hashCode} sono gia' corretti: due record con stessi campi sono uguali.
     */
    public static void uguaglianza() {
        var a = new ClienteRecord(100, "Mario");
        var b = new ClienteRecord(100, "Mario");
        var c = new ClienteRecord(101, "Mario");

        System.out.println("a.equals(b) -> " + a.equals(b));
        System.out.println("a.equals(c) -> " + a.equals(c));
        System.out.println("a == b       -> " + (a == b) + "  (confronto per riferimento, sempre false)");

        Set<ClienteRecord> set = new HashSet<>();
        set.add(a);
        set.add(b);
        set.add(c);
        System.out.println("set.size()  -> " + set.size() + "  (a e b sono lo stesso valore)");
    }

    /**
     * Record generico.
     */
    public static void generico() {
        Risultato<String> testo = Risultato.ok("ok");
        Risultato<Integer> numero = Risultato.errore();

        System.out.println("generico 1 -> " + testo);
        System.out.println("generico 2 -> " + numero);
    }

    /**
     * Record dentro una lista: con {@code List.of} la lista e' gia' immutabile.
     */
    public static void collezioni() {
        List<ClienteRecord> clienti = List.of(
                new ClienteRecord(1, "Mario"),
                new ClienteRecord(2, "Luigi")
        );

        List<String> nomi = clienti.stream()
                .map(ClienteRecord::nome)
                .toList();

        System.out.println("record in lista -> " + nomi);
    }

    public static void compatto() {
        System.out.println("compatto -> " + new Pagina<>(1, List.of("a", "b")));
        System.out.println("vuota    -> " + new Pagina<String>(2, List.of()).vuota());
    }

    /**
     * Cosa NON puoi fare in un record: estendere un'altra classe, dichiarare campi extra,
     * e gli accessor non possono essere riassegnati.
     */
    public static void limiti() {
        // record Cliente extends Persona { }   // <-- non compila: i record non si estendono
        // record Cliente(int id) { int altro; } // <-- non compila: campi extra vietati
        // public int id() { return 999; }      // <-- non compila: l'accessor e' implicito
        System.out.println("limiti    -> i record sono chiusi per ereditari' ma aperti a metodi.");
    }

    public static void main(String[] args) {
        prima();
        dopo();
        uguaglianza();
        generico();
        collezioni();
        compatto();
        limiti();
    }
}