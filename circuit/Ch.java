/**
 * 
 */
package circuit;

/**
 * @author tripunit
 *
 */
public class Ch extends Circuit {

	/**
	 * Ch(x,y,z) from the SHA256 spec, where each of x, y and z are 32 bits long. Output number of wires = 32.
	 * The input wires 0,...31 are x, 32,...,63 are y, and 64,...95 are z.
	 * 
	 * @param n
	 * @throws Exception
	 */
	public Ch() throws Exception {
		super();
		
		Circuit c[] = new Circuit[4];
		c[0] = new BitwiseAndOr(false, 32);
		c[1] = new BitwiseNot(32);
		c[2] = new BitwiseAndOr(false, 32);
		c[3] = new BitwiseXor(32);
		
		for(int i = 0; i < 4; i++) {
			this.union(c[i]);
			while(!this.inputs.isEmpty()) {
				Circuit.Wire w = this.inputs.get(0);
				this.removeAsInput(w);
			}
			if(i < 3) {
				// Retain as output wires, the output wires of final XOR
				while(!this.outputs.isEmpty()) {
					Circuit.Wire w = this.outputs.get(0);
					this.removeAsOutput(w);
				}
			}
		}
		
		for(int i = 0; i < 3*32; i++) {
			this.addNewInput();
		}
		
		// Now hook up the wires
		// First, c[0]
		for(int i = 0; i < 32; i++) {
			this.fuse(this.inputs.get(i), c[0].inputs.get(i));
			this.fuse(this.inputs.get(32+i), c[0].inputs.get(32+i));
		}
		
		// Then, c[1]
		for(int i = 0; i < 32; i++) {
			this.fuse(this.inputs.get(i),  c[1].inputs.get(i));
		}
		
		// Then, c[2]
		for(int i = 0; i < 32; i++) {
			this.fuse(c[1].outputs.get(i), c[2].inputs.get(i));
			this.fuse(this.inputs.get(2*32+i), c[2].inputs.get(32+i));
		}
		
		// And finally, c[3]
		for(int i = 0; i < 32; i++) {
			this.fuse(c[0].outputs.get(i),  c[3].inputs.get(i));
			this.fuse(c[2].outputs.get(i), c[3].inputs.get(32+i));
		}
	}

}
