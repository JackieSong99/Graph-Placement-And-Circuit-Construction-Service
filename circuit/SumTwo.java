/**
 * 
 */
package circuit;


/**
 * @author tripunit
 *
 */
public class SumTwo extends Circuit {
	/**
	 * A circuit to sum an n-bit and an m-bit number. In the output circuit, the first n input wires correspond to the n bits, and
	 * the next m input wires correspond to the m bits. The created circuit has 1 + max(n,m) output wires.
	 * 
	 * @param n
	 * @param m
	 */
	public SumTwo(int n, int m) throws Exception {
		super();
		Circuit c = this;
		
		for(int i = 0; i < n+m; i++) {
			c.addNewInput();
		}
		
		c.union(ZeroOne.getZero());
		Circuit.Wire prevcarryout = (c.getOutputs()).get(0);

		int cnt = 0;
		for(; cnt < n && cnt < m; cnt++) {
			Circuit b = new BitSumWithCarry();
			Circuit.Wire bcarryin = b.getInputs().get(2);
			c.union(b);
			c.fuse(prevcarryout, bcarryin);
			c.removeAsOutput(prevcarryout);
			prevcarryout = c.getOutputs().get(c.getOutputs().size() - 1);
			
			c.fuse(c.getInputs().get(cnt), b.getInputs().get(0));
			c.fuse(c.getInputs().get(cnt+n), b.getInputs().get(1));
		}

		boolean isn;
		if(cnt < n) isn = true;
		else isn = false;
		
		for( ; cnt < n || cnt < m; cnt++) {
			Circuit z = ZeroOne.getZero();
			Circuit b = new BitSumWithCarry();
			c.union(z);
			c.union(b);

			Wire bcarryin = b.getInputs().get(2);
			c.fuse(prevcarryout, bcarryin);

			if(isn) {
				c.fuse(c.getInputs().get(cnt), b.getInputs().get(0));
				c.fuse(z.getOutputs().get(0), b.getInputs().get(1));
			}
			else {
				c.fuse(c.getInputs().get(cnt+n), b.getInputs().get(1));
				c.fuse(z.getOutputs().get(0), b.getInputs().get(0));
			}

			c.removeAsOutput(prevcarryout);
			c.removeAsOutput(z.getOutputs().get(0));
			prevcarryout = c.getOutputs().get(c.getOutputs().size() - 1);
		}
	}
}
