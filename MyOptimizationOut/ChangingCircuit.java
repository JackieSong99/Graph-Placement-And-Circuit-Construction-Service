import circuit.*;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ChangingCircuit {
    public static Circuit changingcircuit(int middle, Circuit c, Circuit changingSubcircuit, List<Circuit.Wire> hook,
                                          Graph demand_graph, Graph supply_graph, Boolean isnew) throws Exception{
        if(changingSubcircuit != null) {
            Set<Circuit.Wire> remwires = changingSubcircuit.wires;
            Set<Circuit.Gate> remgates = changingSubcircuit.gates;
            for(Iterator<Circuit.Gate> git = remgates.iterator(); git.hasNext(); ) {
                Circuit.Gate g = git.next();
                c.gates.remove(g);
            }
            for(Iterator<Circuit.Wire> wit = remwires.iterator(); wit.hasNext(); ) {
                Circuit.Wire w = wit.next();
                if(!(hook.contains(w))) {
                    c.wires.remove(w);
                }
            }

            /* Finally, remove the output of c; there should be 1 */
            c.removeAsOutput(c.getOutputs().get(0));
        }

        // Define subcircuit
        ZeroOne.reset();
        Circuit newsubcircuit = new Circuit();

        // Constrain 5
        // Integer as circuit --k
        int k = middle;
        Circuit IAC_k = new IntegerAsCircuit(k);
        newsubcircuit.union(IAC_k);
        Removeout.removeout(newsubcircuit, IAC_k);

        // Lessequal
        int num_osize_pre = 0;
        if (isnew == false) {
            num_osize_pre = 2 * demand_graph.numOfVertices + 2 * demand_graph.numOfEdges +
                    supply_graph.numOfVertices * supply_graph.vertexlist.get(0).node_weight.size() +
                    supply_graph.numOfEdges + supply_graph.numOfEdges * demand_graph.numOfEdges +
                    demand_graph.numOfEdges * supply_graph.numOfVertices;
        }
        else if (isnew == true) {
            num_osize_pre = 2 * demand_graph.numOfVertices + 2 * demand_graph.numOfEdges +
                    supply_graph.numOfVertices * supply_graph.vertexlist.get(0).node_weight.size() +
                    supply_graph.numOfEdges + supply_graph.numOfEdges * demand_graph.numOfEdges +
                    demand_graph.numOfEdges * supply_graph.numOfVertices + 1;
        }
        int sum_osize_c5 = hook.size() - num_osize_pre;
        Circuit lessequal_constrain3 = new LessEquals(sum_osize_c5, IAC_k.getOutputs().size());
        newsubcircuit.union(lessequal_constrain3);
        Removein.removein(newsubcircuit, lessequal_constrain3);
        Removeout.removeout(newsubcircuit, lessequal_constrain3);

        // final and gate
        int total_input = num_osize_pre + 1;
        Circuit final_and = new BigAndOr(false, total_input);
        newsubcircuit.union(final_and);
        Removein.removein(newsubcircuit, final_and);

        c.union(newsubcircuit);

        // fuse outputs of sum to lessequal
        for (int a = 0; a < sum_osize_c5; a++){
            c.fuse(hook.get(num_osize_pre + a), lessequal_constrain3.getInputs().get(a));
        }
        // fuse outputs of IAC_k to lessequal
        for(int a = 0; a < IAC_k.getOutputs().size(); a++) {
            c.fuse(IAC_k.getOutputs().get(a), lessequal_constrain3.getInputs().get(sum_osize_c5 + a));
        }

        // fuse outputs of lessequal
        c.fuse(lessequal_constrain3.getOutputs().get(0), final_and.getInputs().get(0));
        //Printnio.printnio(c);

        for (int i = 0; i < num_osize_pre; i++) {
            c.fuse(hook.get(i), final_and.getInputs().get(i + 1));
        }

        CircuitUtils.cnfSatToFile(c, "cnf.dimacs");

        return newsubcircuit;
    }
}

