import circuit.BigAndOr;
import circuit.Circuit;
import circuit.IntegerAsCircuit;
import circuit.Sum;

import java.util.ArrayList;
import java.util.List;

// SumOfSupplyEdge.sumofsupply(c, wire_matrix_edge, ret, demand_graph.edgelist.size(), supply_graph.edgelist.size())

public class SumOfSupplyEdge {
    public static void sumofsupply (Circuit c, Circuit.Wire[][] wire_matrix_edge, List<Circuit.Wire> ret,
                                    Integer demandedge_size, Integer supplyedge_size) throws Exception{
        // Part of constrain 6
        // Sum gate
        List<Integer> ones_c6 = new ArrayList<Integer>();
        for (int a = 0; a < supplyedge_size; a++) {
            ones_c6.add(1);
        }

        Circuit zero_padding_constrain6 =  new IntegerAsCircuit(0);
        boolean ifone_Orgate = false;
        if (ones_c6.size() == 1) {
            c.union(zero_padding_constrain6);
            Removeout.removeout(c, zero_padding_constrain6);
            ones_c6.add(zero_padding_constrain6.getOutputs().size());
            ifone_Orgate = true;
        }

        Circuit sum_constrain6 = new Sum(ones_c6);
        c.union(sum_constrain6);
        Removein.removein(c, sum_constrain6);
        Removeout.removeout(c, sum_constrain6);

        if (ifone_Orgate == false) {
            int flag_c3 = 0;
            for (int j = 0; j < supplyedge_size; j++){
                // Bigandor
                Circuit and_constrain6 = new BigAndOr(true, demandedge_size);
                c.union(and_constrain6);
                Removein.removein(c, and_constrain6);
                Removeout.removeout(c, and_constrain6);
                for (int i = 0; i < demandedge_size; i++) {
                    c.fuse(wire_matrix_edge[i][j], and_constrain6.getInputs().get(i));
                }
                c.fuse(and_constrain6.getOutputs().get(0), sum_constrain6.getInputs().get(flag_c3));
                flag_c3 = flag_c3 + 1;
            }
        }
        else {
            // ifone_Orgate == true
            int flag_c3 = 0;
            for (int j = 0; j < supplyedge_size; j++){
                // Bigandor
                Circuit and_constrain6 = new BigAndOr(true, demandedge_size);
                c.union(and_constrain6);
                Removein.removein(c, and_constrain6);
                Removeout.removeout(c, and_constrain6);
                for (int i = 0; i < demandedge_size; i++) {
                    c.fuse(wire_matrix_edge[i][j], and_constrain6.getInputs().get(i));
                }
                c.fuse(and_constrain6.getOutputs().get(0), sum_constrain6.getInputs().get(flag_c3));
                flag_c3 = flag_c3 + 1;
            }
            for (int i = 0; i < ones_c6.get(1); i++) {
                c.fuse(zero_padding_constrain6.getOutputs().get(i), sum_constrain6.getInputs().get(flag_c3));
                flag_c3 = flag_c3 + 1;
            }
        }

        for (int i = 0; i < sum_constrain6.getOutputs().size(); i++) {
            ret.add(sum_constrain6.getOutputs().get(i));
        }
    }
}
