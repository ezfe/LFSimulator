/**
 * The class that initiates all other objects.
 */
public class Simulation {

	/* Object data fields */
	public static RAM mem;

	/*
	 * eline: Changed to 32 bit registers, 32 general purpose registers
	 */
	public static final int registerCount = 32;
	public static final int wordSize = 32;
	public static final int memorySize = 32;
	
	public static void main(String[] args) {

		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mem = RAM.createAndShowGUI(memorySize, wordSize);
			}
		});
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CPU.createAndShowGUI(wordSize, registerCount, mem);
			}
		});
	}
}
