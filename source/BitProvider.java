/*
 * Changes in this file:
 * 
 * - This file is new
 */

/*
 * eline: Created BitProvider class
 */
/**
 * Any class which can provide an array of bits
 * 
 * A class which extends this class need not actually store the bits
 */
public abstract class BitProvider {
	public final int bitcount;

	public BitProvider(int bitcount) {
		this.bitcount = bitcount;
	}
	
	/**
	 * Get the value of the register
	 * @return Array of bits
	 */
	public abstract int[] getBits();
	
	/**
	 * Convert to a hex string
	 * @return The hex representation
	 */
	public String hex() {
		int[] bits = this.getBits();
		
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
	
	/**
	 * Convert the register to an integer
	 * @return The integer representation of the register
	 */
	public int decimal() {
		int[] bits = this.getBits();

		int pow_value = 1;
		int value = 0;

		for (int index = 0; index < bits.length; index++) {
			if (bits[index] == 1) {
				value += pow_value;
			}
			pow_value *= 2;
		}

		return value;
	}

	/**
	 * Convert a range of the register to decimal
	 * @param high The high bound
	 * @param low The low bound
	 * @return The integer representation of the range
	 * @throws Exception
	 */
	public int decimal(int high, int low) throws Exception {
		if (low > high) {
			throw new Exception("Binary range values should have a low value less than or equal to the high");
		}
		
		int[] bits = this.getBits();
		
		int pow_value = 1;
		int value = 0;

		for (int index = low; index <= high; index++) {
			value += bits[index] * pow_value;
			pow_value *= 2;
		}

		return value;
	}
	
	/**
	 * Convert to a binary string
	 * @return The binary representation
	 */
	public String binary() {
		int[] bits = this.getBits();

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
		int[] bits = this.getBits();

		String result = "";

		if (low > high) {
			throw new Exception("Binary range values should have a low value less than or equal to the high");
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
}
