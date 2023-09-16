/**
 * 
 */
package circuit;

/**
 * @author tripunit
 *
 */
public class Choose extends Circuit {
	/**
	 * Build a circuit to choose these y bits. In the constructed circuit, the first input wire is the chooser-bit. Then we have y input wires.
	 * So, total number of input wires = 1+y. The output of the circuit is y wires.
	 * 
	 * @param y
	 */
	public Choose(int y) throws Exception {
		super();
		
		Circuit c = this;
		Circuit.Wire x = c.addNewInput(); /* Selector input wire */
		for(int i = 0; i < y; i++) {
			Circuit.Wire twow[] = new Circuit.Wire[2];
			twow[0] = x;
			twow[1] = c.addNewInput();
			Circuit.Wire w = c.addNewWire();
			Circuit.Gate g = c.addNewGate(Circuit.GateType.AND);
			c.connect(g, twow, w);
			c.setAsOutput(w);
		}
	}
}
