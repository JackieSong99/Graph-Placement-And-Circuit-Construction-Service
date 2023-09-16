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
public class BitsnXnor extends Circuit {
	
	/**
	 * A circuit that takes nbits > 1 input and returns a circuit
	 * that is the xnor of all those nbits. The output is 1 bit
         *
	 * @param nbits
	 * @throws Exception
	 */
	public BitsnXnor(int nbits) throws Exception {
		super();

                if(nbits < 2) {
                    throw new Exception("nbits must be > 1");
                }
		
		for(int i = 0; i < (nbits - 1); i++) {
                    Circuit c = new BitwiseXnor(2);
		    this.union(c);
                    if(i == 0) {
                        // Nothing more to do
                        continue;
                    }

                    // Else, wire gymnastics
                    this.fuse(this.outputs.get(0), c.inputs.get(0));
                    this.removeAsOutput(this.outputs.get(0));
		}
	}
}
