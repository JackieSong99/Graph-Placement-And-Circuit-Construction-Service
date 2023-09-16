import circuit.Circuit;
import circuit.IntegerAsCircuit;

import java.util.ArrayList;

public class GateZeroOneConstruction {
    public static void zeroone_gate_construction(Circuit c, Gate gate_info, ArrayList<CircuitID> circuit_gate_list) throws Exception {
        ArrayList<Integer> input_id_list = gate_info.input_id_list;
        ArrayList<Integer> output_id_list = gate_info.output_id_list;

        Circuit zeroone_gate = new IntegerAsCircuit(gate_info.info_num);
        c.union(zeroone_gate);

        if (!input_id_list.isEmpty()) {
            System.out.println("ZeroOne gate should not have input");
        }
        if (!output_id_list.isEmpty()) {
            Removeout.removeout(c, zeroone_gate);
        }

        CircuitID temp_element = new CircuitID(zeroone_gate, gate_info.id);
        circuit_gate_list.add(temp_element);
    }
}
