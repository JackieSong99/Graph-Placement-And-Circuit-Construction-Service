import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Input the content of result.log and using regex to find the corresponding supply nodes needed for the placement
and output a list storing their indices */
public class FindPlacement {

    public static void findplacement(String result, Integer num_demand_node, Integer num_demand_edge,
                                     Integer num_supply_node, Integer num_supply_edge,
                                     ArrayList<Integer> demandnodes_uid, ArrayList<Integer> demandedges_uid,
                                     ArrayList<Integer> supplynodes_uid, ArrayList<Integer> supplyedges_uid,
                                     Integer num_snodes, Integer num_sedges, Boolean if_noconstrain) {
        if (if_noconstrain == false) {
            result = result.substring(4);
            String reg1 = "\\s[0-9]+";
            String reg2 = "[0-9]+";
            Pattern pattern1 = Pattern.compile(reg1);
            Pattern pattern2 = Pattern.compile(reg2);

            ArrayList<Integer> node_record = new ArrayList<>();
            int interval_start = 0;
            int interval_end = interval_start + 1;
            int flag = 0;
            while (node_record.size() < num_demand_node) {
                if (result.charAt(interval_end) == ' ') {
                    flag += 1;
                }
                if (flag == num_supply_node) {
                    String substring = result.substring(interval_start, interval_end + 1);
                    Matcher matcher1 = pattern1.matcher(substring);
                    if(matcher1.find())
                    {
                        Matcher matcher2 = pattern2.matcher(matcher1.group());
                        while (matcher2.find()) {
                            try {
                                int temp = Integer.parseInt(matcher2.group());
                                int temp_fin = temp % num_supply_node;
                                if (temp_fin == 0) {
                                    temp_fin = num_supply_node;
                                }
                                node_record.add(temp_fin);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        node_record.add(null);
                    }
                    interval_start = interval_end;
                    flag = 0;
                }
                interval_end ++;
            }

            // Construct output JSON
            JSONObject whole_output = new JSONObject();
            JSONObject sub_output = new JSONObject();
            whole_output.element("num_nodes", num_snodes);
            whole_output.element("num_edges", num_sedges);

            for (int i = 0; i < node_record.size(); i++) {
//                System.out.println("Demand node " + (i + 1) + "(uid = " + demandnodes_uid.get(i) + ")" +
//                        " should be placed into supply node " + node_record.get(i) +
//                        "(uid = " + supplynodes_uid.get(node_record.get(i) - 1) + ")");
                sub_output.element(demandnodes_uid.get(i).toString(), supplynodes_uid.get(node_record.get(i) - 1));
            }

            ArrayList<Integer> edge_record = new ArrayList<>();
            while (edge_record.size() < num_demand_edge) {
                if (result.charAt(interval_end) == ' ') {
                    flag += 1;
                }
                if (flag == num_supply_edge) {
                    String substring = result.substring(interval_start, interval_end + 1);
                    Matcher matcher1 = pattern1.matcher(substring);
                    if(matcher1.find())
                    {
                        Matcher matcher2 = pattern2.matcher(matcher1.group());
                        while (matcher2.find()) {
                            try {
                                int temp = Integer.parseInt(matcher2.group());
                                int temp_fin = (temp - num_demand_node * num_supply_node) % num_supply_edge;
                                if (temp_fin == 0) {
                                    temp_fin = num_supply_edge;
                                }
                                edge_record.add(temp_fin);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        edge_record.add(null);
                    }
                    interval_start = interval_end;
                    flag = 0;
                }
                interval_end ++;
            }

            ArrayList<Integer> edgetonode_record = new ArrayList<>();
            while (edgetonode_record.size() < num_demand_edge) {
                if (result.charAt(interval_end) == ' ') {
                    flag += 1;
                }
                if (flag == num_supply_node) {
                    String substring = result.substring(interval_start, interval_end + 1);
                    Matcher matcher1 = pattern1.matcher(substring);
                    if(matcher1.find())
                    {
                        Matcher matcher2 = pattern2.matcher(matcher1.group());
                        while (matcher2.find()) {
                            try {
                                int temp = Integer.parseInt(matcher2.group());
                                int temp_fin = (temp - num_demand_node * num_supply_node - num_demand_edge * num_supply_edge) % num_supply_node;
                                if (temp_fin == 0) {
                                    temp_fin = num_supply_node;
                                }
                                edgetonode_record.add(temp_fin);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        edgetonode_record.add(null);
                    }
                    interval_start = interval_end;
                    flag = 0;
                }
                interval_end ++;
            }

            for (int i = 0; i < edge_record.size(); i++) {
                if (edge_record.get(i) != null) {
//                    System.out.println("Demand edge " + (i + 1) + "(uid = " + demandedges_uid.get(i) + ")"
//                            + " should be placed into supply edge " + edge_record.get(i) +
//                            "(uid = " + supplyedges_uid.get(edge_record.get(i) - 1) + ")");
                    sub_output.element(demandedges_uid.get(i).toString(), supplyedges_uid.get(edge_record.get(i) - 1));
                }
                else if (edge_record.get(i) == null & edgetonode_record.get(i) != null) {
//                    System.out.println("Demand edge " + (i + 1) + "(uid = " + demandedges_uid.get(i) + ")"
//                            + " should be placed into supply node " + edgetonode_record.get(i) +
//                            "(uid = " + supplynodes_uid.get(edgetonode_record.get(i) - 1) + ")");
                    sub_output.element(demandedges_uid.get(i).toString(), supplynodes_uid.get(edgetonode_record.get(i) - 1));
                }
                else {
//                    System.out.println("Error in finding the placement");
                }
            }
            whole_output.element("placement", sub_output);
            String print_output = whole_output.toString();
            CreateJSON.createJsonFile(print_output, "jsonfile/", "output_placement");
            System.out.println(print_output);
        }
        else {
            result = result.substring(4);
            String reg1 = "\\s[0-9]+";
            String reg2 = "[0-9]+";
            Pattern pattern1 = Pattern.compile(reg1);
            Pattern pattern2 = Pattern.compile(reg2); // Matcher matcher1 = pattern1.matcher(result);

            ArrayList<Integer> node_record = new ArrayList<>();
            int interval_start = 0;
            int interval_end = interval_start + 1;
            int flag = 0;
            while (node_record.size() < num_demand_node) {
                if (result.charAt(interval_end) == ' ') {
                    flag += 1;
                }
                if (flag == num_supply_node) {
                    String substring = result.substring(interval_start, interval_end + 1);
                    Matcher matcher1 = pattern1.matcher(substring);
                    if(matcher1.find())
                    {
                        Matcher matcher2 = pattern2.matcher(matcher1.group());
                        while (matcher2.find()) {
                            try {
                                int temp = Integer.parseInt(matcher2.group());
                                int temp_fin = temp % num_supply_node;
                                if (temp_fin == 0) {
                                    temp_fin = num_supply_node;
                                }
                                node_record.add(temp_fin);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        node_record.add(null);
                    }
                    interval_start = interval_end;
                    flag = 0;
                }
                interval_end ++;
            }

            ArrayList<Integer> edge_record = new ArrayList<>();
            while (edge_record.size() < num_demand_edge) {
                if (result.charAt(interval_end) == ' ') {
                    flag += 1;
                }
                if (flag == num_supply_edge) {
                    String substring = result.substring(interval_start, interval_end + 1);
                    Matcher matcher1 = pattern1.matcher(substring);
                    if(matcher1.find())
                    {
                        Matcher matcher2 = pattern2.matcher(matcher1.group());
                        while (matcher2.find()) {
                            try {
                                int temp = Integer.parseInt(matcher2.group());
                                int temp_fin = (temp - num_demand_node * num_supply_node) % num_supply_edge;
                                if (temp_fin == 0) {
                                    temp_fin = num_supply_edge;
                                }
                                edge_record.add(temp_fin);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        edge_record.add(null);
                    }
                    interval_start = interval_end;
                    flag = 0;
                }
                interval_end ++;
            }

            ArrayList<Integer> edgetonode_record = new ArrayList<>();
            while (edgetonode_record.size() < num_demand_edge) {
                if (result.charAt(interval_end) == ' ') {
                    flag += 1;
                }
                if (flag == num_supply_node) {
                    String substring = result.substring(interval_start, interval_end + 1);
                    Matcher matcher1 = pattern1.matcher(substring);
                    if(matcher1.find())
                    {
                        Matcher matcher2 = pattern2.matcher(matcher1.group());
                        while (matcher2.find()) {
                            try {
                                int temp = Integer.parseInt(matcher2.group());
                                int temp_fin = (temp - num_demand_node * num_supply_node - num_demand_edge * num_supply_edge) % num_supply_node;
                                if (temp_fin == 0) {
                                    temp_fin = num_supply_node;
                                }
                                edgetonode_record.add(temp_fin);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        edgetonode_record.add(null);
                    }
                    interval_start = interval_end;
                    flag = 0;
                }
                interval_end ++;
            }

            int num_snode = 0;
            HashSet<Integer> node_set = new HashSet<>();
            for (int i = 0; i < node_record.size(); i++) {
                if(node_set.add(node_record.get(i))) {
                    num_snode = num_snode + 1;
                }
            }
            int num_sedge = 0;
            HashSet<Integer> edge_set = new HashSet<>();
            for (int i = 0; i < edge_record.size(); i++) {
                if(edge_record.get(i) != null && edge_set.add(edge_record.get(i))) {
                    num_sedge = num_sedge + 1;
                }
            }

            // Construct output JSON
            JSONObject whole_output = new JSONObject();
            JSONObject sub_output = new JSONObject();
            whole_output.element("num_nodes", num_snode);
            whole_output.element("num_edges", num_sedge);

//            System.out.println("***************************************************");
//            System.out.println("**   Final number of supply nodes needed is: " + num_snode + "   **");
//            System.out.println("**   Final number of supply edges needed is: " + num_sedge + "   **");
//            System.out.println("***************************************************");

            for (int i = 0; i < node_record.size(); i++) {
//                System.out.println("Demand node " + (i + 1) + "(uid = " + demandnodes_uid.get(i) + ")" +
//                        " should be placed into supply node " + node_record.get(i) +
//                        "(uid = " + supplynodes_uid.get(node_record.get(i) - 1) + ")");
                sub_output.element(demandnodes_uid.get(i).toString(), supplynodes_uid.get(node_record.get(i) - 1));
            }
            for (int i = 0; i < edge_record.size(); i++) {
                if (edge_record.get(i) != null) {
//                    System.out.println("Demand edge " + (i + 1) + "(uid = " + demandedges_uid.get(i) + ")"
//                            + " should be placed into supply edge " + edge_record.get(i) +
//                            "(uid = " + supplyedges_uid.get(edge_record.get(i) - 1) + ")");
                    sub_output.element(demandedges_uid.get(i).toString(), supplyedges_uid.get(edge_record.get(i) - 1));
                }
                else if (edge_record.get(i) == null & edgetonode_record.get(i) != null) {
//                    System.out.println("Demand edge " + (i + 1) + "(uid = " + demandedges_uid.get(i) + ")"
//                            + " should be placed into supply node " + edgetonode_record.get(i) +
//                            "(uid = " + supplynodes_uid.get(edgetonode_record.get(i) - 1) + ")");
                    sub_output.element(demandedges_uid.get(i).toString(), supplynodes_uid.get(edgetonode_record.get(i) - 1));
                }
                else {
//                    System.out.println("Error in finding the placement");
                }
            }
            whole_output.element("placement", sub_output);
            String print_output = whole_output.toString();
            CreateJSON.createJsonFile(print_output, "jsonfile/", "output_placement");
            System.out.println(print_output);
        }
    }
}