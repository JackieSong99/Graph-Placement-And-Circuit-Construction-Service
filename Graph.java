import java.util.ArrayList;
import java.util.Arrays;

public class Graph {
    public ArrayList<Vertex> vertexlist;
    public ArrayList<Edge> edgelist;
    public Edge [][] edges;
    public int numOfEdges;
    public int numOfVertices;
    public ArrayList<Integer> nodes_uid;
    public ArrayList<Integer> edges_uid;

    public Graph(int n){
        this.edges = new Edge[n][n];
        this.vertexlist = new ArrayList<Vertex>(n);
        this.numOfEdges = 0;
        this.edgelist = new ArrayList<Edge>();
        this.nodes_uid = new ArrayList<Integer>();
        this.edges_uid = new ArrayList<Integer>();
    }

    public void insert_nodeid(ArrayList<Integer> int_nodelist){
        this.nodes_uid = int_nodelist;
    }

    public void insert_edgeid(ArrayList<Integer> int_edgelist){
        this.edges_uid = int_edgelist;
    }

    public void insert_Vertex(ArrayList<Vertex> vlist_input) {
        vertexlist = vlist_input;
        numOfVertices = vertexlist.size();
    }

    public void set_default_edge(){
        for (int i = 0; i < numOfVertices; i++){
            Edge Infinity = new Edge(Integer.MAX_VALUE, vertexlist.get(i), vertexlist.get(i), i, i);
            edges[i][i] = Infinity;
        }
        for (int i = 0; i < edges.length; i++){
            for (int j = 0; j < edges[i].length; j++) {
                if (edges[i][j] == null) {
                    Edge zero = new Edge(0, vertexlist.get(i), vertexlist.get(j), i, j);
                    edges[i][j] = zero;
                }
            }
        }
    }

    public void set_default_edge_demand(){
        for (int i = 0; i < edges.length; i++){
            for (int j = 0; j < edges[i].length; j++) {
                if (edges[i][j] == null) {
                    Edge zero = new Edge(0, vertexlist.get(i), vertexlist.get(j), i, j);
                    edges[i][j] = zero;
                }
            }
        }
    }

    public void insert_Edge(Vertex a, Vertex b, Edge edge_input){
        int v1 = vertexlist.indexOf(a);
        int v2 = vertexlist.indexOf(b);
        edges[v1][v2] = edge_input;
        edges[v2][v1] = edge_input;
        edgelist.add(edge_input);
        numOfEdges++;
    }

    public void showGraph(){
        for (int i = 0; i < edges.length; i++) {
            System.out.print("[");
            // System.out.print(edges[0][3].edge_weight + ",");
            for (int j = 0; j < edges[i].length - 1; j++) {
                System.out.print(edges[i][j].edge_weight + ", ");
            }
            System.out.print(edges[i][edges[i].length - 1].edge_weight );
            System.out.println("]");
        }
    }

//    public static void main(String[] args) {
//        // test
//        ArrayList<ArrayList<PairInt<String, Integer>>> all_intattributes = new ArrayList<>();
//        ArrayList<ArrayList<PairBool<String, Boolean>>> all_boolattributes = new ArrayList<>();
//
//        // node 1
//        ArrayList<PairInt<String, Integer>> IntAttributeList_Node1 = new ArrayList<>();
//        PairInt<String, Integer> intattribute1 = new PairInt<String, Integer>("numofVCPU", 1);
//        PairInt<String, Integer> intattribute2 = new PairInt<String, Integer>("numofGPU", 2);
//        PairInt<String, Integer> intattribute3 = new PairInt<String, Integer>("numofMem", 5);
//        IntAttributeList_Node1.add(intattribute1);
//        IntAttributeList_Node1.add(intattribute2);
//        IntAttributeList_Node1.add(intattribute3);
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
//        //PairBool<String, Boolean> boolattribute2_Node2 = new PairBool<String, Boolean>("isAvaliable", true);
//        PairBool<String, Boolean> boolattribute3_Node2 = new PairBool<String, Boolean>("isUsing", false);
//        BoolAttributeList_Node2.add(boolattribute1_Node2);
//        //BoolAttributeList_Node2.add(boolattribute2_Node2);
//        BoolAttributeList_Node2.add(boolattribute3_Node2);
//        all_boolattributes.add(BoolAttributeList_Node2);
//
//        // node 3
//        ArrayList<PairInt<String, Integer>> IntAttributeList_Node3 = new ArrayList<>();
//        PairInt<String, Integer> intattribute1_Node3 = new PairInt<String, Integer>("numofVCPU", 3);
//        PairInt<String, Integer> intattribute2_Node3 = new PairInt<String, Integer>("numofGPU", 4);
//        PairInt<String, Integer> intattribute3_Node3 = new PairInt<String, Integer>("numofMem", 2);
//        IntAttributeList_Node3.add(intattribute1_Node3);
//        IntAttributeList_Node3.add(intattribute2_Node3);
//        IntAttributeList_Node3.add(intattribute3_Node3);
//        all_intattributes.add(IntAttributeList_Node3);
//
//        ArrayList<PairBool<String, Boolean>> BoolAttributeList_Node3 = new ArrayList<>();
//        PairBool<String, Boolean> boolattribute1_Node3 = new PairBool<String, Boolean>("isConnected", false);
//        //PairBool<String, Boolean> boolattribute2_Node3 = new PairBool<String, Boolean>("isAvaliable", false);
//        //PairBool<String, Boolean> boolattribute3_Node3 = new PairBool<String, Boolean>("isUsing", true);
//        BoolAttributeList_Node3.add(boolattribute1_Node3);
//        //BoolAttributeList_Node3.add(boolattribute2_Node3);
//        //BoolAttributeList_Node3.add(boolattribute3_Node3);
//        all_boolattributes.add(BoolAttributeList_Node3);
//
//        // node 4
//        ArrayList<PairInt<String, Integer>> IntAttributeList_Node4 = new ArrayList<>();
//        //PairInt<String, Integer> intattribute1_Node4 = new PairInt<String, Integer>("numofVCPU", 1);
//        //PairInt<String, Integer> intattribute2_Node4 = new PairInt<String, Integer>("numofGPU", 1);
//        PairInt<String, Integer> intattribute3_Node4 = new PairInt<String, Integer>("numofMem", 2);
//        //IntAttributeList_Node4.add(intattribute1_Node4);
//        //IntAttributeList_Node4.add(intattribute2_Node4);
//        IntAttributeList_Node4.add(intattribute3_Node4);
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
//        Graph graph = new Graph(vertex_number);
//
//        ArrayList<Vertex> Vertex_empty = new ArrayList<>();
//        for (int i = 0; i < vertex_number; i++) {
//            name = name.append(name_seed_demand);
//            StringBuffer temp = new StringBuffer(name);
//            Vertex_empty.add(new Vertex(all_intattributes.get(i), all_boolattributes.get(i), temp));
//        }
//        graph.insert_Vertex(Vertex_empty);
//
//        // Set Demand graph edge
//        // edge 1
//        Edge demand_edge1 = new Edge(5, graph.vertexlist.get(0), graph.vertexlist.get(1));
//        // edge 2
//        Edge demand_edge2 = new Edge(15, graph.vertexlist.get(1), graph.vertexlist.get(3));
//        // edge 3
//        Edge demand_edge3 = new Edge(10, graph.vertexlist.get(0), graph.vertexlist.get(3));
//
//        graph.insert_Edge(graph.vertexlist.get(0), graph.vertexlist.get(1),demand_edge1);
//        graph.insert_Edge(graph.vertexlist.get(1), graph.vertexlist.get(3),demand_edge2);
//        graph.insert_Edge(graph.vertexlist.get(0), graph.vertexlist.get(3),demand_edge3);
//        graph.set_default_edge();
//
//        System.out.println("Number of demand edges: " + graph.numOfEdges);
//        System.out.println("Number of demand vertices: " + graph.numOfVertices);
//        graph.showGraph();
//    }
}