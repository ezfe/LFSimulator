/*
 * TODO: Changes in this file
 */
/**
 * Low-level class for storing a single byte of information, 8-bits.
 */
public class Byte extends BitContainer {

	/*
	 * Moved data fields to BitContainer abstract class
	 */
	
	/**
	 * Create a Byte
	 */
	public Byte() {
		/*
		 * Moved construction to BitContainer abstract class
		 */
		super(8);
	}

	/*
	 * eline: Moved common functions to BitContainer abstract class
	 */

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
