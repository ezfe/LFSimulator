/**
 * The hardware controller for driving data path.
 */
public class Controller {

	/* Object data fields */
	int memory_size;
	private final int INSTRUCTION_SPACE = 5;
	RTN[] control_memory;
	CPU data_path;

	int current_entry;

	/* Advancement Types */
	private final int NEXT = 0;
	private final int START = 1;
	private final int CMDNAME = 2;
	/*
	 * eline: Removed UNKNOWN advancement type
	 */

	/**
	 * Create a controller
	 * @param cpu The CPU the controller uses
	 */
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
	 * eline: Renamed to populateControlMemory
	 */
	public void populateControlMemory() {
		/* Set up the fetch instructions */
		control_memory[0] = new Fetch0();
		control_memory[1] = new Fetch1();
		control_memory[2] = new Fetch2();
		
		/* Load the instructions */
		control_memory[instructionStart(0, 0)] = new NOP();
		
		control_memory[instructionStart(1, 0)] = new LOAD_B_RHS();
		control_memory[instructionStart(1, 1)] = new ADD1();
		control_memory[instructionStart(1, 2)] = new LOAD_DEST_C();
		
		control_memory[instructionStart(2, 0)] = new LOAD_B_RHS();
		control_memory[instructionStart(2, 1)] = new SUB1();
		control_memory[instructionStart(2, 2)] = new LOAD_DEST_C();

		control_memory[instructionStart(3, 0)] = new LOAD_B_IMM();
//		control_memory[instructionStart(3, 1)] = new ADDI1();
		control_memory[instructionStart(3, 2)] = new LOAD_DEST_C();

		control_memory[instructionStart(4, 0)] = new LOAD_B_IMM();
//		control_memory[instructionStart(4, 1)] = new SUBI1();
		control_memory[instructionStart(4, 2)] = new LOAD_DEST_C();

		control_memory[instructionStart(5, 0)] = new LOAD_B_RHS();
//		control_memory[instructionStart(5, 1)] = new ADDS1();
		control_memory[instructionStart(5, 2)] = new LOAD_DEST_C();
		
		control_memory[instructionStart(6, 0)] = new LOAD_B_RHS();
//		control_memory[instructionStart(6, 1)] = new SUBS1();
		control_memory[instructionStart(6, 2)] = new LOAD_DEST_C();

		control_memory[instructionStart(7, 0)] = new LOAD_B_IMM();
//		control_memory[instructionStart(7, 1)] = new ADDIS1();
		control_memory[instructionStart(7, 2)] = new LOAD_DEST_C();
		
		control_memory[instructionStart(8, 0)] = new LOAD_B_IMM();
//		control_memory[instructionStart(8, 1)] = new SUBIS1();
		control_memory[instructionStart(8, 2)] = new LOAD_DEST_C();

		control_memory[instructionStart(8, 0)] = new LOAD_B_IMM();
//		control_memory[instructionStart(8, 1)] = new SUBIS1();
		control_memory[instructionStart(8, 2)] = new LOAD_DEST_C();

		control_memory[instructionStart(9, 0)] = new LOAD_B_RHS();
//		control_memory[instructionStart(9, 1)] = new ADD1();
		control_memory[instructionStart(9, 2)] = new LOAD_DEST_C();
		
		control_memory[instructionStart(10, 0)] = new LOAD_B_RHS();
//		control_memory[instructionStart(10, 1)] = new ORR1();
		control_memory[instructionStart(10, 2)] = new LOAD_DEST_C();
		
		control_memory[instructionStart(11, 0)] = new LOAD_B_RHS();
//		control_memory[instructionStart(11, 1)] = new EOR1();
		control_memory[instructionStart(11, 2)] = new LOAD_DEST_C();

		control_memory[instructionStart(12, 0)] = new LOAD_B_IMM();
//		control_memory[instructionStart(12, 1)] = new ADDI1();
		control_memory[instructionStart(12, 2)] = new LOAD_DEST_C();
		
		control_memory[instructionStart(13, 0)] = new LOAD_B_IMM();
//		control_memory[instructionStart(13, 1)] = new ORRI1();
		control_memory[instructionStart(13, 2)] = new LOAD_DEST_C();
		
		control_memory[instructionStart(14, 0)] = new LOAD_B_IMM();
//		control_memory[instructionStart(14, 1)] = new EORI1();
		control_memory[instructionStart(14, 2)] = new LOAD_DEST_C();
		
		control_memory[instructionStart(15, 0)] = new LOAD_B_IMM();
//		control_memory[instructionStart(15, 1)] = new LSL1();
		control_memory[instructionStart(15, 2)] = new LOAD_DEST_C();
		
		control_memory[instructionStart(16, 0)] = new LOAD_B_IMM();
//		control_memory[instructionStart(16, 1)] = new LSR1();
		control_memory[instructionStart(16, 2)] = new LOAD_DEST_C();

		control_memory[instructionStart(17, 0)] = new LOAD_B_IMM();
		control_memory[instructionStart(17, 1)] = new ADD1();
//		control_memory[instructionStart(17, 2)] = new LDUR2();
//		control_memory[instructionStart(17, 3)] = new LDUR3();
//		control_memory[instructionStart(17, 4)] = new LDUR4();
		
		control_memory[instructionStart(18, 0)] = new LOAD_B_IMM();
		control_memory[instructionStart(18, 1)] = new ADD1();
//		control_memory[instructionStart(18, 2)] = new SDUR2();
//		control_memory[instructionStart(18, 3)] = new SDUR3();

		
		//		control_memory[instructionStart(3)] = new BRANCH();
	}
	
	/*
	 * eline: Extracted isntruction adding logic
	 */
	private int instructionStart(int opcode, int offset) {
		return (opcode + 1) * INSTRUCTION_SPACE + offset;
	}

	/*
	 * eline: Changed from class to interface
	 */
	public interface RTN {
		public String toString();

		public void execute();

		public int advance();
	};

	/**
	 * FETCH 0
	 * 
	 * Store the program counter in the memory address register
	 */
	public class Fetch0 implements RTN {

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
	public class Fetch1 implements RTN {
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
	public class Fetch2 implements RTN {

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
	public class NOP implements RTN {		
		public String toString() {
			return new String("NOP");
		}

		public void execute() {
		}

		public int advance() {
			return START;
		}
	}

	/*
	 * eline: Removed LOADI0 class
	 */
	
	/*
	 * eline: Added LOAD_B_RHS class
	 */
	/**
	 * LOAD_B_RHS
	 * 
	 * Move the right hand value into B
	 */
	public class LOAD_B_RHS implements RTN {
		public String toString() {
			return "LOAD_B_RHS";
		}
		
		public void execute() {
			try {
				data_path.bank.store(data_path.IR.decimal(14, 10));
				data_path.B.load();
			} catch (Exception e) {
				System.err.println("In Controller:LOAD_B_RHS:execute");
				System.err.println(e);
			}
		}
		
		public int advance() {
			return NEXT;
		}
	}
	
	/*
	 * eline: Added LOAD_B_IMM class
	 */
	/**
	 * LOAD_B_IMM
	 * 
	 * Move the right hand value into B
	 */
	public class LOAD_B_IMM implements RTN {
		public String toString() {
			return "LOAD_B_IMM";
		}
		
		public void execute() {
			try {
				data_path.master_bus.store(data_path.IR.decimal(14, 0));
				data_path.B.load();
			} catch (Exception e) {
				System.err.println("In Controller:LOAD_B_IMM:execute");
				System.err.println(e);
			}
		}
		
		public int advance() {
			return NEXT;
		}
	}
	
	/*
	 * eline: Added LOAD_DEST_C class
	 */
	/**
	 * LOAD_DEST_C
	 * 
	 * Move C into destination register
	 */
	public class LOAD_DEST_C implements RTN {
		public String toString() {
			return "LOAD_DEST_C";
		}
		
		public void execute() {
			try {
				data_path.C.store();
				data_path.bank.load(data_path.IR.decimal(24, 20));
			} catch (Exception e) {
				System.err.println("In Controller:LOAD_DEST_C:execute");
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
	 * Add together left hand value and B into C
	 */
	public class ADD1 implements RTN {
		public String toString() {
			return "ADD";
		}
		
		public void execute() {
			try {
				data_path.bank.store(data_path.IR.decimal(7, 4));
				
				data_path.alu.set_operation(ALU.Operation.ADD);
				
				data_path.C.load();
			} catch (Exception e) {
				System.err.println("In Controller:ADD:execute");
				e.printStackTrace();
			}
		}
		
		public int advance() {
			return NEXT;
		}
	}
	
	/*
	 * eline: Added SUB class
	 */
	/**
	 * Sub (3)
	 * 
	 * Add together left hand value and B into C
	 */
	public class SUB1 implements RTN {
		public String toString() {
			return "SUB";
		}
		
		public void execute() {
			try {
				data_path.bank.store(data_path.IR.decimal(7, 4));
				
				data_path.alu.set_operation(ALU.Operation.SUBTRACT);
				
				data_path.C.load();
			} catch (Exception e) {
				System.err.println("In Controller:SUB:execute");
				e.printStackTrace();
			}
		}
		
		public int advance() {
			return NEXT;
		}
	}
	
	/*
	 * eline: Added BRANCH class
	 */
	/**
	 * Branch (4?)
	 * 
	 * Changes the program counter appropriately
	 */
	public class BRANCH implements RTN {
		public String toString() {
			return "BRANCH";
		}
		
		public void execute() {
			// IR representation
			// |15 12|11            00|
			// | op  |relative address|
			
			try {
				data_path.PC.increment(2 * data_path.IR.decimal(11, 0));
			} catch (Exception e) {
				System.err.println("In Controller:BRANCH:execute");
				e.printStackTrace();
			}
		}

		@Override
		public int advance() {
			// TODO Auto-generated method stub
			return START;
		}
	}

}
