/**
 * 
 */
package circuit;

/**
 * @author tripunit
 *
 */
public class LessEquals extends Circuit {
	/**
	 * A circuit with x+y input wires. It checks whether the binary number represented by the 
	 * first x bits is less than or equals the binary number represented by the next y bits.
	 * 
	 * @param x
	 * @param y
	 */
	public LessEquals(int x, int y) throws Exception {
		super();
		this.union(new DiffTwo(x, y));
		
		/* OR of all outputs except borrow */
		Circuit circor = new BigAndOr(true, Math.max(x, y)); /* # of output bits for difference = max(x,y) */
		this.union(circor);

		for(int i = 0; i < Math.max(x,y); i++) {
			this.fuse(this.getOutputs().get(0), circor.getInputs().get(i));
			this.removeAsOutput(this.getOutputs().get(0));
		}

		/* a NOT on the output of OR above */
		Circuit.Gate n = this.addNewGate(Circuit.GateType.NOT);
		Circuit.Wire twow[] = new Circuit.Wire[2];
		twow[0] = this.getOutputs().get(1); twow[1] = null;
		Circuit.Wire outwire = this.addNewWire();
		this.connect(n, twow, outwire);
		this.removeAsOutput(this.getOutputs().get(1));
				
		/* Now an OR gate to finish things up */
		Circuit.Gate o = this.addNewGate(Circuit.GateType.OR);
		twow[0] = outwire; twow[1] = this.getOutputs().get(0);
		outwire = this.addNewWire();
		this.connect(o, twow, outwire);
		this.removeAsOutput(this.getOutputs().get(0));
		this.setAsOutput(outwire);
	}
}
