package rs.emulate.modern.script.interp;

import com.google.common.collect.ImmutableMap;
import rs.emulate.modern.script.decomp.instr.Instruction;
import rs.emulate.modern.script.decomp.instr.impl.BranchInstruction;
import rs.emulate.modern.script.decomp.instr.impl.GenericInstruction;
import rs.emulate.modern.script.decomp.instr.impl.IntInstruction;
import rs.emulate.modern.script.decomp.instr.impl.StringInstruction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A Map of opcodes to {@link Instruction}s.
 *
 * @author Major
 */
public final class InstructionMap {

	/**
	 * A creator for a {@link Map} of instruction opcodes to {@link Consumer}s, which modify a {@link ScriptContext}
	 * using side-effects.
	 */
	private static final class InstructionMapCreator {

		/**
		 * The Map of opcodes to ScriptContext Consumers.
		 */
		private final Map<Integer, Instruction> instructions = new HashMap<>(100);

		/**
		 * Creates the {@link Map} of opcodes to {@link Consumer}s.
		 *
		 * @return The Map.
		 */
		public Map<Integer, Instruction> create() {
			return ImmutableMap.copyOf(instructions);
		}

		/**
		 * Fills the instruction Map.
		 */
		public void fill() {
			insert(IntInstruction.create("pushi", 0, context -> context.pushInt(context.getIntOperand())));
			insert(StringInstruction.create("pushs", 3, context -> context.pushString(context.getStringOperand())));

			insert(BranchInstruction.create("goto", 6, ScriptContext::branch));
			insert(BranchInstruction.create("ifi_neq", 7, context -> context.branchIf(context.popInt() != context.popInt())));
			insert(BranchInstruction.create("ifi_eq", 8, context -> context.branchIf(context.popInt() == context.popInt())));
			insert(BranchInstruction.create("ifi_lt", 9, context -> context.branchIf(context.popInt() > context.popInt())));
			insert(BranchInstruction.create("ifi_gt", 10, context -> context.branchIf(context.popInt() < context.popInt())));

			insert(BranchInstruction.create("ifi_geq", 31, context -> context.branchIf(context.popInt() <= context.popInt())));
			insert(BranchInstruction.create("ifi_leq", 32, context -> context.branchIf(context.popInt() >= context.popInt())));

			insert(IntInstruction.create("concat", 37, context -> context.getStrings(context.stringCount() - context.popInt())
					.collect(Collectors.joining())));

			insert(GenericInstruction.create("popi", 38, ScriptContext::popInt));
			insert(GenericInstruction.create("pops", 39, ScriptContext::popString));

			insert(IntInstruction.create("pushl", 54, context -> context.pushLong(context.getLongOperand())));
			insert(IntInstruction.create("popl", 55, ScriptContext::popLong));

			insert(BranchInstruction.create("ifl_neq", 68, context -> context.branchIf(context.popLong() != context.popLong())));
			insert(BranchInstruction.create("ifl_eq", 69, context -> context.branchIf(context.popLong() == context.popLong())));
			insert(BranchInstruction.create("ifl_lt", 70, context -> context.branchIf(context.popLong() > context.popLong())));
			insert(BranchInstruction.create("ifl_gt", 71, context -> context.branchIf(context.popLong() < context.popLong())));

			insert(BranchInstruction.create("ifl_geq", 72, context -> context.branchIf(context.popLong() <= context.popLong())));
			insert(BranchInstruction.create("ifl_leq", 73, context -> context.branchIf(context.popLong() >= context.popLong())));

			insert(BranchInstruction.create("if_true", 86, context -> context.branchIf(context.popInt() == 1)));
			insert(BranchInstruction.create("if_false", 87, context -> context.branchIf(context.popInt() == 0)));
		}

		/**
		 * Inserts the specified {@link Instruction} into the {@link Map}.
		 *
		 * @param instruction The Instruction.
		 */
		private void insert(Instruction instruction) {
			instructions.put(instruction.getOpcode(), instruction);
		}

	}

	/**
	 * The map of opcodes to Consumers which perform functionality.
	 */
	private final Map<Integer, Instruction> instructions;

	/**
	 * Creates the InstructionMap.
	 */
	public InstructionMap() {
		InstructionMapCreator creator = new InstructionMapCreator();
		creator.fill();
		instructions = creator.create();
	}

	/**
	 * Executes the specified opcode for the specified {@link ScriptContext}.
	 *
	 * @param opcode The opcode.
	 * @param script The script.
	 */
	public void execute(int opcode, ScriptContext script) {
		Instruction function = instructions.get(opcode);
		function.evaluate(script);
	}

}