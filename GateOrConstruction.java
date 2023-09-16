import circuit.*;

import java.util.ArrayList;

public class GateOrConstruction {
    public static void or_gate_construction(Circuit c, Gate gate_info, ArrayList<CircuitID> circuit_gate_list) throws Exception {
        ArrayList<Integer> input_id_list = gate_info.input_id_list;
        ArrayList<Integer> output_id_list = gate_info.output_id_list;

        Circuit or_gate = new BigAndOr(true, gate_info.input_id_list.size());
        c.union(or_gate);

        if (input_id_list.isEmpty()) {
            System.out.println("Or gate should have input");
        }
        else {
            Removein.removein(c, or_gate);
        }
        if (!output_id_list.isEmpty()) {
            Removeout.removeout(c, or_gate);
        }

        CircuitID temp_element = new CircuitID(or_gate, gate_info.id);
        circuit_gate_list.add(temp_element);
    }
}
