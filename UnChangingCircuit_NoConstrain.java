import circuit.Circuit;

import java.util.ArrayList;
import java.util.List;

public class UnChangingCircuit_NoConstrain {
    public static List<Circuit.Wire> unchangingcircuit(Circuit c, Circuit.Wire[][] wire_matrix_node,
                                                       circuit.Circuit.Wire[][] wire_matrix_edge,
                                                       circuit.Circuit.Wire[][] wire_matrix_mix,
                                                       Graph demand_graph, Graph supply_graph) throws Exception{

        List<Circuit.Wire> ret = new ArrayList<Circuit.Wire>();
        // COnstrain 1
        IfNodeInjection.ifnodeinjection(c, wire_matrix_node, ret, demand_graph.vertexlist.size(), supply_graph.vertexlist.size());
        // Constrain 2
        IfEdgeInjection.ifedgeinjection(c, wire_matrix_edge, wire_matrix_mix, ret, demand_graph.numOfEdges, supply_graph.vertexlist.size(), supply_graph.numOfEdges);
        // Constrain 3
        IfNodeExceed.ifnodeexceed(c, wire_matrix_node, ret, demand_graph.vertexlist, supply_graph.vertexlist);
        // Constrain 4
        IfEdgeExceed.ifedgeexceed(c, wire_matrix_edge, ret, demand_graph.edgelist, supply_graph.edgelist);
        // Constrain 7
        IfEdgeToEdge.ifedgetoedge(c, wire_matrix_node, wire_matrix_edge, demand_graph.edgelist, supply_graph.edgelist, ret);
        // Constrain 8
        IfEdgeToNode.ifedgetonode(c, wire_matrix_mix, wire_matrix_node, demand_graph.edgelist, supply_graph.vertexlist, ret);
        // Constrain 5
        //SumOfSupplyNode.sumofsupply(c, wire_matrix_node, ret, demand_graph.numOfVertices, supply_graph.numOfVertices);
        //System.out.println("After C5, number of output wires for unchanging part is: " + ret.size());
        return ret;
    }
}
