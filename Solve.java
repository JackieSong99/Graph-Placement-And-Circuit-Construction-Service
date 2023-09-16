import circuit.Circuit;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Solve {
    public static Boolean solve() throws Exception{
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
        return flag;
    }
}
