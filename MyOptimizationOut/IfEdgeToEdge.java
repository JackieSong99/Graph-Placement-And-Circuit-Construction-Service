import circuit.*;

import java.util.ArrayList;
import java.util.List;

public class IfEdgeToEdge {
    public static void ifedgetoedge (Circuit c, Circuit.Wire[][] wire_matrix_node, Circuit.Wire[][] wire_matrix_edge,
                                     ArrayList<Edge> demand_edgelist, ArrayList<Edge> supply_edgelist,
                                     List<circuit.Circuit.Wire> ret) throws Exception{
        // Constrain 7
        for (int i = 0; i < demand_edgelist.size(); i++) {
            int source_index_demand = demand_edgelist.get(i).src_index; // a
            int destination_index_demand = demand_edgelist.get(i).des_index;  // b

            for (int j = 0; j < supply_edgelist.size(); j++) {
                int source_index_supply = supply_edgelist.get(j).src_index;  // c
                int destination_index_supply = supply_edgelist.get(j).des_index;  // d

                // Construct first ChooseGate
                Circuit firstChooseGate_constrain7 = new Choose(2);
                c.union(firstChooseGate_constrain7);
                Removein.removein(c, firstChooseGate_constrain7);
                Removeout.removeout(c, firstChooseGate_constrain7);
                c.fuse(wire_matrix_edge[i][j], firstChooseGate_constrain7.getInputs().get(0));
                c.fuse(wire_matrix_node[source_index_demand][source_index_supply], firstChooseGate_constrain7.getInputs().get(1));
                c.fuse(wire_matrix_node[destination_index_demand][destination_index_supply], firstChooseGate_constrain7.getInputs().get(2));

                // Construct first AndGate
                Circuit firstAndGate_constrain7 = new BigAndOr(false, 2);
                c.union(firstAndGate_constrain7);
                Removein.removein(c, firstAndGate_constrain7);
                Removeout.removeout(c, firstAndGate_constrain7);
                c.fuse(firstChooseGate_constrain7.getOutputs().get(0), firstAndGate_constrain7.getInputs().get(0));
                c.fuse(firstChooseGate_constrain7.getOutputs().get(1), firstAndGate_constrain7.getInputs().get(1));

                // Construct second ChooseGate
                Circuit secondChooseGate_constrain7 = new Choose(2);
                c.union(secondChooseGate_constrain7);
                Removein.removein(c, secondChooseGate_constrain7);
                Removeout.removeout(c, secondChooseGate_constrain7);
                c.fuse(wire_matrix_edge[i][j], secondChooseGate_constrain7.getInputs().get(0));
                c.fuse(wire_matrix_node[source_index_demand][destination_index_supply], secondChooseGate_constrain7.getInputs().get(1));
                c.fuse(wire_matrix_node[destination_index_demand][source_index_supply], secondChooseGate_constrain7.getInputs().get(2));

                // Construct second AndGate
                Circuit secondAndGate_constrain7 = new BigAndOr(false, 2);
                c.union(secondAndGate_constrain7);
                Removein.removein(c, secondAndGate_constrain7);
                Removeout.removeout(c, secondAndGate_constrain7);
                c.fuse(secondChooseGate_constrain7.getOutputs().get(0), secondAndGate_constrain7.getInputs().get(0));
                c.fuse(secondChooseGate_constrain7.getOutputs().get(1), secondAndGate_constrain7.getInputs().get(1));

                // Construct ZeroOne Gate
                Circuit IAC_One_constrain7 = new IntegerAsCircuit(1);
                c.union(IAC_One_constrain7);
                Removeout.removeout(c, IAC_One_constrain7);

                // Construct first LessEqual gate
                int num_IACone = IAC_One_constrain7.getOutputs().size();
                Circuit firstLessEqual_constrain7 = new LessEquals(num_IACone, 1);
                c.union(firstLessEqual_constrain7);
                Removein.removein(c, firstLessEqual_constrain7);
                Removeout.removeout(c, firstLessEqual_constrain7);
                for (int k = 0; k < num_IACone; k++) {
                    c.fuse(IAC_One_constrain7.getOutputs().get(k), firstLessEqual_constrain7.getInputs().get(k));
                }
                c.fuse(firstAndGate_constrain7.getOutputs().get(0), firstLessEqual_constrain7.getInputs().get(num_IACone));

                // Construct second LessEqual gate
                Circuit secondLessEqual_constrain7 = new LessEquals(1, num_IACone);
                c.union(secondLessEqual_constrain7);
                Removein.removein(c, secondLessEqual_constrain7);
                Removeout.removeout(c, secondLessEqual_constrain7);
                c.fuse(firstAndGate_constrain7.getOutputs().get(0), secondLessEqual_constrain7.getInputs().get(0));
                for (int k = 1; k < num_IACone + 1; k++) {
                    c.fuse(IAC_One_constrain7.getOutputs().get(k - 1), secondLessEqual_constrain7.getInputs().get(k));
                }

                // Construct third LessEqual gate
                Circuit thirdLessEqual_constrain7 = new LessEquals(num_IACone, 1);
                c.union(thirdLessEqual_constrain7);
                Removein.removein(c, thirdLessEqual_constrain7);
                Removeout.removeout(c, thirdLessEqual_constrain7);
                for (int k = 0; k < num_IACone; k++) {
                    c.fuse(IAC_One_constrain7.getOutputs().get(k), thirdLessEqual_constrain7.getInputs().get(k));
                }
                c.fuse(secondAndGate_constrain7.getOutputs().get(0), thirdLessEqual_constrain7.getInputs().get(num_IACone));

                // Construct fourth LessEqual gate
                Circuit fourthLessEqual_constrain7 = new LessEquals(1, num_IACone);
                c.union(fourthLessEqual_constrain7);
                Removein.removein(c, fourthLessEqual_constrain7);
                Removeout.removeout(c, fourthLessEqual_constrain7);
                c.fuse(secondAndGate_constrain7.getOutputs().get(0), fourthLessEqual_constrain7.getInputs().get(0));
                for (int k = 1; k < num_IACone + 1; k++) {
                    c.fuse(IAC_One_constrain7.getOutputs().get(k - 1), fourthLessEqual_constrain7.getInputs().get(k));
                }

                // Construct third AndGate
                Circuit thirdAndGate_constrain7 = new BigAndOr(false, 2);
                c.union(thirdAndGate_constrain7);
                Removein.removein(c, thirdAndGate_constrain7);
                Removeout.removeout(c, thirdAndGate_constrain7);
                c.fuse(firstLessEqual_constrain7.getOutputs().get(0), thirdAndGate_constrain7.getInputs().get(0));
                c.fuse(secondLessEqual_constrain7.getOutputs().get(0), thirdAndGate_constrain7.getInputs().get(1));

                // Construct fourth AndGate
                Circuit fourthAndGate_constrain7 = new BigAndOr(false, 2);
                c.union(fourthAndGate_constrain7);
                Removein.removein(c, fourthAndGate_constrain7);
                Removeout.removeout(c, fourthAndGate_constrain7);
                c.fuse(thirdLessEqual_constrain7.getOutputs().get(0), fourthAndGate_constrain7.getInputs().get(0));
                c.fuse(fourthLessEqual_constrain7.getOutputs().get(0), fourthAndGate_constrain7.getInputs().get(1));

                // Construct XOr Gate
                Circuit Xor_constrain7 = new BitsnXor(2);
                c.union(Xor_constrain7);
                Removein.removein(c, Xor_constrain7);
                Removeout.removeout(c, Xor_constrain7);
                c.fuse(thirdAndGate_constrain7.getOutputs().get(0), Xor_constrain7.getInputs().get(0));
                c.fuse(fourthAndGate_constrain7.getOutputs().get(0), Xor_constrain7.getInputs().get(1));

                // IAC Zero
                Circuit IAC_Zero_constrain7 = new IntegerAsCircuit(0);
                c.union(IAC_Zero_constrain7);
                Removeout.removeout(c, IAC_Zero_constrain7);
                int num_IACzero = IAC_Zero_constrain7.getOutputs().size();

                // fifth LessEqual
                Circuit fifthLessEqual_constrain7 = new LessEquals(1, num_IACzero);
                c.union(fifthLessEqual_constrain7);
                Removein.removein(c, fifthLessEqual_constrain7);
                Removeout.removeout(c, fifthLessEqual_constrain7);
                c.fuse(wire_matrix_edge[i][j], fifthLessEqual_constrain7.getInputs().get(0));
                for (int k = 1; k < num_IACzero + 1; k++) {
                    c.fuse(IAC_Zero_constrain7.getOutputs().get(k - 1), fifthLessEqual_constrain7.getInputs().get(k));
                }

                // Or Gate
                Circuit OrGate_constrain7 = new BigAndOr(true, 2);
                c.union(OrGate_constrain7);
                Removein.removein(c, OrGate_constrain7);
                Removeout.removeout(c, OrGate_constrain7);
                c.fuse(Xor_constrain7.getOutputs().get(0), OrGate_constrain7.getInputs().get(0));
                c.fuse(fifthLessEqual_constrain7.getOutputs().get(0), OrGate_constrain7.getInputs().get(1));

                ret.add(OrGate_constrain7.getOutputs().get(0));

                // ret.add(Xor_constrain7.getOutputs().get(0));
            }
        }
    }
}
