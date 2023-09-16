import circuit.*;

import java.util.ArrayList;
import java.util.List;

public class IfEdgeToNode {
    public static void ifedgetonode (Circuit c, Circuit.Wire[][] wire_matrix_mix, Circuit.Wire[][] wire_matrix_node,
                                     ArrayList<Edge> demand_edgelist, ArrayList<Vertex> supply_nodelist,
                                     List<circuit.Circuit.Wire> ret) throws Exception{
        // Constrain 8
        for (int i = 0; i < demand_edgelist.size(); i++) {
            int source_index_demand = demand_edgelist.get(i).src_index;
            int destination_index_demand = demand_edgelist.get(i).des_index;

            for (int j = 0; j < supply_nodelist.size(); j++) {
                // ChooseGate
                Circuit ChooseGate_constrain8 = new Choose(2);
                c.union(ChooseGate_constrain8);
                Removein.removein(c, ChooseGate_constrain8);
                Removeout.removeout(c, ChooseGate_constrain8);
                c.fuse(wire_matrix_mix[i][j], ChooseGate_constrain8.getInputs().get(0));
                c.fuse(wire_matrix_node[source_index_demand][j], ChooseGate_constrain8.getInputs().get(1));
                c.fuse(wire_matrix_node[destination_index_demand][j], ChooseGate_constrain8.getInputs().get(2));

                // first AndGate
                Circuit firstAndGate_constrain8 = new BigAndOr(false, 2);
                c.union(firstAndGate_constrain8);
                Removein.removein(c, firstAndGate_constrain8);
                Removeout.removeout(c, firstAndGate_constrain8);
                c.fuse(ChooseGate_constrain8.getOutputs().get(0), firstAndGate_constrain8.getInputs().get(0));
                c.fuse(ChooseGate_constrain8.getOutputs().get(1), firstAndGate_constrain8.getInputs().get(1));

                // ZeroOne gate
                Circuit IAC_One_constrain8 = new IntegerAsCircuit(1);
                c.union(IAC_One_constrain8);
                Removeout.removeout(c, IAC_One_constrain8);

                // first LessEqual
                int num_IACone = IAC_One_constrain8.getOutputs().size();
                Circuit firstLessEqual_constrain8 = new LessEquals(num_IACone, 1);
                c.union(firstLessEqual_constrain8);
                Removein.removein(c, firstLessEqual_constrain8);
                Removeout.removeout(c, firstLessEqual_constrain8);
                for (int k = 0; k < num_IACone; k++) {
                    c.fuse(IAC_One_constrain8.getOutputs().get(k), firstLessEqual_constrain8.getInputs().get(k));
                }
                c.fuse(firstAndGate_constrain8.getOutputs().get(0), firstLessEqual_constrain8.getInputs().get(num_IACone));

                // second LessEqual
                Circuit secondLessEqual_constrain8 = new LessEquals(1, num_IACone);
                c.union(secondLessEqual_constrain8);
                Removein.removein(c, secondLessEqual_constrain8);
                Removeout.removeout(c, secondLessEqual_constrain8);
                c.fuse(firstAndGate_constrain8.getOutputs().get(0), secondLessEqual_constrain8.getInputs().get(0));
                for (int k = 1; k < num_IACone + 1; k++) {
                    c.fuse(IAC_One_constrain8.getOutputs().get(k - 1), secondLessEqual_constrain8.getInputs().get(k));
                }

                // second AndGate
                Circuit secondAndGate_constrain8 = new BigAndOr(false, 2);
                c.union(secondAndGate_constrain8);
                Removein.removein(c, secondAndGate_constrain8);
                Removeout.removeout(c, secondAndGate_constrain8);
                c.fuse(firstLessEqual_constrain8.getOutputs().get(0), secondAndGate_constrain8.getInputs().get(0));
                c.fuse(secondLessEqual_constrain8.getOutputs().get(0), secondAndGate_constrain8.getInputs().get(1));

                // IAC Zero
                Circuit IAC_Zero_constrain8 = new IntegerAsCircuit(0);
                c.union(IAC_Zero_constrain8);
                Removeout.removeout(c, IAC_Zero_constrain8);
                int num_IACzero = IAC_Zero_constrain8.getOutputs().size();

                // fifth LessEqual
                Circuit fifthLessEqual_constrain8 = new LessEquals(1, num_IACzero);
                c.union(fifthLessEqual_constrain8);
                Removein.removein(c, fifthLessEqual_constrain8);
                Removeout.removeout(c, fifthLessEqual_constrain8);
                c.fuse(wire_matrix_mix[i][j], fifthLessEqual_constrain8.getInputs().get(0));
                for (int k = 1; k < num_IACzero + 1; k++) {
                    c.fuse(IAC_Zero_constrain8.getOutputs().get(k - 1), fifthLessEqual_constrain8.getInputs().get(k));
                }

                // Or Gate
                Circuit OrGate_constrain7 = new BigAndOr(true, 2);
                c.union(OrGate_constrain7);
                Removein.removein(c, OrGate_constrain7);
                Removeout.removeout(c, OrGate_constrain7);
                c.fuse(secondAndGate_constrain8.getOutputs().get(0), OrGate_constrain7.getInputs().get(0));
                c.fuse(fifthLessEqual_constrain8.getOutputs().get(0), OrGate_constrain7.getInputs().get(1));

                ret.add(OrGate_constrain7.getOutputs().get(0));

                // ret.add(secondAndGate_constrain8.getOutputs().get(0));
            }
        }
    }
}
