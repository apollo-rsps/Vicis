package rs.emulate.legacy.config.varbit;

import static rs.emulate.legacy.config.varbit.BitVariableProperty.VARIABLE;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.ConfigPropertyMap;
import rs.emulate.legacy.config.DefaultConfigDefinition;
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
	 * A {@link Supplier} that returns a {@link ConfigPropertyMap} copy of this default definition.
	 */
	public static final Supplier<ConfigPropertyMap> SUPPLIER = DEFAULT::toPropertyMap;

	/**
	 * Creates the DefaultBitVariableDefinition.
	 */
	public DefaultBitVariableDefinition() {
		super();
	}

	@Override
	protected Map<Integer, ConfigProperty<?>> init() {
		Map<Integer, ConfigProperty<?>> properties = new HashMap<>(1);

		BiConsumer<DataBuffer, Variable> encoder = (buffer, bits) -> buffer.putShort(bits.getVariable())
				.putByte(bits.getHigh()).putByte(bits.getLow());

		Function<DataBuffer, Variable> decoder = buffer -> {
			int variable = buffer.getUnsignedShort();
			int low = buffer.getUnsignedByte();
			int high = buffer.getUnsignedByte();

			return new Variable(variable, high, low);
		};

		properties.put(1, new ConfigProperty<>(VARIABLE, Variable.EMPTY, encoder, decoder,
				Short.BYTES + 2 * Byte.BYTES, input -> Optional.empty())); // XXX

		return properties;
	}

}