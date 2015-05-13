package rs.emulate.editor;

import java.io.IOException;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.stage.Stage;
import rs.emulate.legacy.AccessMode;
import rs.emulate.legacy.Cache;
import rs.emulate.legacy.FileDescriptor;
import rs.emulate.legacy.IndexedFileSystem;
import rs.emulate.shared.util.DataBuffer;

import com.google.common.base.MoreObjects;

/**
 * The core class.
 *
 * @author Major
 */
public final class Editor extends Application {

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
	public void start(Stage stage) throws Exception {
		Application.Parameters parameters = getParameters();
		temp(parameters);
	}

	private void temp(Application.Parameters parameters) {
		String version = MoreObjects.firstNonNull(parameters.getNamed().get("version"), "317");
		try {
			this.fs = new IndexedFileSystem(Paths.get("data/resources/", version), AccessMode.READ);

			@SuppressWarnings("unused")
			Cache cache = initCache();
		} catch (Exception e) {
			e.printStackTrace();
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

}