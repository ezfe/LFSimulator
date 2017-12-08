
/*
 * Changes in this file:
 * 
 * - Changed some variables for display and processing logic
 * - Revised ram display techniques
 * - Revised word loading logic
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/***
 * Memory simulation, including visual representation.
 */
public class RAM extends JPanel implements ActionListener {

	/*
	 * eline: Added serialVersionUID variable to remove warnings
	 */
	private static final long serialVersionUID = -7683041314100735717L;

	/* Object data fields */
	protected JButton button;

	protected JTextArea textArea;
	private final static String newline = "\n";

	/**
	 * The bytes of memory in the RAM
	 */
	private Byte memory[];
	/*
	 * eline: Removed size variable
	 */
	/*
	 * eline: Added wordsize variable
	 */
	/**
	 * The wordsize of the CPU the RAM is a part of
	 * 
	 * Affects loading and storing words to RAM
	 */
	private int wordsize;
	/*
	 * eline: Removed byte_width variable
	 */
	/**
	 * The last Byte accessed
	 * 
	 * Affects the arrow in the UI indicating the current location
	 */
	private int last_access;

	/*
	 * eline: Changed to BitProvider (from Register)
	 */
	/**
	 * The address register
	 * 
	 * Holds the byte address to load from or store to
	 */
	private BitProvider address_register;

	/*
	 * eline: Changed to BitContainer (from Register)
	 */
	/**
	 * The data register
	 * 
	 * Holds the data to store or just loaded
	 */
	private BitContainer data_register;

	/*
	 * eline: Changed parameters of RAM constructor
	 */
	/**
	 * Create a RAM object
	 * 
	 * @param numberOfBytes
	 *            The number of bytes the RAM holds
	 * @param wordsize
	 *            The machine wordsize
	 */
	public RAM(int numberOfBytes, int wordsize) {

		super(new GridBagLayout());

		button = new JButton("Refresh");
		button.addActionListener(this);

		textArea = new JTextArea(30, 70);
		textArea.setFont(new Font("Courier", Font.PLAIN, 16));
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);

		// Add Components to this panel.
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;

		c.fill = GridBagConstraints.HORIZONTAL;
		add(button, c);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(scrollPane, c);

		/*
		 * eline: Removed assignment for size, byte_width (as variables are removed)
		 */

		/*
		 * eline: Added assignment for wordsize
		 */
		this.wordsize = wordsize;
		last_access = 0;

		memory = new Byte[numberOfBytes]; // construct memory

		for (int index = 0; index < memory.length; index++) {
			memory[index] = new Byte();
		}

		load_memory("test_file.o"); // load memory file
		
		refresh_display(); // redraw the display
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("Refresh")) {
			refresh_display();
		} else {
			System.err.println("Unknown action (RAM.java): " + evt.getActionCommand());
		}
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked
	 * from the event dispatch thread.
	 */
	public static RAM createAndShowGUI(int numberOfBytes, int wordsize) {
		// Create and set up the window.
		JFrame frame = new JFrame("RAM");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add contents to the window.
		RAM mem = new RAM(numberOfBytes, wordsize);
		frame.add(mem);

		// Display the window.
		frame.pack();
		frame.setVisible(true);

		return mem;
	}

	/**
	 * Load memory from a file
	 * 
	 * @param file_name
	 *            The path to the file
	 */
	public void load_memory(String file_name) {
		Scanner sc = null; // initialize and create scanner mechanism
		String curr_line;

		/*
		 * eline: Added variables tracking wordsize and other information
		 */
		final int bytesPerWord = wordsize / 8;
		int charsPerByte = 2;
		int base = 16;

		try {
			sc = new Scanner(new File(file_name));
		} catch (FileNotFoundException e) {
			System.err.println("Error in load_memory");
			System.err.println(e);
		}

		// load file into memory
		try {
			/*
			 * eline: Changed default line_cnt. This will be set to zero before loading any
			 * data, below
			 */
			int line_cnt = -1;

			while (sc.hasNextLine()) {
				curr_line = sc.nextLine(); // get next line

				/*
				 * eline: Allow base to be configured by setting first line of input file to
				 * `binary` or `hexadecimal` (defaults to hexadecimal when neither is specified)
				 */
				if (line_cnt == -1) {
					/*
					 * Correct line number (for above)
					 */
					line_cnt++;

					if (curr_line.toLowerCase().equals("binary")) {
						System.out.println("Parsing memory file as base 2");
						base = 2;
						charsPerByte = 8;

						continue;
					} else if (curr_line.toLowerCase().equals("hexadecimal")) {
						System.out.println("Parsing memory file as base 16");
						base = 16;
						charsPerByte = 2;

						continue;
					} else {
						System.out.println(
								"No base specified. Using base " + base + ", treating first line as memory data");
					}
				}

				if (curr_line.length() == 0) {
					continue; // skip blank lines
				}

				/*
				 * eline: Improved line parsing and logic for filling memory
				 */
				for (int cnt = 0; cnt < (bytesPerWord * charsPerByte); cnt += charsPerByte) {
					String substring = curr_line.substring(cnt, cnt + charsPerByte);
					memory[(line_cnt * bytesPerWord) + (cnt / charsPerByte)].store(substring, base);
				}

				line_cnt++;
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		last_access = 0;
	}

	/**
	 * Update the display
	 */
	public void refresh_display() {
		textArea.setText(build_display()); // stores memory as a string to widget
	}

	/**
	 * Generate the String to display
	 * 
	 * @return The text to display
	 */
	public String build_display() {
		/*
		 * eline: Added variables with information about the data
		 */
		final int bytesPerWord = (wordsize / 8);
		final int hexRequiredSpaces = Math.max(11, bytesPerWord * 2);

		/*
		 * eline: Switched to StringBuilder
		 */
		StringBuilder result = new StringBuilder();

		/*
		 * eline: Improved display logic
		 */
		/*
		 * HEADERS
		 */

		/* Arrow Header */
		result.append("    ");

		/* Address Header */
		result.append("Address  ");

		/* Hex Header */
		result.append("Hex Display");
		for (int i = 11; i < hexRequiredSpaces + 2; i++) {
			result.append(' ');
		}

		/* Binary Header */
		result.append("Binary Display" + newline);

		/*
		 * SUB-HEADERS
		 */

		/* Arrow Subheader */
		result.append("    ");

		/* Address Subheader */
		result.append("-------  ");

		/* Hex Subheader */
		for (int i = 0; i < hexRequiredSpaces; i++) {
			result.append('-');
		}
		result.append("  ");

		/* Binary Subheader */
		result.append("--------------" + newline);

		/*
		 * ROWS
		 */

		for (int cnt = 0; cnt < memory.length; cnt += bytesPerWord) {
			/* Arrow to last accessed memory */
			if (cnt == last_access) {
				result.append("==> ");
			} else {
				result.append("    ");
			}

			/* Hex Address */
			result.append(String.format("0x%05X  ", cnt));

			for (int cnt2 = 0; cnt2 < (wordsize / 8); cnt2++) { // display hex value
				result.append(memory[cnt + cnt2].hex());
			}
			for (int i = bytesPerWord * 2; i < hexRequiredSpaces + 2; i++) {
				result.append(' ');
			}

			for (int cnt2 = 0; cnt2 < (wordsize / 8); cnt2++) { // display binary value
				result.append(memory[cnt + cnt2].binary() + " ");
			}

			result.append(newline);
		}

		return result.toString();
	}

	/**
	 * Get a word starting at a specific address, in binary
	 * 
	 * @param address
	 *            The address
	 * @return The word in binary
	 */
	public String getMemoryWord_binary(int address) {
		String result = "";

		last_access = address;

		for (int cnt = 0; cnt < wordsize / 8; cnt++) {
			result += memory[address + cnt].binary();
		}

		return result;
	}

	/**
	 * Get a word starting at a specific address, in hexadecimal
	 * 
	 * @param address
	 *            The address
	 * @return The word in hexadecimal
	 */
	public String getMemoryWord_hex(int address) {

		String result = "";

		last_access = address;

		for (int cnt = 0; cnt < wordsize / 8; cnt++) {
			result += memory[address + cnt].hex();
		}

		return result;
	}

	/**
	 * Update the word starting at a specific address
	 * 
	 * @param address
	 *            The address
	 * @param value
	 *            The value for the word, in hexadecimal
	 */
	public void setMemoryWord(int address, String value) {
		last_access = address;

		try {
			for (int cnt = 0; cnt < wordsize / 8; cnt++) {
				memory[address + cnt].store(value.substring((cnt * 2), (cnt * 2) + 1), 16);
			}
		} catch (Exception e) {
			System.err.println("In RAM:setMemoryWord.");
			System.err.println(e);
		}

		refresh_display();
	}

	/*
	 * eline: Changed to accept any BitProvider
	 */
	/**
	 * Set the address register
	 * 
	 * @param addr
	 *            The address register
	 */
	public void set_address_register(BitProvider addr) {
		address_register = addr;
	}

	/*
	 * eline: Changed to accept any BitContainer
	 */
	/**
	 * Set the data register
	 * 
	 * @param cont
	 *            The data register
	 */
	public void set_data_register(BitContainer cont) {
		data_register = cont;
	}

	/**
	 * Load the word at the address in address_register into the data_register
	 */
	public void memory_load() {
		int address = address_register.decimal();
		try {
			data_register.store(getMemoryWord_hex(address), 16);
		} catch (Exception e) {
			System.err.println("An error occurred in RAM:memory_load");
			e.printStackTrace();
		}
	}

	/**
	 * Store the word in the data_register to the address in address_register
	 */
	public void memory_store() {
		int address = address_register.decimal();
		setMemoryWord(address, data_register.hex());
		refresh_display();
	}

}
