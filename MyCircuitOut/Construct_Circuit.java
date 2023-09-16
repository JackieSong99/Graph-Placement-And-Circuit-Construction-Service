import circuit.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Construct_Circuit {
    // print the circuit
    private static void printnio(Circuit c) {
        System.out.println("\t # inputs: " + c.getInputs().size());
        System.out.println("\t # outputs: " + c.getOutputs().size());
    }

    public static void main(String[] args) throws Exception {
//      acquire the information of the gate list, including parsing the json file
        String filename = args[0];
//        ArrayList<Gate> gates_list = ParseJson.parse_json_file("json_file/circuit_input_test6.json");
        ArrayList<Gate> gates_list = ParseJson.parse_json_file(filename);
        ArrayList<Integer> gates_id = new ArrayList<>();
        ArrayList<Gate> gates_normal_list = new ArrayList<>();
        ArrayList<Gate> gates_special_list = new ArrayList<>();
        for (Gate gate: gates_list) {
            gates_id.add(gate.id);

            // Divide gates according to different types
            if (gate.type.equals("And") | gate.type.equals("Or") | gate.type.equals("BitNot") | gate.type.equals("IntegerAsCircuit") | gate.type.equals("ZeroOne")) {
                gates_normal_list.add(gate);
            }
            else if (gate.type.equals("Choose") | gate.type.equals("LessEquals") | gate.type.equals("Sum")) {
                gates_special_list.add(gate);
            }
        }

        // Print information of the gates
        for (Gate gate: gates_list){
            System.out.println("ID = " + gate.id + ", type = " + gate.type + ", information number = " + gate.info_num);
            System.out.println("input_id_list = " + gate.input_id_list + ", output_id_list = " + gate.output_id_list);
        }

        // Error checking
        Boolean err = ErrorChecking.error_checking(gates_list);
        if (err) {
            System.out.println("Error found in the input Json file, will not continue");
        }
        else {
            System.out.println(" ");
            System.out.println("No errors found, continue:");

            // construct the circuit according to the acquired gate information list
            // Reset
            circuit.Circuit.resetIDs();
            circuit.ZeroOne.reset();
            circuit.Circuit c = new circuit.Circuit();
            // set input wire
            ArrayList<circuit.Circuit.Wire> input_wire_list = new ArrayList<>();
            int min_id = SmallestID.find_smallest_ID(gates_id);
            for (int i = 0; i < min_id-1; i++) {
                c.addNewInput();
                input_wire_list.add(c.getInputs().get(i));
            }

            // construct And/Or/BitNot/IAC/ZeroOnes gates, but not fuse,
            ArrayList<CircuitID> circuit_gate_list = new ArrayList<>();
            for (Gate gate: gates_normal_list) {
                if (gate.type.equals("Or")) {
                    GateOrConstruction.or_gate_construction(c, gate, circuit_gate_list);
                } else if (gate.type.equals("And")) {
                    GateAndConstruction.and_gate_construction(c, gate, circuit_gate_list);
                } else if (gate.type.equals("BitNot")) {
                    GateBitNotConstruction.bitnot_gate_construction(c, gate, circuit_gate_list);
                } else if (gate.type.equals("IntegerAsCircuit")) {
                    GateIntegerAsCircuitConstruction.integerascircuit_gate_construction(c, gate, circuit_gate_list);
                } else if (gate.type.equals("ZeroOne")) {
                    GateZeroOneConstruction.zeroone_gate_construction(c, gate, circuit_gate_list);
                }
            }

            // construct LessEquals/Choose/Sum, but not fuse: circuit_gate_list, gates_special_list
            while (circuit_gate_list.size() < gates_list.size()) {
                ArrayList<Integer> constructed_gate_id_list = new ArrayList<>();
                for (CircuitID constructed_gate: circuit_gate_list) {
                    constructed_gate_id_list.add(constructed_gate.id);  // return a list of constructed gate id
                }
                for (Gate sp_gates: gates_special_list) {
                    if (!constructed_gate_id_list.contains(sp_gates.id)) {  // dealing with gate which has not been constructed yet
                        if (sp_gates.type.equals("Choose")) {
                            GateChooseConstruction.choose_gate_construction(c, sp_gates, circuit_gate_list, constructed_gate_id_list, min_id);
                        }
                        else if (sp_gates.type.equals("LessEquals")) {
                            GateLessEqualsConstruction.lessequal_gate_construction(c, sp_gates, circuit_gate_list, constructed_gate_id_list, min_id);
                        } else if (sp_gates.type.equals("Sum")) {
                            GateSumConstruction.sum_gate_construction(c, sp_gates, circuit_gate_list, constructed_gate_id_list, min_id);
                        }
                    }
                }
            }

            // Fuse And/Or/BitNot/IAC/ZeroOnes the wires inside
            FuseWire.fuse_wire(c, input_wire_list, gates_list, circuit_gate_list);

            // print the circuit
            printnio(c);
            System.out.println(" ");


//            List<Boolean> ibool = new LinkedList<Boolean>();
//            ibool.add(Boolean.TRUE);
//            ibool.add(Boolean.TRUE);
//            ibool.add(Boolean.TRUE);
//            ibool.add(Boolean.TRUE);
//            List<Boolean> obool = c.evaluate(ibool);
//            System.out.println("Output bool: " + obool.get(0));

            // solve the circuit
            CircuitUtils.cnfSatToFile(c, "cnf.dimacs");
            boolean flag = CirConSolve.CirConsolve();
            if (flag == false) {
                System.out.println("UNSATISFIABLE");
            }
            else {
                String text = CirConReadFileContent.CirConreadFileContent("result.log");
                System.out.println(text);  // uncomment to see more information of the final results
            }

        }
    }
}
