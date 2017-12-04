/***
 * Bus class used for temporarily storing values between registers.
 */
public class Bus implements BitRepresenting {

	/* Object data fields */
	public int bits[];
	private int wordsize;

	/* Primary constructor */
	public Bus(int wordsize) {

		this.wordsize = wordsize;

		bits = new int[wordsize];

		for (int index = 0; index < bits.length; index++) {
			bits[index] = 0;
		}
	}

	public String hex() {

		int pow_value = 1;
		int value = 0;

		for (int index = 0; index < bits.length; index++) {

			if (bits[index] == 1) {
				value += pow_value;
			}
			pow_value *= 2;
		}

		return String.format("%02X", value);
	}

	public String binary() {
		String result = "";

		for (int index = (bits.length - 1); index >= 0; index--) {
			if (bits[index] == 1) {
				result += "1";
			} else {
				result += "0";
			}
		}

		return result;
	}

	/**
	 * Convert a range of the register to a binary string
	 * @param high The high bound
	 * @param low The low bound
	 * @return The binary representation of the range
	 * @throws Exception
	 */
	public String binary(int high, int low) throws Exception {
		String result = "";

		if (low > high) {
			throw new Exception("Binary range values should have a low " + "value less than or equal to the high");
		}

		for (int index = high; index >= low; index--) {
			if (bits[index] == 1) {
				result += "1";
			} else {
				result += "0";
			}
		}

		return result;
	}

	public int[] getBits() {
		int[] bits = new int[wordsize];
		for (int index = 0; index < wordsize; index++) {
			bits[index] = this.bits[index];
		}
		return bits;
	}
	
	public void store(int value) throws Exception {

		// System.err.println("--" + value + "--");
		if (value < 0 && 255 < value) {
			throw new Exception("Passed value is too large for Bus");
		}

		for (int index = 0; index < bits.length; index++) {
			bits[index] = value % 2;
			value = value / 2;
		}
	}

	public void store(String value) throws Exception {

		// System.err.println("--" + value + "--");
		if (value.length() != 2) {
			throw new Exception("Passed value is not the right length for Bus");
		}

		int int_value = Integer.parseInt(value, 16);

		for (int index = 0; index < bits.length; index++) {
			bits[index] = int_value % 2;
			int_value = int_value / 2;
		}
	}

	public static void main(String args[]) {

		Bus a = new Bus(32);

		try {
			a.store(0xAAFF);

			System.out.println(a.hex());
			System.out.println(a.binary());
			System.out.println(a.binary(3, 0));
			System.out.println(a.binary(2, 0));
			System.out.println(a.binary(18, 13));
			System.out.println(a.binary(13, 13));
			System.out.println(a.binary(14, 14));
			System.out.println(a.binary(15, 15));
			System.out.println(a.binary(16, 16));
			System.out.println(a.binary(17, 17));
			System.out.println(a.binary(18, 18));

		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
