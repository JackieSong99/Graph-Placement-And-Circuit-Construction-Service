import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class ParseInput {

    public static void parseinput(String filename) throws Exception {
        String jsoninput = ReadJson.readjson(filename);
        JSONObject whole_input = JSONObject.fromObject(jsoninput);
        int prioritization = whole_input.getInt("prioritization");
        JSONObject demandG = JSONObject.fromObject(whole_input.getString("demand"));
        System.out.println(demandG);
        JSONObject supplyG = JSONObject.fromObject(whole_input.getString("supply"));
        JSONArray demandG_vertices = demandG.getJSONArray("vertices");
        JSONArray demandG_edges = demandG.getJSONArray("edges");
        JSONArray supplyG_vertices = supplyG.getJSONArray("vertices");
        JSONArray suppplyG_edges = supplyG.getJSONArray("edges");
        System.out.println(prioritization);
        System.out.println(demandG_vertices);
        System.out.println(demandG_edges);
        System.out.println(supplyG_vertices);
        System.out.println(suppplyG_edges);

        for(int i = 0; i < demandG_vertices.size(); i++){
            Iterator<String> keys = JSONObject.fromObject(demandG_vertices.get(i).toString()).keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject node_info = JSONObject.fromObject(JSONObject.fromObject(demandG_vertices.get(i).toString()).getString(key));
                System.out.println(node_info);

            }
        }

    }
        public static void main(String[] args) throws Exception{
            parseinput("mock_graph.json");
        }
}
