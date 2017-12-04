/**
 * Arithmetic Logical Unit
 * 
 * eline: Created the Arithmetic Logical Unit
 */
public class ALU implements BitRepresenting {
//	private int control[]
	
	private int wordsize;
	
	private Bus source_a;
	private Register source_b;
	private Register destination;
//	private flags??
	
	public ALU(int wordsize) {
		this.wordsize = wordsize;
	}
	
	public void set_source_a(Bus source_a) {
		this.source_a = source_a;
	}
	
	public void set_source_b(Register source_b) {
		this.source_b = source_b;
	}

	public void set_destination(Register destination) {
		this.destination = destination;
	}
	
	public int[] getBits() {
		return this.add(0);
	}
	
	/**
	 * Add the two sources together
	 * @param carry A carry-in bit (must be 1 or 0)
	 * @return The resulting binary value
	 */
	private int[] add(int carry) {
		int[] a = source_a.getBits();
		int[] b = source_b.getBits();
		
		int[] res = new int[wordsize];
		for (int index = 0; index < res.length; index++) {
			res[index] = a[index] + b[index] + carry;
			
			if (res[index] == 3) {
				carry = 1;
				res[index] = 1;
			} else if (res[index] == 2) {
				carry = 1;
				res[index] = 0;
			} else {
				carry = 0;
			}
		}
		
		//TODO: Carry-out
		return res;
	}
}