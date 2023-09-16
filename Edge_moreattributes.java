import java.util.ArrayList;

public class Edge_moreattributes {
    public ArrayList<PairInt<String, Integer>> edge_weight;
    public ArrayList<PairBool<String, Boolean>> edge_bool;
    Vertex source;
    Vertex destination;

    public Edge_moreattributes(ArrayList<PairInt<String, Integer>> weight_input, ArrayList<PairBool<String, Boolean>> bool_input,
                               Vertex src, Vertex des){
        this.edge_weight = weight_input;
        this.edge_bool = bool_input;
        this.source = src;
        this.destination = des;
    }

    public static void Print_vertex(Edge_moreattributes e){
        // System.out.println(v.node_name + ": ");
        System.out.print("< ");
        for(int i = 0; i < e.edge_weight.size() - 1; i++){
            System.out.print(e.edge_weight.get(i).int_attribute + " = " +
                    e.edge_weight.get(i).weight + ", ");
        }
        System.out.print(e.edge_weight.get(e.edge_weight.size() - 1 ).int_attribute + " = " +
                e.edge_weight.get(e.edge_weight.size() - 1).weight);
//        for(int i = 0; i < v.node_bool.size(); i ++){
//            System.out.print(v.node_bool.get(i).bool_attribute + "--" +
//                    v.node_bool.get(i).bool);
//        }
        System.out.print(" > \n");
    }
}
