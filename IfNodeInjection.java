import circuit.*;

import java.util.ArrayList;
import java.util.List;

/* Checking for every demand node, if it will be placed into only one supply node */
public class IfNodeInjection {

    public static void ifnodeinjection (circuit.Circuit c, circuit.Circuit.Wire[][] wire_matrix_node, List<circuit.Circuit.Wire> ret,
                                    Integer demandnode_size, Integer supplynode_size) throws Exception{
        for (int d = 0; d < demandnode_size; d++) {
            // Construct Sum gate
            List<Integer> ones_c1 = new ArrayList<>();
            for (int j = 0; j < supplynode_size; j++) {
                ones_c1.add(1);
            }
            circuit.Circuit sum_constrain1 = new circuit.Sum(ones_c1);
            c.union(sum_constrain1);
            Removein.removein(c, sum_constrain1);
            Removeout.removeout(c, sum_constrain1);
            for (int j = 0; j < supplynode_size; j++) {
                c.fuse(wire_matrix_node[d][j], sum_constrain1.getInputs().get(j));
            }

            // ZeroOne.getOne() using IntegerAsCircuit(1) instead
            circuit.Circuit zeroone_constrain1 = new circuit.IntegerAsCircuit(1);;
            c.union(zeroone_constrain1);
            Removeout.removeout(c, zeroone_constrain1);

            // Less equal
            int sum_osize_c1 = sum_constrain1.getOutputs().size();
            circuit.Circuit firstlessequal_constrain1 = new circuit.LessEquals(sum_osize_c1, 1);
            c.union(firstlessequal_constrain1);
            circuit.Circuit secondlessequal_constrain1 = new circuit.LessEquals(1, sum_osize_c1);
            c.union(secondlessequal_constrain1);
            Removein.removein(c, firstlessequal_constrain1);
            Removeout.removeout(c, firstlessequal_constrain1);
            Removein.removein(c, secondlessequal_constrain1);
            Removeout.removeout(c, secondlessequal_constrain1);

            // fuse outputs of zeroones to lessequal
            c.fuse(zeroone_constrain1.getOutputs().get(0), firstlessequal_constrain1.getInputs().get(sum_osize_c1));
            c.fuse(zeroone_constrain1.getOutputs().get(0), secondlessequal_constrain1.getInputs().get(0));
            // fuse outputs of sum to lessequal
            for (int i = 0; i < sum_osize_c1; i++){
                c.fuse(sum_constrain1.getOutputs().get(i), firstlessequal_constrain1.getInputs().get(i));
                c.fuse(sum_constrain1.getOutputs().get(i), secondlessequal_constrain1.getInputs().get(i + 1));
            }
            ret.add(firstlessequal_constrain1.getOutputs().get(0));
            ret.add(secondlessequal_constrain1.getOutputs().get(0));
        }
    }
}
