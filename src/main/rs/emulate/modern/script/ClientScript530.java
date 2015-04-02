package rs.emulate.modern.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rs.emulate.shared.util.DataBuffer;

/**
 * A 'ClientScript' used by the client to provide functionality.
 * 
 * @author Graham
 */
public final class ClientScript530 {

	/**
	 * Decodes a {@link ClientScript530} from a {@link DataBuffer}.
	 * 
	 * @param buffer The buffer.
	 * @return The client script.
	 */
	public static ClientScript530 decode(DataBuffer buffer) {
		ClientScript530 script = new ClientScript530();

		buffer.position(buffer.limit() - 2);
		int trailerLength = buffer.getShort() & 0xFFFF;
		int trailerPosition = buffer.limit() - 14 - trailerLength;
		buffer.position(trailerPosition);

		int ops = buffer.getInt();
		script.intArgsCount = buffer.getUnsignedShort();
		script.stringArgsCount = buffer.getUnsignedShort();
		script.intStackDepth = buffer.getUnsignedShort();
		script.stringStackDepth = buffer.getUnsignedShort();

		int switches = buffer.getUnsignedByte();
		script.switchTables = new ArrayList<>(switches);
		for (int table = 0; table < switches; table++) {
			script.switchTables.add(table, new HashMap<>());

			int size = buffer.getUnsignedShort();
			while (size-- > 0) {
				int index = buffer.getInt();
				int value = buffer.getInt();
				script.switchTables.get(table).put(index, value);
			}
		}

		buffer.position(0);
		script.name = buffer.getCString();
		script.opcodes = new int[ops];
		script.intOperands = new int[ops];
		script.stringOperands = new String[ops];

		int op = 0;
		while (buffer.position() < trailerPosition) {
			int opcode = buffer.getUnsignedShort();
			if (opcode == 3) {
				script.stringOperands[op] = buffer.getCString();
			} else if (opcode >= 100 || opcode == 21 || opcode == 38 || opcode == 39) {
				script.intOperands[op] = buffer.getByte();
			} else {
				script.intOperands[op] = buffer.getInt();
			}

			script.opcodes[op++] = opcode;
		}

		return script;
	}

	private int intArgsCount, stringArgsCount, intStackDepth, stringStackDepth;
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
	 * Gets the string operand at the specified index.
	 * 
	 * @param index The index.
	 * @return The string operand.
	 */
	public String getStringOperand(int index) {
		return stringOperands[index];
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
	 * Gets the {@link List} of switch tables.
	 * 
	 * @return The list of switch tables.
	 */
	public List<Map<Integer, Integer>> getSwitchTables() {
		return switchTables;
	}

}