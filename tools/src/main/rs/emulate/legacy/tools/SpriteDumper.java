package rs.emulate.legacy.tools;

import rs.emulate.legacy.AccessMode;
import rs.emulate.legacy.IndexedFileSystem;
import rs.emulate.legacy.graphics.sprite.Sprite;
import rs.emulate.legacy.graphics.sprite.SpriteDecoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Dumps decoded Sprites into files.
 *
 * @author Major
 */
public final class SpriteDumper {

	/**
	 * The {@link Path} to the resources directory containing the caches.
	 */
	private static final Path RESOURCES_PATH = Paths.get("data/resources");

	/**
	 * Creates a {@link SpriteDumper} for the {@link Sprite}(s) with the specified name.
	 *
	 * @param fs The {@link IndexedFileSystem} containing the Sprite(s).
	 * @param version The version of the cache.
	 * @param name The name of the Sprite(s).
	 * @return The SpriteDumper.
	 * @throws IOException If there is an error decoding the Sprite(s).
	 */
	public static SpriteDumper create(IndexedFileSystem fs, String version, String name) throws IOException {
		Path output = Paths.get("./data/dump/sprites/" + version);
		Files.createDirectories(output);

		SpriteDecoder decoder = SpriteDecoder.create(fs, name);
		return new SpriteDumper(decoder, output);
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
			SpriteDumper dumper = SpriteDumper.create(fs, version, name);
			dumper.dump("png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The SpriteDecoder used to read the Sprites from the cache.
	 */
	private final SpriteDecoder decoder;

	/**
	 * The Path to the output directory.
	 */
	private final Path output;

	/**
	 * Creates the SpriteDumper.
	 *
	 * @param decoder The {@link SpriteDecoder} used to read the Sprites from the cache.
	 * @param output The {@link Path} to the output directory.
	 */
	public SpriteDumper(SpriteDecoder decoder, Path output) {
		this.decoder = decoder;
		this.output = output;
	}

	/**
	 * Dumps all of the {@link Sprite}s into the pre-specified directory.
	 *
	 * @param format The name of the image format.
	 * @throws IOException If there is an error decoding the Sprites.
	 */
	public void dump(String format) throws IOException {
		List<Sprite> sprites = decoder.decode();

		for (int index = 0; index < sprites.size(); index++) {
			Sprite sprite = sprites.get(index);

			int width = sprite.getWidth(), height = sprite.getHeight();
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

			int[] raster = sprite.getRaster();
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					int colour = raster[x + y * width];
					image.setRGB(x, y, colour);
				}
			}

			ImageIO.write(image, format, output.resolve(Paths.get(index + "." + format)).toFile());
		}
	}

}