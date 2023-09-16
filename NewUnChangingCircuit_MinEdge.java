import circuit.BigAndOr;
import circuit.Circuit;
import circuit.IntegerAsCircuit;
import circuit.LessEquals;

import java.util.ArrayList;
import java.util.List;

public class NewUnChangingCircuit_MinEdge {
    public static List<Circuit.Wire> newunchangingcircuit(Circuit new_c, Circuit.Wire[][] new_wire_matrix_node,
                                                          circuit.Circuit.Wire[][] new_wire_matrix_edge,
                                                          circuit.Circuit.Wire[][] new_wire_matrix_mix,
                                                          Graph demand_graph, Graph supply_graph, Integer min_edge) throws Exception{
        List<Circuit.Wire> ret = new ArrayList<Circuit.Wire>();

        // Constrain 1
        IfNodeInjection.ifnodeinjection(new_c, new_wire_matrix_node, ret, demand_graph.vertexlist.size(), supply_graph.vertexlist.size());
        // Constrain 2
        IfEdgeInjection.ifedgeinjection(new_c, new_wire_matrix_edge, new_wire_matrix_mix, ret, demand_graph.numOfEdges, supply_graph.vertexlist.size(), supply_graph.numOfEdges);
        // Constrain 3
        IfNodeExceed.ifnodeexceed(new_c, new_wire_matrix_node, ret, demand_graph.vertexlist, supply_graph.vertexlist);
        // Constrain 4
        IfEdgeExceed.ifedgeexceed(new_c, new_wire_matrix_edge, ret, demand_graph.edgelist, supply_graph.edgelist);
        // Constrain 7
        IfEdgeToEdge.ifedgetoedge(new_c, new_wire_matrix_node, new_wire_matrix_edge, demand_graph.edgelist, supply_graph.edgelist, ret);
        // Constrain 8
        IfEdgeToNode.ifedgetonode(new_c, new_wire_matrix_mix, new_wire_matrix_node, demand_graph.edgelist, supply_graph.vertexlist, ret);

        // Constrain 6 -- Ensure
        List<Circuit.Wire> temp = new ArrayList<Circuit.Wire>();
        SumOfSupplyEdge.sumofsupply(new_c, new_wire_matrix_edge, temp, demand_graph.numOfEdges, supply_graph.numOfEdges);

        Circuit IAC_minedge = new IntegerAsCircuit(min_edge);
        new_c.union(IAC_minedge);
        Removeout.removeout(new_c, IAC_minedge);

        Circuit first_lessequal_constrain6 = new LessEquals(temp.size(), IAC_minedge.getOutputs().size());
        new_c.union(first_lessequal_constrain6);
        Removein.removein(new_c, first_lessequal_constrain6);
        Removeout.removeout(new_c, first_lessequal_constrain6);

        Circuit second_lessequal_constrain6 = new LessEquals(IAC_minedge.getOutputs().size(), temp.size());
        new_c.union(second_lessequal_constrain6);
        Removein.removein(new_c, second_lessequal_constrain6);
        Removeout.removeout(new_c, second_lessequal_constrain6);

        for (int i = 0; i < temp.size(); i++) {
            new_c.fuse(temp.get(i), first_lessequal_constrain6.getInputs().get(i));
            new_c.fuse(temp.get(i), second_lessequal_constrain6.getInputs().get(i + IAC_minedge.getOutputs().size()));
        }
        for (int i = 0; i < IAC_minedge.getOutputs().size(); i++) {
            new_c.fuse(IAC_minedge.getOutputs().get(i), first_lessequal_constrain6.getInputs().get(i + temp.size()));
            new_c.fuse(IAC_minedge.getOutputs().get(i), second_lessequal_constrain6.getInputs().get(i));
        }

        Circuit ensure_andgate_constrain6 = new BigAndOr(false, 2);
        new_c.union(ensure_andgate_constrain6);
        Removein.removein(new_c, ensure_andgate_constrain6);
        Removeout.removeout(new_c, ensure_andgate_constrain6);
        new_c.fuse(first_lessequal_constrain6.getOutputs().get(0), ensure_andgate_constrain6.getInputs().get(0));
        new_c.fuse(second_lessequal_constrain6.getOutputs().get(0), ensure_andgate_constrain6.getInputs().get(1));

        ret.add(ensure_andgate_constrain6.getOutputs().get(0));

        // Constrain 5
        SumOfSupplyNode.sumofsupply(new_c, new_wire_matrix_node, ret, demand_graph.numOfVertices, supply_graph.numOfVertices);

        return ret;
    }
}
