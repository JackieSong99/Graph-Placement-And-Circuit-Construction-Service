/**
 * 
 */
package circuit;

import java.util.List;


/**
 * @author tripunit
 *
 */
public class BitDiffWithBorrow extends Circuit {
	/**
	 * 
	 */
	public BitDiffWithBorrow() throws Exception {
		super();
		
		/* The difference bit first */
		this.union(new BitSumWithoutCarry());
		this.union(new BitSumWithoutCarry());
		List<Circuit.Wire> il = this.getInputs();
		List<Circuit.Wire> ol = this.getOutputs();
		this.fuse(ol.get(0), il.get(2));
		this.removeAsOutput(ol.get(0));
		
		/* Now the borrow bit */
		Circuit.Gate n = this.addNewGate(Circuit.GateType.NOT);
		Circuit.Wire twow[] = new Circuit.Wire[2];
		Circuit.Wire w[] = new Circuit.Wire[6];
		
		w[0] = this.addNewWire();
		twow[0] = this.getInputs().get(0); twow[1] = null;
		this.connect(n, twow, w[0]);
		
		Circuit.Gate a[] = new Circuit.Gate[3];
		a[0] = this.addNewGate(Circuit.GateType.AND);
		a[1] = this.addNewGate(Circuit.GateType.AND);
		a[2] = this.addNewGate(Circuit.GateType.AND);
				
		w[1] = this.addNewWire();
		twow[0] = w[0]; twow[1] = this.getInputs().get(1);
		this.connect(a[0], twow, w[1]);
		
		w[2] = this.addNewWire();
		twow[0] = w[0]; twow[1] = this.getInputs().get(2);
		this.connect(a[1], twow, w[2]);
		
		w[3] = this.addNewWire();
		twow[0] = this.getInputs().get(1); twow[1] = this.getInputs().get(2);
		this.connect(a[2], twow, w[3]);
		
		Circuit.Gate o[] = new Circuit.Gate[2];
		o[0] = this.addNewGate(Circuit.GateType.OR);
		o[1] = this.addNewGate(Circuit.GateType.OR);
		
		w[4] = this.addNewWire();
		twow[0] = w[1]; twow[1] = w[2];
		this.connect(o[0], twow, w[4]);
		
		w[5] = this.addNewWire();
		twow[0] = w[3]; twow[1] = w[4];
		this.connect(o[1], twow, w[5]);
		
		this.setAsOutput(w[5]);
	}
}
