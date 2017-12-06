/***
  * The class that initiates all other objects.
  */
public class Simulation {

  /* Object data fields */
  public static RAM mem;

  public static void main(String[] args) {
    
    //Schedule a job for the event dispatch thread:
    //creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            mem = RAM.createAndShowGUI(32, 0x2);
        }
    });
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
        		/*
        		 * eline: Changed to 32 bit registers, 32 general purpose registers
        		 */
            CPU.createAndShowGUI(32, 32, mem);
        }
    });
  }
}

