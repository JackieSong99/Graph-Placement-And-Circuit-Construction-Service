import circuit.*;

public class CircuitID {
    Circuit sub_circuit;
    int id;
    int already_fused = 0;

    public CircuitID(Circuit input_sub_circuit, int intput_id) {
        sub_circuit = input_sub_circuit;
        id = intput_id;
    }
}
