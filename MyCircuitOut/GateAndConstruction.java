import circuit.BigAndOr;
import circuit.Circuit;

import java.util.ArrayList;

public class GateAndConstruction {
    public static void and_gate_construction(Circuit c, Gate gate_info, ArrayList<CircuitID> circuit_gate_list) throws Exception {
        ArrayList<Integer> input_id_list = gate_info.input_id_list;
        ArrayList<Integer> output_id_list = gate_info.output_id_list;

        Circuit and_gate = new BigAndOr(false, gate_info.input_id_list.size());
        c.union(and_gate);

        if (input_id_list.isEmpty()) {
            System.out.println("And gate should have input");
        }
        else {
            Removein.removein(c, and_gate);
        }
        if (!output_id_list.isEmpty()) {
            Removeout.removeout(c, and_gate);
        }

        CircuitID temp_element = new CircuitID(and_gate, gate_info.id);
        circuit_gate_list.add(temp_element);
    }
}
