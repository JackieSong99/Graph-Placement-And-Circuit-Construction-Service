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
public class BitwiseAndOr extends Circuit {

	/**
	 * BitwiseAndOr of inputs[0,...,nbits-1] and inputs[nbits,...,2xnbits -1]. So we do inputs[i] AND/Or inputs[nbits + i],
	 * for all i in [0,nbits-1]. The number of output wires is of course nbits
	 * 
	 * @param isOr
	 * @param nbits
	 * @throws Exception
	 */
	public BitwiseAndOr(boolean isOr, int nbits) throws Exception {
		super();
		for(int i = 0; i < nbits; i++) {
			if(isOr) {
				this.union(new BigAndOr(true, 2));
			}
			else {
				this.union(new BigAndOr(false, 2));
			}
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
