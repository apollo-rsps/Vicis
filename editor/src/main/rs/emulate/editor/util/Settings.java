package rs.emulate.editor.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

/**
 * A table of settings for the editor.
 *
 * @author Major
 */
public final class Settings { // TODO is this stuff still necessary?

	/**
	 * The map of constant names to values.
	 */
	private final Map<String, String> constants = new HashMap<>();

	/**
	 * The map of setting names to values.
	 */
	private final Map<String, String> settings = new HashMap<>();

	/**
	 * Creates the settings table.
	 */
	public Settings() {
		addDefaultValues();
	}

	/**
	 * Gets the setting with the specified name.
	 *
	 * @param name The name.
	 * @return The setting value.
	 * @throws IllegalArgumentException If the setting does not exist.
	 */
	public String get(String name) {
		String raw = verifySetting(name);
		List<Integer> indices = findInterpolationIndices(raw);

		Set<String> interpolated = new HashSet<>();
		interpolated.add(name);
		return interpolate(raw, indices, interpolated);
	}

	/**
	 * Parses a boolean value from the settings.
	 *
	 * @param name The name of the setting.
	 * @return {@code true} if the value of the setting is {@code "true"}, otherwise {@code false}.
	 */
	public boolean getBoolean(String name) {
		return Boolean.parseBoolean(verifySetting(name));
	}

	/**
	 * Gets the constant with the specified name.
	 *
	 * @param name The name of the constant.
	 * @return The constant value.
	 * @throws IllegalArgumentException If the constant does not exist.
	 */
	public String getConstant(String name) {
		return Objects.requireNonNull(constants.get(name), "No setting called " + name + " exists.");
	}

	/**
	 * Sets the value of a setting.
	 *
	 * @param name The name of the setting.
	 * @param value The value of the setting.
	 */
	public void put(String name, String value) {
		settings.put(name, value);
	}

	/**
	 * Writes the data stored in this Set of settings to the specified {@link Path}.
	 *
	 * @param path The Path.
	 * @throws IOException If there is an error writing to the specified path.
	 */
	public void writeTo(Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"))) {
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + '\n');
			writer.write("<settings>");

			writeEntries(writer, "constant", constants.entrySet(), 1);
			writeEntries(writer, "configurable", settings.entrySet(), 1);

			writer.write("</settings>");
		}
	}

	/**
	 * Gets the constants, as an {@link ImmutableMap}. Used by the unit test.
	 *
	 * @return The constants.
	 */
	protected Map<String, String> getConstants() {
		return ImmutableMap.copyOf(constants);
	}

	/**
	 * Removes the setting with the specified name from the {@link Map}.
	 *
	 * @param name The name of the setting.
	 */
	protected void remove(String name) {
		settings.remove(name);
	}

	/**
	 * Adds the default value of settings to the map.
	 */
	private void addDefaultValues() {
		constants.put("user-home", System.getProperty("user.home"));

		settings.put("load-previous-on-startup", "false");
		settings.put("low-memory", "false");
		settings.put("backup-before-editing", "true");

		settings.put("win-config-path", "$user-home$/AppData/Roaming/Vicis");
		settings.put("unix-config-path", "$user-home$/.vicis");
		settings.put("config-path", PathUtils.ON_WINDOWS ? "$win-config-path$" : "$unix-config-path$");

		settings.put("backup-path", "$config-path$/backups");
		settings.put("data-path", "./data");
		settings.put("resources-path", "$data-path$/resources");
	}

	/**
	 * Finds the indices of the interpolation indicators, '$', in the specified setting.
	 *
	 * @param setting The setting.
	 * @return The {@link List} of indices.
	 */
	private List<Integer> findInterpolationIndices(String setting) {
		List<Integer> indices = new ArrayList<>(6);

		char[] chars = setting.toCharArray();
		for (int index = 0; index < chars.length; index++) {
			if (chars[index] == '$') {
				indices.add(index);
			}
		}

		return indices;
	}

	/**
	 * Gets the setting with the specified name.
	 *
	 * @param name The name.
	 * @return The setting.
	 * @throws IllegalArgumentException If the setting does not exist.
	 */
	private String getRaw(String name) {
		String value = constants.get(name);
		return (value == null) ? verifySetting(name) : value;
	}

	/**
	 * Interpolates the value of a setting.
	 *
	 * @param setting The raw value of the setting being retrieved.
	 * @param indices The {@link List} of indices of the interpolation indicators, '$'.
	 * @param settings The {@link Set} of settings already interpolated, to prevent infinite recursion.
	 * @return The interpolated setting.
	 */
	private String interpolate(String setting, List<Integer> indices, Set<String> settings) {
		int size = indices.size();

		if (size >= 2) {
			for (int index = 0; index < size; index += 2) {
				int start = indices.get(index), end = indices.get(index + 1);
				String first = (start == 0) ? "" : setting.substring(0, start);
				String second = setting.substring(end + 1);

				String interpolant = setting.substring(start + 1, end); // trim the $s
				if (settings.contains(interpolant)) { // Recursive interpolation
					throw new IllegalArgumentException("Cannot have a recursive setting.");
				}

				String value = getRaw(interpolant);
				List<Integer> interpolantIndices = findInterpolationIndices(value);
				if (interpolantIndices.size() > 1) {
					settings.add(interpolant);
					value = interpolate(value, interpolantIndices, settings);
				}

				setting = first + value + second;
			}
		}

		return setting;
	}

	/**
	 * Gets the setting with the specified name, verifying that it is not null.
	 *
	 * @param name The name of the setting.
	 * @return The value of the setting.
	 * @throws IllegalArgumentException If the setting does not exist.
	 */
	private String verifySetting(String name) {
		String value = settings.get(name);
		Preconditions.checkArgument(value != null, "No setting called " + name + " exists.");
		return value;
	}

	/**
	 * Writes the specified {@link Set} of {@link Map} {@link Entry} objects to the specified {@link Writer}.
	 *
	 * @param writer The Writer.
	 * @param name The name of the encapsulating XML tag.
	 * @param entries The {@link Set} of {@link Entry} objects to write.
	 * @param indentation The level of indentation of the written text.
	 * @throws IOException If there is an error writing to the Writer.
	 */
	private <K, V> void writeEntries(Writer writer, String name, Set<Entry<K, V>> entries, int indentation)
			throws IOException {
		StringBuilder builder = new StringBuilder(indentation);
		for (int times = 0; times < indentation; times++) {
			builder.append('\t');
		}

		String baseIndent = builder.toString();
		String entryIndent = builder.append('\t').toString();
		String valueIndent = builder.append('\t').toString();

		writer.write(baseIndent + '<' + name + "s>");

		String open = '<' + name + '>', close = "</" + name + '>';
		for (Map.Entry<K, V> entry : entries) {
			writer.write(entryIndent + open + '\n');
			writer.write(valueIndent + "<name>" + entry.getKey() + "</name>" + '\n');
			writer.write(valueIndent + "<value>" + entry.getValue() + "</value>" + '\n');
			writer.write(entryIndent + close + '\n');
		}

		writer.write(baseIndent + "</" + name + "s>");
	}

}