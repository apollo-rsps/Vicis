package rs.emulate.legacy.config.varbit;

import rs.emulate.legacy.config.DefaultConfigDefinition;
import rs.emulate.legacy.config.SerializableProperty;
import rs.emulate.shared.util.DataBuffer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static rs.emulate.legacy.config.varbit.BitVariableProperty.VARIABLE;

/**
 * A default {@link BitVariableDefinition} used as a base for an actual definition.
 *
 * @param <T> The type of {@link BitVariableDefinition} this default is for.
 * @author Major
 */
public class DefaultBitVariableDefinition<T extends BitVariableDefinition> extends DefaultConfigDefinition<T> {

	@Override
	protected Map<Integer, SerializableProperty<?>> init() {
		Map<Integer, SerializableProperty<?>> properties = new HashMap<>(1);

		BiConsumer<DataBuffer, Variable> encoder = (buffer, bits) -> buffer.putShort(bits.getVariable())
				.putByte(bits.getHigh()).putByte(bits.getLow());

		Function<DataBuffer, Variable> decoder = buffer -> {
			int variable = buffer.getUnsignedShort();
			int low = buffer.getUnsignedByte();
			int high = buffer.getUnsignedByte();

			return new Variable(variable, high, low);
		};

		properties.put(1, new SerializableProperty<>(VARIABLE, Variable.EMPTY, encoder, decoder,
				Short.BYTES + 2 * Byte.BYTES, input -> Optional.empty())); // FIXME parser

		return properties;
	}

}