import circuit.Circuit;

/* Remove the input wires for whole circuit c */
public class Removein {

    public static void removein(Circuit c, Circuit subc) throws Exception {
        for (int j = 0; j < subc.getInputs().size(); j++){
            c.removeAsInput(subc.getInputs().get(j));
        }
    }
}
