/**
 * 
 */
package circuit;

/**
 * @author tripunit
 *
 */
public class DiffTwo extends Circuit {
	/**
	 * 
	 * Difference between an n-bit number and m-bit number. That is, if the n-bit number is N, and the m-bit number is M, this constructs a circuit
	 * that evaluates N-M. The result is a circuit whose output is 1+max{n,m}, where the final bit is the borrow bit, which indicates the sign. I.e.,
	 * if the final bit is set, this means that the result is negative.
	 * 
	 * @throws Exception
	 */
	public DiffTwo(int n, int m) throws Exception {
		super();
		
		for(int i = 0; i < n+m; i++) {
			this.addNewInput();
		}
		
		this.union( ZeroOne.getZero()); /* first borrow input */
		Circuit.Wire prevborrow = this.getOutputs().get(0); /* output of ZeroOne(false) = 0 above */

		int cnt;
		for(cnt = 0; cnt < n && cnt < m; cnt++) {
			Circuit d = new BitDiffWithBorrow();
			this.union(d);
			this.fuse(prevborrow, d.getInputs().get(2));
			this.removeAsOutput(prevborrow);
			prevborrow = d.getOutputs().get(1);
			this.fuse(this.getInputs().get(cnt), d.getInputs().get(0));
			this.fuse(this.getInputs().get(n+cnt), d.getInputs().get(1));
		}
		
		boolean isn;
		if(cnt < n) isn = true;
		else isn = false;

		for(; cnt < n || cnt < m; cnt++) {
			Circuit z = ZeroOne.getZero();
			Circuit d = new BitDiffWithBorrow();
			this.union(z);
			this.union(d);
			
			this.fuse(prevborrow, d.getInputs().get(2));

			if(isn) {
				this.fuse(z.getOutputs().get(0), d.getInputs().get(1));
				this.fuse(this.getInputs().get(cnt), d.getInputs().get(0));
			}
			else {
				this.fuse(z.getOutputs().get(0), d.getInputs().get(0));
				this.fuse(this.getInputs().get(cnt+n), d.getInputs().get(1));
			}
			this.removeAsOutput(z.getOutputs().get(0));
			this.removeAsOutput(prevborrow);
			prevborrow = d.getOutputs().get(1);
		}
	}
}
