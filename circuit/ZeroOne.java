/**
 * 
 */
package circuit;



/**
 * @author tripunit
 *
 */
public class ZeroOne extends Circuit {
	
	/**
	 * A circuit with one output that is guaranteed to be 0, if the arg is false, and guaranteed to be 1 if the arg is true.
	 * 
	 * @param one
	 */
	private static ZeroOne zero = null;
	private static ZeroOne one = null;
	
	private ZeroOne(boolean one) throws Exception {
		super();
		Circuit c = this;
		Wire twow[] = new Wire[2];
		Wire w[] = new Wire[4];
		
		Gate g = c.addNewGate(GateType.NOT);
		w[0] = twow[0] = c.addNewInput(); twow[1] = null;
		w[1] = c.addNewWire();
		c.connect(g, twow, w[1]);

		g = c.addNewGate(GateType.AND);
		w[2] = c.addNewWire();
		twow[0] = w[0]; twow[1] = w[1];
		c.connect(g, twow, w[2]);
		
		if(!one) {
			c.setAsOutput(w[2]);
		}
		else {
			g = c.addNewGate(GateType.NOT);
			w[3] = c.addNewWire();
			twow[0] = w[2]; twow[1] = null;
			c.connect(g, twow, w[3]);
			c.setAsOutput(w[3]);
		}

		/* This circuit has no input wires that are visible externally */
		c.removeAsInput(c.getInputs().get(0));
	}
	
	public static ZeroOne getOne() throws Exception{
		if(one == null){
			one = new ZeroOne(true);
		}
		return one;
	}
	
	public static ZeroOne getZero() throws Exception{
		if(zero == null){
			zero = new ZeroOne(false);
		}
		return zero;
	}
	
	public static ZeroOne get(boolean one) throws Exception{
		return(one)?getOne():getZero();
	}

	public static void reset() throws Exception {
		zero = null;
		one = null;
	}
}
