/*
 * Changes in this file:
 * 
 * - Added all instructions
 * - Changed RTN to interface
 */

/**
 * The hardware controller for driving data path.
 */
public class Controller {

	/*
	 * eline: Added INSTRUCTION_SPACE constant
	 */
	/**
	 * The number of control_memory slots for each instruction
	 */
	private final int INSTRUCTION_SPACE = 5;

	int memory_size;
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
	 * 
	 * @param cpu
	 *            The CPU the controller uses
	 */
	public Controller(CPU cpu) {
		memory_size = 512;
		data_path = cpu;

		control_memory = new RTN[memory_size];
		/*
		 * eline: Updated with new function name
		 */
		populateControlMemory();

		reset();
	}

	/**
	 * Perform the next RTN instruction
	 */
	public void increment_clock() {
		System.err.println(control_memory[current_entry]);
		control_memory[current_entry].execute();

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
				/*
				 * eline: Perform math in function instead of inline eline: Adjusted to new bit
				 * boundaries
				 */
				current_entry = instructionStart(data_path.IR.decimal(31, 25), 0);
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

	/**
	 * Convert the current RTN to String
	 * 
	 * @return The String of the current RTN
	 */
	public String current_rtn() {
		return control_memory[current_entry].toString();
	}

	/**
	 * Reset simulation
	 */
	public void reset() {
		// TODO: Do more?
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

		/*
		 * Load the instructions Information on instructions can be found in the
		 * included report
		 */
		control_memory[instructionStart(0, 0)] = new NOP();

		/*
		 * eline: Removed LOADI0
		 */

		/*
		 * ADD
		 */
		control_memory[instructionStart(1, 0)] = new LOAD_B_RHS();
		control_memory[instructionStart(1, 1)] = new ADD_LHS_B();
		control_memory[instructionStart(1, 2)] = new LOAD_DEST_C();

		/*
		 * SUB
		 */
		control_memory[instructionStart(2, 0)] = new LOAD_B_RHS();
		control_memory[instructionStart(2, 1)] = new SUB_LHS_B();
		control_memory[instructionStart(2, 2)] = new LOAD_DEST_C();

		/*
		 * ADDI
		 */
		control_memory[instructionStart(3, 0)] = new LOAD_B_IMM();
		control_memory[instructionStart(3, 1)] = new ADD_LHS_B();
		control_memory[instructionStart(3, 2)] = new LOAD_DEST_C();

		/*
		 * SUBI
		 */
		control_memory[instructionStart(4, 0)] = new LOAD_B_IMM();
		control_memory[instructionStart(4, 1)] = new SUB_LHS_B();
		control_memory[instructionStart(4, 2)] = new LOAD_DEST_C();

		/*
		 * ADDS
		 */
		control_memory[instructionStart(5, 0)] = new LOAD_B_RHS();
		control_memory[instructionStart(5, 1)] = new ADDS_LHS_B();
		control_memory[instructionStart(5, 2)] = new LOAD_DEST_C();

		/*
		 * SUBS
		 */
		control_memory[instructionStart(6, 0)] = new LOAD_B_RHS();
		control_memory[instructionStart(6, 1)] = new SUBS_LHS_B();
		control_memory[instructionStart(6, 2)] = new LOAD_DEST_C();

		/*
		 * ADDIS
		 */
		control_memory[instructionStart(7, 0)] = new LOAD_B_IMM();
		control_memory[instructionStart(7, 1)] = new ADDS_LHS_B();
		control_memory[instructionStart(7, 2)] = new LOAD_DEST_C();

		/*
		 * SUBIS
		 */
		control_memory[instructionStart(8, 0)] = new LOAD_B_IMM();
		control_memory[instructionStart(8, 1)] = new SUBS_LHS_B();
		control_memory[instructionStart(8, 2)] = new LOAD_DEST_C();

		/*
		 * AND
		 */
		control_memory[instructionStart(9, 0)] = new LOAD_B_RHS();
		control_memory[instructionStart(9, 1)] = new AND_LHS_B();
		control_memory[instructionStart(9, 2)] = new LOAD_DEST_C();

		/*
		 * ORR
		 */
		control_memory[instructionStart(10, 0)] = new LOAD_B_RHS();
		control_memory[instructionStart(10, 1)] = new ORR_LHS_B();
		control_memory[instructionStart(10, 2)] = new LOAD_DEST_C();

		/*
		 * EOR
		 */
		control_memory[instructionStart(11, 0)] = new LOAD_B_RHS();
		control_memory[instructionStart(11, 1)] = new EOR_LHS_B();
		control_memory[instructionStart(11, 2)] = new LOAD_DEST_C();

		/*
		 * ANDI
		 */
		control_memory[instructionStart(12, 0)] = new LOAD_B_IMM();
		control_memory[instructionStart(12, 1)] = new AND_LHS_B();
		control_memory[instructionStart(12, 2)] = new LOAD_DEST_C();

		/*
		 * ORRI
		 */
		control_memory[instructionStart(13, 0)] = new LOAD_B_IMM();
		control_memory[instructionStart(13, 1)] = new ORR_LHS_B();
		control_memory[instructionStart(13, 2)] = new LOAD_DEST_C();

		/*
		 * EORI
		 */
		control_memory[instructionStart(14, 0)] = new LOAD_B_IMM();
		control_memory[instructionStart(14, 1)] = new EOR_LHS_B();
		control_memory[instructionStart(14, 2)] = new LOAD_DEST_C();

		/*
		 * LDUR
		 */
		control_memory[instructionStart(15, 0)] = new LOAD_B_IMM();
		control_memory[instructionStart(15, 1)] = new ADD_LHS_B();
		control_memory[instructionStart(15, 2)] = new LDUR2();
		control_memory[instructionStart(15, 3)] = new LDUR3();
		control_memory[instructionStart(15, 4)] = new LDUR4();

		/*
		 * STUR
		 */
		control_memory[instructionStart(16, 0)] = new LOAD_B_IMM();
		control_memory[instructionStart(16, 1)] = new ADD_LHS_B();
		control_memory[instructionStart(16, 2)] = new STUR2();
		control_memory[instructionStart(16, 3)] = new STUR3();

		/*
		 * CBZ
		 */
		control_memory[instructionStart(17, 0)] = new LOAD_PC_REG_Z();

		/*
		 * CBNZ
		 */
		control_memory[instructionStart(18, 0)] = new LOAD_PC_REG_NZ();

		/*
		 * B
		 */
		control_memory[instructionStart(19, 0)] = new LOAD_PC_PTR();

		/*
		 * BR
		 */
		control_memory[instructionStart(20, 0)] = new LOAD_PC_REG();

		/*
		 * B.EQ
		 */
		control_memory[instructionStart(21, 0)] = new LOAD_PC_PTR_EQ();

		/*
		 * B.NE
		 */
		control_memory[instructionStart(22, 0)] = new LOAD_PC_PTR_NE();

		/*
		 * B.LT
		 */
		control_memory[instructionStart(23, 0)] = new LOAD_PC_PTR_LT();

		/*
		 * B.LE
		 */
		control_memory[instructionStart(24, 0)] = new LOAD_PC_PTR_LE();

		/*
		 * B.GT
		 */
		control_memory[instructionStart(25, 0)] = new LOAD_PC_PTR_GT();

		/*
		 * B.GE
		 */
		control_memory[instructionStart(26, 0)] = new LOAD_PC_PTR_GE();

		/*
		 * B.MI
		 */
		control_memory[instructionStart(27, 0)] = new LOAD_PC_PTR_MI();

		/*
		 * B.PL
		 */
		control_memory[instructionStart(28, 0)] = new LOAD_PC_PTR_PL();

		/*
		 * B.VS
		 */
		control_memory[instructionStart(29, 0)] = new LOAD_PC_PTR_VS();

		/*
		 * B.VC
		 */
		control_memory[instructionStart(30, 0)] = new LOAD_PC_PTR_VC();

		/*
		 * MOVZ
		 */
		control_memory[instructionStart(31, 0)] = new LOAD_REG_PTR();
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
		/**
		 * Execute the RTN
		 */
		public void execute();

		/**
		 * How to advance
		 * 
		 * @return START, NEXT, etc..
		 */
		public int advance();
	};

	/**
	 * Fetch0
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
	 * FETCH1
	 * 
	 * Increments the program counter and loads (previous pc) to memory data
	 * register
	 */
	public class Fetch1 implements RTN {
		public String toString() {
			return new String("Fetch1");
		}

		public void execute() {
			/*
			 * eline: Properly increment based on wordsize
			 */
			data_path.PC.increment(data_path.getWordSize() / 8);
			data_path.main_memory.memory_load();
		}

		public int advance() {
			return NEXT;
		}
	}

	/**
	 * FETCH2
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
	 * NOP
	 * 
	 * The RTN for doing nothing with the processor. Should always be in location 1
	 * of the control memory.
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
	 * Move the right hand register value into B
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
	 * eline: Added ADD_LHS_B class
	 */
	/**
	 * ADD_LHS_B
	 * 
	 * Add together left hand register value and B into C
	 */
	public class ADD_LHS_B implements RTN {
		public String toString() {
			return "ADD_LHS_B";
		}

		public void execute() {
			try {
				data_path.bank.store(data_path.IR.decimal(19, 15));

				data_path.alu.set_operation(ALU.Operation.ADD);
				data_path.alu.set_isSettingFlags(false);

				data_path.C.load();
			} catch (Exception e) {
				System.err.println("In Controller:ADD_LHS_B:execute");
				e.printStackTrace();
			}
		}

		public int advance() {
			return NEXT;
		}
	}

	/*
	 * eline: Added SUB_LHS_B class
	 */
	/**
	 * SUB_LHS_B
	 * 
	 * Add together left hand register value and B into C
	 */
	public class SUB_LHS_B implements RTN {
		public String toString() {
			return "SUB_LHS_B";
		}

		public void execute() {
			try {
				data_path.bank.store(data_path.IR.decimal(19, 15));

				data_path.alu.set_operation(ALU.Operation.SUBTRACT);
				data_path.alu.set_isSettingFlags(false);

				data_path.C.load();
			} catch (Exception e) {
				System.err.println("In Controller:SUB_LHS_B:execute");
				e.printStackTrace();
			}
		}

		public int advance() {
			return NEXT;
		}
	}

	/*
	 * eline: Added ADDS_LHS_B class
	 */
	/**
	 * ADDS_LHS_B
	 * 
	 * Add together left hand register value and B into C (setting flags)
	 */
	public class ADDS_LHS_B implements RTN {
		public String toString() {
			return "ADDS_LHS_B";
		}

		public void execute() {
			try {
				data_path.bank.store(data_path.IR.decimal(19, 15));

				data_path.alu.set_operation(ALU.Operation.ADD);
				data_path.alu.set_isSettingFlags(true);

				data_path.C.load();
			} catch (Exception e) {
				System.err.println("In Controller:ADDS_LHS_B:execute");
				e.printStackTrace();
			}
		}

		public int advance() {
			return NEXT;
		}
	}

	/*
	 * eline: Added SUBS_LHS_B class
	 */
	/**
	 * SUBS_LHS_B
	 * 
	 * Add together left hand register value and B into C (setting flags)
	 */
	public class SUBS_LHS_B implements RTN {
		public String toString() {
			return "SUBS_LHS_B";
		}

		public void execute() {
			try {
				data_path.bank.store(data_path.IR.decimal(19, 15));

				data_path.alu.set_operation(ALU.Operation.SUBTRACT);
				data_path.alu.set_isSettingFlags(true);

				data_path.C.load();
			} catch (Exception e) {
				System.err.println("In Controller:SUBS_LHS_B:execute");
				e.printStackTrace();
			}
		}

		public int advance() {
			return NEXT;
		}
	}

	/*
	 * eline: Added AND_LHS_B class
	 */
	/**
	 * AND_LHS_B
	 * 
	 * Logical AND together left hand register value and B into C
	 */
	public class AND_LHS_B implements RTN {
		public String toString() {
			return "AND_LHS_B";
		}

		public void execute() {
			try {
				data_path.bank.store(data_path.IR.decimal(19, 15));

				data_path.alu.set_operation(ALU.Operation.AND);

				data_path.C.load();
			} catch (Exception e) {
				System.err.println("In Controller:AND_LHS_B:execute");
				e.printStackTrace();
			}
		}

		public int advance() {
			return NEXT;
		}
	}

	/*
	 * eline: Added ORR_LHS_B class
	 */
	/**
	 * ORR_LHS_B
	 * 
	 * Logical OR together left hand register value and B into C
	 */
	public class ORR_LHS_B implements RTN {
		public String toString() {
			return "ORR_LHS_B";
		}

		public void execute() {
			try {
				data_path.bank.store(data_path.IR.decimal(19, 15));

				data_path.alu.set_operation(ALU.Operation.OR);

				data_path.C.load();
			} catch (Exception e) {
				System.err.println("In Controller:ORR_LHS_B:execute");
				e.printStackTrace();
			}
		}

		public int advance() {
			return NEXT;
		}
	}

	/*
	 * eline: Added EOR_LHS_B class
	 */
	/**
	 * EOR_LHS_B
	 * 
	 * Logical OR together left hand register value and B into C
	 */
	public class EOR_LHS_B implements RTN {
		public String toString() {
			return "EOR_LHS_B";
		}

		public void execute() {
			try {
				data_path.bank.store(data_path.IR.decimal(19, 15));

				data_path.alu.set_operation(ALU.Operation.XOR);

				data_path.C.load();
			} catch (Exception e) {
				System.err.println("In Controller:EOR_LHS_B:execute");
				e.printStackTrace();
			}
		}

		public int advance() {
			return NEXT;
		}
	}

	/*
	 * eline: Added LDUR2 class
	 */
	/**
	 * LDUR2
	 * 
	 * Load C into MA
	 */
	public class LDUR2 implements RTN {
		public String toString() {
			return "LDUR2";
		}

		public void execute() {
			try {
				data_path.C.store();
				data_path.MA.load();
			} catch (Exception e) {
				System.err.println("In Controller:LDUR2:execute");
				e.printStackTrace();
			}
		}

		public int advance() {
			return NEXT;
		}
	}

	/*
	 * eline: Added LDUR3 class
	 */
	/**
	 * LDUR3
	 * 
	 * Load main memory
	 */
	public class LDUR3 implements RTN {
		public String toString() {
			return "LDUR3";
		}

		public void execute() {
			try {
				data_path.main_memory.memory_load();
			} catch (Exception e) {
				System.err.println("In Controller:LDUR3:execute");
				e.printStackTrace();
			}
		}

		public int advance() {
			return NEXT;
		}
	}

	/*
	 * eline: Added LDUR4 class
	 */
	/**
	 * LDUR4
	 * 
	 * Store MD in destination register
	 */
	public class LDUR4 implements RTN {
		public String toString() {
			return "LDUR4";
		}

		public void execute() {
			try {
				data_path.MD.store();
				data_path.bank.load(data_path.IR.decimal(24, 20));
			} catch (Exception e) {
				System.err.println("In Controller:LDUR4:execute");
				e.printStackTrace();
			}
		}

		public int advance() {
			return START;
		}
	}

	/*
	 * eline: Added STUR2 class
	 */
	/**
	 * STUR2
	 * 
	 * Load C into MA Load source register into MD
	 */
	public class STUR2 implements RTN {
		public String toString() {
			return "STUR2";
		}

		public void execute() {
			try {
				data_path.C.store();
				data_path.MA.load();

				data_path.bank.store(data_path.IR.decimal(24, 20));
				data_path.MD.load();
			} catch (Exception e) {
				System.err.println("In Controller:STUR2:execute");
				e.printStackTrace();
			}
		}

		public int advance() {
			return NEXT;
		}
	}

	/*
	 * eline: Added STUR3 class
	 */
	/**
	 * STUR3
	 * 
	 * Store main memory
	 */
	public class STUR3 implements RTN {
		public String toString() {
			return "STUR3";
		}

		public void execute() {
			try {
				data_path.main_memory.memory_store();
			} catch (Exception e) {
				System.err.println("In Controller:STUR3:execute");
				e.printStackTrace();
			}
		}

		public int advance() {
			return START;
		}
	}

	/*
	 * eline: Added LOAD_PC_PTR class
	 */
	/**
	 * LOAD_PC_PTR
	 * 
	 * Move the pointer value into PC
	 */
	public class LOAD_PC_PTR implements RTN {
		public String toString() {
			return "LOAD_PC_PTR";
		}

		public void execute() {
			try {
				data_path.master_bus.store(data_path.IR.decimal(19, 0));
				data_path.PC.load();
			} catch (Exception e) {
				System.err.println("In Controller:LOAD_PC_PTR:execute");
				System.err.println(e);
			}
		}

		public int advance() {
			return START;
		}
	}

	/*
	 * eline: Added LOAD_PC_REG_Z class
	 */
	/**
	 * LOAD_PC_REG_Z
	 * 
	 * If the register is zero, then move the pointer value into PC
	 */
	public class LOAD_PC_REG_Z implements RTN {
		public String toString() {
			return "LOAD_PC_REG_Z";
		}

		public void execute() {
			try {
				if (data_path.bank.decimal(data_path.IR.decimal(24, 20)) == 0) {
					data_path.master_bus.store(data_path.IR.decimal(19, 0));
					data_path.PC.load();
				}
			} catch (Exception e) {
				System.err.println("In Controller:LOAD_PC_REG_Z:execute");
				System.err.println(e);
			}
		}

		public int advance() {
			return START;
		}
	}

	/*
	 * eline: Added LOAD_PC_REG_NZ class
	 */
	/**
	 * LOAD_PC_REG_NZ
	 * 
	 * If the register isn't zero, then move the pointer value into PC
	 */
	public class LOAD_PC_REG_NZ implements RTN {
		public String toString() {
			return "LOAD_PC_REG_NZ";
		}

		public void execute() {
			try {
				if (data_path.bank.decimal(data_path.IR.decimal(24, 20)) != 0) {
					data_path.master_bus.store(data_path.IR.decimal(19, 0));
					data_path.PC.load();
				}
			} catch (Exception e) {
				System.err.println("In Controller:LOAD_PC_REG_NZ:execute");
				System.err.println(e);
			}
		}

		public int advance() {
			return START;
		}
	}

	/*
	 * eline: Added LOAD_PC_REG class
	 */
	/**
	 * LOAD_PC_REG
	 * 
	 * Move the register value into PC
	 */
	public class LOAD_PC_REG implements RTN {
		public String toString() {
			return "LOAD_PC_REG";
		}

		public void execute() {
			try {
				data_path.bank.store(data_path.IR.decimal(24, 20));
				data_path.PC.load();
			} catch (Exception e) {
				System.err.println("In Controller:LOAD_PC_REG:execute");
				System.err.println(e);
			}
		}

		public int advance() {
			return START;
		}
	}

	/*
	 * eline: Added LOAD_PC_PTR_EQ class
	 */
	/**
	 * LOAD_PC_PTR_EQ
	 * 
	 * If Z == 1, then move the pointer value into PC
	 */
	public class LOAD_PC_PTR_EQ implements RTN {
		public String toString() {
			return "LOAD_PC_PTR_EQ";
		}

		public void execute() {
			try {
				if (data_path.alu.zeroFlag.decimal() == 1) {
					data_path.master_bus.store(data_path.IR.decimal(19, 0));
					data_path.PC.load();
				}
			} catch (Exception e) {
				System.err.println("In Controller:LOAD_PC_PTR_EQ:execute");
				System.err.println(e);
			}
		}

		public int advance() {
			return START;
		}
	}

	/*
	 * eline: Added LOAD_PC_PTR_NE class
	 */
	/**
	 * LOAD_PC_PTR_NE
	 * 
	 * If Z == 0, then move the pointer value into PC
	 */
	public class LOAD_PC_PTR_NE implements RTN {
		public String toString() {
			return "LOAD_PC_PTR_NE";
		}

		public void execute() {
			try {
				if (data_path.alu.zeroFlag.decimal() == 0) {
					data_path.master_bus.store(data_path.IR.decimal(19, 0));
					data_path.PC.load();
				}
			} catch (Exception e) {
				System.err.println("In Controller:LOAD_PC_PTR_NE:execute");
				System.err.println(e);
			}
		}

		public int advance() {
			return START;
		}
	}

	/*
	 * eline: Added LOAD_PC_PTR_LT class
	 */
	/**
	 * LOAD_PC_PTR_LT
	 * 
	 * If N != V, then move the pointer value into PC
	 */
	public class LOAD_PC_PTR_LT implements RTN {
		public String toString() {
			return "LOAD_PC_PTR_LT";
		}

		public void execute() {
			try {
				if (data_path.alu.negativeFlag.decimal() != data_path.alu.overflowFlag.decimal()) {
					data_path.master_bus.store(data_path.IR.decimal(19, 0));
					data_path.PC.load();
				}
			} catch (Exception e) {
				System.err.println("In Controller:LOAD_PC_PTR_LT:execute");
				System.err.println(e);
			}
		}

		public int advance() {
			return START;
		}
	}

	/*
	 * eline: Added LOAD_PC_PTR_LE class
	 */
	/**
	 * LOAD_PC_PTR_LE
	 * 
	 * If !(Z == 0 && N == V), then move the pointer value into PC
	 */
	public class LOAD_PC_PTR_LE implements RTN {
		public String toString() {
			return "LOAD_PC_PTR_LE";
		}

		public void execute() {
			try {
				if (!(data_path.alu.zeroFlag.decimal() == 0
						&& data_path.alu.negativeFlag.decimal() == data_path.alu.overflowFlag.decimal())) {
					data_path.master_bus.store(data_path.IR.decimal(19, 0));
					data_path.PC.load();
				}
			} catch (Exception e) {
				System.err.println("In Controller:LOAD_PC_PTR_LE:execute");
				System.err.println(e);
			}
		}

		public int advance() {
			return START;
		}
	}

	/*
	 * eline: Added LOAD_PC_PTR_GT class
	 */
	/**
	 * LOAD_PC_PTR_GT
	 * 
	 * If Z == 0 && N == V, then move the pointer value into PC
	 */
	public class LOAD_PC_PTR_GT implements RTN {
		public String toString() {
			return "LOAD_PC_PTR_GT";
		}

		public void execute() {
			try {
				if (data_path.alu.zeroFlag.decimal() == 0
						&& data_path.alu.negativeFlag.decimal() == data_path.alu.overflowFlag.decimal()) {
					data_path.master_bus.store(data_path.IR.decimal(19, 0));
					data_path.PC.load();
				}
			} catch (Exception e) {
				System.err.println("In Controller:LOAD_PC_PTR_GT:execute");
				System.err.println(e);
			}
		}

		public int advance() {
			return START;
		}
	}

	/*
	 * eline: Added LOAD_PC_PTR_GE class
	 */
	/**
	 * LOAD_PC_PTR_GE
	 * 
	 * If N == V, then move the pointer value into PC
	 */
	public class LOAD_PC_PTR_GE implements RTN {
		public String toString() {
			return "LOAD_B_PTR_GE";
		}

		public void execute() {
			try {
				if (data_path.alu.negativeFlag.decimal() == data_path.alu.overflowFlag.decimal()) {
					data_path.master_bus.store(data_path.IR.decimal(19, 0));
					data_path.PC.load();
				}
			} catch (Exception e) {
				System.err.println("In Controller:LOAD_PC_PTR_GEx:execute");
				System.err.println(e);
			}
		}

		public int advance() {
			return START;
		}
	}

	/*
	 * eline: Added LOAD_PC_PTR_MI class
	 */
	/**
	 * LOAD_PC_PTR_MI
	 * 
	 * If N == 1, then move the pointer value into PC
	 */
	public class LOAD_PC_PTR_MI implements RTN {
		public String toString() {
			return "LOAD_PC_PTR_MI";
		}

		public void execute() {
			try {
				if (data_path.alu.negativeFlag.decimal() == 1) {
					data_path.master_bus.store(data_path.IR.decimal(19, 0));
					data_path.PC.load();
				}
			} catch (Exception e) {
				System.err.println("In Controller:LOAD_PC_PTR_MI:execute");
				System.err.println(e);
			}
		}

		public int advance() {
			return START;
		}
	}

	/*
	 * eline: Added LOAD_PC_PTR_PL class
	 */
	/**
	 * LOAD_PC_PTR_PL
	 * 
	 * If N == 0, then move the pointer value into PC
	 */
	public class LOAD_PC_PTR_PL implements RTN {
		public String toString() {
			return "LOAD_PC_PTR_PL";
		}

		public void execute() {
			try {
				if (data_path.alu.negativeFlag.decimal() == 0) {
					data_path.master_bus.store(data_path.IR.decimal(19, 0));
					data_path.PC.load();
				}
			} catch (Exception e) {
				System.err.println("In Controller:LOAD_PC_PTR_PL:execute");
				System.err.println(e);
			}
		}

		public int advance() {
			return START;
		}
	}

	/*
	 * eline: Added LOAD_PC_PTR_VS class
	 */
	/**
	 * LOAD_PC_PTR_VS
	 * 
	 * If V == 1, then move the pointer value into PC
	 */
	public class LOAD_PC_PTR_VS implements RTN {
		public String toString() {
			return "LOAD_PC_PTR_VS";
		}

		public void execute() {
			try {
				if (data_path.alu.overflowFlag.decimal() == 1) {
					data_path.master_bus.store(data_path.IR.decimal(19, 0));
					data_path.PC.load();
				}
			} catch (Exception e) {
				System.err.println("In Controller:LOAD_PC_PTR_VS:execute");
				System.err.println(e);
			}
		}

		public int advance() {
			return START;
		}
	}

	/*
	 * eline: Added LOAD_PC_PTR_VC class
	 */
	/**
	 * LOAD_PC_PTR_VC
	 * 
	 * If V == 0, then move the pointer value into PC
	 */
	public class LOAD_PC_PTR_VC implements RTN {
		public String toString() {
			return "LOAD_PC_PTR_VC";
		}

		public void execute() {
			try {
				if (data_path.alu.overflowFlag.decimal() == 0) {
					data_path.master_bus.store(data_path.IR.decimal(19, 0));
					data_path.PC.load();
				}
			} catch (Exception e) {
				System.err.println("In Controller:LOAD_PC_PTR_VC:execute");
				System.err.println(e);
			}
		}

		public int advance() {
			return START;
		}
	}

	/*
	 * eline: Added LOAD_REG_PTR class
	 */
	/**
	 * LOAD_REG_PTR
	 * 
	 * Move the value of the pointer immediate into the register
	 */
	public class LOAD_REG_PTR implements RTN {
		public String toString() {
			return "LOAD_REG_PTR";
		}

		public void execute() {
			try {
				data_path.master_bus.store(data_path.IR.decimal(19, 0));
				data_path.bank.load(data_path.IR.decimal(24, 20));
			} catch (Exception e) {
				System.err.println("In Controller:LOAD_REG_PTR:execute");
				System.err.println(e);
			}
		}

		public int advance() {
			return START;
		}
	}
}
