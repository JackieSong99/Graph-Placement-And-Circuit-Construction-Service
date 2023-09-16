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
public class BitwiseXor extends Circuit {
	
	/**
	 * A circuit for bitwise xor. It takes the nbits argument, and doubles it, so we have two sets of input wires,
	 * inputs[0,...nbits-1] and inputs[nbits,...2 x nbits - 1]. Each pair, inputs[i] and inputs[i + nbits], for
	 * all i from 0 to nbits-1, is then xor-ed. The number of output wires is nbits.
	 * 
	 * @param nbits
	 * @throws Exception
	 */
	public BitwiseXor(int nbits) throws Exception {
		super();
		
		for(int i = 0; i < nbits; i++) {
			this.union(new BitXor());
		}
		
		List<Circuit.Wire> onewlist, otherwlist;
		onewlist = new LinkedList<Circuit.Wire>();
		otherwlist = new LinkedList<Circuit.Wire>();

		for(int i = 0; i < nbits; i++) {
			Circuit.Wire w = this.inputs.get(0);
			onewlist.add(w);
			this.removeAsInput(w);
			w = this.inputs.get(0);
			otherwlist.add(w);
			this.removeAsInput(w);
		}
		
		for(int i = 0; i < nbits; i++) {
			this.setAsInput(onewlist.get(i));
		}
		
		for(int i = 0; i < nbits; i++) {
			this.setAsInput(otherwlist.get(i));
		}
	}
}
