import circuit.BitNot;
import circuit.Circuit;

import java.util.ArrayList;

public class GateBitNotConstruction {
    public static void bitnot_gate_construction(Circuit c, Gate gate_info, ArrayList<CircuitID> circuit_gate_list) throws Exception {
        ArrayList<Integer> input_id_list = gate_info.input_id_list;
        ArrayList<Integer> output_id_list = gate_info.output_id_list;

        Circuit bitnot_gate = new BitNot();
        c.union(bitnot_gate);

        if (input_id_list.isEmpty()) {
            System.out.println("Bit not gate should have input");
        }
        else {
            Removein.removein(c, bitnot_gate);
        }
        if (!output_id_list.isEmpty()) {
            Removeout.removeout(c, bitnot_gate);
        }

        CircuitID temp_element = new CircuitID(bitnot_gate, gate_info.id);
        circuit_gate_list.add(temp_element);
    }
}
