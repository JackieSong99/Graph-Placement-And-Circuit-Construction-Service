import circuit.*;

import java.util.ArrayList;
import java.util.List;

/* Checking for every supply node, if the sum of all demand nodes placed into it will exceed its capacity */
public class IfNodeExceed {

    public static void ifnodeexceed (Circuit c, Circuit.Wire[][] wire_matrix_node, List<Circuit.Wire> ret,
                                 ArrayList<Vertex> demand_input, ArrayList<Vertex> supply_input) throws Exception{

        for (int att = 0; att < demand_input.get(0).node_weight.size(); att++) {
            List<Circuit> IAC_demand_list = new ArrayList<Circuit>();
            ArrayList<Integer> demand_input_weight = new ArrayList<>();
            for (int i = 0; i < demand_input.size(); i++) {
                demand_input_weight.add(demand_input.get(i).node_weight.get(att).weight);
            }

            for (Integer integer : demand_input_weight) {
                Circuit IAC_demand_constrain3 = new IntegerAsCircuit(integer);
                c.union(IAC_demand_constrain3);
                Removeout.removeout(c, IAC_demand_constrain3);
                IAC_demand_list.add(IAC_demand_constrain3);
            }

            // for each supply node
            for (int j = 0; j < supply_input.size(); j++){
                // Integer as circuit --supply
                Circuit IAC_supply_constrain3 = new IntegerAsCircuit(supply_input.get(j).node_weight.get(att).weight);
                c.union(IAC_supply_constrain3);
                Removeout.removeout(c, IAC_supply_constrain3);

                // Using list to store choose gates
                List<Circuit> Choose_list = new ArrayList<Circuit>();
                for (int i = 0; i < demand_input_weight.size(); i++) {
                    Circuit Choosegate_constrain3 = new Choose(IAC_demand_list.get(i).getOutputs().size());
                    c.union(Choosegate_constrain3);
                    Removein.removein(c, Choosegate_constrain3);
                    Removeout.removeout(c, Choosegate_constrain3);
                    c.fuse(wire_matrix_node[i][j], Choosegate_constrain3.getInputs().get(0));
                    for (int a = 0; a < IAC_demand_list.get(i).getOutputs().size(); a++) {
                        c.fuse(IAC_demand_list.get(i).getOutputs().get(a), Choosegate_constrain3.getInputs().get(a + 1));
                    }
                    Choose_list.add(Choosegate_constrain3);
                }

                // Sum gate
                List<Integer> ones_c2 = new ArrayList<Integer>();
                // int choose_osize_c2 = 0;
                for (Circuit circuit : Choose_list) {
                    ones_c2.add(circuit.getOutputs().size());
                }

                Circuit sum_constrain3 = new Sum(ones_c2);
                c.union(sum_constrain3);
                Removein.removein(c, sum_constrain3);
                Removeout.removeout(c, sum_constrain3);

                int flag = 0;
                for(int i = 0; i < demand_input_weight.size(); i++){
                    int num = Choose_list.get(i).getOutputs().size();
                    for(int a = 0; a < num; a++){
                        c.fuse(Choose_list.get(i).getOutputs().get(a), sum_constrain3.getInputs().get(flag));
                        flag = flag + 1;
                    }
                }

                // Less equal
                int sum_osize_c3 = sum_constrain3.getOutputs().size();
                Circuit lessequal_constrain3 = new LessEquals(sum_osize_c3, IAC_supply_constrain3.getOutputs().size());
                c.union(lessequal_constrain3);
                Removein.removein(c, lessequal_constrain3);
                Removeout.removeout(c, lessequal_constrain3);

                // fuse outputs of sum to lessequal
                for(int a = 0; a < sum_osize_c3; a++){
                    c.fuse(sum_constrain3.getOutputs().get(a), lessequal_constrain3.getInputs().get(a));
                }

                // fuse outputs of IAC_supply to lessequal
                for(int a = 0; a < IAC_supply_constrain3.getOutputs().size(); a++) {
                    c.fuse(IAC_supply_constrain3.getOutputs().get(a), lessequal_constrain3.getInputs().get(sum_osize_c3 + a));
                }
                ret.add(lessequal_constrain3.getOutputs().get(0));
            }
        }
    }
}
