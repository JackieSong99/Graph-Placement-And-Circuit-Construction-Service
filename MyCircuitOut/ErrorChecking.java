import java.util.*;

public class ErrorChecking {
    public static boolean error_checking(ArrayList<Gate> gates_list) {
        Boolean ret = false;
        ArrayList<Integer> gates_id = new ArrayList<>();
        ArrayList<String> gates_type = new ArrayList<>();
        ArrayList<ArrayList<Integer>> gates_input_list = new ArrayList<>();
        ArrayList<ArrayList<Integer>> gates_output_list = new ArrayList<>();

        for (Gate gate: gates_list) {
            gates_id.add(gate.id);
            gates_type.add(gate.type);
            gates_input_list.add(gate.input_id_list);
            gates_output_list.add(gate.output_id_list);
        }

        // Error1: If ids are repeated
        Set<Integer> ids_set = new HashSet<>(gates_id);
        if (ids_set.size() != gates_id.size()) {
            ret = true;
            System.out.println("Error 1: Duplicate ids found in JSON file");
        }
        // Error 2: If ids have negative values
        for (Integer id: gates_id) {
            if (id < 0) {
                ret = true;
                System.out.println("Error 2: Negative id " + id + " occurred in JSON file.");
            }
        }

        // Error 3: If gates type are allowed
        String[] allowed_gates = {"Or","And","BitcoinCircuit","BitDiffWithBorrow", "BitNot","BitsnXnor","BitsnXor",
                "BitSumWithCarry","BitSumWithoutCarry","BitwiseAnd", "BitwiseOr", "BitwiseNot","BitwiseXnor",
                "BitwiseXor","BitXor","Ch","Choose", "Compress","DiffTwo","IntegerAsCircuit", "LessEquals", "Maj",
                "package-frame","package-summary","package-tree","Rightrotate","Rightrotate", "Rightshift","ShaCircuit",
                "Sigma","Sum","SumTwo","SumTwoMod32","VariableIntegerAsCircuit","ZeroOne"};
        for (String gate_type: gates_type) {
            if (Arrays.asList(allowed_gates).contains(gate_type)) {} else {
                System.out.println("Error 3: Input gate type '" + gate_type + "' is not allowed here.");
                ret = true;
            }
        }

        // Constrains of gates for different type
        for (Gate gate: gates_list) {
            // Error 4: When gate.type == And/Or/Choose/Sum, input_id_list.size() should > 0
            if (gate.type.equals("And") | gate.type.equals("Or") | gate.type.equals("Sum") | gate.type.equals("Choose")) {
                if (gate.input_id_list.isEmpty()) {
                    System.out.println("Error 4: The input gate list of gate id = " + gate.id + " should not be empty.");
                    ret = true;
                }
            }
            // Error 5: When gate.type == IntegerAsCircuit, input_id_list should be [], the information number should be none negative integer
            if (gate.type.equals("IntegerAsCircuit")) {
                if (!gate.input_id_list.isEmpty()) {
                    System.out.println("Error 5: The input gate list of IntegerAsCircuit id = " + gate.id + " should be empty.");
                    ret = true;
                }
                if (gate.info_num < 0) {
                    System.out.println("Error 5: The input gate list of IntegerAsCircuit id = " + gate.id + " should larger than 0 but not " + gate.info_num + ".");
                    ret = true;
                }
            }
            // Error 6: When gate.type == BitNot, input_id_list.size() should = 1
            if (gate.type.equals("BitNot")) {
                if (gate.input_id_list.isEmpty()) {
                    System.out.println("Error 6: The input gate list of BitNot id = " + gate.id + " should not be empty.");
                    ret = true;
                }
            }
            // Error 7: When gate.type == LessEquals, input_id_list.size() should = 2, order matter
            if (gate.type.equals("LessEquals")) {
                if (gate.input_id_list.size() != 2) {
                    System.out.println("Error 7: The input gate list of LessEquals id = " + gate.id + " should be 2.");
                    ret = true;
                }
            }
            // Error 8: When gate.type == ZeroOne, input_id_list should be [], the information number should only be 0 or 1
            if (gate.type.equals("ZeroOne")) {
                if (!gate.input_id_list.isEmpty()) {
                    System.out.println("Error 8: The input gate list of ZeroOne id = " + gate.id + " should be empty.");
                    ret = true;
                }
                if (gate.info_num != 0 & gate.info_num!= 1) {
                    System.out.println("Error 8: The input gate list of ZeroOne id = " + gate.id + " should only be 0 or 1 but not " + gate.info_num + ".");
                    ret = true;
                }
            }
        }

        // If input and output wires matches
        // the smallest gate id
        int min_id = SmallestID.find_smallest_ID(gates_id);
        // the biggest gate id
        int max_id = LargestID.find_largest_ID(gates_id);
        // Error 9: check if there exist and only exist an output (the output_list should = [])
        int empty = 0;
        for (ArrayList<Integer> output_list: gates_output_list) {
            if (output_list.isEmpty()) {
                empty = empty + 1;
            }

            // Error 10: check if there are redundant input in the output id list
            Set<Integer> output_ids_set = new HashSet<>(output_list);
            if (output_ids_set.size() != output_list.size()) {
                ret = true;
                System.out.println("Error 10: Duplicate ids found in output id list: " + output_list);
            }

            for (int i = 0; i < output_list.size(); i++) {
                // Error 11: check if the output_id_list has element id smaller than the first id gate (the gate output should not be fused with input wires)
                if (output_list.get(i) < min_id && output_list.get(i) > 0) {
                    ret = true;
                    System.out.println("Error 11: Output id list " + output_list + " of gates contains an input wire.");
                }
                // Error 12: check if the output_id_list contains negative id
                if (output_list.get(i) < 0) {
                    ret = true;
                    System.out.println("Error 12: Output id list " + output_list + " of gates has negative id.");
                }
                // Error 13: check if the output_id_list contains id exceeds the max id
                if (output_list.get(i) > max_id) {
                    ret = true;
                    System.out.println("Error 13: Output id list " + output_list + " of gates contains id exceeds the maximum gate id.");
                }
            }
        }
        if (empty > 1){
            System.out.println("Error 9: Multiple output wires are defined, should be 1.");
        }
        else if (empty < 1){
            System.out.println("Error 9: No output wires are defined, should be 1.");
        }

        // Error 14: check if the output ids of a gate correspond with the input ids of the destination gate
        for (int i = 0; i < gates_list.size(); i++) {
            Gate SourceGate = gates_list.get(i);
            for (int DestGate_id: SourceGate.output_id_list) {
                for (int j = 0; j < gates_list.size(); j++) {
                    if (gates_list.get(j).id == DestGate_id && !gates_list.get(j).input_id_list.contains(SourceGate.id)) {
                        ret = true;
                        System.out.println("Error 14: Error: Gate id=" + SourceGate.id + " with its output id list " + SourceGate.output_id_list +
                                " contains gate id (id=" + DestGate_id + ") which input list doesn't contains " +
                                "the source gate (id=" + SourceGate.id + ")");
                    }
                }
            }
        }


        for (ArrayList<Integer> input_list: gates_input_list) {
            // Error 15: check if there are redundant input in the input id list
            Set<Integer> input_ids_set = new HashSet<>(input_list);
            if (input_ids_set.size() != input_list.size()) {
                ret = true;
                System.out.println("Error 15: Duplicate ids found in input id list: " + input_list);
            }

            for (int i = 0; i < input_list.size(); i++) {
                // Error 16: check if the input_id_list contains negative id
                if (input_list.get(i) <= 0) {
                    ret = true;
                    System.out.println("Error 16: Input id list " + input_list + " of gates has none positive id.");
                }
                // Error 17: check if the input_id_list contains id exceeds the max id
                if (input_list.get(i) > max_id) {
                    ret = true;
                    System.out.println("Error 17: Input id list " + input_list + " of gates contains id exceeds the maximum gate id.");
                }
            }
        }

        // Error 18: check if the input ids of a gate correspond with the output ids of the source gate
        for (int i = 0; i < gates_list.size(); i++) {
            Gate DestGate = gates_list.get(i);
            for (int SourceGate_id: DestGate.input_id_list) {
                for (int j = 0; j < gates_list.size(); j++) {
                    if (gates_list.get(j).id == SourceGate_id && !gates_list.get(j).output_id_list.contains(DestGate.id)) {
                        ret = true;
                        System.out.println("Error 18: Gate id=" + DestGate.id + " with its input id list " + DestGate.input_id_list +
                                " contains gate id (id=" + SourceGate_id + ") which output list doesn't contains " +
                                "the destination gate (id=" + DestGate.id + ")");
                    }
                }
            }
        }

        return ret;
    }
}
