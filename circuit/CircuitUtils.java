package circuit;

import java.io.BufferedWriter;
import circuit.Circuit.Gate;
import circuit.Circuit.Wire;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CircuitUtils {
	static Long delim = Long.valueOf(0);
	
	static void reduceANDGate(Long o, Long i1, Long i2, List<Long> lits, Map<Long,Long> m){
		o = m.get(o);
		i1 = m.get(i1);
		i2 = m.get(i2);
		Long no = Long.valueOf(-1 * o.longValue());
		Long ni1 = Long.valueOf(-1 * i1.longValue());
		Long ni2 = Long.valueOf(-1 * i2.longValue());

		lits.add(no);
		lits.add(i1);
		lits.add(delim);//(!o OR i1) AND
		lits.add(no);
		lits.add(i2);
		lits.add(delim);//(!o OR i2) AND
		lits.add(ni1);
		lits.add(ni2);
		lits.add(o);
		lits.add(delim);//(o OR !i1 OR !i2)
	}
	
	static void reduceORGate(Long o, Long i1, Long i2, List<Long> lits, Map<Long,Long> m){
		o = m.get(o);
		i1 = m.get(i1);
		i2 = m.get(i2);
		Long no = Long.valueOf(-1 * o.longValue());
		Long ni1 = Long.valueOf(-1 * i1.longValue());
		Long ni2 = Long.valueOf(-1 * i2.longValue());

		lits.add(no);
		lits.add(i1);
		lits.add(i2);
		lits.add(delim);//(!o OR i1 OR i2) AND
		lits.add(o);
		lits.add(ni1);
		lits.add(delim);//(o OR !i1) AND
		lits.add(o);
		lits.add(ni2);
		lits.add(delim);//(o OR !i2)
	}
	
	static void reduceNOTGate(Long o, Long i, List<Long> lits, Map<Long,Long> m){
		o = m.get(o);
		i = m.get(i);
		Long no = Long.valueOf(-1 * o.longValue());
		Long ni = Long.valueOf(-1 * i.longValue());

		lits.add(ni);
		lits.add(no);
		lits.add(delim);//(!o OR !i) AND
		lits.add(i);
		lits.add(o);
		lits.add(delim);//(o OR i)
	}
	
	static void reduceGateToCNFClause(Gate g, List<Long> lits, Map<Long,Long> m){
		switch(g.getType()){
		case AND:
			reduceANDGate(
					g.getOutput().getId(), 
					g.getInputs()[0].getId(),
					g.getInputs()[1].getId(),
					lits, m
					);
			break;
		case OR:
			reduceORGate(
					g.getOutput().getId(), 
					g.getInputs()[0].getId(),
					g.getInputs()[1].getId(),
					lits, m
					);
			break;
		case NOT:
			reduceNOTGate(
					g.getOutput().getId(), 
					g.getInputs()[0].getId(),
					lits, m
					);
			break;
		}
	}
	
	/**
	 * Renumber the wires, so the wires are in [1, # wires]. And the input wires are numbered contiguously starting at 1.
	 * 
	 * @param c
	 * @return
	 * @throws Exception
	 */
	private static Map<Long,Long> wiresMap(Circuit c) throws Exception {
		Map<Long,Long> ret = new HashMap<Long,Long>();
		long curr;
		
		// First, the output wires get the max IDs
		curr = c.wires.size() - c.outputs.size() + 1;
		for(Iterator<Circuit.Wire> t = c.outputs.iterator(); t.hasNext(); ) {
			Circuit.Wire w = t.next();
			ret.put(w.getId(), Long.valueOf(curr++));
		}
		
		// Then inputs -- get min IDs
		curr = 1;
		for(Iterator<Circuit.Wire> t = c.inputs.iterator(); t.hasNext(); ) {
			Circuit.Wire w = t.next();
			ret.put(w.getId(), Long.valueOf(curr++));
		}

		Long wireWithMax = null;
		for(Iterator<Circuit.Wire> t = c.wires.iterator(); t.hasNext(); ) {
			Circuit.Wire w = t.next();
			if(!ret.containsKey(w.getId())) {
				ret.put(w.getId(), Long.valueOf(curr++));
			}
			wireWithMax = w.getId();
		}
		
		return ret;
	}
	
	/**
	 * @param	c: the input circuit MUST have EXACTLY one output wire. 
	 * 			a 'Exception' is thrown otherwise.
	 * @return	A list that contains CNF clauses in the DIMACS format.
	 * @see		http://fmv.jku.at/lingeling/
	 * @throws	Exception 
	 */
	private static List<Long> circuitToCNF(Circuit c) throws Exception{
		Map<Long, Long> m = wiresMap(c);
		
		List<Long> lits = new ArrayList<Long>();
		List<Wire> owires = c.getOutputs();
		if(owires.size() != 1){
			throw new Exception("Unable to reduce circuits with more or less than exactly 1 output wire");
		}
		lits.add(m.get(owires.get(0).getId()));
		lits.add(delim);
		for(Gate g: c.getGates()){
			reduceGateToCNFClause(g,lits,m);
		}
		return lits;
	}
	
	/**
	 * @param c: circuit
	 * @return CNF-SAT instance in DIMACS format.
	 */
	public static String createCNF(Circuit c) throws Exception {
		List<Long> lits = circuitToCNF(c);
		StringBuilder sb = new StringBuilder();
		int numClauses = 0;
		int maxVar = 0;
		for(Long lit:lits){
			sb.append(lit);
			sb.append(' ');
			if(lit.intValue() == 0){
				sb.append('\n');
				numClauses++;
			}
			if(lit.intValue() > maxVar){
				maxVar = lit.intValue();
			}
		}
		sb.insert(0, "p cnf " + maxVar + " " + numClauses + "\n");
		return sb.toString();
	}
	
	/**
	 * Converts a circuit with one output to cnf-sat. NOTE: the input wires are mapped to the
	 * variables 1, 2, 3, ..., in order. So, for example, if we have 10 inputs wires to c, the
	 * CNF variables 1, 2, ... , 10 correspond to those input wires, in order.
	 * 
	 * @param c
	 * @param loc
	 */
	public static void cnfSatToFile(Circuit c, String loc) throws Exception {
		if(c.getOutputs().size() != 1) {
			throw new Exception("Cannot handle a circuit with "+c.getOutputs().size()+" outputs.");
		}
		File f = new File(loc);
		BufferedWriter writer = new BufferedWriter(new FileWriter(f));
		writer.write(createCNF(c));
		writer.flush();
		writer.close();		
	}
}
