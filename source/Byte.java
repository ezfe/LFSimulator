/***
 * Low-level class for storing a single byte of information, 8-bits.
 */
public class Byte implements BitRepresenting {

	/** Object data fields */
	private int bits[];

	/** Primary constructor */
	public Byte() {
		bits = new int[8];

		for (int index = 0; index < bits.length; index++) {
			bits[index] = 0;
		}
	}

	/**
	 * Convert to a hex string
	 * @return The hex representation
	 */
	public String hex() {
		int pow_value = 1;
		int value = 0;

		for (int index = 0; index < 8; index++) {
			if (bits[index] == 1) {
				value += pow_value;
			}
			pow_value *= 2;
		}

		return String.format("%02X", value);
	}

	/**
	 * Convert to binary string
	 * @return The binary representation
	 */
	public String binary() {
		String result = "";

		for (int index = 7; index >= 0; index--) {
			if (bits[index] == 1) {
				result += "1";
			} else {
				result += "0";
			}
		}

		return result;
	}

	/**
	 * Convert a range of the byte to a binary string
	 * @param high The high bound
	 * @param low The low bound
	 * @return The binary representation of the range
	 */
	public String binary(int high, int low) {
		String result = "";

		for (int index = high; index >= low; index--) {
			if (bits[index] == 1) {
				result += "1";
			} else {
				result += "0";
			}
		}

		return result;
	}
	
	/**
	 * Get the value of the register
	 * @return Array of bits
	 */
	public int[] getBits() {
		int[] bits = new int[8];
		for (int index = 0; index < 8; index++) {
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
		if (value < 0 || 255 < value) {
			throw new Exception("Passed value is too large for Byte (" + value + ", should be [0, 255])");
		}

		for (int index = 0; index < 8; index++) {
			bits[index] = value % 2;
			value = value / 2;
		}
	}

	/*
	 * eline: Renamed to storeHex(value:) for clarity
	 */
	
	/**
	 * Update with a 
	 * @param value
	 * @throws Exception
	 */
	public void storeHex(String value) throws Exception {
		// System.err.println("--" + value + "--");
		if (value.length() != 2) {
			throw new Exception("Passed value is not the right length for Byte");
		}

		int int_value = Integer.parseInt(value, 16);

		/*
		 * eline: changed to call existing function
		 */
		this.store(int_value);
	}

	public static void main(String args[]) {
		/* Examples of usage. */
		Byte a = new Byte();

		try {
			a.store(0xAA);

			System.out.println(a.hex());
			System.out.println(a.binary());
			System.out.println(a.binary(3, 0));
			a.store(5);
			System.out.println(a.binary(2, 0));

			a.storeHex("AB");
			System.out.println(a.binary());

			a.storeHex("D9");
			System.out.println(a.binary());

			a.storeHex("00");
			System.out.println(a.hex());
			System.out.println(a.binary());

		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
