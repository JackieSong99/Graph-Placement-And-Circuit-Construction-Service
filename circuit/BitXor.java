/**
 * 
 */
package circuit;

/**
 * @author tripunit
 *
 */
public class BitXor extends Circuit {

	/**
	 * Two bit input xor.
	 * 
	 * a xor b = (not a and b) or (a and not b)
	 * 
	 * @throws Exception
	 */
	public BitXor() throws Exception {
		super();
		
		Circuit c[] = new Circuit[5];
		c[0] = new BitNot(); // for not a
		c[1] = new BitNot(); // for not b
		c[2] = new BigAndOr(false, 2); // for (not a and b)
		c[3] = new BigAndOr(false, 2); // for (a and not b)
		c[4] = new BigAndOr(true, 2); // for the final or
		
		this.union(c[0]); this.union(c[1]);
		this.union(c[2]);
		this.fuse(c[0].getOutputs().get(0), c[2].getInputs().get(0));
		this.fuse(this.getInputs().get(1), c[2].getInputs().get(1));
		
		this.union(c[3]);
		this.fuse(this.getInputs().get(0), c[3].getInputs().get(0));
		this.fuse(c[1].getOutputs().get(0), c[3].getInputs().get(1));
		
		while(this.getOutputs().size() > 0) {
			this.removeAsOutput(this.getOutputs().get(0));
		}
		
		this.union(c[4]);
		this.fuse(c[2].getOutputs().get(0), c[4].getInputs().get(0));
		this.fuse(c[3].getOutputs().get(0), c[4].getInputs().get(1));
	}
}