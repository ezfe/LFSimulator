/*
 * Changes in this file:
 * var source
 * func set_source
 * func set_destination
 */
/**
 * Register class for implementing memory components
 * that hang together thru the bus.
 */
public class Register implements BitRepresenting {

	/* Object data fields */
	private int bits[];
	private int wordsize;

	/*
	 * eline: Changed to a BitRepresenting object
	 */
	private BitRepresenting source;
	private Bus destination;

	/**
	 * Create a Register
	 * 
	 * Initializes to zeroes
	 * 
	 * @param wordsize The wordsize of the register
	 */
	public Register(int wordsize) {
		this.wordsize = wordsize;

		bits = new int[wordsize];
		for (int index = 0; index < bits.length; index++) {
			bits[index] = 0;
		}
	}

	/*
	 * eline: Renamed to set_source, changed parameter to BitRepresenting (from Bus)
	 */
	/**
	 * Set the source of the register
	 * @param bus The source
	 */
	public void set_source(BitRepresenting source) {
		this.source = source;
	}

	/*
	 * eline: Renamed to set_destination (from set_destination_bus)
	 */
	/**
	 * Set the destination bus of the register
	 * @param bus The destination bus
	 */
	public void set_destination(Bus bus) {
		destination = bus;
	}

	/**
	 * Load the source into this register
	 */
	public void load() {
		int[] srcBits = source.getBits();
		for (int cnt = 0; cnt < wordsize; cnt++) {
			bits[cnt] = srcBits[cnt];
		}
	}

	/**
	 * Store this register into the destination
	 */
	public void store() throws Exception {
		if (destination == null) {
			throw new Exception("There is no destination object");
		}
		
		for (int cnt = 0; cnt < wordsize; cnt++) {
			destination.bits[cnt] = bits[cnt];
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

			for (int inner_cnt = 0; inner_cnt < wordsize; inner_cnt++) {
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
		for (int cnt = 0; cnt < wordsize; cnt++) {
			if (bits[cnt] == 1) {
				bits[cnt] = 0;
			} else {
				bits[cnt] = 1;
			}
		}

		increment();
	}

	/**
	 * Convert the register to an integer
	 * @return The integer representation of the register
	 */
	public int decimal() {
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
		
		int pow_value = 1;
		int value = 0;

		for (int index = low; index <= high; index++) {
			value += bits[index] * pow_value;
			pow_value *= 2;
		}

		return value;
	}

	/**
	 * Convert to a hex string
	 * @return The hex representation
	 */
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

	/**
	 * Convert to a binary string
	 * @return The binary representation
	 */
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
	
	/**
	 * Get the value of the register
	 * @return Array of bits
	 */
	public int[] getBits() {
		int[] bits = new int[wordsize];
		for (int index = 0; index < wordsize; index++) {
			bits[index] = this.bits[index];
		}
		return bits;
	}

	/**
	 * Store a decimal value in the register
	 * @param value The integer value to store
	 */
	public void store(int value) {
		for (int index = 0; index < bits.length; index++) {
			bits[index] = value % 2;
			value = value / 2;
		}
	}

	/**
	 * Store a hex value in the register
	 * @param value The hex string to store
	 */
	public void store(String value) {
		int int_value = Integer.parseInt(value, 16);

		for (int index = 0; index < bits.length; index++) {
			bits[index] = int_value % 2;
			int_value = int_value / 2;
		}
	}

	public static void main(String args[]) {

		/* Examples of usage. */
		Register a = new Register(64);

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

			a.increment();
			System.out.println(a.binary());
			a.increment();
			System.out.println(a.binary());
			a.increment();
			System.out.println(a.binary());
			a.increment();
			System.out.println(a.binary());
			a.increment();
			System.out.println(a.binary());
			a.increment();
			System.out.println(a.binary());
			a.negate();
			System.out.println(a.binary());

		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
