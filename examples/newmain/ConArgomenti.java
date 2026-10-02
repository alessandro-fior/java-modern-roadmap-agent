// Compact source file - Java 25 (JEP 512)
// Il main di istanza CON gli argomenti della riga di comando.
//
// Esecuzione:  java examples/newmain/ConArgomenti.java Mario 42 Anna
//
// Con Java 25 il main puo' restare di istanza e dichiarare String[]:
// il launcher crea l'istanza e passa i parametri come elementi dell'array,
// esattamente come fa con il main statico.

void main(String[] args) {
    IO.println("Argomenti ricevuti: " + args.length);

    for (int i = 0; i < args.length; i++) {
        IO.println("  args[" + i + "] = " + args[i]);
    }

    var numerici = java.util.Arrays.stream(args)
            .map(s -> {
                try {
                    return Integer.valueOf(s);
                } catch (NumberFormatException e) {
                    return null;
                }
            })
            .filter(java.util.Objects::nonNull)
            .toList();

    IO.println("Argomenti numerici: " + numerici);
    IO.println("Somma: " + numerici.stream().mapToInt(Integer::intValue).sum());
}