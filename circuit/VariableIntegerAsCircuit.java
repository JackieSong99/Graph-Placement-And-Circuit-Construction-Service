/**
 * 
 */
package circuit;

import java.util.ArrayList;
import java.util.List;



/**
 * @author tripunit
 *
 */
public class VariableIntegerAsCircuit extends Circuit {
	
	private int maxValue;
	private int numbits;
	
	private List<Gate> isolatedOutputs;
	
	public VariableIntegerAsCircuit(int maxValue) throws Exception {
		super();
		this.numbits = 0;
		if(maxValue < 0) throw new Exception("Can encode positive integers only as an IntegerAsCircuit circuit.");
		this.maxValue = maxValue;
		Circuit c = this;
		c.union(ZeroOne.getZero());
		c.union(ZeroOne.getOne());
		removeAsOutput(ZeroOne.getZero().getOutputs().get(0));
		removeAsOutput(ZeroOne.getOne().getOutputs().get(0));
		
		this.isolatedOutputs = new ArrayList<Gate>();
		
		
		for(int j = this.maxValue; j > 0; j /= 2) {
			Gate g = addNewGate(GateType.NOT);
			g.getInputs()[0] = ZeroOne.getOne().getOutputs().get(0);
			this.isolatedOutputs.add(g);
			Wire out = addNewWire();
			g.setAsOutput(out);
			setAsOutput(g.getOutput());
			this.numbits++;
		}
		
		if(this.numbits == 0){
			Gate g = addNewGate(GateType.NOT);
			g.getInputs()[0] = ZeroOne.getOne().getOutputs().get(0);
			this.isolatedOutputs.add(g);
			Wire out = addNewWire();
			g.setAsOutput(out);
		}
		
	}
	
	
	public void updateValue(int k) throws Exception{
		if(k < 0 || k > this.maxValue)  throw new Exception("The value does not fit int the desired range");
		List<Boolean> bits = new ArrayList<Boolean>();
		for(int j = k; j > 0; j /= 2) {
			if(j%2 == 1) bits.add(Boolean.valueOf(true));
			else bits.add(Boolean.valueOf(false));
		}
		
		int remainingBits = this.numbits - bits.size();
		for(int i = 0; i < remainingBits; i++){
			bits.add(Boolean.valueOf(false));
		}
		
		for(int i = 0; i < this.numbits; i++){
			Gate g = this.isolatedOutputs.get(i);
			if(bits.get(i).booleanValue()){
				g.getInputs()[0] = ZeroOne.getZero().getOutputs().get(0);
			}else{
				g.getInputs()[0] = ZeroOne.getOne().getOutputs().get(0);
			}
		}
		
	}
}
