package rs.emulate.legacy.config.varbit;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.DefaultConfigDefinition;
import rs.emulate.shared.prop.PropertyMap;
import rs.emulate.shared.util.DataBuffer;

/**
 * A default {@link BitVariableDefinition} used as a base for an actual definition.
 * 
 * @author Major
 */
public class DefaultBitVariableDefinition extends DefaultConfigDefinition {

	/**
	 * The default definition.
	 */
	private static final DefaultBitVariableDefinition DEFAULT = new DefaultBitVariableDefinition();

	/**
	 * A {@link Supplier} that returns a {@link PropertyMap} copy of this default definition.
	 */
	public static final Supplier<PropertyMap> SUPPLIER = DEFAULT::toPropertyMap;

	/**
	 * Creates the default bit variable definition.
	 */
	private DefaultBitVariableDefinition() {
		super();
	}

	@Override
	protected Map<Integer, ConfigProperty<?>> init() {
		Map<Integer, ConfigProperty<?>> properties = new HashMap<>(1);

		BiConsumer<DataBuffer, Variable> encoder = (buffer, bits) -> buffer.putShort(bits.getVariable()).putByte(bits.getHigh())
				.putByte(bits.getLow());

		Function<DataBuffer, Variable> decoder = buffer -> {
			int variable = buffer.getUnsignedShort();
			int low = buffer.getUnsignedByte();
			int high = buffer.getUnsignedByte();

			return new Variable(variable, high, low);
		};

		properties.put(1, new ConfigProperty<>(BitVariableProperty.VARIABLE, Variable.EMPTY, encoder, decoder, Short.BYTES
				+ 2 * Byte.BYTES));

		return properties;
	}

}