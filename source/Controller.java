/**
 * The hardware controller for driving data path.
 */
public class Controller {

	/* Object data fields */
	int memory_size;
	private final int INSTRUCTION_SPACE = 5;
	RTN control_memory[];
	CPU data_path;

	int current_entry;

	/* Advancement Types */
	private final int NEXT = 0;
	private final int START = 1;
	private final int CMDNAME = 2;
	private final int UNKNOWN = 3;

	/* Primary constructor */
	public Controller(CPU cpu) {
		memory_size = 512;
		data_path = cpu;

		control_memory = new RTN[memory_size];
		populateControlMemory();

		reset();
	}

	public void increment_clock() {
		System.err.println(control_memory[current_entry]);
		try {
			control_memory[current_entry].execute();
		} catch (Exception e) {
			System.err.println("In Controller:increment_clock");
			System.err.println(e);
		}

		switch (control_memory[current_entry].advance()) {

		case NEXT: {
			current_entry++;
			break;
		}

		case START: {
			current_entry = 0;
			break;
		}

		case CMDNAME: {
			try {
				current_entry = (data_path.IR.decimal(15, 12) + 1) * INSTRUCTION_SPACE;
				System.err.println("--------");
				System.err.println(data_path.IR.binary());
				System.err.println(current_entry);
				System.err.println("--------");
			} catch (Exception e) {
				System.err.println("In Controller:increment_clock");
				System.err.println(e);
			}

			break;
		}
		}
	}

	public String current_rtn() {
		return control_memory[current_entry].toString();
	}

	public void reset() {
		current_entry = 0;
	}

	/*
	 * eline: Renamed to populateControlMemory() for clarity
	 */
	public void populateControlMemory() {
		/* Set up the fetch instructions */
		control_memory[0] = new Fetch0();
		control_memory[1] = new Fetch1();
		control_memory[2] = new Fetch2();
		
		/* Load the instructions */
		addInstruction(new NOP(), 0);
		addInstruction(new LOADI0(), 1);
		addInstruction(new ADD(), 2);
	}
	
	/*
	 * eline: Created for cleaned adding process
	 */
	private void addInstruction(RTN ins, int opcode) {
		control_memory[(opcode + 1) * INSTRUCTION_SPACE] = ins;
	}

	public class RTN {
		public String toString() {
			return new String("RTN parent toString method.");
		}

		public void execute() {
			System.err.println("You are executing the RTN parent.");
		}

		public int advance() {
			return UNKNOWN;
		}
	};

	/**
	 * FETCH 0
	 * 
	 * Store the program counter in the memory address register
	 */
	public class Fetch0 extends RTN {

		public String toString() {
			return new String("Fetch0");
		}

		public void execute() {
			try {
				data_path.PC.store();
				data_path.MA.load();
			} catch (Exception e) {
				System.err.println("In Controller:Fetch0:execute");
				System.err.println(e);
			}
		}

		public int advance() {
			return NEXT;
		}

	}

	/**
	 * FETCH 1
	 * 
	 * Increments the program counter and loads (previous pc) to memory data register 
	 */
	public class Fetch1 extends RTN {
		public String toString() {
			return new String("Fetch1");
		}

		public void execute() {
			data_path.PC.increment(2);
			data_path.main_memory.memory_load();
		}

		public int advance() {
			return NEXT;
		}
	}

	/**
	 * FETCH 2
	 * 
	 * Moves the data from the memory data register to the instruction register
	 */
	public class Fetch2 extends RTN {

		public String toString() {
			return new String("Fetch2");
		}

		public void execute() {
			try {
				data_path.MD.store();
				data_path.IR.load();
			} catch (Exception e) {
				System.err.println("In Controller:Fetch2:execute");
				System.err.println(e);
			}
		}

		public int advance() {
			return CMDNAME;
		}
	}

	/**
	 * No Operation (0)
	 * 
	 * The RTN for doing nothing with the processor.
	 * Should always be in location 1 of the
	 * control memory.
	 */
	public class NOP extends RTN {		
		public String toString() {
			return new String("NOP");
		}

		public void execute() {
		}

		public int advance() {
			return START;
		}
	}

	/**
	 * Load Immediate (1)
	 * 
	 * Will store the immediate value in 
	 * the destination register.
	 */
	public class LOADI0 extends RTN {

		public String toString() {
			return new String("LOADI0");
		}

		public void execute() {
			// IR representation
			// |1 1|1 0|0 0|
			// |5 2|1 8|7 0|
			// | op |dest| immed |

			try {
				int source = data_path.IR.decimal(7, 0);
				int destination = data_path.IR.decimal(11, 8);

				data_path.master_bus.store(source);
				data_path.bank.load(destination);

			} catch (Exception e) {
				System.err.println("In Controller:LOADI0:execute");
				System.err.println(e);
			}
		}

		public int advance() {
			return START;
		}
	}
	
	/*
	 * eline: Added ADD class
	 */
	/**
	 * Add (2)
	 * 
	 * Uses the ALU to add together two register values, storing
	 * the result in a third register
	 */
	public class ADD extends RTN {
		public String toString() {
			return "ADD";
		}
		
		public void execute() {
			// IR representation
			// |15 12|11 08|07 04|03 00|
			// | op  |dest |src1 |src2 |

			try {
				int source1 = data_path.IR.decimal(7, 4);
				int source2 = data_path.IR.decimal(3, 0);
				int destination = data_path.IR.decimal(11, 8);
				
				data_path.bank.store(source2);
				data_path.B.load();
				
				data_path.bank.store(source1);
				
				//TODO: ALU always adds right now
				
				data_path.C.load();
				
				data_path.C.store();
				data_path.bank.load(destination);
			} catch (Exception e) {
				System.err.println("In Controller:ADD:execute");
				System.err.println(e);
			}
		}
		
		public int advance() {
			return START;
		}
	}

}
