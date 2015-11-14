package rs.emulate.legacy.tools;

import rs.emulate.legacy.AccessMode;
import rs.emulate.legacy.IndexedFileSystem;
import rs.emulate.legacy.graphics.image.ImageDecoder;
import rs.emulate.legacy.graphics.image.IndexedImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Dumps decoded Images into files.
 *
 * @author Major
 */
public final class ImageDumper {

	/**
	 * The {@link Path} to the resources directory containing the caches.
	 */
	private static final Path RESOURCES_PATH = Paths.get("data/resources");

	/**
	 * Creates a {@link ImageDumper} for the {@link IndexedImage}(s) with the specified name.
	 *
	 * @param fs The {@link IndexedFileSystem} containing the Image(s).
	 * @param version The version of the cache.
	 * @param name The name of the Image(s).
	 * @return The ImageDumper.
	 * @throws IOException If there is an error decoding the Image(s).
	 */
	public static ImageDumper create(IndexedFileSystem fs, String version, String name) throws IOException {
		Path output = Paths.get("./data/dump/images/" + version);
		Files.createDirectories(output);

		ImageDecoder decoder = ImageDecoder.create(fs, name);
		return new ImageDumper(decoder, output, name);
	}

	/**
	 * The main entry point of the application.
	 *
	 * @param args The application arguments.
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("SpriteDumper must have two arguments: cache version and sprite name.");
			System.exit(1);
		}

		String version = args[0];
		String name = args[1];

		try {
			IndexedFileSystem fs = new IndexedFileSystem(RESOURCES_PATH.resolve(version), AccessMode.READ);
			ImageDumper dumper = ImageDumper.create(fs, version, name);
			dumper.dump("png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The ImageDecoder used to read the Images from the cache.
	 */
	private final ImageDecoder decoder;

	/**
	 * The name of the IndexedImage.
	 */
	private final String name;

	/**
	 * The Path to the output directory.
	 */
	private final Path output;

	/**
	 * Creates the ImageDumper.
	 *
	 * @param decoder The {@link ImageDecoder} used to read the {@link IndexedImage}s from the cache.
	 * @param output The {@link Path} to the output directory.
	 * @param name The name of the Image.
	 */
	public ImageDumper(ImageDecoder decoder, Path output, String name) {
		this.decoder = decoder;
		this.output = output;
		this.name = name;
	}

	/**
	 * Dumps all of the {@link IndexedImage}s into the pre-specified directory.
	 *
	 * @param format The name of the image format.
	 * @throws IOException If there is an error decoding the Images.
	 */
	public void dump(String format) throws IOException {
		List<IndexedImage> images = decoder.decode();

		for (int index = 0; index < images.size(); index++) {
			IndexedImage image = images.get(index);

			int width = image.getWidth(), height = image.getHeight();
			BufferedImage buffered = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

			int[] raster = image.getRaster();
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					int colour = raster[x + y * width];
					buffered.setRGB(x, y, colour);
				}
			}

			ImageIO.write(buffered, format, output.resolve(Paths.get(name + "_" + index + "." + format)).toFile());
		}
	}

}