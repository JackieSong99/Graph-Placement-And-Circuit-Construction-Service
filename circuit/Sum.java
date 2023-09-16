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
public class Sum extends Circuit {
	/**
	 * Return a circuit that has number of input wires = sum of integer values in x. The input is to be perceived as |x| binary numbers.
	 * The circuit represents the sum of those binary numbers. Thus, the number of output wires of the circuit is at most log_2 |x| + max_i{x}, where
	 * max_i{x} is the largest integer value in x.
	 * 
	 */
	public Sum(List<Integer> x) throws Exception {
		super();
		if(x.size() == 0) return;
		if(x.size() == 1) {
			for(int i = 0; i < x.get(0).intValue(); i++) {
				Circuit.Wire w = this.addNewInput();
				this.setAsOutput(w);
			}
			return;
		}
		if(x.size() == 2) {
			Circuit c = new SumTwo(x.get(0).intValue(), x.get(1).intValue());
			this.union(c);
			return;
		}
		else {
			List<Integer> l1 = new LinkedList<Integer>(), l2 = new LinkedList<Integer>();
			for(int i = 0; i < x.size(); i++) {
				if(i > (int)(x.size()/2)) {
					l2.add(x.get(i));
				}
				else {
					l1.add(x.get(i));
				}
			}
			Circuit s1 = new Sum(l1), s2 = new Sum(l2);
			this.union(s1); this.union(s2);
			List<Circuit.Wire> o1 = s1.getOutputs(), o2 = s2.getOutputs();
			Circuit t = new SumTwo(o1.size(), o2.size());
			this.union(t);
			List<Circuit.Wire> ti = t.getInputs();
			for(int i = 0; i < o1.size(); i++) {
				this.fuse(o1.get(i), ti.get(i));
				this.removeAsOutput(o1.get(i));
			}
			for(int i = 0; i < o2.size(); i++) {
				this.fuse(o2.get(i), ti.get(i+o1.size()));
				this.removeAsOutput(o2.get(i));
			}
			
			return;
		}
	}
}
