/*
 * Changes in this file:
 * 
 * - This file is new
 */

/*
 * eline: Created ALU class
 */
/**
 * Arithmetic Logical Unit
 */
public class ALU extends BitProvider {
	
	public enum Operation {
		ADD, SUBTRACT, AND, OR, XOR
	}
	
	private BitProvider source_a;
	private BitProvider source_b;

	private Operation operation = Operation.ADD;
	private boolean isSettingFlags = false;
	
	public BitContainer negativeFlag;
	public BitContainer zeroFlag;
	public BitContainer carryFlag;
	public BitContainer overflowFlag;
	
	/**
	 * Create an ALU
	 * @param wordsize The wordsize of the ALU
	 */
	public ALU(int wordsize) {
		super(wordsize);
		
		this.negativeFlag = new Register(1);
		this.zeroFlag = new Register(1);
		this.carryFlag = new Register(1);
		this.overflowFlag = new Register(1);
	}
	
	/**
	 * Set the left hand source
	 * @param source_a The left hand source
	 */
	public void set_source_a(BitProvider source_a) {
		this.source_a = source_a;
	}
	
	/**
	 * Set the right hand source
	 * @param source_b The right hand source
	 */
	public void set_source_b(BitProvider source_b) {
		this.source_b = source_b;
	}
	
	/**
	 * Get the ALU operation
	 * @return The operation
	 */
	public Operation get_operation() {
		return this.operation;
	}
	
	/**
	 * Set the ALU operation
	 * @param operation The operation
	 */
	public void set_operation(Operation operation) {
		this.operation = operation;
	}
	
	/**
	 * Get whether the ALU is setting flags
	 * @return  Whether the ALU is setting flags
	 */
	public boolean get_isSettingFlags() {
		return this.isSettingFlags;
	}
	
	/**
	 * Set whether the ALU is setting flags
	 * @param isf Whether the ALU should set flags
	 */
	public void set_isSettingFlags(boolean isf) {
		this.isSettingFlags = isf;
	}
	
	@Override
	public int[] getBits() {
		int[] a = source_a.getBits();
		int[] b = source_b.getBits();
		
		switch (this.operation) {
		case ADD: {
			return add(a, b, 0);
		}
		case SUBTRACT: {
			return subtract(a, b);
		}
		case AND: {
			return and(a, b);
		}
		case OR: {
			return or(a, b);
		}
		case XOR: {
			return xor(a, b);
		}
		}
		
		System.err.println("Escaped switch block in ALU getBits for operation " + this.operation.name());
		return a;
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
		int[] res = add(a, b_not, 1);
		
		if (this.isSettingFlags) {
			try {
				final int a_sign = a[a.length - 1];
				final int b_sign = b[b.length - 1];
				final int res_sign = res[res.length - 1];
				
				// Negative Flag is set in add()
				// Zero Flag is set in add()
				
				// Store overflow if `-` - `+` = `+` OR `+` - `-` = `-`
				boolean overflow = (a_sign != b_sign) && (b_sign == res_sign);
				this.overflowFlag.store(overflow ? 1 : 0);
				
				// Carry Flag is set in add()
			} catch (Exception e) {
				System.err.println("Error occurred setting flags (ALU:sub)");
				e.printStackTrace();
			}
		}
		
		return res;
	}
	
	/**
	 * Add two numbers
	 * @param a The first number
	 * @param b The second number
	 * @param carry The carry-in bit
	 * @return The result
	 */
	private int[] add(int[] a, int[] b, int carry) {
		int[] res = new int[a.length];
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
		
		if (this.isSettingFlags) {
			try {
				final int a_sign = a[a.length - 1];
				final int b_sign = b[b.length - 1];
				final int res_sign = res[res.length - 1];
								
				// Store the sign bit of the result in the negative flag
				this.negativeFlag.store(res_sign);
				
				// Set the zero flag to TRUE
				this.zeroFlag.store(1);
				// Then check for non-zero bits, and set to FALSE
				for (int i = 0; i < res.length; i++) {
					if (res[i] != 0) {
						this.zeroFlag.store(0);
						break;
					}
				}
				
				// Store overflow if `+` - `+` = `-` OR `+` - `-` = `+`
				boolean overflow = (a_sign == b_sign) && (b_sign != res_sign);
				this.overflowFlag.store(overflow ? 1 : 0);
				
				// Store the last carry bit (the carry-out) in the carry flag
				this.carryFlag.store(carry);
			} catch (Exception e) {
				System.err.println("Error occurred setting flags (ALU:add)");
				e.printStackTrace();
			}
		}
		
		return res;
	}
	
	/**
	 * AND two numbers
	 * @param a The first number
	 * @param b The second number
	 * @return The result
	 */
	private int[] and(int[] a, int[] b) {
		int[] res = new int[a.length];
		for(int index = 0; index < res.length; index++) {
			res[index] = (a[index] + b[index] == 2) ? 1 : 0;
		}
		return res;
	}
	
	/**
	 * OR two numbers
	 * @param a The first number
	 * @param b The second number
	 * @return The result
	 */
	private int[] or(int[] a, int[] b) {
		int[] res = new int[a.length];
		for(int index = 0; index < res.length; index++) {
			res[index] = (a[index] + b[index] >= 1) ? 1 : 0;
		}
		return res;
	}
	
	/**
	 * XOR two numbers
	 * @param a The first number
	 * @param b The second number
	 * @return The result
	 */
	private int[] xor(int[] a, int[] b) {
		int[] res = new int[a.length];
		for(int index = 0; index < res.length; index++) {
			res[index] = (a[index] + b[index] == 1) ? 1 : 0;
		}
		return res;
	}
}