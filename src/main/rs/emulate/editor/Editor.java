package rs.emulate.editor;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;
import rs.emulate.legacy.AccessMode;
import rs.emulate.legacy.Cache;
import rs.emulate.legacy.FileDescriptor;
import rs.emulate.legacy.IndexedFileSystem;
import rs.emulate.legacy.archive.Archive;
import rs.emulate.legacy.archive.CompressionType;
import rs.emulate.legacy.config.ConfigDecoder;
import rs.emulate.legacy.config.ConfigEncoder;
import rs.emulate.legacy.config.Suppliers;
import rs.emulate.legacy.config.animation.AnimationDefinition;
import rs.emulate.shared.util.DataBuffer;

import com.google.common.base.MoreObjects;

/**
 * The core class.
 * 
 * @author Major
 */
public final class Editor extends Application {

	/**
	 * The application settings.
	 */
	public static final Settings settings = new Settings();

	/**
	 * The main entry point for the editor.
	 *
	 * @param args The program arguments.
	 */
	public static void main(String[] args) {
		try {
			Application.launch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The indexed file system currently in use.
	 */
	private IndexedFileSystem fs;

	@Override
	public void start(Stage primaryStage) {
		Application.Parameters parameters = getParameters();
		String version = MoreObjects.firstNonNull(parameters.getNamed().get("version"), "319");

		try {
			this.fs = new IndexedFileSystem(Paths.get("data/resources/", version), AccessMode.READ_WRITE);

			Cache cache = initCache();
			cli(cache);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		System.exit(0);
	}

	private Cache initCache() throws IOException { // TODO this is shit
		Cache cache = new Cache();

		for (int type = 0; type < 5; type++) {
			for (int file = 0; file < fs.getFileCount(type); file++) {
				FileDescriptor descriptor = new FileDescriptor(type, file);
				DataBuffer buffer = fs.getFile(descriptor);

				if (type == 0 && file == 0) { // skip the empty block.
					continue;
				}

				cache.putFile(descriptor, buffer);
			}
		}

		return cache;
	}

	private void cli(Cache cache) throws IOException { // temporary
		FileDescriptor descriptor = new FileDescriptor(0, 2);
		Archive config = cache.getArchive(descriptor);
		ConfigDecoder<AnimationDefinition> decoder = new ConfigDecoder<>(config, Suppliers.ANIMATION_SUPPLIER);
		List<AnimationDefinition> items = decoder.decode();

		ConfigEncoder<AnimationDefinition> encoder = new ConfigEncoder<>(AnimationDefinition.ENTRY_NAME, items);
		Archive archive = encoder.encodeInto(config);
		cache.replaceArchive(descriptor, archive, CompressionType.ENTRY_COMPRESSION);

		cache.encode(Paths.get("./data/test/317"));
	}

}