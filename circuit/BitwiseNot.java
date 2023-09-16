/**
 * 
 */
package circuit;

/**
 * @author tripunit
 *
 */
public class BitwiseNot extends Circuit {

	/**
	 * BitNot of inputs[0,...,nbits-1]. The number of output wires is of course nbits.
	 * 
	 * @param nbits
	 * @throws Exception
	 */
	public BitwiseNot(int nbits) throws Exception {
		super();
		
		for(int i = 0; i < nbits; i++) {
			this.union(new BitNot());
		}
		
		//System.out.println("BitwiseNot(): nInputs: "+this.inputs.size()+", nOutputs: "+this.outputs.size());
	}
}