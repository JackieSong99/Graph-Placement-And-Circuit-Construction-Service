import java.util.ArrayList;

public class SmallestID {
    public static int find_smallest_ID(ArrayList<Integer>  gates_id) {
        int min_id = gates_id.get(0);
        for (int i = 1; i < gates_id.size(); i++){
            if (gates_id.get(i) < min_id) {
                min_id = gates_id.get(i);
            }
        }
        return min_id;
    }
}
