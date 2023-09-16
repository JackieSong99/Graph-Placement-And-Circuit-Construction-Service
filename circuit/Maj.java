/**
 * 
 */
package circuit;

/**
 * @author tripunit
 *
 */
public class Maj extends Circuit {

	/**
	 * Maj(x,y,z) from SHA256 spec, where each of x, y and z is 32 bits long. Output number of wires = 32.
	 * The input wires 0,...31 are x, 32,...,63 are y, and 64,...95 are z.
	 *  
	 * @throws Exception
	 */
	public Maj() throws Exception {
		super();
		
		Circuit c[] = new Circuit[5];
		
		for(int i = 0; i < 5; i++) {
			if(i < 3) c[i] = new BitwiseAndOr(false, 32);
			else c[i] = new BitwiseXor(32); 
			this.union(c[i]);
			while(!this.inputs.isEmpty()) {
				Circuit.Wire w = this.inputs.get(0);
				this.removeAsInput(w);
			}
			if(i < 4) {
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
		// First, c[0] = x AND y
		for(int i = 0; i < 32; i++) {
			this.fuse(this.inputs.get(i), c[0].inputs.get(i));
			this.fuse(this.inputs.get(32+i), c[0].inputs.get(32+i));
		}
		
		// Then, c[1] = x AND z
		for(int i = 0; i < 32; i++) {
			this.fuse(this.inputs.get(i), c[1].inputs.get(i));
			this.fuse(this.inputs.get(2*32+i), c[1].inputs.get(32+i));
		}
		
		// Then, c[2] = y AND z
		for(int i = 0; i < 32; i++) {
			this.fuse(this.inputs.get(32+i), c[2].inputs.get(i));
			this.fuse(this.inputs.get(2*32+i), c[2].inputs.get(32+i));
		}
		
		// Then, c[3] = c[0] XOR c[1]
		for(int i = 0; i < 32; i++) {
			this.fuse(c[0].outputs.get(i),  c[3].inputs.get(i));
			this.fuse(c[1].outputs.get(i), c[3].inputs.get(32+i));
		}
		
		// Finally, c[4] = c[2] XOR c[3]
		for(int i = 0; i < 32; i++) {
			this.fuse(c[2].outputs.get(i),  c[4].inputs.get(i));
			this.fuse(c[3].outputs.get(i), c[4].inputs.get(32+i));
		}
	}
}
