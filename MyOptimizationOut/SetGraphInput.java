import java.util.ArrayList;
import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SetGraphInput {
    ArrayList<Vertex> demand_nodes = new ArrayList<>();
    ArrayList<Vertex> supply_nodes = new ArrayList<>();
    Graph demand_graph;
    Graph supply_graph;
    Integer prioritization;
    ArrayList<Integer> demandnodes_uid = new ArrayList<>();
    ArrayList<Integer> demandedges_uid = new ArrayList<>();
    ArrayList<Integer> supplynodes_uid = new ArrayList<>();
    ArrayList<Integer> supplyedges_uid = new ArrayList<>();

    public static Graph definegraph(JSONArray G_vertices, JSONArray G_edges) {
        ArrayList<ArrayList<PairInt<String, Integer>>> all_intattributes = new ArrayList<>();
        ArrayList<ArrayList<PairBool<String, Boolean>>> all_boolattributes = new ArrayList<>();
        ArrayList<String> node_name = new ArrayList<>();
        ArrayList<Integer> node_uid = new ArrayList<>();
        ArrayList<Integer> edge_uid = new ArrayList<>();
        int vertex_number = G_vertices.size();

        for (int i = 0; i < vertex_number; i++) {
            Iterator<String> keys = JSONObject.fromObject(G_vertices.get(i).toString()).keys();
            ArrayList<PairInt<String, Integer>> IntAttributeList_Node = new ArrayList<>();
            ArrayList<PairBool<String, Boolean>> BoolAttributeList_Node = new ArrayList<>();
            while (keys.hasNext()) {
                String key = keys.next();
                node_name.add(key);
                JSONObject node_info = JSONObject.fromObject(JSONObject.fromObject(G_vertices.get(i).toString()).getString(key));
                Iterator<String> info_keys = node_info.keys();
                while (info_keys.hasNext()) {
                    String info_key = info_keys.next();
                    if (info_key.equals("uid")){
                        node_uid.add(node_info.getInt(info_key));
                    }
                    if ((!info_key.equals("uid")) && (node_info.get(info_key) instanceof Integer)) {
                        PairInt<String, Integer> intattribute = new PairInt<>(info_key, node_info.getInt(info_key));
                        IntAttributeList_Node.add(intattribute);
                    }
                    if (node_info.get(info_key) instanceof Boolean) {
                        PairBool<String, Boolean> boolattribute = new PairBool<String, Boolean>(info_key, node_info.getBoolean(info_key));
                        BoolAttributeList_Node.add(boolattribute);
                    }
                }
            }
            all_intattributes.add(IntAttributeList_Node);
            all_boolattributes.add(BoolAttributeList_Node);
        }

        Graph graph = new Graph(vertex_number);
        graph.insert_nodeid(node_uid);

        ArrayList<Vertex> Vertex_empty = new ArrayList<>();
        for (int i = 0; i < vertex_number; i++) {
            Vertex_empty.add(new Vertex(all_intattributes.get(i), all_boolattributes.get(i), node_name.get(i), node_uid.get(i)));
        }
        graph.insert_Vertex(Vertex_empty);

        int edge_number = G_edges.size();
        for (int i = 0; i < edge_number; i++) {
            Iterator<String> keys = JSONObject.fromObject(G_edges.get(i).toString()).keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject edge_info = JSONObject.fromObject(JSONObject.fromObject(G_edges.get(i).toString()).getString(key));
                int uid_edge = edge_info.getInt("uid");
                edge_uid.add(uid_edge);
                int weight = edge_info.getInt("weight");
                int source = edge_info.getInt("source");
                int destination = edge_info.getInt("destination");
                int src = Checkindex(source, Vertex_empty);
                int des = Checkindex(destination, Vertex_empty);
                Edge edge = new Edge(weight, graph.vertexlist.get(src), graph.vertexlist.get(des), src, des);
                graph.insert_Edge(graph.vertexlist.get(src), graph.vertexlist.get(des), edge);
            }
        }
        graph.set_default_edge();
        graph.insert_edgeid(edge_uid);
        return graph;
    }

    public static Integer Checkindex(Integer input_index, ArrayList<Vertex> vertex_list){
        int real_index = -1;
        for (int i = 0; i < vertex_list.size(); i++){
            if (vertex_list.get(i).node_uid == input_index){
                real_index = i;
            }
        }
        return real_index;
    }

    public SetGraphInput (String filename) throws Exception {
        String jsoninput = ReadJson.readjson(filename);
        JSONObject jsonObject = JSONObject.fromObject(jsoninput);
        this.prioritization = jsonObject.getInt("prioritization");
        JSONObject demandG = JSONObject.fromObject(jsonObject.getString("demand"));
        JSONObject supplyG = JSONObject.fromObject(jsonObject.getString("supply"));
        JSONArray demandG_vertices = demandG.getJSONArray("vertices");
        JSONArray demandG_edges = demandG.getJSONArray("edges");
        JSONArray supplyG_vertices = supplyG.getJSONArray("vertices");
        JSONArray suppplyG_edges = supplyG.getJSONArray("edges");

        this.demand_graph = definegraph(demandG_vertices, demandG_edges);
        this.demand_nodes = demand_graph.vertexlist;
        this.demandnodes_uid = demand_graph.nodes_uid;
        this.demandedges_uid = demand_graph.edges_uid;

        this.supply_graph = definegraph(supplyG_vertices, suppplyG_edges);
        this.supply_nodes = supply_graph.vertexlist;
        this.supplynodes_uid = supply_graph.nodes_uid;
        this.supplyedges_uid = supply_graph.edges_uid;
    }
}