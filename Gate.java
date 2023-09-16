import java.util.ArrayList;

public class Gate {
    int id;
    String type;
    ArrayList<Integer> input_id_list;
    ArrayList<Integer> output_id_list;
    int info_num;

    public Gate(int input_id, String input_type, ArrayList<Integer> input_input_id_list, ArrayList<Integer> input_output_id_list, int input_info_num){
        id = input_id;
        type = input_type;
        input_id_list = input_input_id_list;
        output_id_list = input_output_id_list;
        info_num = input_info_num;

//        System.out.println("ID = " + id + ", type = " + type + ", information number = " + info_num);
//        System.out.println("input_id_list = " + input_id_list + ", output_id_list = " + output_id_list);

    }
}
