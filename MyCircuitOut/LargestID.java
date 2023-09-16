import java.util.ArrayList;

public class LargestID {
    public static int find_largest_ID(ArrayList<Integer> gates_id) {
        int max_id = gates_id.get(0);
        for (int i = 1; i < gates_id.size(); i++){
            if (gates_id.get(i) > max_id) {
                max_id = gates_id.get(i);
            }
        }
        return max_id;
    }
}
