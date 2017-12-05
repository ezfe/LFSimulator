/***
 * Bus class used for temporarily storing values between registers.
 */
public class Bus extends BitContainer {

	/*
	 * Moved data fields to BitContainer abstract class
	 */

	/* Primary constructor */
	public Bus(int wordsize) {
		/*
		 * Moved construction to BitContainer abstract class
		 */
		super(wordsize);
	}
	
	/*
	 * eline: Moved common functions to BitContainer abstract class
	 */

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
