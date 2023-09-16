/**
 * 
 */
package circuit;

/**
 * @author tripunit
 *
 */
public class Rightshift extends Circuit {
	
	/**
	 * Right shift the input bit string of m wires, by n bits. The output is m wires.
	 * 
	 * @param m
	 * @param n
	 * @throws Exception
	 */
	public Rightshift(int m, int n) throws Exception {
		super();

		assert(m > 0 && n >= 0);

		if(n > m) n = m;
		
		for(int i = 0; i < m; i++) {
			this.addNewInput();
		}
		
		Circuit zero = ZeroOne.getZero();
		Circuit one = ZeroOne.getOne();
		this.union(zero); this.union(one);
		this.removeAsOutput(zero.outputs.get(0));
		this.removeAsOutput(one.outputs.get(0));
		
		Circuit ands[] = new Circuit[m];
		for(int i = 0; i < m; i++) {
			ands[i] = new BigAndOr(false, 2);
			this.union(ands[i]);
			this.removeAsInput(ands[i].inputs.get(0));
			this.removeAsInput(ands[i].inputs.get(1));
			
			if(i > m - n - 1) {
				this.fuse(zero.outputs.get(0), ands[i].inputs.get(0));
				this.fuse(zero.outputs.get(0), ands[i].inputs.get(1));
			}
			else {
				this.fuse(this.inputs.get(n+i), ands[i].inputs.get(0));
				this.fuse(one.outputs.get(0), ands[i].inputs.get(1));				
			}
		}		
	}

}
