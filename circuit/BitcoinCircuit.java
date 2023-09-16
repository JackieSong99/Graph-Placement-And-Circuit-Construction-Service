/**
 * 
 */
package circuit;

/**
 * @author tripunit
 *
 */
public class BitcoinCircuit extends Circuit {

	/**
	 * Builds the bitcoin circuit. The argument, which is an array of bytes, are the known 32 bytes of input,
	 * followed by xxx bytes of the target. That is, b[0,...,31] is the known 32 bytes of input, and the
	 * remainder are the target.
	 * 
	 * @param b
	 * @throws Exception
	 */
	public BitcoinCircuit(Byte[] b) throws Exception {
		super();
	}
}
