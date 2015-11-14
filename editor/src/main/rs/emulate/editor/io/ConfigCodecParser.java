package rs.emulate.editor.io;

import com.google.common.base.Preconditions;
import org.apollo.util.xml.XmlNode;
import org.apollo.util.xml.XmlParser;
import org.xml.sax.SAXException;
import rs.emulate.editor.util.PathUtils;
import rs.emulate.legacy.archive.ArchiveUtils;
import rs.emulate.legacy.config.DefinitionSupplier;
import rs.emulate.legacy.config.MutableConfigDefinition;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Parses an {@code XML} file containing the types of
 *
 * @author Major
 */
public final class ConfigCodecParser {

	/**
	 * The config file path for debugging.
	 */
	public static final Path DEBUG_CONFIG_FILE_PATH = Paths.get("data/config.xml");

	/**
	 * The Path to the directory containing the custom classes.
	 */
	private static final Path CUSTOM_CLASS_PATH = PathUtils.getStoragePath().resolve("codec");

	/**
	 * The Path to the directory containing the custom classes, for debugging.
	 */
	private static final Path DEBUG_CLASS_PATH = Paths.get("data/codec");

	/**
	 * The InputStream to read input from.
	 */
	private final InputStream input;

	/**
	 * The XmlParser.
	 */
	private final XmlParser parser = new XmlParser();

	/**
	 * Creates the ConfigCodecParser.
	 *
	 * @param input The {@link BufferedInputStream} to read input from.
	 * @throws SAXException If there is an error creating the XmlParser.
	 */
	public ConfigCodecParser(BufferedInputStream input) throws SAXException {
		this.input = input;
	}

	/**
	 * Creates the ConfigCodecParser.
	 *
	 * @param path The {@link Path} to the file to parse.
	 * @throws IOException If there is an error creating a FileInputStream for the specified Path.
	 * @throws SAXException If there is an error creating the XmlParser.
	 */
	public ConfigCodecParser(Path path) throws IOException, SAXException {
		this(new BufferedInputStream(Files.newInputStream(path)));
	}

	/**
	 * Parses the backing {@link InputStream}.
	 *
	 * @return The {@link Map} of {@link ArchiveUtils#hash archive entry hashes} to {@link DefinitionSupplier}s.
	 * @throws Exception If there is an error parsing the file.
	 */
	public Map<Integer, DefinitionSupplier<?>> parse() throws Exception {
		XmlNode root = parser.parse(input);
		Preconditions.checkArgument(root.getName().equals("codecs"), "XML root node must be named 'codecs'.");

		XmlNode legacy = root.getChild("legacy").orElseThrow(createExceptionSupplier("codecs", "legacy"));
		Collection<XmlNode> children = legacy.getChildren();

		Map<Integer, DefinitionSupplier<?>> defaults = new HashMap<>(children.size());
		for (XmlNode child : children) {
			String name = child.getAttribute("name").orElseThrow(
					() -> new IllegalArgumentException("<legacy> child nodes must have a 'name' attribute."));

			XmlNode definition = child.getChild("definition").orElseThrow(createExceptionSupplier(name, "definition"));
			XmlNode immutable = child.getChild("default").orElseThrow(createExceptionSupplier(name, "default"));

			Class<? extends MutableConfigDefinition> definitionClass = loadClass(definition.getValue());
			@SuppressWarnings("rawtypes")
			Class defaultClass = loadClass(immutable.getValue());

			@SuppressWarnings("unchecked")
			DefinitionSupplier<?> supplier = DefinitionSupplier.create(name, definitionClass, defaultClass);
			int hash = ArchiveUtils.hash(name);
			defaults.put(hash, supplier);
		}

		return defaults;
	}

	/**
	 * Creates a {@link Supplier} for an {@link IllegalArgumentException}, with a message indicating which child
	 * {@link XmlNode} is missing from the appropriate parent node.
	 *
	 * @param parent The parent XmlNode.
	 * @param child The child XmlNode.
	 * @return The Supplier.
	 */
	private Supplier<IllegalArgumentException> createExceptionSupplier(String parent, String child) {
		return () -> new IllegalArgumentException(parent + " node must contain a child named " + child + ".");
	}

	/**
	 * Attempts to load a {@link Class} dynamically, first by looking in the current classpath, then looking for
	 * appropriately-named files in the {@link #DEBUG_CLASS_PATH} and {@link #CUSTOM_CLASS_PATH} directories.
	 *
	 * @param name The name of the class.
	 * @return The Class.
	 * @throws ClassNotFoundException If the Class could not be found.
	 * @throws IOException If there was an error reading from the Class file.
	 */
	@SuppressWarnings("unchecked")
	private <T> Class<T> loadClass(String name) throws ClassNotFoundException, IOException {
		try {
			return (Class<T>) Class.forName(name);
		} catch (ClassNotFoundException exception) {
			URL[] urls = { DEBUG_CLASS_PATH.toUri().toURL(), CUSTOM_CLASS_PATH.toUri().toURL() };

			try (URLClassLoader loader = new URLClassLoader(urls)) {
				int index = name.lastIndexOf(".");
				String file = name.substring(index);

				return (Class<T>) loader.loadClass(file);
			}
		}
	}

}