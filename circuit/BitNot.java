/**
 * 
 */
package circuit;

/**
 * @author tripunit
 *
 */
public class BitNot extends Circuit {

	/**
	 * A circuit that takes 1-bit input, and has 1-bit output, which is the negation of the input.
	 * 
	 * @throws Exception
	 */
	public BitNot() throws Exception {
		super();
		
		Circuit.Wire inwire = this.addNewInput();
		Circuit.Gate g = this.addNewGate(Circuit.GateType.NOT);
		Circuit.Wire onew[] = new Circuit.Wire[1];
		Circuit.Wire outwire = this.addNewWire();
		onew[0] = inwire;
		this.connect(g, onew, outwire);
		this.setAsOutput(outwire);
	}
}