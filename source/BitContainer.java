/*
 * eline: Created BitContainer class
 */
/**
 * An object representing an array of bits, such as a register or byte
 */
public abstract class BitContainer extends BitProvider {
	/* Object data fields */
	protected int bits[];
	
	public BitContainer(int wordsize) {
		super(wordsize);

		bits = new int[wordsize];
		for (int index = 0; index < bits.length; index++) {
			bits[index] = 0;
		}
	}
	
	/**
	 * Increment this register by 1
	 */
	public void increment() {
		increment(1);
	}

	/**
	 * Increment this register by 1 a certain number of times
	 * @param times The number of times to increment the register by 1
	 */
	public void increment(int times) {
		for (int cnt = 0; cnt < times; cnt++) {
			int carry = 1;

			for (int inner_cnt = 0; inner_cnt < bitcount; inner_cnt++) {
				bits[inner_cnt] += carry;

				if (bits[inner_cnt] > 1) {
					bits[inner_cnt] = 0;
					carry = 1;
				} else {
					carry = 0;
				}
			}
		}
	}
	
	/**
	 * Negate the register (takes the ones-complement and increments)
	 */
	public void negate() {
		for (int cnt = 0; cnt < bitcount; cnt++) {
			if (bits[cnt] == 1) {
				bits[cnt] = 0;
			} else {
				bits[cnt] = 1;
			}
		}

		increment();
	}
	
	/**
	 * Get the value of the register
	 * @return Array of bits
	 */
	@Override
	public int[] getBits() {
		int[] bits = new int[bitcount];
		for (int index = 0; index < bitcount; index++) {
			bits[index] = this.bits[index];
		}
		return bits;
	}
	
	/**
	 * Update with a numerical value
	 * @param value The value
	 * @throws Exception
	 */
	public void store(int value) throws Exception {
		// System.err.println("--" + value + "--");
		if (value < 0 || value >= Math.pow(2, bitcount)) {
			throw new Exception("Passed value is too large for Byte (Should be 0 ≤ " + value + " < " + Math.pow(2, bitcount) + ")");
		}

		for (int index = 0; index < bits.length; index++) {
			bits[index] = value % 2;
			value = value / 2;
		}
	}
	
	/*
	 * eline: Generalized for all bases
	 */
	/**
	 * Update with a String value
	 * @param value The value
	 * @param base The base
	 * @throws Exception
	 */
	public void store(String value, int base) throws Exception {
		int int_value = Integer.parseInt(value, base);

		/*
		 * eline: changed to call existing function
		 */
		this.store(int_value);
	}
}
