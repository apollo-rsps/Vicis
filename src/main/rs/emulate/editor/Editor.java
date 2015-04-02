package rs.emulate.editor;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.stage.Stage;
import rs.emulate.legacy.AccessMode;
import rs.emulate.legacy.Cache;
import rs.emulate.legacy.FileDescriptor;
import rs.emulate.legacy.IndexedFileSystem;
import rs.emulate.legacy.archive.Archive;
import rs.emulate.legacy.config.ConfigDecoder;
import rs.emulate.legacy.config.MutableConfigDefinition;
import rs.emulate.legacy.config.Suppliers;
import rs.emulate.shared.util.DataBuffer;

import com.google.common.base.MoreObjects;
import com.google.common.primitives.Ints;

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
			test(cache);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		System.exit(0);
	}

	private Cache initCache() throws IOException { // TODO this is shit
		Cache cache = new Cache();

		for (int type = 0; type < fs.getIndexCount(); type++) {
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

	private void test(Cache cache) throws IOException { // temporary
		FileDescriptor descriptor = new FileDescriptor(0, 2);
		Archive config = cache.getArchive(descriptor);
		ConfigDecoder<? extends MutableConfigDefinition> decoder = new ConfigDecoder<>(config, Suppliers.OBJECT_SUPPLIER);
		List<? extends MutableConfigDefinition> definitions = decoder.decode();

		System.out.print("Type an id: ");
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				String in = scanner.nextLine();

				if (in.equals("q")) {
					break;
				}

				Integer value = Ints.tryParse(in);
				if (value != null && value >= 0 && value < definitions.size()) {
					System.out.println(definitions.get(value));
				}
			}
		}
	}

}