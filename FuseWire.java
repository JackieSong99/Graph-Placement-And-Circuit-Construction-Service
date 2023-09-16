import circuit.Circuit;

import java.util.ArrayList;

public class FuseWire {  // fuse wire for And/Or/Bitnot gate
    public static void fuse_wire(Circuit c, ArrayList<circuit.Circuit.Wire> wires_list,ArrayList<Gate> gates_list, ArrayList<CircuitID> subcircuit_id) throws Exception {
        int num_wires = wires_list.size();
        for (Gate gate: gates_list) {
            if (!gate.input_id_list.isEmpty()) {  // fuse only when input_id_list is not empty

                // found the target sub_circuit in subcircuit_id according to its id
                for (int source: gate.input_id_list) {  // for every id in the input_id_list
                    //  source is an input wire
                    if (source <= num_wires) {  // 0 < source <= num_wires
                        CircuitID des = FindTarget.find_target(subcircuit_id, gate.id);
                        int num_fused = des.already_fused;
                        c.fuse(wires_list.get(source-1), des.sub_circuit.getInputs().get(num_fused));
                        des.already_fused += 1;
                    }
                    // source is another gate
                    else {
                        CircuitID src = FindTarget.find_target(subcircuit_id, source);
                        CircuitID des = FindTarget.find_target(subcircuit_id, gate.id);
                        for (int i = 0; i < src.sub_circuit.getOutputs().size(); i++) {
                            int num_fused = des.already_fused;
                            c.fuse(src.sub_circuit.getOutputs().get(i), des.sub_circuit.getInputs().get(num_fused));
                            des.already_fused += 1;
                        }
                    }
                }
            }
        }
    }
}
