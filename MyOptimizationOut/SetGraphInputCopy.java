//import java.util.ArrayList;
//import com.google.gson.*;
//import com.google.gson.reflect.TypeToken;
//
//
//public class SetGraphInputCopy {
//    ArrayList<Vertex> demand_nodes = new ArrayList<>();
//    ArrayList<Vertex> supply_nodes = new ArrayList<>();
//    Graph demand_graph;
//    Graph supply_graph;
//
//
//    public void setdemandgraph() {
//        // test
//        ArrayList<ArrayList<PairInt<String, Integer>>> all_intattributes = new ArrayList<>();
//        ArrayList<ArrayList<PairBool<String, Boolean>>> all_boolattributes = new ArrayList<>();
//
//        // node 1
//        ArrayList<PairInt<String, Integer>> IntAttributeList_Node1 = new ArrayList<>();
//        PairInt<String, Integer> intattribute1 = new PairInt<>("numofVCPU", 1);
//        PairInt<String, Integer> intattribute2 = new PairInt<String, Integer>("numofGPU", 2);
//        //PairInt<String, Integer> intattribute3 = new PairInt<String, Integer>("numofMem", 5);
//        IntAttributeList_Node1.add(intattribute1);
//        IntAttributeList_Node1.add(intattribute2);
//        //IntAttributeList_Node1.add(intattribute3);
//        all_intattributes.add(IntAttributeList_Node1);
//
//        ArrayList<PairBool<String, Boolean>> BoolAttributeList_Node1 = new ArrayList<>();
//        PairBool<String, Boolean> boolattribute1 = new PairBool<String, Boolean>("isConnected", true);
//        PairBool<String, Boolean> boolattribute2 = new PairBool<String, Boolean>("isAvaliable", false);
//        PairBool<String, Boolean> boolattribute3 = new PairBool<String, Boolean>("isUsing", true);
//        BoolAttributeList_Node1.add(boolattribute1);
//        BoolAttributeList_Node1.add(boolattribute2);
//        BoolAttributeList_Node1.add(boolattribute3);
//        all_boolattributes.add(BoolAttributeList_Node1);
//
//        // node 2
//        ArrayList<PairInt<String, Integer>> IntAttributeList_Node2 = new ArrayList<>();
//        PairInt<String, Integer> intattribute1_Node2 = new PairInt<String, Integer>("numofVCPU", 2);
//        PairInt<String, Integer> intattribute2_Node2 = new PairInt<String, Integer>("numofGPU", 2);
//        //PairInt<String, Integer> intattribute3_Node2 = new PairInt<String, Integer>("numofMem", 10);
//        IntAttributeList_Node2.add(intattribute1_Node2);
//        IntAttributeList_Node2.add(intattribute2_Node2);
//        //IntAttributeList_Node2.add(intattribute3_Node2);
//        all_intattributes.add(IntAttributeList_Node2);
//
//        ArrayList<PairBool<String, Boolean>> BoolAttributeList_Node2 = new ArrayList<>();
//        PairBool<String, Boolean> boolattribute1_Node2 = new PairBool<String, Boolean>("isConnected", true);
//        PairBool<String, Boolean> boolattribute2_Node2 = new PairBool<String, Boolean>("isAvaliable", true);
//        PairBool<String, Boolean> boolattribute3_Node2 = new PairBool<String, Boolean>("isUsing", false);
//        BoolAttributeList_Node2.add(boolattribute1_Node2);
//        BoolAttributeList_Node2.add(boolattribute2_Node2);
//        BoolAttributeList_Node2.add(boolattribute3_Node2);
//        all_boolattributes.add(BoolAttributeList_Node2);
//
//        // node 3
//        ArrayList<PairInt<String, Integer>> IntAttributeList_Node3 = new ArrayList<>();
//        PairInt<String, Integer> intattribute1_Node3 = new PairInt<String, Integer>("numofVCPU", 3);
//        PairInt<String, Integer> intattribute2_Node3 = new PairInt<String, Integer>("numofGPU", 4);
//        //PairInt<String, Integer> intattribute3_Node3 = new PairInt<String, Integer>("numofMem", 2);
//        IntAttributeList_Node3.add(intattribute1_Node3);
//        IntAttributeList_Node3.add(intattribute2_Node3);
//        //IntAttributeList_Node3.add(intattribute3_Node3);
//        all_intattributes.add(IntAttributeList_Node3);
//
//        ArrayList<PairBool<String, Boolean>> BoolAttributeList_Node3 = new ArrayList<>();
//        PairBool<String, Boolean> boolattribute1_Node3 = new PairBool<String, Boolean>("isConnected", false);
//        PairBool<String, Boolean> boolattribute2_Node3 = new PairBool<String, Boolean>("isAvaliable", false);
//        PairBool<String, Boolean> boolattribute3_Node3 = new PairBool<String, Boolean>("isUsing", true);
//        BoolAttributeList_Node3.add(boolattribute1_Node3);
//        BoolAttributeList_Node3.add(boolattribute2_Node3);
//        BoolAttributeList_Node3.add(boolattribute3_Node3);
//        all_boolattributes.add(BoolAttributeList_Node3);
//
//        // node 4
//        ArrayList<PairInt<String, Integer>> IntAttributeList_Node4 = new ArrayList<>();
//        PairInt<String, Integer> intattribute1_Node4 = new PairInt<String, Integer>("numofVCPU", 2);
//        PairInt<String, Integer> intattribute2_Node4 = new PairInt<String, Integer>("numofGPU", 1);
//        //PairInt<String, Integer> intattribute3_Node4 = new PairInt<String, Integer>("numofMem", 2);
//        IntAttributeList_Node4.add(intattribute1_Node4);
//        IntAttributeList_Node4.add(intattribute2_Node4);
//        //IntAttributeList_Node4.add(intattribute3_Node4);
//        all_intattributes.add(IntAttributeList_Node4);
//
//        ArrayList<PairBool<String, Boolean>> BoolAttributeList_Node4 = new ArrayList<>();
//        PairBool<String, Boolean> boolattribute1_Node4 = new PairBool<String, Boolean>("isConnected", false);
//        PairBool<String, Boolean> boolattribute2_Node4 = new PairBool<String, Boolean>("isAvaliable", false);
//        PairBool<String, Boolean> boolattribute3_Node4 = new PairBool<String, Boolean>("isUsing", true);
//        BoolAttributeList_Node4.add(boolattribute1_Node4);
//        BoolAttributeList_Node4.add(boolattribute2_Node4);
//        BoolAttributeList_Node4.add(boolattribute3_Node4);
//        all_boolattributes.add(BoolAttributeList_Node4);
//
//
//        String name_seed_demand = "D";
//        StringBuffer name = new StringBuffer("");
//        int vertex_number = all_intattributes.size();
//
//        Graph demand_graph = new Graph(vertex_number);
//        ArrayList<Vertex> demandVertex_empty = new ArrayList<>();
//        for (int i = 0; i < vertex_number; i++) {
//            name = name.append(name_seed_demand);
//            StringBuffer temp = new StringBuffer(name);
//            demandVertex_empty.add(new Vertex(all_intattributes.get(i), all_boolattributes.get(i), temp));
//        }
//        demand_graph.insert_Vertex(demandVertex_empty);
//
//
//        // edge 1
//        Edge demand_edge1 = new Edge(10, demand_graph.vertexlist.get(0), demand_graph.vertexlist.get(1), 0, 1);
//        // edge 2
//        Edge demand_edge2 = new Edge(20, demand_graph.vertexlist.get(0), demand_graph.vertexlist.get(2), 0, 2);
//        // edge 3
//        Edge demand_edge3 = new Edge(5, demand_graph.vertexlist.get(1), demand_graph.vertexlist.get(2), 1, 2);
//
//        demand_graph.insert_Edge(demand_graph.vertexlist.get(0), demand_graph.vertexlist.get(1),demand_edge1);
//        demand_graph.insert_Edge(demand_graph.vertexlist.get(0), demand_graph.vertexlist.get(2),demand_edge2);
//        demand_graph.insert_Edge(demand_graph.vertexlist.get(1), demand_graph.vertexlist.get(2),demand_edge3);
//        demand_graph.set_default_edge_demand();
//
//        this.demand_graph = demand_graph;
//        this.demand_nodes = demand_graph.vertexlist;
//    }
//
//    public void setsupplygraph() {
//        // test
//        ArrayList<ArrayList<PairInt<String, Integer>>> all_intattributes = new ArrayList<>();
//        ArrayList<ArrayList<PairBool<String, Boolean>>> all_boolattributes = new ArrayList<>();
//
//        // node 1
//        ArrayList<PairInt<String, Integer>> IntAttributeList_Node1 = new ArrayList<>();
//        PairInt<String, Integer> intattribute1 = new PairInt<String, Integer>("numofVCPU", 7);
//        PairInt<String, Integer> intattribute2 = new PairInt<String, Integer>("numofGPU", 7);
//        //PairInt<String, Integer> intattribute3 = new PairInt<String, Integer>("numofMem", 17);
//        IntAttributeList_Node1.add(intattribute1);
//        IntAttributeList_Node1.add(intattribute2);
//        //IntAttributeList_Node1.add(intattribute3);
//        all_intattributes.add(IntAttributeList_Node1);
//
//        ArrayList<PairBool<String, Boolean>> BoolAttributeList_Node1 = new ArrayList<>();
//        PairBool<String, Boolean> boolattribute1 = new PairBool<String, Boolean>("isConnected", true);
//        PairBool<String, Boolean> boolattribute2 = new PairBool<String, Boolean>("isAvaliable", false);
//        PairBool<String, Boolean> boolattribute3 = new PairBool<String, Boolean>("isUsing", true);
//        BoolAttributeList_Node1.add(boolattribute1);
//        BoolAttributeList_Node1.add(boolattribute2);
//        BoolAttributeList_Node1.add(boolattribute3);
//        all_boolattributes.add(BoolAttributeList_Node1);
//
//        // node 2
//        ArrayList<PairInt<String, Integer>> IntAttributeList_Node2 = new ArrayList<>();
//        PairInt<String, Integer> intattribute1_Node2 = new PairInt<String, Integer>("numofVCPU", 5);
//        PairInt<String, Integer> intattribute2_Node2 = new PairInt<String, Integer>("numofGPU", 5);
//        //PairInt<String, Integer> intattribute3_Node2 = new PairInt<String, Integer>("numofMem", 9);
//        IntAttributeList_Node2.add(intattribute1_Node2);
//        IntAttributeList_Node2.add(intattribute2_Node2);
//        //IntAttributeList_Node2.add(intattribute3_Node2);
//        all_intattributes.add(IntAttributeList_Node2);
//
//        ArrayList<PairBool<String, Boolean>> BoolAttributeList_Node2 = new ArrayList<>();
//        PairBool<String, Boolean> boolattribute1_Node2 = new PairBool<String, Boolean>("isConnected", true);
//        PairBool<String, Boolean> boolattribute2_Node2 = new PairBool<String, Boolean>("isAvaliable", true);
//        PairBool<String, Boolean> boolattribute3_Node2 = new PairBool<String, Boolean>("isUsing", false);
//        BoolAttributeList_Node2.add(boolattribute1_Node2);
//        BoolAttributeList_Node2.add(boolattribute2_Node2);
//        BoolAttributeList_Node2.add(boolattribute3_Node2);
//        all_boolattributes.add(BoolAttributeList_Node2);
//
//        // node 3
//        ArrayList<PairInt<String, Integer>> IntAttributeList_Node3 = new ArrayList<>();
//        PairInt<String, Integer> intattribute1_Node3 = new PairInt<String, Integer>("numofVCPU", 8);
//        PairInt<String, Integer> intattribute2_Node3 = new PairInt<String, Integer>("numofGPU", 8);
//        //PairInt<String, Integer> intattribute3_Node3 = new PairInt<String, Integer>("numofMem", 11);
//        IntAttributeList_Node3.add(intattribute1_Node3);
//        IntAttributeList_Node3.add(intattribute2_Node3);
//        //IntAttributeList_Node3.add(intattribute3_Node3);
//        all_intattributes.add(IntAttributeList_Node3);
//
//        ArrayList<PairBool<String, Boolean>> BoolAttributeList_Node3 = new ArrayList<>();
//        PairBool<String, Boolean> boolattribute1_Node3 = new PairBool<String, Boolean>("isConnected", false);
//        PairBool<String, Boolean> boolattribute2_Node3 = new PairBool<String, Boolean>("isAvaliable", false);
//        PairBool<String, Boolean> boolattribute3_Node3 = new PairBool<String, Boolean>("isUsing", true);
//        BoolAttributeList_Node3.add(boolattribute1_Node3);
//        BoolAttributeList_Node3.add(boolattribute2_Node3);
//        BoolAttributeList_Node3.add(boolattribute3_Node3);
//        all_boolattributes.add(BoolAttributeList_Node3);
//
//        // node 4
//        ArrayList<PairInt<String, Integer>> IntAttributeList_Node4 = new ArrayList<>();
//        PairInt<String, Integer> intattribute1_Node4 = new PairInt<String, Integer>("numofVCPU", 5);
//        PairInt<String, Integer> intattribute2_Node4 = new PairInt<String, Integer>("numofGPU", 5);
//        //PairInt<String, Integer> intattribute3_Node3 = new PairInt<String, Integer>("numofMem", 11);
//        IntAttributeList_Node4.add(intattribute1_Node4);
//        IntAttributeList_Node4.add(intattribute2_Node4);
//        //IntAttributeList_Node3.add(intattribute3_Node3);
//        all_intattributes.add(IntAttributeList_Node4);
//
//        ArrayList<PairBool<String, Boolean>> BoolAttributeList_Node4 = new ArrayList<>();
//        PairBool<String, Boolean> boolattribute1_Node4 = new PairBool<String, Boolean>("isConnected", false);
//        PairBool<String, Boolean> boolattribute2_Node4 = new PairBool<String, Boolean>("isAvaliable", false);
//        PairBool<String, Boolean> boolattribute3_Node4 = new PairBool<String, Boolean>("isUsing", true);
//        BoolAttributeList_Node4.add(boolattribute1_Node4);
//        BoolAttributeList_Node4.add(boolattribute2_Node4);
//        BoolAttributeList_Node4.add(boolattribute3_Node4);
//        all_boolattributes.add(BoolAttributeList_Node4);
//
//        String name_seed_demand = "S";
//        StringBuffer name = new StringBuffer("");
//        int vertex_number = all_intattributes.size();
//
//        Graph supply_graph = new Graph(vertex_number);
//        ArrayList<Vertex> supplyVertex_empty = new ArrayList<>();
//        for (int i = 0; i < vertex_number; i++) {
//            name = name.append(name_seed_demand);
//            StringBuffer temp = new StringBuffer(name);
//            supplyVertex_empty.add(new Vertex(all_intattributes.get(i), all_boolattributes.get(i), temp));
//        }
//        supply_graph.insert_Vertex(supplyVertex_empty);
//
//        // edge 1
//        Edge supply_edge1 = new Edge(15, supply_graph.vertexlist.get(0), supply_graph.vertexlist.get(1), 0, 1);
//        // edge 2
//        Edge supply_edge2 = new Edge(25, supply_graph.vertexlist.get(0), supply_graph.vertexlist.get(2), 0, 2);
//        // edge 3
//        Edge supply_edge3 = new Edge(25, supply_graph.vertexlist.get(1), supply_graph.vertexlist.get(2), 1, 2);
//
//        supply_graph.insert_Edge(supply_graph.vertexlist.get(0), supply_graph.vertexlist.get(1),supply_edge1);
//        supply_graph.insert_Edge(supply_graph.vertexlist.get(0), supply_graph.vertexlist.get(2),supply_edge2);
//        supply_graph.insert_Edge(supply_graph.vertexlist.get(1), supply_graph.vertexlist.get(2),supply_edge3);
//        supply_graph.set_default_edge();
//
//        this.supply_graph = supply_graph;
//        this.supply_nodes = supply_graph.vertexlist;
//    }
//
//    public SetGraphInputCopy () {
//        setdemandgraph();
//        setsupplygraph();
//    }
//
//    public static void main(String[] args) {
//        Gson gs_dv = new Gson();
//        Gson gs_sv = new Gson();
//        Gson gs_dg = new Gson();
//        Gson gs_sg = new Gson();
//        ArrayList<Vertex> demand_v = new SetGraphInput().demand_nodes;
//        ArrayList<Vertex> supply_v = new SetGraphInput().supply_nodes;
//        Graph demand_graph = new SetGraphInput().demand_graph;
//        Graph supply_graph = new SetGraphInput().supply_graph;
//        String js_dv = gs_dv.toJson(demand_v);
//        String js_sv = gs_sv.toJson(supply_v);
//        String js_dg = gs_dg.toJson(demand_graph);
//        String js_sg = gs_sg.toJson(supply_graph);
//
//        ArrayList<Vertex> newDemandV = gs_dv.fromJson(js_dv, new TypeToken<ArrayList<Vertex>>(){}.getType());
//        ArrayList<Vertex> newSupplyV = gs_sv.fromJson(js_sv, new TypeToken<ArrayList<Vertex>>(){}.getType());
//        Graph newDemandGraph = gs_dg.fromJson(js_dg,Graph.class);
//        Graph newSupplyGraph = gs_sg.fromJson(js_sg,Graph.class);
//
//    }
//}
