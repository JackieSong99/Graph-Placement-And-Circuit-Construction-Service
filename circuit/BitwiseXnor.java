/**
 * 
 */
package circuit;

import java.util.LinkedList;
import java.util.List;

/**
 * @author tripunit
 *
 */
public class BitwiseXnor extends Circuit {
	
	/**
	 * A circuit for bitwise xnor. It takes the nbits argument, and doubles it, so we have two sets of input wires,
	 * inputs[0,...nbits-1] and inputs[nbits,...2 x nbits - 1]. Each pair, inputs[i] and inputs[i + nbits], for
	 * all i from 0 to nbits-1, is then xnor-ed. The number of output wires is nbits.
	 * 
	 * @param nbits
	 * @throws Exception
	 */
	public BitwiseXnor(int nbits) throws Exception {
		super();

                Circuit xorcirc = new BitwiseXor(nbits);
		this.union(xorcirc);

                Circuit notcirc = new BitwiseNot(nbits);
                this.union(notcirc);

                for(int i = 0; i < nbits; i++) {
                    this.fuse(this.getOutputs().get(0), notcirc.getInputs().get(i));
                    this.removeAsOutput(this.getOutputs().get(0));
                }
	}
}
