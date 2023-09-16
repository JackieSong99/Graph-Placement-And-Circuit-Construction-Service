import java.util.ArrayList;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ParseJson {
    /*
     * ParseJson.read_json(filename) will parse the string which contains the information of the original json file,
     * and output the corresponding information in new format
     */
    public static ArrayList<Gate> parse_json_file(String filename) {
        ArrayList<Gate> gate_list = new ArrayList<>();
        try{
            // circuit information in string format
            String circuit_json_input = CirConReadJson.CirConread_json(filename);
            // circuit information in JSON object format
            JSONObject circuit_info = JSONObject.fromObject(circuit_json_input);

            // the number of input wires
            int input_wire_number = circuit_info.getInt("InputWire");

            // the information of all gates
            JSONArray gates_info = circuit_info.getJSONArray("Gates");
            // analyse the information for a single gate
            for (Object gate: gates_info) {
                // parse the information for one single gate
                JSONObject json_gate = JSONObject.fromObject(gate);
//                System.out.println(json_gate);

                // parse ID and type
                int gate_id = json_gate.getInt("ID");
                String gate_type = json_gate.getString("Type");

                // parse input id list
                JSONArray gate_json_input_id_list = json_gate.getJSONArray("Input");
                ArrayList<Integer> gate_input_id_list = new ArrayList<>();
                for (int i = 0; i < gate_json_input_id_list.size(); i++) {
                    gate_input_id_list.add((int)gate_json_input_id_list.get(i));
                }
//                System.out.println(gate_input_id_list);

                // parse output id list
                JSONArray gate_json_output_id_list = json_gate.getJSONArray("Output");
                ArrayList<Integer> gate_output_id_list = new ArrayList<>();
                for (int i = 0; i < gate_json_output_id_list.size(); i++) {
                    gate_output_id_list.add((int)gate_json_output_id_list.get(i));
                }
//                System.out.println(gate_output_id_list);

                // parse info num
                int info_num = json_gate.getInt("Info");

                // construct the gate class
                Gate temp_gate = new Gate(gate_id, gate_type, gate_input_id_list, gate_output_id_list, info_num);
                gate_list.add(temp_gate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gate_list;
    }

    public static void main(String[] args) throws Exception{
        ArrayList<Gate> gate_list = parse_json_file("json_file/circuit_input.json");
    }
}
