/*
 * Changes in this file:
 * 
 * - Changed some method names
 * - Removed unused functions
 */

/**
 * Class implementing a collection of registers. Source and destination
 * selection is implemented here to control how the bus is manipulated by this
 * device.
 */
public class RegisterBank {
	/* Object data fields */
	private Register registers[];

	private int wordsize;
	private int register_cnt;

	/* Primary constructor */
	public RegisterBank(int wordSize, int registerCnt) {
		wordsize = wordSize;
		register_cnt = registerCnt;

		registers = new Register[register_cnt];

		for (int index = 0; index < register_cnt; index++) {
			registers[index] = new Register(wordsize);
		}
	}

	/*
	 * eline: Renamed to set_source (from set_source_bus) and changed parameter to BitProvider (from Bus)
	 */
	/**
	 * Set the source for all registers in the bank
	 * @param source The source
	 */
	public void set_source(BitProvider source) {
		for (int index = 0; index < register_cnt; index++) {
			registers[index].set_source(source);
		}
	}

	/*
	 * eline: Renamed to set_destination (from set_destination_bus) and changed parameter to BitContainer (from Bus)
	 */
	/**
	 * Set the destination for all registers in the bank
	 * @param dest The destination
	 */
	public void set_destination(BitContainer dest) {
		for (int index = 0; index < register_cnt; index++) {
			registers[index].set_destination(dest);
		}
	}

	/**
	 * Load the source into a specified register
	 * @param register_id The register
	 * @throws Exception
	 */
	public void load(int register_id) throws Exception {
		if (register_id < 0 || register_cnt <= register_id) {
			throw new Exception("RegisterBank binary register_id out of range.");
		}

		registers[register_id].load();
	}

	/**
	 * Store the specified register value into the destination
	 * @param register_id THe register
	 * @throws Exception
	 */
	public void store(int register_id) throws Exception {
		if (register_id < 0 || register_cnt <= register_id) {
			throw new Exception("RegisterBank binary register_id out of range.");
		}

		registers[register_id].store();
	}

	/*
	 * eline: Removed increment function
	 */

	/*
	 * eline: Removed negate function
	 */
	
	/**
	 * Convert the specified register to a binary string
	 * @param register_id The register
	 * @return The binary representation
	 * @throws Exception
	 */
	public String binary(int register_id) throws Exception {
		if (register_id < 0 || register_cnt <= register_id) {
			throw new Exception("RegisterBank binary register_id out of range.");
		}

		return registers[register_id].binary();
	}

	/**
	 * Convert the specified register to a hex string
	 * @param register_id The register
	 * @return The hex representation
	 * @throws Exception
	 */
	public String hex(int register_id) throws Exception {
		if (register_id < 0 || register_cnt <= register_id) {
			throw new Exception("RegisterBank binary register_id out of range.");
		}

		return registers[register_id].hex();
	}
	
	/**
	 * Get decimal value of a register
	 * @param register_id The register 
	 * @return The decimal value
	 */
	public int decimal(int register_id) throws Exception {
		if (register_id < 0 || register_cnt <= register_id) {
			throw new Exception("RegisterBank binary register_id out of range.");
		}
		
		return registers[register_id].decimal();
	}

	/**
	 * Store a decimal value in the specified register
	 * @param value The value
	 * @param register_id The register
	 * @throws Exception
	 */
	public void store(int value, int register_id) throws Exception {
		if (register_id < 0 || register_cnt <= register_id) {
			throw new Exception("RegisterBank store(int) register_id out of range.");
		}

		registers[register_id].store(value);
	}

	/**
	 * Store a hex value in the specified register
	 * @param value The hex string
	 * @param register_id The register
	 * @throws Exception
	 */
	public void store(String value, int register_id) throws Exception {
		if (register_id < 0 || register_cnt <= register_id) {
			throw new Exception("RegisterBank store(str) register_id out of range.");
		}

		registers[register_id].store(value, 16);
	}

	public static void main(String args[]) {

		/*** Examples of usage. ***/
		RegisterBank a = new RegisterBank(32, 8);

		try {

			for (int cnt = 0; cnt < 8; cnt++) {
				System.err.println(String.format("Reg 0x%02X: %s", cnt, a.binary(cnt)));
			}

			System.err.println("------------------------------------------");
			a.store(0x4A5, 0);
			a.store(0xFFFFFF, 1);
			a.store(0x38D, 2);
			a.store(0x3AB3, 3);

			for (int cnt = 0; cnt < 8; cnt++) {
				System.err.println(String.format("Reg 0x%02X: %s", cnt, a.binary(cnt)));
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
