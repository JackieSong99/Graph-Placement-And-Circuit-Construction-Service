package circuit;

/**
 * @author tripunit
 *
 */
public class BitSumWithoutCarry extends Circuit {

	/**
	 * 
	 */
	public BitSumWithoutCarry() throws Exception {
		Circuit.Wire twow[] = new Circuit.Wire[2]; // For use in connecting gates and wires
		Circuit.Wire onew[] = new Circuit.Wire[1]; // For use in connecting gates and wires

		Wire x[] = new Wire[7]; // All the wires in the circuit
		
		/* First the input wires */
		x[0] = this.addNewInput();
		x[1] = this.addNewInput();

		/* Two NOT gates */
		Circuit.Gate n[] = new Circuit.Gate[2];
		n[0] = this.addNewGate(Circuit.GateType.NOT);
		n[1] = this.addNewGate(Circuit.GateType.NOT);

		/* connect those NOT gates */
		x[2] = this.addNewWire();
		onew[0] = x[0];
		this.connect(n[0], onew, x[2]);
		
		x[3] = this.addNewWire();
		onew[0] = x[1];
		this.connect(n[1], onew, x[3]);

		/* Two OR gates */
		Circuit.Gate o[] = new Circuit.Gate[2];
		o[0] = this.addNewGate(Circuit.GateType.OR);
		o[1] = this.addNewGate(Circuit.GateType.OR);
		
		/* Connect those OR gates */
		x[4] = this.addNewWire();
		twow[0] = x[0]; twow[1] = x[1];
		this.connect(o[0], twow, x[4]);

		x[5] = this.addNewWire();
		twow[0] = x[2]; twow[1] = x[3];
		this.connect(o[1], twow, x[5]);
		
		/* One AND gate */
		Circuit.Gate a = this.addNewGate(Circuit.GateType.AND);	

		/* Connect the AND gate */
		x[6] = this.addNewWire();
		twow[0] = x[4]; twow[1] = x[5];
		this.connect(a, twow, x[6]);
		
		/* Set circuit output */
		this.setAsOutput(x[6]);
	}
}
