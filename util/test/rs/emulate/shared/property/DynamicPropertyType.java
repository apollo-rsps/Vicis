package rs.emulate.shared.property;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import rs.emulate.legacy.config.ConfigPropertyType;
import rs.emulate.util.Assertions;

/**
 * A {@link ConfigPropertyType} that can be created at any point during runtime (rather than during the linking process,
 * as part of an enumerated type). Useful when there are lots of similar properties that would result in a huge amount
 * of enumerators (such as original and replacement colours). This class is immutable.
 * 
 * @author Major
 */
public final class DynamicPropertyType implements ConfigPropertyType {

	/**
	 * The Map (used as a cache) of opcodes to DynamicPropertyTypes.
	 */
	private static final Map<Integer, DynamicPropertyType> cache = new HashMap<>();

	/**
	 * Creates a new DynamicPropertyType with the specified name and opcode, or returns a previously-created value from
	 * the cache.
	 * 
	 * @param name The name of the DynamicPropertyType. Must not be {@code null}.
	 * @param opcode The opcode of the DynamicPropertyType. Must be positive (i.e. {@code > 0}).
	 * @return The DynamicPropertyType.
	 */
	public static DynamicPropertyType valueOf(String name, int opcode) {
		Assertions.checkPositive(opcode, "Opcode must be positive.");
		int hash = Objects.hash(name, opcode);

		return cache.computeIfAbsent(hash, key -> new DynamicPropertyType(name, opcode));
	}

	/**
	 * The name of this DynamicPropertyType.
	 */
	private final String name;

	/**
	 * The opcode of this DynamicPropertyType.
	 */
	private final int opcode;

	/**
	 * Creates the DynamicPropertyType.
	 * 
	 * @param name The name of the DynamicPropertyType.
	 * @param opcode The opcode of the DynamicPropertyType.
	 */
	private DynamicPropertyType(String name, int opcode) {
		Assertions.checkNonEmpty(name, "Name of a property cannot be null or empty.");
		Assertions.checkPositive(opcode, "Opcode must be positive.");

		this.name = name;
		this.opcode = opcode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, opcode);
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public int opcode() {
		return opcode;
	}

	@Override
	public String toString() {
		return name;
	}

}