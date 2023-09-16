import java.util.ArrayList;

public class Vertex {
    public ArrayList<PairInt<String, Integer>> node_weight;
    public ArrayList<PairBool<String, Boolean>> node_bool;
    public String node_name;
    public Integer node_uid;

    public Vertex(ArrayList<PairInt<String, Integer>> weight_input, ArrayList<PairBool<String, Boolean>> bool_input,
                  String name_input, Integer uid_input){
        this.node_weight = weight_input;
        this.node_bool = bool_input;
        this.node_name = name_input;
        this.node_uid = uid_input;
    }

    public static void Print_vertex(Vertex v){
        // System.out.println(v.node_name + ": ");
        System.out.print("< ");
        for(int i = 0; i < v.node_weight.size() - 1; i++){
            System.out.print(v.node_weight.get(i).int_attribute + " = " +
                    v.node_weight.get(i).weight + ", ");
        }
        System.out.print(v.node_weight.get(v.node_weight.size() - 1 ).int_attribute + " = " +
                v.node_weight.get(v.node_weight.size() - 1).weight);
//        for(int i = 0; i < v.node_bool.size(); i ++){
//            System.out.print(v.node_bool.get(i).bool_attribute + "--" +
//                    v.node_bool.get(i).bool);
//        }
        System.out.print(" > \n");
    }
}

