import circuit.Circuit;

import java.util.ArrayList;
import java.util.List;

/* Binary search, using the output of BinPackingAn.binpacking_An(middle, test_demand, test_supply)
    to decide how to change low and high */
public class BinarySearch {
    public static ReturnToRun binary (int low, int high, Circuit c, Circuit changingSubcircuit,
                                             List<Circuit.Wire> hook, Graph demand_graph,
                                             Graph supply_graph, ArrayList<Integer> record_k, Boolean isnew) throws Exception{
        if (low > high){
            ReturnToRun kandsubc = new ReturnToRun(record_k, changingSubcircuit);
            return kandsubc;
        }
        int middle = (low + high) / 2;
        // System.out.println(" ");
        // System.out.println("------------Now testing k = " + middle + "--------------");
        ReturnV returnvalue =  BinPackingAn.binpacking_An(middle, c, changingSubcircuit, hook, demand_graph, supply_graph, isnew);
        if (returnvalue.re_bool == true) {
            record_k.add(middle);
            high = middle - 1;
            return binary(low, high, c, returnvalue.re_subc, hook, demand_graph, supply_graph, record_k, isnew);
        }
        else if (returnvalue.re_bool == false){
            low = middle + 1;
            return binary(low, high, c, returnvalue.re_subc, hook, demand_graph, supply_graph, record_k, isnew);
        }
        else{
            // questions here!!!
            return binary(low, high, c, returnvalue.re_subc, hook, demand_graph, supply_graph, record_k, isnew);
        }
    }
}

