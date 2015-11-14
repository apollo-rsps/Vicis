package rs.emulate.modern.script.decomp.instr;

/**
 * The type of an operand for an instruction.
 *
 * @author Major
 */
public enum OperandType {

	/**
	 * The integer operand type.
	 */
	INT(int.class),

	/**
	 * The long operand type.
	 */
	LONG(long.class),

	/**
	 * The String operand type.
	 */
	STRING(String.class);

	/**
	 * The Java {@link Class} representing this OperandType.
	 */
	private final Class<?> clazz;

	/**
	 * Creates the OperandType.
	 *
	 * @param clazz The Java {@link Class} representing this OperandType.
	 */
	private OperandType(Class<?> clazz) {
		this.clazz = clazz;
	}

	/**
	 * Gets the Java {@link Class} representing this OperandType.
	 *
	 * @return The Java Class.
	 */
	public Class<?> getRepresentingClass() {
		return clazz;
	}

}