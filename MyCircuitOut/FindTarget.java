import java.util.ArrayList;

public class FindTarget {
    public static CircuitID find_target(ArrayList<CircuitID> subcircuit_id, int target) throws Exception {
        int index = 0;
        for (int i = 0; i < subcircuit_id.size(); i++) {
            if (subcircuit_id.get(i).id == target) {
                index = i;
                break;
            }
        }
        return subcircuit_id.get(index);
    }
}
