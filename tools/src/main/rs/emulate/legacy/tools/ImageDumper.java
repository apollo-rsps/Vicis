package rs.emulate.legacy.tools;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;

import rs.emulate.legacy.IndexedFileSystem;
import rs.emulate.legacy.graphics.image.ImageDecoder;
import rs.emulate.legacy.graphics.image.IndexedImage;

/**
 * Dumps decoded Images into files.
 *
 * @author Major
 */
public final class ImageDumper {

	/**
	 * Creates a {@link ImageDumper} for the {@link IndexedImage}(s) with the specified name.
	 * 
	 * @param fs The {@link IndexedFileSystem} containing the Image(s).
	 * @param name The name of the Image(s).
	 * @return The ImageDumper.
	 * @throws IOException If there is an error decoding the Image(s).
	 */
	public static ImageDumper create(IndexedFileSystem fs, String name) throws IOException {
		ImageDecoder decoder = ImageDecoder.create(fs, name);
		Path output = Paths.get("./data/dump/images");
		Files.createDirectories(output);

		return new ImageDumper(decoder, output, name);
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

			ImageIO.write(buffered, format, output.resolve(Paths.get(name + "." + format.toLowerCase())).toFile());
		}
	}

}