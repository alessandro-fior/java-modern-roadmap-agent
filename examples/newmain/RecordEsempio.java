// Compact source file - Java 25 (JEP 512)
// record (Java 16) + switch expression (Java 14) dentro lo stesso file.
//
// In un compact source file le classi dichiarate diventano classi annidate
// della classe implicita: si possono usare normalmente.
//
// Esecuzione:  java examples/newmain/RecordEsempio.java

record Cliente(int id, String nome, int eta, String citta) {

    Cliente {
        if (id <= 0) {
            throw new IllegalArgumentException("id deve essere positivo");
        }
    }

    boolean maggiorenne() {
        return eta >= 18;
    }

    // switch expression: produce direttamente un valore
    String fasciaEta() {
        return switch (eta) {
            case 0 -> "neonato";
            default -> eta < 18 ? "minorenne" : "maggioreenne";
        };
    }
}

void main() {
    var clienti = List.of(
            new Cliente(1, "Mario", 25, "Milano"),
            new Cliente(2, "Luigi", 17, "Roma"),
            new Cliente(3, "Anna", 32, "Milano"),
            new Cliente(4, "Paolo", 45, "Napoli"),
            new Cliente(5, "Laura", 16, "Roma")
    );

    IO.println("--- tutti i clienti ---");
    clienti.forEach(c -> IO.println(c));

    IO.println("--- accessori ---");
    var primo = clienti.get(0);
    IO.println("id    -> " + primo.id());
    IO.println("nome  -> " + primo.nome());
    IO.println("eta   -> " + primo.eta());
    IO.println("citta -> " + primo.citta());

    IO.println("--- maggiorenni, ordinati per nome ---");
    clienti.stream()
            .filter(Cliente::maggiorenne)
            .sorted(Comparator.comparing(Cliente::nome))
            .forEach(c -> IO.println(c.nome() + " (" + c.fasciaEta() + ")"));

    IO.println("--- eta' media ---");
    var media = clienti.stream()
            .mapToInt(Cliente::eta)
            .average();

    IO.println("media -> " + media.orElse(0.0));

    IO.println("--- record uguali? ---");
    IO.println("record con stessi dati -> " + new Cliente(1, "Mario", 25, "Milano").equals(primo));
}