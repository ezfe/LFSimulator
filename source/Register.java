/*
 * TODO: Changes in this file
 */
/**
 * Register class for implementing memory components
 * that hang together thru the bus.
 */
public class Register extends BitContainer {
	/*
	 * Moved data fields to BitContainer abstract class
	 */
	
	/*
	 * eline: Changed to a BitContainer object
	 */
	private BitRepresenting source;
	private BitContainer destination;

	/**
	 * Create the Register
	 * @param wordsize The wordsize of the register
	 */
	public Register(int wordsize) {
		/*
		 * Moved construction to BitContainer abstract class
		 */
		super(wordsize);
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
	 * eline: Renamed to set_destination, changed parameter to BitContainer (from Bus)
	 */
	/**
	 * Set the destination bus of the register
	 * @param bus The destination
	 */
	public void set_destination(BitContainer destination) {
		this.destination = destination;
	}

	/**
	 * Load the source into this register
	 */
	public void load() {
		int[] srcBits = source.getBits();
		for (int cnt = 0; cnt < bitcount; cnt++) {
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
		
		for (int cnt = 0; cnt < bitcount; cnt++) {
			destination.bits[cnt] = bits[cnt];
		}
	}
	
	/*
	 * eline: Moved common functions to BitContainer abstract class
	 */

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
