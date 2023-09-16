/**
 * 
 */
package circuit;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author tripunit
 *
 */
public class ShaCircuit extends Circuit {

	/**
	 * 
	 */
	public static long H[] = { 
			0x6a09e667l, 0xbb67ae85l, 0x3c6ef372l, 0xa54ff53al, 
			0x510e527fl, 0x9b05688cl, 0x1f83d9abl, 0x5be0cd19l
	};

	/**
	 * Builds a SHA 256 circuit. The argument is the number of 512-bit blocks of input. Note that to actually compute the hash, one must:
	 * (1) pad as prescribed in the SHA spec to a multiple of 512-bits, and,
	 * (2) Instantiate appropriate IntegerAsCircuit circuits, fuse their outputs with the input wires of this circuit, and invoke evaluate() on this circuit.
	 * 
	 * @param n
	 * @throws Exception
	 */
	public ShaCircuit(int n) throws Exception {
		
		for(int i = 0; i < n; i++) {
			Circuit c = new Compress();
			this.union(c);

			if(i == 0) {
				for(int j = 0; j < H.length; j++) {
					Circuit intcirc = new IntegerAsCircuit(H[j]);
					while(intcirc.outputs.size() < 32) {
						Circuit z = ZeroOne.getZero();
						intcirc.union(z);
					}

					this.union(intcirc);

					for(int k = 0; k < 32; k++) {
						this.fuse(intcirc.outputs.get(k), c.inputs.get(512+j*32+k));
						this.removeAsOutput(intcirc.outputs.get(k));
					}
				}
			}
			else {
				for(int j = 0; j < 256; j++) {
					this.fuse(this.outputs.get(0), c.inputs.get(512+j));
					this.removeAsOutput(this.outputs.get(0));
				}
			}
		}
	}
	
	/**
	 * 
	 * @param bar
	 * @return
	 * @throws Exception
	 */
	public static long[] reorderForIntegerCircuits(byte[] bar) throws Exception {
		if(bar.length % 4 != 0) throw new Exception("arg must have multiple of 4 bytes.");
		
		long[] ret = new long[bar.length/4];
	
		for(int i = 0; i < bar.length; i+= 4) { // 32 bits at a time.
			long[] l = new long[4];
			ret[i/4] = 0;
			
			for(int j = 0; j < 4; j++) {	
				l[j] = ((long)bar[i+j]) & 0xffl;
				l[j] <<= (8*(3-j));
			}
			
			ret[i/4] = l[0] | l[1] | l[2] | l[3];
		}
		
		return ret;	
	}

	
	/**
	 * 
	 * @param bar
	 * @return
	 * @throws Exception
	 */
	public static List<Boolean> reorderForCircuitInput(byte[] bar) throws Exception {
		List<Boolean> ret = new LinkedList<Boolean>();
	
		for(int i = 0; i < bar.length; i+= 4) { // 32 bits at a time.
			for(int j = i+3; j >= i; j--) {
				byte b = bar[j];

				for(int k = 0; k < 8; k++) {
					if((b & 0x1) > 0) {
						ret.add(Boolean.TRUE);
					}
					else {
						ret.add(Boolean.FALSE);
					}
				
					b >>= 1;
				}
			}
		}
		
		return ret;	
	}
	
	public static byte[] evaluateShaCircuitOnly(byte[] in) throws Exception {		
		int nbits = in.length * 8;
		int npad = 65; // At least 65 bits of padding
		if((nbits+npad) > ((nbits+npad)/512)*512) {
			npad = ((nbits+npad)/512+1)*512 - nbits;
		}
		
		byte[] intomysha = new byte[(nbits+npad)/8];
		for(int i = 0; i < in.length; i++) {
			intomysha[i] = in[i];
		}
		
		intomysha[in.length] = (byte)0x80;
		for(int i = in.length + 1; i < intomysha.length; i++) {
			intomysha[i] = (byte)0x00;
		}

		byte[] lengthpad = new byte[8];
		int l = in.length * 8;
		int j = 0;
		while(l > 0) {
			if((l & 0x1) == 1) {
				byte b = (byte)0x1;
				for(int k = 0; k < j%8; k++) b <<= 1;
				lengthpad[j/8] |= b;
			}
			
			j++; l >>= 1;
		}
				
		for(int i = 0; i < 8; i++) {
			intomysha[intomysha.length - 1 - i] = lengthpad[i];
		}
		
		int ncompress = (intomysha.length)/64;
		Circuit c = new ShaCircuit(ncompress);

		long[] incirclongs = reorderForIntegerCircuits(intomysha);
		for(int i = 0; i < incirclongs.length; i++) {
			Circuit lc = new IntegerAsCircuit(incirclongs[i]);
			while(lc.outputs.size() < 32) {
				lc.union(ZeroOne.getZero());
			}

			c.union(lc);

			for(int k = 0; k < 32; k++) {
				c.fuse(lc.outputs.get(k), c.inputs.get(0));
				c.removeAsOutput(lc.outputs.get(k));
			}
		}
		
		List<Boolean> incirc = new LinkedList<Boolean>(); // Empty list
		List<Boolean> outcirc = c.evaluateStartingInput(incirc);
		
		byte[] out = new byte[32];
		for(int i = 0; i < 32; i++) out[i] = (byte)0x0;
		
		for(int i = 0; i < outcirc.size(); i++) {
			if(outcirc.get(i)) {
				int idx = i/8;
				int bitloc = i%8;
				byte thebit = (byte)0x1;
				thebit <<= bitloc;
				
				out[idx] |= thebit;
			}
		}
		
		for(int i = 0; i < 32; i += 4) {
			byte tmp = out[i];
			out[i] = out[i+3];
			out[i+3] = tmp;
			
			tmp = out[i+1];
			out[i+1] = out[i+2];
			out[i+2] = tmp;
		}
		
		return out;
	}
	
	/**
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static byte[] evaluateSha(byte[] in) throws Exception {
		
		int nbits = in.length * 8;
		int npad = 65; // At least 65 bits of padding
		if((nbits+npad) > ((nbits+npad)/512)*512) {
			npad = ((nbits+npad)/512+1)*512 - nbits;
		}
		
		byte[] intomysha = new byte[(nbits+npad)/8];
		for(int i = 0; i < in.length; i++) {
			intomysha[i] = in[i];
		}
		
		intomysha[in.length] = (byte)0x80;
		for(int i = in.length + 1; i < intomysha.length; i++) {
			intomysha[i] = (byte)0x00;
		}

		byte[] lengthpad = new byte[8];
		int l = in.length * 8;
		int j = 0;
		while(l > 0) {
			if((l & 0x1) == 1) {
				byte b = (byte)0x1;
				for(int k = 0; k < j%8; k++) b <<= 1;
				lengthpad[j/8] |= b;
			}
			
			j++; l >>= 1;
		}
				
		for(int i = 0; i < 8; i++) {
			intomysha[intomysha.length - 1 - i] = lengthpad[i];
		}
		
		int ncompress = (intomysha.length)/64;
		Circuit c = new ShaCircuit(ncompress);

		List<Boolean> incirc = reorderForCircuitInput(intomysha);		
		List<Boolean> outcirc = c.evaluateStartingInput(incirc);
		
		byte[] out = new byte[32];
		for(int i = 0; i < 32; i++) out[i] = (byte)0x0;
		
		for(int i = 0; i < outcirc.size(); i++) {
			if(outcirc.get(i)) {
				int idx = i/8;
				int bitloc = i%8;
				byte thebit = (byte)0x1;
				thebit <<= bitloc;
				
				out[idx] |= thebit;
			}
		}
		
		for(int i = 0; i < 32; i += 4) {
			byte tmp = out[i];
			out[i] = out[i+3];
			out[i+3] = tmp;
			
			tmp = out[i+1];
			out[i+1] = out[i+2];
			out[i+2] = tmp;
		}
		
		return out;
	}
	
	/**
	 * 
	 * @param puzzle
	 * @return
	 * @throws Exception
	 */
	public static Circuit getBitCoinCircuitWithExtraOutputs(byte[] puzzle, byte[] THRESHOLD) throws Exception {
		if(puzzle.length != 32) {
			throw new Exception("Puzzle is "+puzzle.length+" bytes long. It needs to be exactly 32 bytes.");
		}
		
		long[] puzzleaswords = reorderForIntegerCircuits(puzzle);
		Circuit c = getBitCoinCircuitWithExtraOutputsNoPuzzle(THRESHOLD);
		
		
		// Hookup puzzle
		for(int i = 0; i < puzzleaswords.length; i++) {
			Circuit pc = new IntegerAsCircuit(puzzleaswords[i]);
			while(pc.outputs.size() < 32) {
				pc.union(ZeroOne.getZero());
			}
			
			c.union(pc);
			for(Iterator<Circuit.Wire> t = pc.outputs.iterator(); t.hasNext(); ) {
				Circuit.Wire w = t.next();
				c.fuse(w, c.inputs.get(32));
				c.removeAsOutput(w);
			}
		}
		
		return c;
	}

	/**
	 * 
	 * @param puzzle
	 * @return
	 * @throws Exception
	 */
	public static Circuit getBitcoinCircuit(byte[] puzzle, byte[] THRESHOLD) throws Exception {
		Circuit c = getBitCoinCircuitWithExtraOutputs(puzzle, THRESHOLD);
		int noutputs = c.getOutputs().size();
		
		for(int i = 0; i < noutputs-1; i++) c.removeAsOutput(c.getOutputs().get(0));

		return c;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Circuit getBitCoinCircuitWithExtraOutputsNoPuzzle(byte[] THRESHOLD) throws Exception {
		final long[] PAD_ONE = {
				0x80000000l, 
				0x00000000l, 0x00000000l, 0x00000000l, 0x00000000l, 0x00000000l, 0x00000000l, 0x00000000l, 0x00000000l, 0x00000000l, 
				0x00000000l, 0x00000280l,
		};
		
		final long[] PAD_TWO = {
				0x80000000l, 
				0x00000000l, 0x00000000l, 0x00000000l, 0x00000000l, 0x00000000l,
				0x00000000l, 0x00000100l,
		};
		
		Circuit[] c = new Circuit[3]; // c[0] will be our eventual return value
		c[2] = new LessEquals(256, THRESHOLD.length * 8);

		// First, the less equals
		for(int i = THRESHOLD.length - 1; i >= 0; i--) {
			Circuit pc = new IntegerAsCircuit(THRESHOLD[i]);
			
			c[2].union(pc);
			
			for(Iterator<Circuit.Wire> t = pc.outputs.iterator(); t.hasNext(); ) {
				Circuit.Wire w = t.next();
				c[2].fuse(w, c[2].inputs.get(256));
				c[2].removeAsOutput(w);
			}
		}

		// Then outputs of 2nd SHA -- beware of byte ordering
		c[1] = new ShaCircuit(1);
		c[1].union(c[2]);

		for(int i = 0; i < 8; i++) {
			for(int j = 3; j >= 0; j--) {
				c[1].fuse(c[1].outputs.get(i*32+8*j+0), c[1].inputs.get(512));
				c[1].fuse(c[1].outputs.get(i*32+8*j+1), c[1].inputs.get(512));
				c[1].fuse(c[1].outputs.get(i*32+8*j+2), c[1].inputs.get(512));
				c[1].fuse(c[1].outputs.get(i*32+8*j+3), c[1].inputs.get(512));
				c[1].fuse(c[1].outputs.get(i*32+8*j+4), c[1].inputs.get(512));
				c[1].fuse(c[1].outputs.get(i*32+8*j+5), c[1].inputs.get(512));
				c[1].fuse(c[1].outputs.get(i*32+8*j+6), c[1].inputs.get(512));
				c[1].fuse(c[1].outputs.get(i*32+8*j+7), c[1].inputs.get(512));
			}
		}

		// Then, c[1]
		// Hookup the padding first
		for(int i = 0; i < PAD_TWO.length; i++) {
			Circuit pc = new IntegerAsCircuit(PAD_TWO[i]);
			while(pc.outputs.size() < 32) {
				pc.union(ZeroOne.getZero());
			}
			
			c[1].union(pc);
			for(Iterator<Circuit.Wire> t = pc.outputs.iterator(); t.hasNext(); ) {
				Circuit.Wire w = t.next();
				c[1].fuse(w, c[1].inputs.get(256));
				c[1].removeAsOutput(w);
			}
		}

		// Finally, c[0]
		c[0] = new ShaCircuit(2);

		// First outputs of 1st SHA --> inputs of 2nd sha; no byte-ordering issue
		c[0].union(c[1]);
		for(int i = 0; i < 256; i++) {
			c[0].fuse(c[0].outputs.get(i), c[0].inputs.get(1024));
		}
		
		// Then, hookup the padding
		for(int i = 0; i < PAD_ONE.length; i++) {
			Circuit pc = new IntegerAsCircuit(PAD_ONE[i]);
			while(pc.outputs.size() < 32) {
				pc.union(ZeroOne.getZero());
			}
			
			c[0].union(pc);
			for(Iterator<Circuit.Wire> t = pc.outputs.iterator(); t.hasNext(); ) {
				Circuit.Wire w = t.next();
				c[0].fuse(w, c[0].inputs.get(640));
				c[0].removeAsOutput(w);
			}
		}
				
		return c[0];
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Circuit getBitcoinCircuitNoPuzzle(byte[] THRESHOLD) throws Exception {
		Circuit c = getBitCoinCircuitWithExtraOutputsNoPuzzle(THRESHOLD);
		int noutputs = c.getOutputs().size();
		
		for(int i = 0; i < noutputs-1; i++) c.removeAsOutput(c.getOutputs().get(0));

		return c;
	}
	
	/**
	 * 
	 * @param puzzle
	 * @return
	 * @throws Exception
	 */
	public static String getBitCoinCNF(byte[] puzzle, byte[] THRESHOLD) throws Exception {
		System.out.print("Building circuit..."); System.out.flush();
		Circuit c = getBitcoinCircuit(puzzle, THRESHOLD);
		System.out.print("done! Generating CNF as String..."); System.out.flush();
		String s =  CircuitUtils.createCNF(c);
		System.out.println("done!");

		return s;
	}
	
	/**
	 * 
	 * @param puzzle
	 * @param loc
	 * @throws Exception
	 */
	public static void dumpBitCoinCNF(byte[] puzzle, byte[] THRESHOLD, String loc) throws Exception {
		System.out.print("Building circuit..."); System.out.flush();
		Circuit c = getBitcoinCircuit(puzzle, THRESHOLD);
		System.out.print("done! Generating CNF and writing to file "+loc+"..."); System.out.flush();
		CircuitUtils.cnfSatToFile(c, loc);
		System.out.println("done!");
	}

	/**
	 * 
	 * @param loc
	 * @throws Exception
	 */
	public static void dumpBitCoinCNFNoPuzzle(byte[] THRESHOLD, String loc) throws Exception {
		System.out.print("Building circuit..."); System.out.flush();
		Circuit c = getBitcoinCircuitNoPuzzle(THRESHOLD);
		System.out.print("done! Generating CNF and writing to file "+loc+"..."); System.out.flush();
		CircuitUtils.cnfSatToFile(c, loc);
		System.out.println("done!");		
	}
}
