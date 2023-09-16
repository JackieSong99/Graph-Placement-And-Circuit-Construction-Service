//import java.util.ArrayList;
//
//public class Functions {
//    public static void printgraph(ArrayList<Vertex> demand_v, ArrayList<Vertex> supply_v, Graph demand_graph, Graph supply_graph){
//        System.out.println("Checking node information for demand graph: ");
//        for (int k = 0; k < demand_v.size(); k++){
//            Vertex.Print_vertex(demand_v.get(k));
//        }
//        System.out.println(" ");
//        System.out.println("--------------");
//        System.out.println("Checking edge list for demand graph: ");
//        System.out.print("Edge List: [");
//        for (int i = 0; i < demand_graph.edgelist.size(); i++) {
//            System.out.print(demand_graph.edgelist.get(i).edge_weight + ", ");
//        }
//        System.out.println("]");
//        System.out.println(" ");
//        System.out.println("--------------");
//        System.out.println("Calling vertices according to edges: ");
//        for (int i = 0; i < demand_graph.edgelist.size(); i++) {
//            System.out.println( "Demand node " + demand_graph.edgelist.get(i).src_index + "(with its first weight being:"
//                    + demand_graph.edgelist.get(i).source.node_weight.get(0).weight+ ") --- " +
//                    demand_graph.edgelist.get(i).edge_weight + " --- " + "Demand node " + demand_graph.edgelist.get(i).des_index
//                    + "(with its first weight being:"  + demand_graph.edgelist.get(i).destination.node_weight.get(0).weight + ")");
//        }
//        System.out.println(" ");
//        System.out.println("--------------");
//        System.out.println("Show demand graph: ");
//        demand_graph.showGraph();
//        System.out.println(" ");
//        System.out.println("**************");
//
//        System.out.println("Checking node list for supply graph: ");
//        for (int k = 0; k < supply_v.size(); k++){
//            Vertex.Print_vertex(supply_v.get(k));
//        }
//        System.out.println(" ");
//        System.out.println("--------------");
//        System.out.println("Checking edge list for supply graph: ");
//        System.out.print("Edge List: [");
//        for (int i = 0; i < supply_graph.edgelist.size(); i++) {
//            System.out.print(supply_graph.edgelist.get(i).edge_weight + ", ");
//        }
//        System.out.println("]");
//        System.out.println(" ");
//        System.out.println("--------------");
//        System.out.println("Calling vertices according to edges: ");
//        for (int i = 0; i < supply_graph.edgelist.size(); i++) {
//            System.out.println( "Supply node " + supply_graph.edgelist.get(i).src_index + "(with its first weight being:"
//                    + supply_graph.edgelist.get(i).source.node_weight.get(0).weight+ ") --- " +
//                    supply_graph.edgelist.get(i).edge_weight + " --- " + "Supply node " + supply_graph.edgelist.get(i).des_index
//                    + "(with its first weight being:"  + supply_graph.edgelist.get(i).destination.node_weight.get(0).weight + ")");
//        }
//        System.out.println(" ");
//        System.out.println("--------------");
//        supply_graph.showGraph();
//    }
//}
