/*
 * eline: Created the ALU
 */
/**
 * Arithmetic Logical Unit
 */
public class ALU extends BitRepresenting {
	
	public enum Operation {
		ADD, SUBTRACT, AND, OR
	}
	
//	private int control[]
	
	private BitRepresenting source_a;
	private BitRepresenting source_b;

	private Operation operation = Operation.ADD;
	
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
	public void set_source_a(BitRepresenting source_a) {
		this.source_a = source_a;
	}
	
	/**
	 * Set the right hand source
	 * @param source_b The right hand source
	 */
	public void set_source_b(BitRepresenting source_b) {
		this.source_b = source_b;
	}
	
	/**
	 * Set the ALU operation
	 * @param operation The operation
	 */
	public void set_operation(Operation operation) {
		this.operation = operation;
	}
	
	@Override
	public int[] getBits() {
		int[] a = source_a.getBits();
		int[] b = source_b.getBits();
		
		switch (this.operation) {
		case ADD: {
			return this.add(a, b, 0);
		}
		case SUBTRACT: {
			return this.subtract(a, b);
		}
		
		default: {
			return a;
		}
		}
	}
	
	/**
	 * Subtract two numbers
	 * @param a The first (left) number
	 * @param b The second (right) number
	 * @return The result
	 */
	private int[] subtract(int[] a, int[] b) {
		int[] b_not = new int[b.length];
		for (int index = 0; index < b.length; index++) {
			b_not[index] = (b[index] == 0) ? 1 : 0;
		}
		
		return add(a, b_not, 1);
	}
	
	/**
	 * Add two numbers
	 * @param a The first number
	 * @param b The second number
	 * @param carry The carry-in bit
	 * @return The result
	 */
	private int[] add(int[] a, int[] b, int carry) {
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