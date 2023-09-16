import circuit.Circuit;

/* Remove the output wires for whole circuit c */
public class Removeout {

    public static void removeout(Circuit c, Circuit subc) throws Exception {
        for (int j = 0; j < subc.getOutputs().size(); j++){
            c.removeAsOutput(subc.getOutputs().get(j));
        }
    }
}
