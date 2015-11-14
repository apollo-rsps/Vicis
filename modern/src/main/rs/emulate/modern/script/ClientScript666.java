package rs.emulate.modern.script;

import rs.emulate.shared.util.DataBuffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A 'ClientScript' used to provide functionality.
 * <p>
 * Variables: like local variables.
 *
 * Parameters: like things passed to method calls
 *
 * @author Major
 */
public final class ClientScript666 {

	/**
	 * Decodes a ClientScript.
	 *
	 * @param buffer The Buffer.
	 * @return The ClientScript.
	 */
	@SuppressWarnings("unused")
	public static ClientScript666 decode(DataBuffer buffer) {
		ClientScript666 cs = new ClientScript666();

		buffer.position(buffer.limit() - 2);
		int trailerLength = buffer.getShort() & 0xFFFF;
		int trailerPosition = buffer.limit() - 18 - trailerLength;
		buffer.position(trailerPosition);

		int operations = buffer.getInt();
		int intVariables = buffer.getUnsignedShort();
		int stringVariables = buffer.getUnsignedShort();
		int longVariables = buffer.getUnsignedShort();

		int intParameterCount = buffer.getUnsignedShort();
		int stringParameterCount = buffer.getUnsignedShort();
		int longParameterCount = buffer.getUnsignedShort();

		int switches = buffer.getUnsignedByte();

		List<Map<Integer, Integer>> tables = new ArrayList<>(switches);
		for (int index = 0; index < switches; index++) {
			int cases = buffer.getUnsignedShort();
			Map<Integer, Integer> table = new HashMap<>();

			while (cases-- > 0) {
				int value = buffer.getInt();
				int offset = buffer.getInt();
				table.put(value, offset);
			}

			tables.add(table);
		}

		cs.switchTables = tables;

		buffer.position(0);

		String name = buffer.getCString();
		cs.name = name;
		cs.opcodes = new int[operations];
		cs.intOperands = new int[operations];
		cs.stringOperands = new String[operations];
		cs.longOperands = new long[operations];

		int index = 0;
		while (buffer.position() < trailerPosition) {
			int opcode = buffer.getUnsignedShort();

			if (opcode == 3) {
				cs.stringOperands[index] = buffer.getCString();
			} else if (opcode == 54) {
				cs.longOperands[index] = buffer.getLong();
			} else if (opcode >= 150 || opcode == 21 || opcode == 38 || opcode == 39) {
				cs.intOperands[index] = buffer.getUnsignedByte();
			} else {
				cs.intOperands[index] = buffer.getInt();
			}

			cs.opcodes[index++] = opcode;
		}

		return cs;
	}

	private int intArgsCount, stringArgsCount, intStackDepth, stringStackDepth;

	private long[] longOperands;

	private String name;

	private int[] opcodes, intOperands;

	private String[] stringOperands;

	private List<Map<Integer, Integer>> switchTables;

	/**
	 * Gets the integer argument count.
	 *
	 * @return The integer argument count.
	 */
	public int getIntArgsCount() {
		return intArgsCount;
	}

	/**
	 * Gets the integer operand at the specified index.
	 *
	 * @param index The index.
	 * @return The integer operand.
	 */
	public int getIntOperand(int index) {
		return intOperands[index];
	}

	/**
	 * Gets the integer stack depth.
	 *
	 * @return The integer stack depth.
	 */
	public int getIntStackDepth() {
		return intStackDepth;
	}

	/**
	 * Gets the amount of opcodes in this script.
	 *
	 * @return The amount of opcodes.
	 */
	public int getLength() {
		return opcodes.length;
	}

	/**
	 * Gets the long operand at the specified index.
	 *
	 * @param index The index.
	 * @return The long operand.
	 */
	public long getLongOperand(int index) {
		return longOperands[index];
	}

	/**
	 * Gets the name of the client script.
	 *
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the opcode at the specified index.
	 *
	 * @param index The index.
	 * @return The opcode.
	 */
	public int getOpcode(int index) {
		return opcodes[index];
	}

	/**
	 * Gets the string argument count.
	 *
	 * @return The string argument count.
	 */
	public int getStrArgsCount() {
		return stringArgsCount;
	}

	/**
	 * Gets the string stack depth.
	 *
	 * @return The string stack depth.
	 */
	public int getStrStackDepth() {
		return stringStackDepth;
	}

	/**
	 * Gets the string operand at the specified index.
	 *
	 * @param index The index.
	 * @return The string operand.
	 */
	public String getStringOperand(int index) {
		return stringOperands[index];
	}

	/**
	 * Gets the {@link List} of switch tables.
	 *
	 * @return The list of switch tables.
	 */
	public List<Map<Integer, Integer>> getSwitchTables() {
		return switchTables;
	}

}