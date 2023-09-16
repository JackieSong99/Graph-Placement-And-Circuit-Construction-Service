/**
 * 
 */
package circuit;


/**
 * @author tripunit
 *
 */
public class BigAndOr extends Circuit {

	/**
	 * Create a big AND or OR. The first argument, which is a boolean, indicates whether we want an OR (isOr = true) or an AND (isOR = false).
	 * The second argument, nInputWires, is the number of input wires. E.g., if nInputWires == 2, we'll get exactly one gate, because each
	 * AND and OR gate takes two inputs. If nInputWires = 4, we'll get a circuit with 3 gates. The number of output wires is always 1, of course.
	 * 
	 * @param isOr
	 * @param nInputWires
	 * @throws Exception
	 */
	public BigAndOr(boolean isOr, int nInputWires) throws Exception {
		super();
		
		for(int i = 0; i < nInputWires; i++) {
			this.addNewInput();
		}
		this.setAsOutput(this.getInputs().get(0));

		for(int i = 1; i < nInputWires; i++) {
			Circuit.Gate g;
			if(isOr) g = this.addNewGate(Circuit.GateType.OR);
			else g = this.addNewGate(Circuit.GateType.AND);

			Circuit.Wire twow[] = new Circuit.Wire[2];
			Circuit.Wire outwire = this.addNewWire();
			twow[0] = this.getOutputs().get(0); twow[1] = this.getInputs().get(i);
			this.connect(g, twow, outwire);
			this.removeAsOutput(this.getOutputs().get(0));
			this.setAsOutput(outwire);
		}
	}
}
