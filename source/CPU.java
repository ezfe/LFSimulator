import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/***
 * CPU simulation class, including visual representation.
 */
public class CPU extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1095864483548437843L;

	/* Object data fields */
	protected JButton button_refresh;
	protected JButton button_clock_tick;
	protected JButton button_reset;

	protected JTextArea textArea;
	private final static String newline = "\n";

	/**
	 * The program counter holds the address to the next instruction in memory
	 */
	public Register PC;
	/**
	 * The instruction register holds the value of the current instruction
	 */
	public Register IR;
	/**
	 * The memory address register contains the address to access in or store to memory
	 */
	public Register MA;
	/**
	 * The memory data register contains the data accessed or to be stored in memory
	 */
	public Register MD;
	
	/**
	 * The b-register holds temporary data for the ALU
	 */
	public Register B;
	/**
	 * The c-register holds result data from the ALU
	 */
	public Register C;
	
	/**
	 * Register Bank
	 */
	public RegisterBank bank;
	
	/**
	 * Master Bus
	 */
	public Bus master_bus;

	/**
	 * The Arithmetic Logical Unit
	 */
	public ALU alu;
	
	private int clock_ticks;
	private int general_purpose_reg_cnt;
	private int wordsize;

	public RAM main_memory;
	private Controller controler;

	/* Primary constructor */
	public CPU(int wordSize, int register_cnt, RAM mem) {

		super(new GridBagLayout());

		button_refresh = new JButton("Refresh");
		button_refresh.addActionListener(this);
		button_clock_tick = new JButton("Increment Clock");
		button_clock_tick.addActionListener(this);
		button_reset = new JButton("Reset");
		button_reset.addActionListener(this);

		textArea = new JTextArea(30, 30);
		textArea.setFont(new Font("Courier", Font.PLAIN, 16));
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);

		// Add Components to this panel.
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;

		c.fill = GridBagConstraints.HORIZONTAL;
		add(button_refresh, c);
		add(button_clock_tick, c);
		add(button_reset, c);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(scrollPane, c);

		/* --- Build the machine here. --- */
		general_purpose_reg_cnt = register_cnt;
		wordsize = wordSize;
		main_memory = mem;
		controler = new Controller(this);

		master_bus = new Bus(wordsize);

		PC = new Register(wordsize);
		IR = new Register(wordsize);
		MA = new Register(wordsize);
		MD = new Register(wordsize);

		B = new Register(wordsize);
		C = new Register(wordsize);
		
		bank = new RegisterBank(wordsize, general_purpose_reg_cnt);

		bank.set_source_bus(master_bus);
		bank.set_destination_bus(master_bus);

		PC.set_source(master_bus);
		PC.set_destination(master_bus);
		IR.set_source(master_bus);
		IR.set_destination(master_bus);
		MA.set_source(master_bus);
		MA.set_destination(master_bus);
		MD.set_source(master_bus);
		MD.set_destination(master_bus);

		main_memory.set_address_register(MA);
		main_memory.set_data_register(MD);

		alu = new ALU(wordsize);
		alu.set_source_a(master_bus);
		alu.set_source_b(B);
		
		B.set_source(master_bus);
		B.set_destination(null);
		
		C.set_source(alu);
		C.set_destination(master_bus);
		
		reset(); // clear everything to zero
		refresh_display(); // redraw the display
	}

	public void actionPerformed(ActionEvent evt) {

		if (evt.getActionCommand().equals("Increment Clock")) {
			increment_clock();
		} else if (evt.getActionCommand().equals("Reset")) {
			reset();
		} else if (evt.getActionCommand().equals("Refresh")) {
			refresh_display();
		} else {
			System.err.println("Unknown action (CPU.java): " + evt.getActionCommand());
		}
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked
	 * from the event dispatch thread.
	 */
	public static void createAndShowGUI(int wordSize, int register_cnt, RAM mem) {
		// Create and set up the window.
		JFrame frame = new JFrame("CPU");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add contents to the window.
		frame.add(new CPU(wordSize, register_cnt, mem));

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public void increment_clock() {
		clock_ticks++;
		controler.increment_clock();
		refresh_display();
		main_memory.refresh_display();
	}

	/***
	 * Clears all the registers.
	 */
	public void reset() {

		try {

			clock_ticks = 0;

			PC.store(0x0);
			IR.store(0x0);
			MA.store(0x0);
			MD.store(0x0);

			for (int cnt = 0; cnt < general_purpose_reg_cnt; cnt++) {
				bank.store(0x0, cnt);
			}

			controler.reset();

		} catch (Exception e) {

			System.err.println("In CPU:reset");
			System.err.println(e);
		}

	}

	public void refresh_display() {

		// textArea.setText(""); // clears the text
		textArea.setText(build_display()); // stores memory as a string to widget
	}

	public String build_display() {
		int offset = 0;
		String result = "";

		result += "Wordsize   : " + wordsize;
		result += "Reg Count  : " + general_purpose_reg_cnt + newline;
		result += "Clock Ticks: " + clock_ticks + newline;
		result += "Curr RTN   : " + controler.current_rtn() + newline;

		result += newline;
		result += "Bus: " + master_bus.binary() + newline;

		result += newline;
		result += "IR : " + IR.binary() + newline;
		result += "PC : " + PC.binary() + newline;
		result += newline;
		result += "MA : " + MA.binary() + newline;
		result += "MD : " + MD.binary() + newline;
		result += newline;

		result += newline + "----- Register Bank -----" + newline;
		try {
			for (int cnt = 0; cnt < 8; cnt++) {
				result += String.format("%02d: %s", cnt, bank.binary(cnt)) + newline;
			}
		} catch (Exception e) {
			System.err.println("In CPU:build_display");
			System.err.println(e);
		}

		return result;
	}

}
