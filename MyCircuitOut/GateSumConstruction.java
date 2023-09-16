import circuit.Circuit;
import circuit.Sum;

import java.util.ArrayList;

public class GateSumConstruction {

    public static void sum_gate_construction(Circuit c, Gate gate_info, ArrayList<CircuitID> circuit_gate_list, ArrayList<Integer> constructed_gate_id_list, int min_id) throws Exception {
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
            ArrayList<Integer> sum_input = new ArrayList<>();
            for (CircuitID constructed_gate: circuit_gate_list) {
                if (input_id_list.contains(constructed_gate.id)) {
                    sum_input.add(constructed_gate.sub_circuit.getOutputs().size());
                }
            }

            Circuit sum_gate = new Sum(sum_input);
            c.union(sum_gate);

            if (input_id_list.isEmpty()) {
                System.out.println("Sum gate gate should have input");
            }
            else {
                Removein.removein(c, sum_gate);
            }
            if (!output_id_list.isEmpty()) {
                Removeout.removeout(c, sum_gate);
            }

            CircuitID temp_element = new CircuitID(sum_gate, gate_info.id);
            circuit_gate_list.add(temp_element);
        }


    }
}
