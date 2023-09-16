import circuit.BigAndOr;
import circuit.Circuit;
import circuit.Sum;

import java.util.ArrayList;
import java.util.List;

public class SumOfSupplyNode {
    public static void sumofsupply (Circuit c, Circuit.Wire[][] wire_matrix_node, List<Circuit.Wire> ret,
                                    Integer demandnode_size, Integer supplynode_size) throws Exception{
        // Part of constrain 5
        // Sum gate
        List<Integer> ones_c3 = new ArrayList<Integer>();
        for (int a = 0; a < supplynode_size; a++) {
            ones_c3.add(1);
        }
        Circuit sum_constrain5 = new Sum(ones_c3);
        c.union(sum_constrain5);
        Removein.removein(c, sum_constrain5);
        Removeout.removeout(c, sum_constrain5);

        int flag_c3 = 0;
        for (int j = 0; j < supplynode_size; j++){
            // Bigandor
            Circuit and_constrain6 = new BigAndOr(true, demandnode_size);
            c.union(and_constrain6);
            Removein.removein(c, and_constrain6);
            Removeout.removeout(c, and_constrain6);
            for (int i = 0; i < demandnode_size; i++) {
                c.fuse(wire_matrix_node[i][j], and_constrain6.getInputs().get(i));
            }
            c.fuse(and_constrain6.getOutputs().get(0), sum_constrain5.getInputs().get(flag_c3));
            flag_c3 = flag_c3 + 1;
        }
        for (int i = 0; i < sum_constrain5.getOutputs().size(); i++) {
            ret.add(sum_constrain5.getOutputs().get(i));
        }
    }
}
