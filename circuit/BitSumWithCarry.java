package circuit;

import java.util.List;

/**
 * 
 * @author tripunit
 *
 */
public class BitSumWithCarry extends BitSumWithoutCarry {

	/**
	 * 
	 */
	public BitSumWithCarry() throws Exception {
		super();
		List<Wire> ol = this.getOutputs();
		
		/* First compute the sum. We need a BitSumWithoutCarry with the output of first
		 * BitSumWithoutCarry circuit, and the cin.
		 */
		BitSumWithoutCarry b = new BitSumWithoutCarry();
		this.union(b);
		List<Wire> il = this.getInputs();
		this.fuse(ol.get(0), il.get(2));
		this.removeAsOutput(ol.get(0));
		
		/* Now we need the carry-out */
		il = this.getInputs();
		Wire x[] = new Wire[5];
		
		Gate o[] = new Circuit.Gate[3];
		o[0] = this.addNewGate(Circuit.GateType.OR);
		o[1] = this.addNewGate(Circuit.GateType.OR);
		o[2] = this.addNewGate(Circuit.GateType.OR);
		
		Gate a[] = new Circuit.Gate[2];
		a[0] = this.addNewGate(Circuit.GateType.AND);
		a[1] = this.addNewGate(Circuit.GateType.AND);
		
		Wire twow[] = new Wire[2];

		/* Connect OR gates */
		x[0] = this.addNewWire();
		twow[0] = il.get(0); twow[1] = il.get(1);
		this.connect(o[0], twow, x[0]);

		x[1] = this.addNewWire();
		twow[0] = il.get(0); twow[1] = il.get(2);
		this.connect(o[1], twow, x[1]);

		x[2] = this.addNewWire();
		twow[0] = il.get(1); twow[1] = il.get(2);
		this.connect(o[2], twow, x[2]);
		
		/* Connect AND gates */
		x[3] = this.addNewWire();
		twow[0] = x[0]; twow[1] = x[1];
		this.connect(a[0], twow, x[3]);

		x[4] = this.addNewWire();
		twow[0] = x[2]; twow[1] = x[3];
		this.connect(a[1], twow, x[4]);
		
		/* Add x[4] as output -- this is the carry-out */
		this.setAsOutput(x[4]);
	}
}
