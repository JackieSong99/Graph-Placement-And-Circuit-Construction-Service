import circuit.BigAndOr;
import circuit.Circuit;
import circuit.CircuitUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.sf.json.*;
// java -Djava.library.path=/home/jialingsong/UW/LFV0417/lib Run
// javac -cp /home/jialingsong/UW/LFV0417/lib/*.jar Run.java
public class Run {
    public static void main(String[] args) throws Exception{
//        String filename = "mock_graph.json";
//        SetGraphInput newG = new SetGraphInput(filename); // "mock_graph.json"
        SetGraphInput newG = new SetGraphInput(args[0]);
        ArrayList<Vertex> demand_v = newG.demand_nodes;
        ArrayList<Vertex> supply_v = newG.supply_nodes;
        Graph demand_graph = newG.demand_graph;
        Graph supply_graph = newG.supply_graph;
        Integer indicator = newG.prioritization;
        ArrayList<Integer> demandnodes_uid = newG.demandnodes_uid;
        ArrayList<Integer> demandedges_uid = newG.demandedges_uid;
        ArrayList<Integer> supplynodes_uid = newG.supplynodes_uid;
        ArrayList<Integer> supplyedges_uid = newG.supplyedges_uid;

        if (supply_v.size() == 1) {
            GraphSpecialCases.onesupplynode(demand_v, supply_v);
        }
        else if (demand_v.size() == 1) {
            GraphSpecialCases.onedemandnode(demand_v, supply_v);
        }
        else if (demand_v.size() > 1 & supply_v.size() > 1) {
            // Define the name of the whole circuit and the sub circuit of changing circuit
            circuit.Circuit.resetIDs();
            circuit.ZeroOne.reset();
            circuit.Circuit c = new circuit.Circuit();
            circuit.Circuit changingSubcircuit = null;
            // Construct a wire for every xij.
            int m = 0;
            circuit.Circuit.Wire[][] wire_matrix_node = new circuit.Circuit.Wire[demand_v.size()][supply_v.size()]; // Define wire matrix
            for (int i = 0; i < demand_v.size(); i++)
            {
                for (int j = 0; j < supply_v.size(); j++)
                {
                    c.addNewInput();
                    wire_matrix_node[i][j] = c.getInputs().get(m);
                    m++;
                }
            }
            circuit.Circuit.Wire[][] wire_matrix_edge = new circuit.Circuit.Wire[demand_graph.numOfEdges][supply_graph.numOfEdges]; // Define wire matrix
            for (int i = 0; i < demand_graph.numOfEdges; i++)
            {
                for (int j = 0; j < supply_graph.numOfEdges; j++)
                {
                    c.addNewInput();
                    wire_matrix_edge[i][j] = c.getInputs().get(m);
                    m++;
                }
            }
            circuit.Circuit.Wire[][] wire_matrix_mix = new circuit.Circuit.Wire[demand_graph.numOfEdges][supply_v.size()]; // Define wire matrix
            for (int i = 0; i < demand_graph.numOfEdges; i++)
            {
                for (int j = 0; j < supply_v.size(); j++)
                {
                    c.addNewInput();
                    wire_matrix_mix[i][j] = c.getInputs().get(m);
                    m++;
                }
            }

            if (indicator == 0) {
                // No Constrain
//                 System.out.println("No contrains are made: ");
                List<Circuit.Wire> hook = UnChangingCircuit_NoConstrain.unchangingcircuit(c, wire_matrix_node, wire_matrix_edge,
                        wire_matrix_mix, demand_graph, supply_graph);
                Circuit finalAndGate = new BigAndOr(false, hook.size());
                c.union(finalAndGate);
                Removein.removein(c, finalAndGate);
                for (int i = 0; i < hook.size(); i++) {
                    c.fuse(hook.get(i), finalAndGate.getInputs().get(i));
                }

//                List<Boolean> ibool = new LinkedList<Boolean>();
//                ibool.add(Boolean.TRUE);
//                ibool.add(Boolean.FALSE);
//
//                ibool.add(Boolean.FALSE);
//                ibool.add(Boolean.TRUE);
//
//                ibool.add(Boolean.TRUE);
//
//                ibool.add(Boolean.FALSE);
//                ibool.add(Boolean.FALSE);
//
//                List<Boolean> obool = c.evaluate(ibool);
//                System.out.println("Output bool: " + obool.get(0));

                CircuitUtils.cnfSatToFile(c, "cnf.dimacs");
                Boolean flag = Solve.solve();
                if (flag == false) {
                    System.out.println("UNSATISFIABLE");
                }
                else {
                    String text = ReadFileContent.readFileContent("result.log");
                    // System.out.println(text);  // uncomment to see more information of the final results
                    FindPlacement.findplacement(text, demand_graph.numOfVertices,
                            demand_graph.numOfEdges, supply_graph.numOfVertices, supply_graph.numOfEdges,
                            demandnodes_uid, demandedges_uid, supplynodes_uid, supplyedges_uid,
                            null, null, true);
                }
            }

            else if (indicator == 1) {
                // Minimize node first
//                 System.out.println("Minimize the number of supply nodes needed first: ");

                // Define hook and newhook
                List<Circuit.Wire> hook = UnChangingCircuit_MinNode.unchangingcircuit(c, wire_matrix_node, wire_matrix_edge,
                        wire_matrix_mix, demand_graph, supply_graph);

                // Define changing circuit during binary search
                int lo = 0;
                int hi = supply_graph.numOfVertices;
                ArrayList<Integer> init = new ArrayList<Integer>();
                ReturnToRun final_records = BinarySearch.binary(lo, hi, c, changingSubcircuit, hook, demand_graph, supply_graph, init, false);
                if (final_records.final_record_k.size() == 0) {
                    System.out.println("UNSATISFIABLE");
                }
                else if (final_records.final_record_k.size() > 0){
                    final_records.final_record_k.sort(Comparator.naturalOrder());
                    int final_node_num = final_records.final_record_k.get(0);
//                    System.out.println("***************************************************");
//                    System.out.println("**   Final number of supply nodes needed is: " + final_node_num + "   **");

                    // Constructing new unchanging circuit for finding the minimum number of supply nodes
                    circuit.Circuit.resetIDs();
                    circuit.ZeroOne.reset();
                    circuit.Circuit new_c = new circuit.Circuit();

                    // Set new circuit's wire input
                    int new_m = 0;
                    circuit.Circuit.Wire[][] new_wire_matrix_node = new circuit.Circuit.Wire[demand_v.size()][supply_v.size()]; // Define wire matrix
                    for (int i = 0; i < demand_v.size(); i++)
                    {
                        for (int j = 0; j < supply_v.size(); j++)
                        {
                            new_c.addNewInput();
                            new_wire_matrix_node[i][j] = new_c.getInputs().get(new_m);
                            new_m++;
                        }
                    }
                    circuit.Circuit.Wire[][] new_wire_matrix_edge = new circuit.Circuit.Wire[demand_graph.numOfEdges][supply_graph.numOfEdges]; // Define wire matrix
                    for (int i = 0; i < demand_graph.numOfEdges; i++)
                    {
                        for (int j = 0; j < supply_graph.numOfEdges; j++)
                        {
                            new_c.addNewInput();
                            new_wire_matrix_edge[i][j] = new_c.getInputs().get(new_m);
                            new_m++;
                        }
                    }
                    circuit.Circuit.Wire[][] new_wire_matrix_mix = new circuit.Circuit.Wire[demand_graph.numOfEdges][supply_v.size()]; // Define wire matrix
                    for (int i = 0; i < demand_graph.numOfEdges; i++)
                    {
                        for (int j = 0; j < supply_v.size(); j++)
                        {
                            new_c.addNewInput();
                            new_wire_matrix_mix[i][j] = new_c.getInputs().get(new_m);
                            new_m++;
                        }
                    }

                    List<Circuit.Wire> new_hook = NewUnChangingCircuit_MinNode.newunchangingcircuit(new_c,
                            new_wire_matrix_node, new_wire_matrix_edge, new_wire_matrix_mix, demand_graph, supply_graph, final_node_num);
                    int new_lo = 0;
                    int new_hi = supply_graph.numOfEdges;
                    ArrayList<Integer> new_init = new ArrayList<Integer>();
                    circuit.Circuit new_changingSubcircuit = null;
                    ReturnToRun final_edgerecords = BinarySearch.binary(new_lo, new_hi, new_c, new_changingSubcircuit, new_hook, demand_graph, supply_graph, new_init, true);
                    if (final_edgerecords.final_record_k.size() == 0) {
                        System.out.println("UNSATISFIABLE");
                    }
                    else if (final_edgerecords.final_record_k.size() > 0) {
                        final_edgerecords.final_record_k.sort(Comparator.naturalOrder());
                        int final_edge_num = final_edgerecords.final_record_k.get(0);
//                        System.out.println("**   Final number of supply edges needed is: " + final_edge_num + "   **");
//                        System.out.println("***************************************************");

                        // finalize the edge placement
                        ChangingCircuit.changingcircuit(final_edgerecords.final_record_k.get(0), new_c, final_edgerecords.final_subc, new_hook, demand_graph, supply_graph, true);

                        Runtime execute = Runtime.getRuntime();
                        try {
                            BufferedReader minisat_output = new BufferedReader(new InputStreamReader(execute.exec("minisat cnf.dimacs result.log").getInputStream()));
                            String output = "";
                            StringBuffer temp_output=new StringBuffer();
                            while ((output = minisat_output.readLine())!=null) {
                                temp_output.append(output+"\n");
                            }
                            // System.out.println(temp_output);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                        String text = ReadFileContent.readFileContent("result.log");
                        // System.out.println(text);  // uncomment to see more information of the final results
                        FindPlacement.findplacement(text, demand_graph.numOfVertices,
                                demand_graph.numOfEdges, supply_graph.numOfVertices, supply_graph.numOfEdges,
                                demandnodes_uid, demandedges_uid, supplynodes_uid, supplyedges_uid,
                                final_node_num, final_edge_num, false);
                    }
                }
            }
            else if (indicator == 2) {
                    // Minimize edge first
//                     System.out.println("Minimize the number of supply edges needed first: ");

                    // Define hook and newhook
                    List<Circuit.Wire> hook = UnChangingCircuit_MinEdge.unchangingcircuit(c, wire_matrix_node, wire_matrix_edge,
                            wire_matrix_mix, demand_graph, supply_graph);

                    // Define changing circuit during binary search
                    int lo = 0;
                    int hi = supply_graph.numOfEdges;
                    ArrayList<Integer> init = new ArrayList<Integer>();
                    ReturnToRun final_edge_records = BinarySearch.binary(lo, hi, c, changingSubcircuit, hook, demand_graph, supply_graph, init, false);
                    if (final_edge_records.final_record_k.size() == 0) {
                        System.out.println("UNSATISFIABLE");
                    }
                    else if (final_edge_records.final_record_k.size() > 0){
                        final_edge_records.final_record_k.sort(Comparator.naturalOrder());
                        int final_edge_num = final_edge_records.final_record_k.get(0);
//                        System.out.println("***************************************************");
//                        System.out.println("**   Final number of supply edges needed is: " + final_edge_num + "   **");

                        // Constructing new unchanging circuit for finding the minimum number of supply nodes
                        circuit.Circuit.resetIDs();
                        circuit.ZeroOne.reset();
                        circuit.Circuit new_c = new circuit.Circuit();

                        // Set new circuit's wire input
                        int new_m = 0;
                        circuit.Circuit.Wire[][] new_wire_matrix_node = new circuit.Circuit.Wire[demand_v.size()][supply_v.size()]; // Define wire matrix
                        for (int i = 0; i < demand_v.size(); i++)
                        {
                            for (int j = 0; j < supply_v.size(); j++)
                            {
                                new_c.addNewInput();
                                new_wire_matrix_node[i][j] = new_c.getInputs().get(new_m);
                                new_m++;
                            }
                        }
                        circuit.Circuit.Wire[][] new_wire_matrix_edge = new circuit.Circuit.Wire[demand_graph.numOfEdges][supply_graph.numOfEdges]; // Define wire matrix
                        for (int i = 0; i < demand_graph.numOfEdges; i++)
                        {
                            for (int j = 0; j < supply_graph.numOfEdges; j++)
                            {
                                new_c.addNewInput();
                                new_wire_matrix_edge[i][j] = new_c.getInputs().get(new_m);
                                new_m++;
                            }
                        }
                        circuit.Circuit.Wire[][] new_wire_matrix_mix = new circuit.Circuit.Wire[demand_graph.numOfEdges][supply_v.size()]; // Define wire matrix
                        for (int i = 0; i < demand_graph.numOfEdges; i++)
                        {
                            for (int j = 0; j < supply_v.size(); j++)
                            {
                                new_c.addNewInput();
                                new_wire_matrix_mix[i][j] = new_c.getInputs().get(new_m);
                                new_m++;
                            }
                        }

                        List<Circuit.Wire> new_hook = NewUnChangingCircuit_MinEdge.newunchangingcircuit(new_c,
                                new_wire_matrix_node, new_wire_matrix_edge, new_wire_matrix_mix, demand_graph, supply_graph, final_edge_num);
                        int new_lo = 0;
                        int new_hi = supply_graph.numOfVertices;
                        ArrayList<Integer> new_init = new ArrayList<Integer>();
                        circuit.Circuit new_changingSubcircuit = null;
                        ReturnToRun final_noderecords = BinarySearch.binary(new_lo, new_hi, new_c, new_changingSubcircuit, new_hook, demand_graph, supply_graph, new_init, true);
                        if (final_noderecords.final_record_k.size() == 0) {
                            System.out.println("UNSATISFIABLE");
                        }
                        else if (final_noderecords.final_record_k.size() > 0) {
                            final_noderecords.final_record_k.sort(Comparator.naturalOrder());
                            int final_node_num = final_noderecords.final_record_k.get(0);
//                            System.out.println("**   Final number of supply nodes needed is: " + final_node_num + "   **");
//                            System.out.println("***************************************************");

                            // finalize the edge placement
                            ChangingCircuit.changingcircuit(final_noderecords.final_record_k.get(0), new_c, final_noderecords.final_subc, new_hook, demand_graph, supply_graph, true);

                            Runtime execute = Runtime.getRuntime();
                            try {
                                BufferedReader minisat_output = new BufferedReader(new InputStreamReader(execute.exec("minisat cnf.dimacs result.log").getInputStream()));
                                String output = "";
                                StringBuffer temp_output=new StringBuffer();
                                while ((output = minisat_output.readLine())!=null) {
                                    temp_output.append(output+"\n");
                                }
                                // System.out.println(temp_output);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                            String text = ReadFileContent.readFileContent("result.log");
                            // System.out.println(text);  // uncomment to see more information of the final results
                            FindPlacement.findplacement(text, demand_graph.numOfVertices,
                                    demand_graph.numOfEdges, supply_graph.numOfVertices, supply_graph.numOfEdges,
                                    demandnodes_uid, demandedges_uid, supplynodes_uid, supplyedges_uid,
                                    final_node_num, final_edge_num, false);
                }
            }
        }
    }
}
}
