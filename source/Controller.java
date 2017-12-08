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
		
		/* 
		 * Load the instructions
		 * Information on instructions can be found in the included report
		 */
		control_memory[instructionStart(0, 0)] = new NOP();
		
		control_memory[instructionStart(1, 0)] = new LOAD_B_RHS();
		control_memory[instructionStart(1, 1)] = new ADD_LHS_B();
		control_memory[instructionStart(1, 2)] = new LOAD_DEST_C();
		
		control_memory[instructionStart(2, 0)] = new LOAD_B_RHS();
		control_memory[instructionStart(2, 1)] = new SUB_LHS_B();
		control_memory[instructionStart(2, 2)] = new LOAD_DEST_C();

		control_memory[instructionStart(3, 0)] = new LOAD_B_IMM();
		control_memory[instructionStart(3, 1)] = new ADD_LHS_B();
		control_memory[instructionStart(3, 2)] = new LOAD_DEST_C();

		control_memory[instructionStart(4, 0)] = new LOAD_B_IMM();
		control_memory[instructionStart(4, 1)] = new SUB_LHS_B();
		control_memory[instructionStart(4, 2)] = new LOAD_DEST_C();

		control_memory[instructionStart(5, 0)] = new LOAD_B_RHS();
		control_memory[instructionStart(5, 1)] = new ADDS_LHS_B();
		control_memory[instructionStart(5, 2)] = new LOAD_DEST_C();
		
		control_memory[instructionStart(6, 0)] = new LOAD_B_RHS();
		control_memory[instructionStart(6, 1)] = new SUBS_LHS_B();
		control_memory[instructionStart(6, 2)] = new LOAD_DEST_C();

		control_memory[instructionStart(7, 0)] = new LOAD_B_IMM();
		control_memory[instructionStart(7, 1)] = new ADDS_LHS_B();
		control_memory[instructionStart(7, 2)] = new LOAD_DEST_C();
		
		control_memory[instructionStart(8, 0)] = new LOAD_B_IMM();
		control_memory[instructionStart(8, 1)] = new SUBS_LHS_B();
		control_memory[instructionStart(8, 2)] = new LOAD_DEST_C();

		control_memory[instructionStart(9, 0)] = new LOAD_B_RHS();
		control_memory[instructionStart(9, 1)] = new AND_LHS_B();
		control_memory[instructionStart(9, 2)] = new LOAD_DEST_C();
		
		control_memory[instructionStart(10, 0)] = new LOAD_B_RHS();
		control_memory[instructionStart(10, 1)] = new ORR_LHS_B();
		control_memory[instructionStart(10, 2)] = new LOAD_DEST_C();
		
		control_memory[instructionStart(11, 0)] = new LOAD_B_RHS();
		control_memory[instructionStart(11, 1)] = new EOR_LHS_B();
		control_memory[instructionStart(11, 2)] = new LOAD_DEST_C();

		control_memory[instructionStart(12, 0)] = new LOAD_B_IMM();
		control_memory[instructionStart(12, 1)] = new AND_LHS_B();
		control_memory[instructionStart(12, 2)] = new LOAD_DEST_C();
		
		control_memory[instructionStart(13, 0)] = new LOAD_B_IMM();
		control_memory[instructionStart(13, 1)] = new ORR_LHS_B();
		control_memory[instructionStart(13, 2)] = new LOAD_DEST_C();
		
		control_memory[instructionStart(14, 0)] = new LOAD_B_IMM();
		control_memory[instructionStart(14, 1)] = new EOR_LHS_B();
		control_memory[instructionStart(14, 2)] = new LOAD_DEST_C();
		
		control_memory[instructionStart(15, 0)] = new LOAD_B_IMM();
		control_memory[instructionStart(15, 1)] = new ADD_LHS_B();
		control_memory[instructionStart(15, 2)] = new LDUR2();
		control_memory[instructionStart(15, 3)] = new LDUR3();
		control_memory[instructionStart(15, 4)] = new LDUR4();
		
		control_memory[instructionStart(16, 0)] = new LOAD_B_IMM();
		control_memory[instructionStart(16, 1)] = new ADD_LHS_B();
		control_memory[instructionStart(16, 2)] = new STUR2();
		control_memory[instructionStart(16, 3)] = new STUR3();

		control_memory[instructionStart(17, 0)] = new LOAD_PC_REG_Z();
		
		control_memory[instructionStart(18, 0)] = new LOAD_PC_REG_NZ();
		
		control_memory[instructionStart(19, 0)] = new LOAD_PC_PTR();
		
		control_memory[instructionStart(20, 0)] = new LOAD_PC_REG();
		
		control_memory[instructionStart(21, 0)] = new LOAD_PC_PTR_EQ();
		
		control_memory[instructionStart(22, 0)] = new LOAD_PC_PTR_NE();
		
		control_memory[instructionStart(23, 0)] = new LOAD_PC_PTR_LT();
		
		control_memory[instructionStart(24, 0)] = new LOAD_PC_PTR_LE();
		
		control_memory[instructionStart(25, 0)] = new LOAD_PC_PTR_GT();
		
		control_memory[instructionStart(26, 0)] = new LOAD_PC_PTR_GE();
		
		control_memory[instructionStart(27, 0)] = new LOAD_PC_PTR_MI();
		
		control_memory[instructionStart(28, 0)] = new LOAD_PC_PTR_PL();
		
		control_memory[instructionStart(29, 0)] = new LOAD_PC_PTR_VS();
		
		control_memory[instructionStart(30, 0)] = new LOAD_PC_PTR_VC();
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
			data_path.PC.increment(4);
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
		@Override
		public String toString() {
			return "LDUR2";
		}
		@Override
		public void execute() {
			try {
				data_path.C.store();
				data_path.MA.load();
			} catch (Exception e) {
				System.err.println("In Controller:LDUR2:execute");
				e.printStackTrace();
			}
		}

		@Override
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
		@Override
		public String toString() {
			return "LDUR3";
		}
		@Override
		public void execute() {
			try {
				data_path.main_memory.memory_load();
			} catch (Exception e) {
				System.err.println("In Controller:LDUR3:execute");
				e.printStackTrace();
			}
		}

		@Override
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
		@Override
		public String toString() {
			return "LDUR4";
		}
		@Override
		public void execute() {
			try {
				data_path.MD.store();
				data_path.bank.load(data_path.IR.decimal(24, 20));
			} catch (Exception e) {
				System.err.println("In Controller:LDUR4:execute");
				e.printStackTrace();
			}
		}

		@Override
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
	 * Load C into MA
	 * Load source register into MD
	 */
	public class STUR2 implements RTN {
		@Override
		public String toString() {
			return "STUR2";
		}
		@Override
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

		@Override
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
		@Override
		public String toString() {
			return "STUR3";
		}
		@Override
		public void execute() {
			try {
				data_path.main_memory.memory_store();
			} catch (Exception e) {
				System.err.println("In Controller:STUR3:execute");
				e.printStackTrace();
			}
		}

		@Override
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
				if (data_path.alu.negativeFlag.decimal() == data_path.alu.overflowFlag.decimal()) {
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
				if ( !(data_path.alu.zeroFlag.decimal() == 0 && data_path.alu.negativeFlag.decimal() == data_path.alu.overflowFlag.decimal())) {
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
				if (data_path.alu.zeroFlag.decimal() == 0 && data_path.alu.negativeFlag.decimal() == data_path.alu.overflowFlag.decimal()) {
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
		@Override
		public String toString() {
			return "LOAD_REG_PTR";
		}
		
		@Override
		public void execute() {
			try {
				data_path.master_bus.store(data_path.IR.decimal(19, 0));
				data_path.bank.load(data_path.IR.decimal(24, 20));
			} catch (Exception e) {
				System.err.println("In Controller:LOAD_REG_PTR:execute");
				System.err.println(e);
			}
		}
		
		@Override
		public int advance() {
			return START;
		}
	}
}
