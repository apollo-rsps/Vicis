package rs.emulate.legacy.config;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import rs.emulate.legacy.archive.Archive;
import rs.emulate.legacy.config.Suppliers.DefinitionSupplier;
import rs.emulate.shared.prop.PropertyMap;
import rs.emulate.shared.util.DataBuffer;

/**
 * Decodes data from ArchiveEntry objects in the {@code config} {@link Archive}.
 * 
 * @author Major
 *
 * @param <T> The type the data is decoded into.
 */
public final class ConfigDecoder<T extends MutableDefinition> {

	/**
	 * The Buffer containing the data.
	 */
	private final DataBuffer data;

	/**
	 * The Buffer containing the indices.
	 */
	private final DataBuffer index;

	/**
	 * The DefinitionSupplier for the specified type.
	 */
	private final DefinitionSupplier<T> supplier;

	/**
	 * Creates the ConfigDecoder.
	 * 
	 * @param config The config {@link Archive}.
	 * @param supplier The {@link DefinitionSupplier}.
	 * @throws FileNotFoundException If either the data or the index ArchiveEntry could not be found.
	 */
	public ConfigDecoder(Archive config, DefinitionSupplier<T> supplier) throws FileNotFoundException {
		this.data = config.getEntry(supplier.getName() + ConfigConstants.DATA_EXTENSION).getBuffer();
		this.index = config.getEntry(supplier.getName() + ConfigConstants.INDEX_EXTENSION).getBuffer();
		this.supplier = supplier;
	}

	/**
	 * Decodes the data into a {@link List} of definitions.
	 * 
	 * @return The List of definitions.
	 */
	public List<T> decode() {
		int count = index.getUnsignedShort();
		List<T> definitions = new ArrayList<>(count);

		int position = Short.BYTES; // Skip the count
		for (int id = 0; id < count; id++) {
			data.position(position);
			definitions.add(decode(id));
			position += index.getUnsignedShort();
		}

		return definitions;
	}

	/**
	 * Decodes an individual definition with the specified id from the specified {@link DataBuffer}.
	 *
	 * @param id The id.
	 * @return The decoded definition.
	 */
	private T decode(int id) {
		PropertyMap properties = supplier.supplyDefault();

		int opcode = data.getUnsignedByte();
		while (opcode != 0) {
			properties.get(opcode).decode(data);
			opcode = data.getUnsignedByte();
		}

		return supplier.createDefinition(id, properties);
	}

}