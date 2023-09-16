/**
 * 
 */
package circuit;

/**
 * @author tripunit
 *
 */
public class IntegerAsCircuit extends Circuit {

	/**
	 * 
	 * @param b
	 * @throws Exception
	 */
	public IntegerAsCircuit(byte b) throws Exception {
    	super();
    	Circuit c = this;
    	
    	for(int i = 0; i < 8; i++) {
    		if(((b >> i) & (0x01)) != 0) {
    			c.union(ZeroOne.getOne());
    		}
    		else {
    			c.union(ZeroOne.getZero());
    		}
    	}
    }

    /**
     * Encode the non-negative integer i in binary as a boolean circuit.
     * 
     * @param i
     * @throws Exception
     */
    public IntegerAsCircuit(int i) throws Exception {
        super();

        if (i < 0)
            throw new Exception(
                    "Can encode positive integers only as an IntegerAsCircuit circuit.");

        Circuit c = this;
        if (i == 0) {
            c.union(ZeroOne.getZero());
            return;
        }

        for (int j = i; j > 0; j /= 2) {
            boolean b;
            if (j % 2 == 1)
                b = true;
            else
                b = false;

            c.union(ZeroOne.get(b));
        }
    }

    /**
     * 
     * Buyer beware on this one. Negative args can really mess one up.
     * 
     * @param i
     * @throws Exception
     */
    public IntegerAsCircuit(long i) throws Exception {
        super();

        //System.out.println(String.format("IntegerAsCircuit: %08x", i));
        
        Circuit c = this;
        if (i == 0) {
            c.union(ZeroOne.getZero());
            return;
        }

        for (long j = i; j > 0; j /= 2) {
            boolean b;
            if (j % 2 == 1)
                b = true;
            else
                b = false;

            c.union(ZeroOne.get(b));
        }
    }

}
