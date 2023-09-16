import circuit.BigAndOr;
import circuit.Circuit;
import circuit.IntegerAsCircuit;
import circuit.LessEquals;

import java.util.ArrayList;
import java.util.List;

public class NewUnChangingCircuit_MinNode {
    public static List<Circuit.Wire> newunchangingcircuit(Circuit new_c, Circuit.Wire[][] new_wire_matrix_node,
                                                          circuit.Circuit.Wire[][] new_wire_matrix_edge,
                                                          circuit.Circuit.Wire[][] new_wire_matrix_mix,
                                                          Graph demand_graph, Graph supply_graph, Integer min_node) throws Exception{
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

        // Constrain 5 -- Ensure
        List<Circuit.Wire> temp = new ArrayList<Circuit.Wire>();
        SumOfSupplyNode.sumofsupply(new_c, new_wire_matrix_node, temp, demand_graph.numOfVertices, supply_graph.numOfVertices);

        Circuit IAC_minnode = new IntegerAsCircuit(min_node);
        new_c.union(IAC_minnode);
        Removeout.removeout(new_c, IAC_minnode);

        Circuit first_lessequal_constrain5 = new LessEquals(temp.size(), IAC_minnode.getOutputs().size());
        new_c.union(first_lessequal_constrain5);
        Removein.removein(new_c, first_lessequal_constrain5);
        Removeout.removeout(new_c, first_lessequal_constrain5);

        Circuit second_lessequal_constrain5 = new LessEquals(IAC_minnode.getOutputs().size(), temp.size());
        new_c.union(second_lessequal_constrain5);
        Removein.removein(new_c, second_lessequal_constrain5);
        Removeout.removeout(new_c, second_lessequal_constrain5);

        for (int i = 0; i < temp.size(); i++) {
            new_c.fuse(temp.get(i), first_lessequal_constrain5.getInputs().get(i));
            new_c.fuse(temp.get(i), second_lessequal_constrain5.getInputs().get(i + IAC_minnode.getOutputs().size()));
        }
        for (int i = 0; i < IAC_minnode.getOutputs().size(); i++) {
            new_c.fuse(IAC_minnode.getOutputs().get(i), first_lessequal_constrain5.getInputs().get(i + temp.size()));
            new_c.fuse(IAC_minnode.getOutputs().get(i), second_lessequal_constrain5.getInputs().get(i));
        }

        Circuit ensure_andgate_constrain5 = new BigAndOr(false, 2);
        new_c.union(ensure_andgate_constrain5);
        Removein.removein(new_c, ensure_andgate_constrain5);
        Removeout.removeout(new_c, ensure_andgate_constrain5);
        new_c.fuse(first_lessequal_constrain5.getOutputs().get(0), ensure_andgate_constrain5.getInputs().get(0));
        new_c.fuse(second_lessequal_constrain5.getOutputs().get(0), ensure_andgate_constrain5.getInputs().get(1));

        ret.add(ensure_andgate_constrain5.getOutputs().get(0));

        // Constrain 6
        SumOfSupplyEdge.sumofsupply(new_c, new_wire_matrix_edge, ret, demand_graph.numOfEdges, supply_graph.numOfEdges);

        return ret;
    }
}
