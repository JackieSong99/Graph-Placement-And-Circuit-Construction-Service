import circuit.Circuit;

/* Print the circuit's input and output wires */
public class Printnio {

    public static void printnio(Circuit c)
    {
        System.out.println("\t # inputs: " + c.getInputs().size());
        System.out.println("\t # outputs: " + c.getOutputs().size());
    }
}
