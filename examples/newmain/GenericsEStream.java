// Compact source file - Java 25 (JEP 512)
// Generics (Java 5) + Stream API (Java 8) + var (Java 10) + record (Java 16).
//
// Esecuzione:  java examples/newmain/GenericsEStream.java

void main() {
    // List.of produce gia' una lista immutabile (Java 9)
    List<Integer> numeri = List.of(10, 20, 30, 40, 50);

    IO.println("sorgente    -> " + numeri);

    var maggioriDi25 = numeri.stream()
            .filter(n -> n > 25)
            .toList();

    IO.println("filter >25  -> " + maggioriDi25);

    var etichette = numeri.stream()
            .map(n -> "n." + n)
            .toList();

    IO.println("map         -> " + etichette);

    var ordinati = numeri.stream()
            .sorted(Comparator.reverseOrder())
            .toList();

    IO.println("sorted desc -> " + ordinati);

    var somma = numeri.stream()
            .mapToInt(Integer::intValue)
            .sum();

    IO.println("sum         -> " + somma);

    IO.println("distinct    -> " + List.of(3, 1, 3, 2, 1, 3).stream().distinct().toList());

    // Optional: nessun null
    Optional<String> primo = numeri.stream()
            .filter(n -> n > 100)
            .map(Object::toString)
            .findFirst();

    IO.println("optional    -> " + primo);
    IO.println("o con default -> " + primo.orElse("nessun numero sopra 100"));
}