package rs.emulate.shared.prop;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import rs.emulate.util.Assertions;

import com.google.common.base.Preconditions;

/**
 * A {@link PropertyType} that can be created at any point during runtime (rather than during the linking process as
 * part of an enumerated type). Useful when there are lots of similar properties that would result in a huge amount of
 * enumerators (such as original and replacement colours). This class is immutable.
 * 
 * @author Major
 */
public final class DynamicPropertyType implements PropertyType {

	/**
	 * The map (used as a cache) of opcodes to DynamicPropertyTypes.
	 */
	private static final Map<Integer, DynamicPropertyType> types = new HashMap<>();

	/**
	 * Creates a new DynamicPropertyType with the specified name and opcode, or returns a previously-created value from
	 * the cache.
	 * 
	 * @param name The name of the property. Must not be {@code null}.
	 * @param opcode The opcode of the property. Must be positive (i.e. {@code > 0}).
	 * @return The DynamicPropertyType.
	 */
	public static DynamicPropertyType valueOf(String name, int opcode) {
		Assertions.checkPositive(opcode, "Opcode must be positive.");
		int hash = Objects.hash(name, opcode);
		DynamicPropertyType cached = types.get(hash);

		if (cached == null) {
			cached = new DynamicPropertyType(name, opcode);
			types.put(hash, cached);
		}

		return cached;
	}

	/**
	 * The name of this property.
	 */
	private final String name;

	/**
	 * The opcode of this property.
	 */
	private final int opcode;

	/**
	 * Creates the DynamicPropertyType.
	 * 
	 * @param name The name of the property.
	 * @param opcode The opcode of the property.
	 */
	private DynamicPropertyType(String name, int opcode) {
		Preconditions.checkArgument(name != null && !name.isEmpty(), "Name of a property cannot be null or empty.");
		Assertions.checkPositive(opcode, "Opcode must be positive.");

		this.name = name;
		this.opcode = opcode;
	}

	@Override
	public int getOpcode() {
		return opcode;
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
	public String toString() {
		return name;
	}

}