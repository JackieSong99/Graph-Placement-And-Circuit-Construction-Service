import java.util.ArrayList;

public class GraphSpecialCases {
    public static void onesupplynode(ArrayList<Vertex> demand_input, ArrayList<Vertex> supply_input) throws Exception{
        int flag = 0;
        for (int i = 0; i < demand_input.get(0).node_weight.size(); i++){
            int sum = 0;
            for (int j = 0; j < demand_input.size(); j++) {
                sum = sum + demand_input.get(j).node_weight.get(i).weight;
            }
            if (sum <= supply_input.get(0).node_weight.get(i).weight) {
                flag += 1;
            }
        }
        if (flag == demand_input.get(0).node_weight.size()) {
            System.out.println("**************************************************");
            System.out.println("Final number of supply nodes needed is: " + 1 );

            for (int k = 0; k < demand_input.size(); k++){
                System.out.print("Demand node " + k + " with weights:");
                Vertex.Print_vertex(demand_input.get(k));
                System.out.print("should be places into supply node 1" + " with weights:");
                Vertex.Print_vertex(supply_input.get(0));
            }
        }
        else if (flag < supply_input.get(0).node_weight.get(0).weight) {
            System.out.println("UNSATISFIABLE");
        }
    }

    public static void onedemandnode(ArrayList<Vertex> demand_input, ArrayList<Vertex> supply_input) throws Exception{
        int flag = 0;
        int num = 0;

        while (num < supply_input.size() & flag < demand_input.get(0).node_weight.size()) {
            for (int j = 0; j < demand_input.get(0).node_weight.size(); j++) {
                if (supply_input.get(num).node_weight.get(j).weight >= demand_input.get(0).node_weight.get(j).weight) {
                    flag += 1;
                }
            }
            num++;
            if (flag != demand_input.get(0).node_weight.size()) {
                flag = 0;
            }
        }

        if (flag == demand_input.get(0).node_weight.size()) {
            System.out.println("**************************************************");
            System.out.println("Final number of supply nodes needed is: " + 1);
            System.out.print("Demand node " + 1 + ", " + "with weights: ");
            Vertex.Print_vertex(demand_input.get(0));
            System.out.print("should be placed into supply node " + (num) + ", " + "with weights: ");
            Vertex.Print_vertex(supply_input.get(num-1));
        }
        else {
            System.out.println("UNSATISFIABLE");
        }
    }
}
