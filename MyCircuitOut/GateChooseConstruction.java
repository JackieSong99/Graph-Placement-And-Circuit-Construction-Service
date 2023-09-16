import circuit.Choose;
import circuit.Circuit;

import java.util.ArrayList;

public class GateChooseConstruction {
    public static void choose_gate_construction(Circuit c, Gate gate_info, ArrayList<CircuitID> circuit_gate_list, ArrayList<Integer> constructed_gate_id_list, int min_id) throws Exception {
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

        // only construct if and only if all previous src is constructed
        if (flag == true) {
            int y = 0;
            for (CircuitID constructed_gate: circuit_gate_list) {
                if (input_id_list.contains(constructed_gate.id)) {
                    int output_num = constructed_gate.sub_circuit.getOutputs().size();
                    y = y + output_num;
                }
            }

            Circuit choose_gate = new Choose(y);
            c.union(choose_gate);

            if (input_id_list.isEmpty()) {
                System.out.println("Choose gate should have input");
            }
            else {
                Removein.removein(c, choose_gate);
            }
            if (!output_id_list.isEmpty()) {
                Removeout.removeout(c, choose_gate);
            }

            CircuitID temp_element = new CircuitID(choose_gate, gate_info.id);
            circuit_gate_list.add(temp_element);
        }


    }
}
