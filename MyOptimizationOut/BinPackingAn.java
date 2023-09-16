import circuit.Circuit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/* Automatically call the SAT Slover to solve cnf.dimacs.
   If the output of "minisat cnf.dimacs" is satisfiable, then output true
   If the output of "minisat cnf.dimacs" is unsatisfiable, then output false */
public class BinPackingAn { // binpacking_An(int middle, ArrayList<Integer> test_demand, ArrayList<Integer> test_supply, Circuit c, Circuit final_or, Circuit.Wire[][] wire_matrix)

    public static ReturnV binpacking_An(int middle, Circuit c, Circuit changingSubcircuit, List<Circuit.Wire> hook,
                                        Graph demand_graph, Graph supply_graph, Boolean isnew) throws Exception{
        changingSubcircuit = ChangingCircuit.changingcircuit(middle, c, changingSubcircuit, hook, demand_graph, supply_graph, isnew);
        Runtime execute = Runtime.getRuntime();
        Boolean flag = true;
        try {
            BufferedReader minisat_output = new BufferedReader(new InputStreamReader(execute.exec("minisat cnf.dimacs result.log").getInputStream()));
            String output = "";
            StringBuffer temp_output=new StringBuffer();
            while ((output = minisat_output.readLine())!=null) {
                temp_output.append(output+"\n");
                if(output.contains("UNSATISFIABLE")){
                    flag = false;
                }
            }
            // System.out.println(temp_output);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ReturnV return_value = new ReturnV(flag, changingSubcircuit);
        return return_value;
    }
}
