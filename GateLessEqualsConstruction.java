import circuit.Circuit;
import circuit.LessEquals;

import java.util.ArrayList;

public class GateLessEqualsConstruction {
    public static void lessequal_gate_construction(Circuit c, Gate gate_info, ArrayList<CircuitID> circuit_gate_list, ArrayList<Integer> constructed_gate_id_list, int min_id) throws Exception {
        ArrayList<Integer> input_id_list = gate_info.input_id_list;
        ArrayList<Integer> output_id_list = gate_info.output_id_list;

        // check if the input source gate has all been constructed
        boolean flag = true;
        for (int src: input_id_list) {
            if (src >= min_id) {
                if (!constructed_gate_id_list.contains(src)) {
                    flag = false;
                    break;
                }
            }
        }

        if (flag == true) {
            int x = -1;
            int y = -1;
            for (CircuitID constructed_gate: circuit_gate_list) {
                if (input_id_list.get(0) == constructed_gate.id) {
                    x = constructed_gate.sub_circuit.getOutputs().size();
                }
                else if (input_id_list.get(1) == constructed_gate.id) {
                    y = constructed_gate.sub_circuit.getOutputs().size();
                }
            }
            if (x == -1 | y == -1) {
                System.out.println("Error occurs when finding the output of source gate");
            }

            Circuit lessequal_gate = new LessEquals(x, y);
            c.union(lessequal_gate);

            if (input_id_list.isEmpty()) {
                System.out.println("LessEquals gate should have input");
            }
            else {
                Removein.removein(c, lessequal_gate);
            }
            if (!output_id_list.isEmpty()) {
                Removeout.removeout(c, lessequal_gate);
            }

            CircuitID temp_element = new CircuitID(lessequal_gate, gate_info.id);
            circuit_gate_list.add(temp_element);
        }
    }
}
