/**
 * 
 */
package circuit;

/**
 * @author tripunit
 *
 */
public class SumTwoMod32 extends Circuit {
	
	/**
	 * Sums the inputs whose num wires = n and m, interpreted as positive integers, mod 2^32.
	 * This works only for n,m <= 32. I.e., the input integers can be at most 2^32 - 1.
	 * @param n
	 * @param m
	 * @throws Exception
	 */
	public SumTwoMod32(int n, int m) throws Exception {
		super();
		
		if(n <= 0 || m <= 0 || n > 32 || m > 32) {
			throw new Exception("Illegal value(s) for n and/or m.");
		}
		
		Circuit sumc = new SumTwo(n, m); this.union(sumc);		
		Circuit lec = new LessEquals(33, sumc.outputs.size()); this.union(lec);
		Circuit choosec = new Choose(33); this.union(choosec);
		long theConst = 0x100000000l;
		
		Circuit constc = new IntegerAsCircuit(theConst); // 2^32

		assert(constc.outputs.size() == 33);

		this.union(constc);
		while(!this.outputs.isEmpty()) {
			Circuit.Wire w = this.getOutputs().get(0);
			this.removeAsOutput(w);
		}
		
		Circuit subc = new DiffTwo(sumc.outputs.size(), 33); 
		this.union(subc);
		
		assert(subc.outputs.size() > 32);

		for(int i = 0; i < 33; i++) {
			this.fuse(constc.outputs.get(i), lec.inputs.get(i));
		}
		
		for(int i = 33; i < sumc.outputs.size(); i++) {
			this.fuse(sumc.outputs.get(i-33),  lec.inputs.get(i));
		}
		
		this.fuse(lec.outputs.get(0), choosec.inputs.get(0));
		for(int i = 0; i < 33; i++) {
			this.fuse(constc.outputs.get(i), choosec.inputs.get(i+1));
		}
		
		for(int i = 0; i < sumc.outputs.size(); i++) {
			this.fuse(sumc.outputs.get(i), subc.inputs.get(i));
		}
		
		for(int i = 0; i < 33; i++) {
			this.fuse(choosec.outputs.get(i), subc.inputs.get(i + sumc.outputs.size()));
		}

		// Keep only Least Significant 32 bits as output
		while(this.outputs.size() > 32) {
			Circuit.Wire w = this.outputs.get(32);
			this.removeAsOutput(w);
		}
		
		assert(this.outputs.size() == 32);
		
		// Remove all inputs except those for sumc
		while(this.inputs.size() > n+m) {
			Circuit.Wire w = this.inputs.get(n+m);
			this.removeAsInput(w);
		}
	}
}
