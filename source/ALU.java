/*
 * eline: Created the ALU
 */
/**
 * Arithmetic Logical Unit
 */
public class ALU extends BitRepresenting {
//	private int control[]
	
	private int wordsize;
	
	private BitContainer source_a;
	private BitContainer source_b;
	private Register destination;
//	private flags??
	
	/**
	 * Create an ALU
	 * @param wordsize The wordsize of the ALU
	 */
	public ALU(int wordsize) {
		super(wordsize);
	}
	
	/**
	 * Set the left hand source
	 * @param source_a The left hand source
	 */
	public void set_source_a(BitContainer source_a) {
		this.source_a = source_a;
	}
	
	/**
	 * Set the right hand source
	 * @param source_b The right hand source
	 */
	public void set_source_b(BitContainer source_b) {
		this.source_b = source_b;
	}

	/**
	 * Set the destination register
	 * @param destination The destination register
	 */
	public void set_destination(Register destination) {
		this.destination = destination;
	}
	
	@Override
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