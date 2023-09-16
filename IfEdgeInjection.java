import circuit.*;

import java.util.ArrayList;
import java.util.List;

public class IfEdgeInjection {
//  demand_graph.numOfEdges, supply_graph.vertexlist.size(), supply_graph.numOfEdges
    public static void ifedgeinjection (circuit.Circuit c, circuit.Circuit.Wire[][] wire_matrix_edge,
                                        circuit.Circuit.Wire[][] wire_matrix_mix, List<circuit.Circuit.Wire> ret,
                                        Integer demandedge_size, Integer supplynode_size, Integer supplyedge_size) throws Exception{
        Integer numofslots = supplynode_size + supplyedge_size;
        for (int d = 0; d < demandedge_size; d++) {
            // Construct Sum gate
            List<Integer> ones_c1 = new ArrayList<>();
            for (int j = 0; j < numofslots; j++) {
                ones_c1.add(1);
            }
            circuit.Circuit sum_constrain2 = new circuit.Sum(ones_c1);
            c.union(sum_constrain2);
            Removein.removein(c, sum_constrain2);
            Removeout.removeout(c, sum_constrain2);
            for (int j = 0; j < supplyedge_size; j++) {
                c.fuse(wire_matrix_edge[d][j], sum_constrain2.getInputs().get(j));
            }
            for (int k = supplyedge_size; k < numofslots; k++) {
                c.fuse(wire_matrix_mix[d][k - supplyedge_size], sum_constrain2.getInputs().get(k));
            }

            // ZeroOne.getOne() using IntegerAsCircuit(1) instead
            circuit.Circuit zeroone_constrain2 = new circuit.IntegerAsCircuit(1);;
            c.union(zeroone_constrain2);
            Removeout.removeout(c, zeroone_constrain2);

            // Less equal
            int sum_osize_c1 = sum_constrain2.getOutputs().size();
            circuit.Circuit firstlessequal_constrain2 = new circuit.LessEquals(sum_osize_c1, 1);
            c.union(firstlessequal_constrain2);
            circuit.Circuit secondlessequal_constrain2 = new circuit.LessEquals(1, sum_osize_c1);
            c.union(secondlessequal_constrain2);
            Removein.removein(c, firstlessequal_constrain2);
            Removeout.removeout(c, firstlessequal_constrain2);
            Removein.removein(c, secondlessequal_constrain2);
            Removeout.removeout(c, secondlessequal_constrain2);

            // fuse outputs of zeroones to lessequal
            c.fuse(zeroone_constrain2.getOutputs().get(0), firstlessequal_constrain2.getInputs().get(sum_osize_c1));
            c.fuse(zeroone_constrain2.getOutputs().get(0), secondlessequal_constrain2.getInputs().get(0));
            // fuse outputs of sum to lessequal
            for (int i = 0; i < sum_osize_c1; i++){
                c.fuse(sum_constrain2.getOutputs().get(i), firstlessequal_constrain2.getInputs().get(i));
                c.fuse(sum_constrain2.getOutputs().get(i), secondlessequal_constrain2.getInputs().get(i + 1));
            }
            ret.add(firstlessequal_constrain2.getOutputs().get(0));
            ret.add(secondlessequal_constrain2.getOutputs().get(0));
        }
    }

}
