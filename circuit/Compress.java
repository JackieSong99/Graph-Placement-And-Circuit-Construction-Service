/**
 * 
 */
package circuit;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

/**
 * @author tripunit
 *
 */
public class Compress extends Circuit {
	
	
	public static final long K[] = {
			0x428a2f98l, 0x71374491l, 0xb5c0fbcfl, 0xe9b5dba5l, 0x3956c25bl, 0x59f111f1l, 0x923f82a4l, 0xab1c5ed5l,
			0xd807aa98l, 0x12835b01l, 0x243185bel, 0x550c7dc3l, 0x72be5d74l, 0x80deb1fel, 0x9bdc06a7l, 0xc19bf174l,
			0xe49b69c1l, 0xefbe4786l, 0x0fc19dc6l, 0x240ca1ccl, 0x2de92c6fl, 0x4a7484aal, 0x5cb0a9dcl, 0x76f988dal,
			0x983e5152l, 0xa831c66dl, 0xb00327c8l, 0xbf597fc7l, 0xc6e00bf3l, 0xd5a79147l, 0x06ca6351l, 0x14292967l,
			0x27b70a85l, 0x2e1b2138l, 0x4d2c6dfcl, 0x53380d13l, 0x650a7354l, 0x766a0abbl, 0x81c2c92el, 0x92722c85l,
			0xa2bfe8a1l, 0xa81a664bl, 0xc24b8b70l, 0xc76c51a3l, 0xd192e819l, 0xd6990624l, 0xf40e3585l, 0x106aa070l,
			0x19a4c116l, 0x1e376c08l, 0x2748774cl, 0x34b0bcb5l, 0x391c0cb3l, 0x4ed8aa4al, 0x5b9cca4fl, 0x682e6ff3l,
			0x748f82eel, 0x78a5636fl, 0x84c87814l, 0x8cc70208l, 0x90befffal, 0xa4506cebl, 0xbef9a3f7l, 0xc67178f2l
	};
	
	/**
	 * Creates a circuit that corresponds to one SHA256 compression function. The resultant circuit has
	 * 512+256 = 768 input wires, and 256 output wires. Suppose the input wires are indexed 0,...,767. Then, the
	 * wires 0,...,511 correspond to the message blocks, M^(i)_j's. That is, wires 0,...,31 correspond to 
	 * M^(i)_0, wires 32,..,63 correspond to M^(i)_1, and so on, through M^(i)_15. The remaining 256 input
	 * wires correspond to H^(i-1)_1, ..., H^(i-1)_8 in order. That is, input wires 512,...,543 correspond to
	 * H^(i-1)_1, and so on.
	 * 
	 * @throws Exception
	 */
	public Compress() throws Exception {
		super();

		Circuit z = ZeroOne.getZero();
		this.union(z); this.removeAsOutput(z.outputs.get(0));
		
		Circuit Kascirc[] = new Circuit[K.length];
		for(int p = 0; p < K.length; p++) {
			Kascirc[p] = new IntegerAsCircuit(K[p] & 0xffffffffl);

			while(Kascirc[p].outputs.size() < 32) {
				Kascirc[p].union(z);
			}

			this.union(Kascirc[p]);
			for(int q = 0; q < Kascirc[p].outputs.size(); q++) {
				this.removeAsOutput(Kascirc[p].outputs.get(q));
			}
		}

		for(int i = 0; i < (512+256); i++) {
			this.addNewInput();
		}
		
		Circuit.Wire a[] = new Circuit.Wire[32];
		for(int i = 0; i < 32; i++) {
			a[i] = this.inputs.get(i+512+32*0);
		}

		Circuit.Wire b[] = new Circuit.Wire[32];
		for(int i = 0; i < 32; i++) {
			b[i] = this.inputs.get(i+512+32*1);
		}
		
		Circuit.Wire c[] = new Circuit.Wire[32];
		for(int i = 0; i < 32; i++) {
			c[i] = this.inputs.get(i+512+32*2);
		}

		Circuit.Wire d[] = new Circuit.Wire[32];
		for(int i = 0; i < 32; i++) {
			d[i] = this.inputs.get(i+512+32*3);
		}

		Circuit.Wire e[] = new Circuit.Wire[32];
		for(int i = 0; i < 32; i++) {
			e[i] = this.inputs.get(i+512+32*4);
		}

		Circuit.Wire f[] = new Circuit.Wire[32];
		for(int i = 0; i < 32; i++) {
			f[i] = this.inputs.get(i+512+32*5);
		}

		Circuit.Wire g[] = new Circuit.Wire[32];
		for(int i = 0; i < 32; i++) {
			g[i] = this.inputs.get(i+512+32*6);
		}

		Circuit.Wire h[] = new Circuit.Wire[32];
		for(int i = 0; i < 32; i++) {
			h[i] = this.inputs.get(i+512+32*7);
		}
		
		// The W's
		Circuit.Wire W[][] = new Circuit.Wire[64][32];
		
		for(int j = 0; j < 64; j++) {
			// First, Ch(e, f, g)
			Circuit ch = new Ch();
			this.union(ch);
			for(int l = 0; l < ch.inputs.size(); l++) {
				this.removeAsInput(ch.inputs.get(l));
			}
			for(int l = 0; l < ch.outputs.size(); l++) {
				this.removeAsOutput(ch.outputs.get(l));
			}

			for(int l = 0; l < 32; l++) {
				this.fuse(e[l], ch.inputs.get(l));
				this.fuse(f[l], ch.inputs.get(l+32));
				this.fuse(g[l], ch.inputs.get(l+32*2));
			}
			
			// Then, Maj(a,b,c)
			Circuit maj = new Maj();
			this.union(maj);
			for(int l = 0; l < maj.inputs.size(); l++) {
				this.removeAsInput(maj.inputs.get(l));
			}
			for(int l = 0; l < maj.outputs.size(); l++) {
				this.removeAsOutput(maj.outputs.get(l));
			}
			
			for(int l = 0; l < 32; l++) {
				this.fuse(a[l], maj.inputs.get(l));
				this.fuse(b[l], maj.inputs.get(l+32));
				this.fuse(c[l], maj.inputs.get(l+32*2));
			}
			
			// Then, \Sigma_0(a)
			Circuit sigma0a = new Sigma(0);
			this.union(sigma0a);
			for(int l = 0; l < sigma0a.inputs.size(); l++) {
				this.removeAsInput(sigma0a.inputs.get(l));
			}
			for(int l = 0; l < sigma0a.outputs.size(); l++) {
				this.removeAsOutput(sigma0a.outputs.get(l));
			}
			for(int l = 0; l < 32; l++) {
				this.fuse(a[l], sigma0a.inputs.get(l));
			}
			
			// Then, \Sigma_1(e)
			Circuit sigma1e = new Sigma(1);
			this.union(sigma1e);
			for(int l = 0; l < sigma1e.inputs.size(); l++) {
				this.removeAsInput(sigma1e.inputs.get(l));
			}
			for(int l = 0; l < sigma1e.outputs.size(); l++) {
				this.removeAsOutput(sigma1e.outputs.get(l));
			}
			for(int l = 0; l < 32; l++) {
				this.fuse(e[l], sigma1e.inputs.get(l));
			}

			// Then, W[j]
			if(j < 16) {
				for(int l = 0; l < 32; l++) {
					W[j][l] = this.inputs.get(j*32 + l);
				}
			}
			else {
				Circuit littlesigma1 = new Sigma(3);
				this.union(littlesigma1);
				for(int l = 0; l < littlesigma1.inputs.size(); l++) {
					this.removeAsInput(littlesigma1.inputs.get(l));
				}
				for(int l = 0; l < littlesigma1.outputs.size(); l++) {
					this.removeAsOutput(littlesigma1.outputs.get(l));
				}
				for(int l = 0; l < 32; l++) {
					this.fuse(W[j-2][l], littlesigma1.inputs.get(l));
				}
				
				Circuit littlesigma0 = new Sigma(2);
				this.union(littlesigma0);
				for(int l = 0; l < littlesigma0.inputs.size(); l++) {
					this.removeAsInput(littlesigma0.inputs.get(l));
				}
				for(int l = 0; l < littlesigma0.outputs.size(); l++) {
					this.removeAsOutput(littlesigma0.outputs.get(l));
				}
				for(int l = 0; l < 32; l++) {
					this.fuse(W[j-15][l], littlesigma0.inputs.get(l));
				}
				
				Circuit s[] = new Circuit[3];
				for(int l = 0; l < 3; l++) {
					s[l] = new SumTwoMod32(32, 32);
					this.union(s[l]);
					for(int p = 0; p < s[l].inputs.size(); p++) {
						this.removeAsInput(s[l].inputs.get(p));
					}
					for(int p = 0; p < s[l].outputs.size(); p++) {
						this.removeAsOutput(s[l].outputs.get(p));
					}
				}
				
				// Now wires for each s[] circuit
				for(int p = 0; p < 32; p++) { // indexes the input wires to each sum circuit
					// s[0]
					this.fuse(littlesigma1.outputs.get(p), s[0].inputs.get(p));
					this.fuse(W[j-7][p], s[0].inputs.get(32+p));
					
					// s[1]
					this.fuse(s[0].outputs.get(p), s[1].inputs.get(p));
					this.fuse(littlesigma0.outputs.get(p), s[1].inputs.get(32+p));
					
					// s[2]
					this.fuse(s[1].outputs.get(p), s[2].inputs.get(p));
					this.fuse(W[j-16][p], s[2].inputs.get(32+p));
					
					// Simultaneously instantiate this W[j]
					W[j][p] = s[2].outputs.get(p);
				}
			}
			
			// T1
			Circuit.Wire T[][] = new Circuit.Wire[2][32];
			Circuit T1circ[] = new Circuit[4];
			for(int l = 0; l < 4; l++) {
				T1circ[l] = new SumTwoMod32(32,32);
				this.union(T1circ[l]);
				for(int p = 0; p < T1circ[l].inputs.size(); p++) {
					this.removeAsInput(T1circ[l].inputs.get(p));
				}
				for(int p = 0; p < T1circ[l].outputs.size(); p++) {
					this.removeAsOutput(T1circ[l].outputs.get(p));
				}
			}
			
			for(int l = 0; l < 32; l++) {
				// First sum
				this.fuse(h[l], T1circ[0].inputs.get(l));
				this.fuse(sigma1e.outputs.get(l), T1circ[0].inputs.get(32+l));
				
				//2nd sum
				this.fuse(T1circ[0].outputs.get(l), T1circ[1].inputs.get(l));
				this.fuse(ch.outputs.get(l), T1circ[1].inputs.get(32+l));
				
				//3rd sum
				this.fuse(T1circ[1].outputs.get(l), T1circ[2].inputs.get(l));
				this.fuse(Kascirc[j].outputs.get(l), T1circ[2].inputs.get(32+l));
				
				//4th sum
				this.fuse(T1circ[2].outputs.get(l), T1circ[3].inputs.get(l));
				this.fuse(W[j][l], T1circ[3].inputs.get(32+l));
				
				// Record wires
				T[0][l] = T1circ[3].outputs.get(l);
			}
			
			// T2
			Circuit T2circ = new SumTwoMod32(32,32);
			this.union(T2circ);
			for(int l = 0; l < T2circ.inputs.size(); l++) {
				this.removeAsInput(T2circ.inputs.get(l));
			}
			for(int l = 0; l < T2circ.outputs.size(); l++) {
				this.removeAsOutput(T2circ.outputs.get(l));
			}
			
			for(int l = 0; l < 32; l++) {
				this.fuse(sigma0a.outputs.get(l), T2circ.inputs.get(l));
				this.fuse(maj.outputs.get(l), T2circ.inputs.get(32+l));
				
				T[1][l] = T2circ.outputs.get(l);
			}
			
			for(int l = 0; l < 32; l++) {
				h[l] = g[l];
				g[l] = f[l];
				f[l] = e[l];
			}
			
			// little circuit to update e
			Circuit ecirc = new SumTwoMod32(32,32);
			this.union(ecirc);
			for(int l = 0; l < ecirc.inputs.size(); l++) {
				this.removeAsInput(ecirc.inputs.get(l));
			}
			for(int l = 0; l < ecirc.outputs.size(); l++) {
				this.removeAsOutput(ecirc.outputs.get(l));
			}
			for(int l = 0; l < 32; l++) {
				this.fuse(d[l], ecirc.inputs.get(l));
				this.fuse(T[0][l], ecirc.inputs.get(32+l));
				
				e[l] = ecirc.outputs.get(l);
				
				// Also update d, c, b
				d[l] = c[l];
				c[l] = b[l];
				b[l] = a[l];
			}

			//little circuit to update a
			Circuit acirc = new SumTwoMod32(32, 32);
			this.union(acirc);
			for(int l = 0; l < acirc.inputs.size(); l++) {
				this.removeAsInput(acirc.inputs.get(l));
			}
			for(int l = 0; l < acirc.outputs.size(); l++) {
				this.removeAsOutput(acirc.outputs.get(l));
			}
			for(int l = 0; l < 32; l++) {
				this.fuse(T[0][l], acirc.inputs.get(l));
				this.fuse(T[1][l], acirc.inputs.get(32+l));
				
				a[l] = acirc.outputs.get(l);
			}
		}

		// Final sums
		Circuit s[] = new Circuit[8];
		for(int l = 0; l < 8; l++) {
			s[l] = new SumTwoMod32(32,32);
			this.union(s[l]);
			for(int p = 0; p < s[l].inputs.size(); p++) {
				this.removeAsInput(s[l].inputs.get(p));
			}
			
			// But outputs of the s[l]'s are exactly the outputs of this circuit, i.e., the compression function.
		}
		
		for(int l = 0; l < 32; l++) {
			// a
			this.fuse(a[l], s[0].inputs.get(l));
			this.fuse(this.inputs.get(512+32*0+l), s[0].inputs.get(32+l));
			
			// b
			this.fuse(b[l], s[1].inputs.get(l));
			this.fuse(this.inputs.get(512+32*1+l), s[1].inputs.get(32+l));

			// c
			this.fuse(c[l], s[2].inputs.get(l));
			this.fuse(this.inputs.get(512+32*2+l), s[2].inputs.get(32+l));

			// d
			this.fuse(d[l], s[3].inputs.get(l));
			this.fuse(this.inputs.get(512+32*3+l), s[3].inputs.get(32+l));
			
			// e
			this.fuse(e[l], s[4].inputs.get(l));
			this.fuse(this.inputs.get(512+32*4+l), s[4].inputs.get(32+l));

			// f
			this.fuse(f[l], s[5].inputs.get(l));
			this.fuse(this.inputs.get(512+32*5+l), s[5].inputs.get(32+l));

			// g
			this.fuse(g[l], s[6].inputs.get(l));
			this.fuse(this.inputs.get(512+32*6+l), s[6].inputs.get(32+l));

			// h
			this.fuse(h[l], s[7].inputs.get(l));
			this.fuse(this.inputs.get(512+32*7+l), s[7].inputs.get(32+l));
		}		
	}
}
