package circuit;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

/**
 * 
 * @author tripunit
 *
 */
public class Circuit {

	/**
	 * 
	 */
	private static long nxtId = 1;

	/**
	 * 
	 */
	public Set<Wire> wires;
	
	/**
	 * 
	 */
	public Set<Gate> gates;
	
	/**
	 * 
	 */
	public List<Wire> inputs;
	
	/**
	 * 
	 */
	public List<Wire> outputs;

	/**
	 * 
	 */
	public enum GateType { AND, OR, NOT };

	/**
	 *
	 */
	public class Gate implements Comparable<Gate> {
		private Long id;
		private GateType typ;
		private Wire inputs[]; /* Exactly 2 for AND and OR gates. Exactly 1 for NOT gate. */
		private Wire output;
		
		public Gate(Circuit.GateType t) throws Exception { 
			id = Long.valueOf(Circuit.getNextId());
			typ = t; 
			switch (t) {
			case NOT:
				inputs = new Wire[1];
				inputs[0] = null;
				break;
			case AND:
			case OR:
				inputs = new Wire[2];
				inputs[0] = inputs[1] = null;
				break;
			default:
				throw new Exception("Invalid gate-type.");
			}

			output = null;
		}

		/**
		 * 
		 * @param t
		 * @param in
		 * @param out
		 * @throws Exception
		 */
		public Gate(Circuit.GateType t, Wire[] in, Wire out) throws Exception {
			this(t);
			if(t != Circuit.GateType.NOT) {
				inputs[1] = in[1];
			}
			inputs[0] = in[0];
			output = out;
		}

		/**
		 * 
		 * @param w
		 * @throws Exception
		 */
		private void setAsInput(Wire w) throws Exception {
			if(inputs[0] == null) { inputs[0] = w; return; }
			else if(this.getType() != Circuit.GateType.NOT && inputs[1] == null) { inputs[1] = w; return; }
			/* else */
			throw new Exception("Attempt to add too many inputs to gate.");
		}

		/**
		 * 
		 * @param w
		 * @throws Exception
		 */
		protected void setAsOutput(Wire w) throws Exception {
			if(output == null) { output = w; return; }
			/* else */
			throw new Exception("Attempt to add too many outputs to gate.");
		}
		
		/**
		 * 
		 * 
		 * @param before
		 * @param after
		 */
		private void replaceWire(Wire before, Wire after) {
			if(this.typ != Circuit.GateType.NOT) {
				if(before.equals(inputs[1])) {
					inputs[1] = after;
				}
			}
			
			if(before.equals(inputs[0])) {
				inputs[0] = after;
			}
			
			if(before.equals(output)) {
				output = after;
			}
		}
		
		/**
		 * 
		 * @return
		 */
		public Long getId() {
			return id;
		}
		
		/**
		 * 
		 * @param g
		 * @return
		 */
		public int compareTo(Gate g) {
			return (int)(this.getId() - g.getId());
		}
		
		/**
		 * 
		 * @return
		 */
		public Circuit.GateType getType() {
			return typ;
		}
		
		/**
		 * 
		 * @return
		 */
		public String getTypeAsString() {
			switch (this.typ) {
			case NOT:
				return new String("NOT");
			case AND:
				return new String("AND");
			default:
				return new String("OR");
			}
		}
		
		/**
		 * 
		 * @return
		 */
		public Circuit.Wire[] getInputs() {
			return inputs;
		}

		/**
		 * 
		 * @return
		 */
		public Circuit.Wire getOutput() {
			return output;
		}
	}

	/**
	 * 
	 */
	public class Wire implements Comparable<Wire> {
		private Long id;
		private Set<Gate> inputTo;
		private Gate outputOf;

		/**
		 * 
		 */
		public Wire() { 
			id = Long.valueOf(Circuit.getNextId()); 
			inputTo = new HashSet<Gate>(); 
			outputOf = null; 
		}

		/**
		 * 
		 * @return
		 */
		public Long getId() {
			return id;
		}
		
		/**
		 * 
		 * @return
		 */
		public Set<Gate> getInputTo() {
			return inputTo;
		}
		
		/**
		 * 
		 * @return
		 */
		public Gate getOutputOf() {
			return outputOf;			
		}

		/**
		 * 
		 * @param g
		 */
		private void addInputTo(Gate g) {
			inputTo.add(g);
		}

		/**
		 * 
		 * @param g
		 */
		private void setOutputOf(Gate g) throws Exception {
			if(outputOf == null) {
				outputOf = g; return;
			}
			/* else */
			throw new Exception("Attempt to set wire as output to new gate.");
		}

		/**
		 * 
		 * @param w
		 * @return
		 */
		public int compareTo(Wire w) {
			return (int)(this.getId() - w.getId());
		}
	}

	/**
	 * 
	 */
	public Circuit() {
		inputs = new LinkedList<Wire>();
		outputs = new LinkedList<Wire>();
		gates = new HashSet<Gate>();
		wires = new HashSet<Wire>();
	}
	
	/**
	 * 
	 * @return
	 */
	public Wire addNewWire() {
		Wire w = new Wire();
//		if(w.getId() == 230 || w.getId() == 234 || w.getId() == 235) {
//			 (new java.lang.Throwable()).printStackTrace();
//		}
		wires.add(w);
		return w;
	}

	/**
	 * 
	 * @return
	 */
	public Wire addNewInputAt(int loc) {
		Wire w = addNewWire();
		inputs.add(loc, w);
		return w;
	}

	/**
	 * 
	 * @return
	 */
	public Wire addNewInput() {
		return addNewInputAt(inputs.size());
	}

	/**
	 * 
	 * @param w
	 * @throws Exception
	 */
	public void setAsInput(Wire w) throws Exception {
		setAsInputAt(w, inputs.size());
	}

	/**
	 * 
	 * @param w
	 * @param loc
	 * @throws Exception
	 */
	public void setAsInputAt(Wire w, int loc) throws Exception {
		if(inputs.contains(w)) {
			throw new Exception("Attempted to set same wire as input more than once.");
		}
		
		if(!wires.contains(w)) {
			throw new Exception("Attempted to set wire unknown to circuit as input.");			
		}
		
		inputs.add(loc, w);
	}

	/**
	 * 
	 * @param w
	 * @throws Exception
	 */
	public void setAsOutput(Wire w) throws Exception {
		setAsOutputAt(w, outputs.size());
	}
	
	/**
	 * 
	 * @param w
	 * @param loc
	 * @throws Exception
	 */
	public void setAsOutputAt(Wire w, int loc) throws Exception {
		if(outputs.contains(w)) {
			throw new Exception("Attempted to set same wire as output more than once.");
		}
		
		if(!wires.contains(w)) {
			throw new Exception("Attempted to set wire unknown to circuit as output.");			
		}
		
		outputs.add(loc, w);
	}
	
	/**
	 * 
	 * Replace wire before with wire after, but in fields owned by the Circuit object only. I.e., do not mess with elements
	 * of gates inside the circuit.
	 * 
	 * @param before
	 * @param after
	 */
	private void replaceWire(Wire before, Wire after) throws Exception {
		int idx = inputs.indexOf(before);
		if(idx >= 0) {
			inputs.remove(before);
			if(after.getOutputOf() == null && !inputs.contains(after)) {
				inputs.add(idx, after);
			}
		}
		
		idx = outputs.indexOf(before);
		if(idx >= 0) {
			outputs.remove(before);
			
			/* No similar check as above here, because any wire can be an output */
			outputs.add(idx, after);
		}
		
		wires.remove(before);
	}

	/**
	 * 
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public Gate addNewGate(Circuit.GateType t) throws Exception {
		Gate g = new Gate(t);
		gates.add(g);
		return g;
	}

	/**
	 * 
	 * @return
	 */
	public List<Wire> getInputs() {
		return inputs;
	}

	/**
	 * 
	 * @return
	 */
	public List<Wire> getOutputs() {
		return outputs;
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<Wire> getWires() {
		return wires;
	}

	/**
	 * 
	 * @return
	 */
	public Set<Gate> getGates() {
		return gates;
	}
	
	/**
	 * 
	 * @param w
	 */
	public void removeAsOutput(Wire w) {
		outputs.remove(w);
//		System.out.println("After removeAsOutput("+w.getId()+")");
//		this.printWireList();
	}

	/**
	 * 
	 * @param w
	 */
	public void removeAsInput(Wire w) throws Exception {
		if(!inputs.contains(w)) throw new Exception("Cannot remove as input a wire that is not an input.");
		inputs.remove(w);
	}
	
	
	/**
	 * 
	 * @param c
	 */
	public void cleanInternalCircuitFromInputOutput(Circuit c) throws Exception {
		cleanInternalCircuitFromInput(c);
		cleanInternalCircuitFromOutput(c);
	}
	
	/**
	 * 
	 * @param c
	 */
	public void cleanInternalCircuitFromInput(Circuit c) throws Exception {
		Iterator<Wire> itw = c.getInputs().iterator();
		while(itw.hasNext()){
			removeAsInput(itw.next());
		}
	}
	
	/**
	 * 
	 * @param c
	 */
	public void cleanInternalCircuitFromOutput(Circuit c) throws Exception {
		Iterator<Wire> itw = c.getOutputs().iterator();
		while(itw.hasNext()){
			removeAsOutput(itw.next());
		}
	}
		
	/*
	 * 
	 */
	private void printGivenWireList(List<Circuit.Wire> wl) {
		Collections.sort(wl);
		for(Iterator<Circuit.Wire> wit = wl.iterator(); wit.hasNext(); ) {
			Circuit.Wire w = wit.next();
			System.out.print(w.getId());
			if(wit.hasNext()) {
				System.out.print(", ");
			}
			else {
				System.out.println();
			}
		}
	}
	
	/**
	 * 
	 */
	public void printWireList() {		
		List<Circuit.Wire> wl = new LinkedList<Circuit.Wire>(this.wires);
		System.out.print("Wires: ");
		printGivenWireList(wl);
	}
	
	/*
	 * 
	 */
	public void printInputWireList() {
		List<Circuit.Wire> il = new LinkedList<Circuit.Wire>(this.inputs); 
		System.out.print("Inputs: ");
		printGivenWireList(il);
	}
	
	/**
	 * 
	 */
	public void printOutputWireList() {
		List<Circuit.Wire> ol = new LinkedList<Circuit.Wire>(this.outputs); 
		System.out.print("Outputs: ");
		printGivenWireList(ol);
	}

	
	/**
	 * Union another circuit with this one. This circuit them becomes the union. The inputs of the argument are added as inputs of this circuit,
	 * and same for outputs.
	 * 
	 * Warning: this routine does *not* check uniqueness of IDs and such. It just does a dumb union.
	 * @param c
	 */
	public void union(Circuit c) {
		this.wires.addAll(c.getWires());
		this.gates.addAll(c.getGates());		
		this.inputs.addAll(c.getInputs());
		this.outputs.addAll(c.getOutputs());
	}
	
	/**
	 * 
	 * "Fuse" wires p and t, i.e. make them the same. A common use-case for this is
	 * to make an output wire from a (sub-)circuit the input wire of another (sub-)circuit.
	 * Returns the fused wire. Note that p stands for "permanent," i.e., this is the wire that
	 * is kept. The arg t stands for "throw-away." This is the wire that is discarded.
	 * 
	 * @param p
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public Circuit.Wire fuse(Wire p, Wire t) throws Exception {
//		System.out.println("Before fuse("+p.getId()+", "+t.getId()+")");
//		this.printWireList();

		//System.out.print("fuse(): ");
		if(!wires.contains(p)) {
			throw new Exception("Trying to fuse a wire p = "+p.getId()+" unfamiliar to circuit.");
		}
		if(!wires.contains(t)) {
			throw new Exception("Trying to fuse a wire t = "+t.getId()+" unfamiliar to circuit.");
		}
		
		if(t.getOutputOf() != null) {
			/* No way to fuse */
			throw new Exception("Trying to fuse into a wire that is an output of a gate.");
		}
		
//		System.out.println("Fusing wires "+p.getId()+" (perm), "+t.getId()+".");
		
		Set<Gate> gs = t.getInputTo();
		for(Iterator<Gate> i = gs.iterator(); i.hasNext(); ) {
			Gate g = i.next();
			
			g.replaceWire(t, p); /* replace t with p in the gate */
			p.addInputTo(g); /* p is now an input to g */
			//System.out.println(g.id+" "); System.out.flush();
		}
		
		this.replaceWire(t, p); /* replace t with p in the gate in the circuit */
		
		return p;
	}
	
	/**
	 * Connect input and output wires to a gate. The gate and wires should already exist in the circuit.
	 * 
	 * @param g
	 * @param in
	 * @param out
	 * @throws Exception
	 */
	public void connect(Gate g, Wire[] in, Wire out) throws Exception {
		/* First do the wires */

		if(g.getType() != Circuit.GateType.NOT) {
			if(!(wires.contains(in[1]))) {
				throw new Exception("Attempt to connect input wire not in circuit.");
			}
			/* else */
			(in[1]).addInputTo(g);
		}

		if(!(wires.contains(in[0]))) {
			throw new Exception("Attempt to connect input wire not in circuit.");
		}
		
		if(!(wires.contains(out))) {
			throw new Exception("Attempt to connect output wire not in circuit.");
		}
		
		(in[0]).addInputTo(g);
		out.setOutputOf(g);
		
		/* Now do the gate */
		if(!gates.contains(g)) {
			throw new Exception("Attempt to connect gate not in circuit.");
		}
		
		g.setAsInput(in[0]);
		if(g.getType() != Circuit.GateType.NOT) g.setAsInput(in[1]);
		g.setAsOutput(out);
	}

	/**
	 * 
	 * @return
	 */
	public static long getNextId() {
		return(nxtId++);
	}
	
	/**
	 * Some, likely ugly, way of printing this circuit as text.
	 */
	public void textPrint() {
		System.out.print("Circuit Inputs: ");
		List<Wire> il = this.getInputs();
		for(Iterator<Wire> i = il.iterator(); i.hasNext(); ) {
			Wire w = i.next();
			System.out.print(w.getId());
			if(i.hasNext()) System.out.print(", ");
		}
		System.out.println(".");

		System.out.println("Gates:");
		List<Gate> gs = new LinkedList<Gate>(this.getGates());
		Collections.sort(gs);
		for(Iterator<Gate> i = gs.iterator(); i.hasNext(); ) {
			Gate g = i.next();
			
			System.out.print("\t");
			Wire[] wa = g.getInputs();
			System.out.print(wa[0].getId());
			if(g.getType() != Circuit.GateType.NOT) {
				System.out.print(", "+wa[1].getId());
			}
			
//			System.out.print(" --> "+g.getTypeAsString()+" ("+g.getId()+") --> ");
			System.out.print(" --> "+g.getTypeAsString()+" --> ");
			
			Wire w = g.getOutput();
			System.out.println(w.getId());
		}
		
		System.out.print("Circuit Outputs: ");
		List<Wire> ol = this.getOutputs();
		for(Iterator<Wire> i = ol.iterator(); i.hasNext(); ) {
			Wire w = i.next();
			System.out.print(w.getId());
			if(i.hasNext()) System.out.print(", ");
		}
		System.out.println(".");
	}

	/**
	 * Not to be used when the circuit has no input.
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public List<Boolean> evaluateStartingInput(List<Boolean> in) throws Exception {
		if(in.size() != this.inputs.size()) throw new Exception("in.size() = "+in.size()+" != "+this.inputs.size()+" this.inputs.size()");

		Map<Circuit.Wire, Boolean> m = new HashMap<Circuit.Wire, Boolean>(); // Mapping of wire ID to value
		Set<Circuit.Gate> gs = new HashSet<Circuit.Gate>();
		
		m.clear();
		for(int i = 0; i < in.size(); i++) {
			m.put(this.inputs.get(i), in.get(i)); 
//			System.out.println("m.put("+this.inputs.get(i).getId()+", "+in.get(i)+")");  System.out.flush();
			for(Iterator<Circuit.Gate> t = this.inputs.get(i).inputTo.iterator(); t.hasNext(); ) {
				Circuit.Gate g = t.next();
				Circuit.Wire[] win = g.getInputs();
				
				if(g.typ == Circuit.GateType.NOT) {
					if(m.containsKey(win[0])) {
						gs.add(g);
					}
				}
				else {
					if(m.containsKey(win[0]) && m.containsKey(win[1])) {
						gs.add(g);
					}
				}
			}
		}
		
		// Now find wires that are not the output of any gate. Arbitrarily assign 0 to them.
		Set<Circuit.Wire> nonoutputwires = new HashSet<Circuit.Wire>();
		nonoutputwires.addAll(this.wires);
		nonoutputwires.removeAll(this.inputs);
		for(Iterator<Circuit.Wire> wt = nonoutputwires.iterator(); wt.hasNext(); ) {
			Circuit.Wire w = wt.next();
			if(w.getOutputOf() != null) continue;
			m.put(w, Boolean.valueOf(false));
			for(Iterator<Circuit.Gate> gt = w.inputTo.iterator(); gt.hasNext(); ) {
				Circuit.Gate g = gt.next();
				Circuit.Wire[] win = g.getInputs();
				
				if(g.typ == Circuit.GateType.NOT) {
					if(m.containsKey(win[0])) {
						gs.add(g);
						//System.out.println("Adding "+g.getId());
					}
				}
				else {
					if(m.containsKey(win[0]) && m.containsKey(win[1])) {
						gs.add(g);
						//System.out.println("Adding "+g.getId());
					}
				}
			}
		}
		
		while(!gs.isEmpty()) {
			Circuit.Gate g = gs.iterator().next();
			
			// Should be able to evaluate output wire of g at this point
			Circuit.Wire[] win = g.getInputs();
			Boolean[] bin = new Boolean[2];
			bin[0] = m.get(win[0]);
			if(bin[0] == null) {
				throw new Exception("Gate "+g.id+" of type "+g.getTypeAsString()+" not evaluate-able, as in-wire "+win[0].id+" has no value yet!");
			}

			if(g.typ != Circuit.GateType.NOT) {
				bin[1] = m.get(win[1]);
				if(bin[1] == null) {
					throw new Exception("Gate "+g.id+" of type "+g.getTypeAsString()+" not evaluate-able, as in-wire "+win[1].id+" has no value yet!");
				}
			}
			
			Circuit.Wire wout = g.getOutput();			
			if(m.containsKey(wout)) {
				throw new Exception("Wire "+wout.id+" already has a value, yet is out of another gate!");
			}

			Boolean bout;
			if(g.typ == Circuit.GateType.NOT) {
				bout = Boolean.valueOf(!(bin[0]));
			}
			else if(g.typ == Circuit.GateType.AND) {
				bout = Boolean.valueOf(bin[0] && bin[1]);
			}
			else { // OR
				bout = Boolean.valueOf(bin[0] || bin[1]);
			}
			m.put(wout, bout);
//			System.out.println("m.put("+wout.getId()+", "+bout+")"); System.out.flush();
			
			gs.remove(g);
			for(Iterator<Circuit.Gate> i = wout.getInputTo().iterator(); i.hasNext(); ) {
				Circuit.Gate nextg = i.next();
				Circuit.Wire[] nextgwin = nextg.getInputs();

				if(nextg.typ == Circuit.GateType.NOT) {
					if(m.containsKey(nextgwin[0])) {
						gs.add(nextg);
					}
				}
				else if(m.containsKey(nextgwin[0]) && m.containsKey(nextgwin[1])) {
					gs.add(nextg);
				}
			}
		}

		if(!m.keySet().containsAll(this.outputs)) {
			throw new Exception("Not all output wires have values!");
		}
		
		List<Boolean> out = new LinkedList<Boolean>();
		for(Iterator<Circuit.Wire> i = this.outputs.iterator(); i.hasNext(); ) {
			Circuit.Wire w = i.next();
			out.add(m.get(w));
		}

		return out;
	}

	/**
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public List<Boolean> evaluate(List<Boolean> in) throws Exception {
		if(in.size() != this.inputs.size()) throw new Exception("in.size() = "+in.size()+" != "+this.inputs.size()+" this.inputs.size()");
		
		/* Start at the inputs. Assign a value to each wire. */
		List<Circuit.Wire> w = new LinkedList<Circuit.Wire>(this.inputs);
		List<Boolean> wval = new LinkedList<Boolean>(in);
		
		while(w.size() < this.wires.size()) {
			Wire nxt = null;
			for(Iterator<Wire> i = this.getWires().iterator(); i.hasNext(); ) {
				nxt = i.next();
				if(w.contains(nxt)) continue;
				else break;
			}
			
			/* Keep working back till we can assign a value to a wire */
			//for(int cnt = 1; true; cnt++) {
			//	if(cnt%100 == 0) { System.out.print("."); cnt = 0; }
			while(true) {
				//System.out.print("Evaluate(): nxt: "+nxt.id);
				Gate g = nxt.getOutputOf();
				if(g != null) {
					//System.out.println(", g: "+g.id);
				}
				else {
					//System.out.println(", g: null");
				}
				if(g == null) {
					//System.out.println("Break 1");
					break;
				}
				Wire gins[] = g.getInputs();
				if(!w.contains(gins[0])) {
					nxt = gins[0]; continue;
				}
				else if(g.getType() != Circuit.GateType.NOT && !w.contains(gins[1])) {
					nxt = gins[1]; continue;
				}
				else {
					/* We can evaluate nxt */
					//System.out.println("Break 2");
					break;
				}				
			}
			
			Gate g = nxt.getOutputOf();
			if(g == null) {
				wval.add(Boolean.valueOf(true));
				w.add(nxt);
				continue;
			}
			Wire gins[] = g.getInputs();
			Boolean ginsvals[] = new Boolean[2];
			ginsvals[0] = wval.get(w.indexOf(gins[0]));
			if(g.getType() != Circuit.GateType.NOT) {
				ginsvals[1] = wval.get(w.indexOf(gins[1]));
			}
			
			if(g.getType() == Circuit.GateType.NOT) {
				boolean v = ginsvals[0].booleanValue();
				wval.add(Boolean.valueOf(!v));
			}
			else {
				boolean v1 = ginsvals[0].booleanValue();
				boolean v2 = ginsvals[1].booleanValue();

				if(g.getType() == Circuit.GateType.AND){
					wval.add(Boolean.valueOf(v1 & v2));
				}
				else { /* OR */
					wval.add(Boolean.valueOf(v1 | v2));
				}
			}
			w.add(nxt);
		}
		
		List<Boolean> ret = new LinkedList<Boolean>();
		for(Iterator<Wire> i = this.outputs.iterator(); i.hasNext(); ) {
			ret.add(Boolean.valueOf(wval.get(w.indexOf(i.next()))));
		}
		
		return ret;
	}
	
	public static void resetIDs(){
		nxtId = 1;
	}
}
