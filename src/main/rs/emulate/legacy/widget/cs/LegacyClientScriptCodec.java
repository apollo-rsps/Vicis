package rs.emulate.legacy.widget.cs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rs.emulate.shared.util.DataBuffer;

import com.google.common.collect.ImmutableList;

/**
 * Encodes and decodes {@link LegacyClientScript}s stored in binary form.
 *
 * @author Major
 */
public final class LegacyClientScriptCodec {

	/**
	 * Decodes a {@link List} of {@link LegacyClientScript}s from the specified {@link DataBuffer}.
	 * 
	 * @param buffer The DataBuffer.
	 * @return The List of LegacyClientScripts.
	 */
	public static List<LegacyClientScript> decode(DataBuffer buffer) {
		int count = buffer.getUnsignedByte();
		List<RelationalOperator> operators = (count == 0) ? ImmutableList.of() : new ArrayList<>(count);
		int[] defaults = new int[count];

		for (int index = 0; index < count; index++) {
			RelationalOperator operator = RelationalOperator.valueOf(buffer.getUnsignedByte());
			operators.add(operator);
			defaults[index] = buffer.getUnsignedShort();
		}

		count = buffer.getUnsignedByte();
		List<LegacyClientScript> scripts = (count == 0) ? ImmutableList.of() : new ArrayList<>(count);

		for (int index = 0; index < count; index++) {
			int instructionCount = buffer.getUnsignedShort();
			List<LegacyInstruction> instructions = new ArrayList<>(instructionCount);

			for (int instruction = 0; instruction < instructionCount; instruction++) {
				LegacyInstructionType type = LegacyInstructionType.valueOf(buffer.getUnsignedShort());
				int[] operands = new int[type.getOperandCount()];

				for (int operand = 0; operand < operands.length; operand++) {
					operands[operand] = buffer.getUnsignedShort();
				}

				instructions.add(LegacyInstruction.create(type, operands));
			}

			scripts.add(new LegacyClientScript(operators.get(index), defaults[index], instructions));
		}

		return scripts;
	}

	/**
	 * Encodes the {@link List} of {@link LegacyClientScript}s into a {@link DataBuffer}.
	 * 
	 * @param scripts The List of LegacyClientScripts.
	 * @return The DataBuffer.
	 */
	public static DataBuffer encode(List<LegacyClientScript> scripts) {
		int count = scripts.size();

		DataBuffer operators = DataBuffer.allocate(count * Byte.BYTES);
		DataBuffer defaults = DataBuffer.allocate(count * Short.BYTES);
		List<DataBuffer> buffers = new ArrayList<>(count);
		int size = count * (Short.BYTES + Byte.BYTES);

		for (int index = 0; index < count; index++) {
			LegacyClientScript script = scripts.get(index);

			operators.putByte(script.getRelationalOperator().getValue());
			defaults.putShort(script.getDefault());

			List<LegacyInstruction> instructions = script.getInstructions();
			DataBuffer buffer = DataBuffer.allocate(instructions.size() * Short.BYTES);

			for (LegacyInstruction instruction : instructions) {
				buffer.putShort(instruction.getType().toInteger());
				Arrays.stream(instruction.getOperands()).forEach(buffer::putShort);
			}

			size += buffer.position();
			buffers.add(buffer.flip());
		}

		DataBuffer buffer = DataBuffer.allocate(size);
		buffer.put(operators.flip()).put(defaults.flip());
		buffers.forEach(buffer::put);

		return buffer.flip();
	}

}