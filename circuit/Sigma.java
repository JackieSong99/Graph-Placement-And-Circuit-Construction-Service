/**
 * 
 */
package circuit;

/**
 * @author tripunit
 *
 */
public class Sigma extends Circuit {

	/**
	 * Sigma(x) from SHA256 spec. Where the arg, sigmanum specifies which of the 4.
	 * We assume number of input wires = number of output wires = 32.
	 * 
	 * Note that \Sigma_0, \Sigma_1 (big sigmas) correspond to sigmanum 0 and 1, respectively, and
	 * \sigma_0 and \sigma_1 (small sigmas, which have both right shift and rotate) correspond to
	 * sigmanum 2 and 3 respectively.
	 * 
	 * @param sigmanum
	 * @param n
	 * @throws Exception
	 */
	public Sigma(int sigmanum) throws Exception {
		super();

		assert(sigmanum >= 0 && sigmanum < 4);
		
		Circuit c[] = new Circuit[5];
		
		if(sigmanum == 0) {
			c[0] = new Rightrotate(32, 2);
			c[1] = new Rightrotate(32, 13);
			c[2] = new Rightrotate(32, 22);
		}
		else if(sigmanum == 1) {
			c[0] = new Rightrotate(32, 6);
			c[1] = new Rightrotate(32, 11);
			c[2] = new Rightrotate(32, 25);
		}
		else if(sigmanum == 2) {
			c[0] = new Rightrotate(32, 7);
			c[1] = new Rightrotate(32, 18);
			c[2] = new Rightshift(32, 3);
		}
		else { // sigmaum == 3
			c[0] = new Rightrotate(32, 17);
			c[1] = new Rightrotate(32, 19);
			c[2] = new Rightshift(32, 10);
		}
		
		c[3] = new BitwiseXor(32); c[4] = new BitwiseXor(32);
		
		for(int i = 0; i < 5; i++) this.union(c[i]);
		while(!this.inputs.isEmpty()) this.removeAsInput(this.inputs.get(0));
		while(this.outputs.size() > 32) this.removeAsOutput(this.outputs.get(0));
		for(int i = 0; i < 32; i++) this.addNewInput();
		
		// Now connect wires. First, c[0],...,c[2]
		for(int i = 0; i < 32; i++) {
			for(int j = 0; j < 3; j++) {
				this.fuse(this.inputs.get(i), c[j].inputs.get(i));
			}
		}
		
		// Then, c[3] = c[0] XOR c[1]
		for(int i = 0; i < 32; i++) {
			this.fuse(c[0].outputs.get(i), c[3].inputs.get(i));
			this.fuse(c[1].outputs.get(i), c[3].inputs.get(32+i));
		}
		
		// Then, and finally, c[4] = c[2] XOR c[3]
		for(int i = 0; i < 32; i++) {
			this.fuse(c[2].outputs.get(i), c[4].inputs.get(i));
			this.fuse(c[3].outputs.get(i), c[4].inputs.get(32+i));
		}
	}
}
