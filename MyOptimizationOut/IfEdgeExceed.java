import circuit.*;

import java.util.ArrayList;
import java.util.List;

/* Checking for every supply edge, if the sum of all demand nodes placed into it will exceed its capacity */
public class IfEdgeExceed {

    public static void ifedgeexceed (Circuit c, Circuit.Wire[][] wire_matrix_edge, List<Circuit.Wire> ret,
                                     ArrayList<Edge> demand_edgelist, ArrayList<Edge> supply_edgelist) throws Exception{


        List<Circuit> IAC_demand_list = new ArrayList<Circuit>();
        ArrayList<Integer> demand_edgelist_weight = new ArrayList<>();
        for (int i = 0; i < demand_edgelist.size(); i++) {
            demand_edgelist_weight.add(demand_edgelist.get(i).edge_weight);
        }

        for (Integer integer : demand_edgelist_weight) {
            Circuit IAC_demand_constrain4 = new IntegerAsCircuit(integer);
            c.union(IAC_demand_constrain4);
            Removeout.removeout(c, IAC_demand_constrain4);
            IAC_demand_list.add(IAC_demand_constrain4);
        }

        // for each supply node
        for (int j = 0; j < supply_edgelist.size(); j++){
            // Integer as circuit --supply
            Circuit IAC_supply_constrain4 = new IntegerAsCircuit(supply_edgelist.get(j).edge_weight);
            c.union(IAC_supply_constrain4);
            Removeout.removeout(c, IAC_supply_constrain4);

            // Using list to store choose gates
            List<Circuit> Choose_list_constrain4 = new ArrayList<Circuit>();
            for (int i = 0; i < demand_edgelist_weight.size(); i++) {
                Circuit Choosegate_constrain4 = new Choose(IAC_demand_list.get(i).getOutputs().size());
                c.union(Choosegate_constrain4);
                Removein.removein(c, Choosegate_constrain4);
                Removeout.removeout(c, Choosegate_constrain4);
                c.fuse(wire_matrix_edge[i][j], Choosegate_constrain4.getInputs().get(0));
                for (int a = 0; a < IAC_demand_list.get(i).getOutputs().size(); a++) {
                    c.fuse(IAC_demand_list.get(i).getOutputs().get(a), Choosegate_constrain4.getInputs().get(a + 1));
                }
                Choose_list_constrain4.add(Choosegate_constrain4);
            }

            // Sum gate
            List<Integer> ones_c4 = new ArrayList<Integer>();
            // int choose_osize_c2 = 0;
            for (Circuit singlechoose : Choose_list_constrain4) {
                ones_c4.add(singlechoose.getOutputs().size());
            }

            Circuit zero_padding =  new IntegerAsCircuit(0);
            boolean ifone_Choosegate = false;
            if (ones_c4.size() == 1) {
                // if number of demand edges is 1
                c.union(zero_padding);
                Removeout.removeout(c, zero_padding);
                ones_c4.add(zero_padding.getOutputs().size());
                ifone_Choosegate = true;
            }

            Circuit sum_constrain4 = new Sum(ones_c4);
            c.union(sum_constrain4);
            Removein.removein(c, sum_constrain4);
            Removeout.removeout(c, sum_constrain4);

            if (ifone_Choosegate == false) {
                int flag = 0;
                for (int i = 0; i < Choose_list_constrain4.size(); i++) {
                    int num = Choose_list_constrain4.get(i).getOutputs().size();
                    for (int a = 0; a < num; a++) {
                        c.fuse(Choose_list_constrain4.get(i).getOutputs().get(a), sum_constrain4.getInputs().get(flag));
                        flag = flag + 1;
                    }
                }
            }
            else {
                int flag = 0;
                for(int i = 0; i < Choose_list_constrain4.size(); i++){
                    int num = Choose_list_constrain4.get(i).getOutputs().size();
                    for(int a = 0; a < num; a++){
                        c.fuse(Choose_list_constrain4.get(i).getOutputs().get(a), sum_constrain4.getInputs().get(flag));
                        flag = flag + 1;
                    }
                }
                for (int i = 0; i < ones_c4.get(1); i++) {
                    c.fuse(zero_padding.getOutputs().get(i), sum_constrain4.getInputs().get(flag));
                    flag = flag + 1;
                }
            }

            // Less equal
            int sum_osize_c4 = sum_constrain4.getOutputs().size();
            Circuit lessequal_constrain4 = new LessEquals(sum_osize_c4, IAC_supply_constrain4.getOutputs().size());
            c.union(lessequal_constrain4);
            Removein.removein(c, lessequal_constrain4);
            Removeout.removeout(c, lessequal_constrain4);

            // fuse outputs of sum to lessequal
            for(int a = 0; a < sum_osize_c4; a++){
                c.fuse(sum_constrain4.getOutputs().get(a), lessequal_constrain4.getInputs().get(a));
            }

            // fuse outputs of IAC_supply to lessequal
            for(int a = 0; a < IAC_supply_constrain4.getOutputs().size(); a++) {
                c.fuse(IAC_supply_constrain4.getOutputs().get(a), lessequal_constrain4.getInputs().get(sum_osize_c4 + a));
            }
            ret.add(lessequal_constrain4.getOutputs().get(0));
        }
    }
}


