package rs.emulate.modern.tools;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import rs.emulate.modern.Container;
import rs.emulate.modern.Entry;
import rs.emulate.modern.FileStore;
import rs.emulate.modern.ReferenceTable;
import rs.emulate.shared.util.CacheStringUtils;
import rs.emulate.shared.util.DataBuffer;
import rs.emulate.shared.util.crypto.Xtea;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

@SuppressWarnings("javadoc")
public final class XteaAggregator {

	private enum ProcessResult {
		HAVE_KEY(Color.GREEN), PLAINTEXT_EMPTY(Color.BLUE), HAVE_KEY_FROM_OTHER_CACHE(Color.YELLOW), FAILED(Color.RED);

		private final int rgb;

		private ProcessResult(Color color) {
			this.rgb = color.getRGB();
		}

		public int getRgb() {
			return rgb;
		}
	}

	private static final class CacheRef {

		private final boolean rs3;

		private final FileStore store;

		private final ReferenceTable table;

		public CacheRef(FileStore store, ReferenceTable table, boolean rs3) {
			this.store = store;
			this.table = table;
			this.rs3 = rs3;
		}

		public int findLandscapeFile(int x, int y) {
			if (rs3) {
				return (y << 7) | x;
			}

			String landscapeFile = "l" + x + "_" + y;
			int landscapeIdentifier = CacheStringUtils.hash(landscapeFile);

			for (int id = 0; id < table.capacity(); id++) {
				Entry entry = table.getEntry(id);
				if (entry == null)
					continue;

				if (entry.getIdentifier() == landscapeIdentifier)
					return id;
			}

			return -1; /* doesn't exist */
		}
	}

	private static final class Key {

		public static final Key ZERO = new Key(new int[4]);

		private final int[] k;

		public Key(int[] k) {
			if (k.length != 4)
				throw new IllegalArgumentException();
			this.k = k;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			final Key key = (Key) o;

			if (!Arrays.equals(k, key.k))
				return false;

			return true;
		}

		@Override
		public int hashCode() {
			return Arrays.hashCode(k);
		}

		public boolean isZero() {
			return k[0] == 0 && k[1] == 0 && k[2] == 0 && k[3] == 0;
		}
	}

	public static final int MAP_INDEX = 5;

	public static final int MAP_SIZE = 256;

	public static final int RT_INDEX = 255;

	public static void main(String[] args) throws IOException {
		/* write output table headings */
		System.out.println("Typ\tFile\tVer\tVe'\tCRC\t\t\tCRC'");

		/* open the master cache */
		FileStore store = FileStore.open("./game/data/cache");
		Container rtContainer = Container.decode(store.read(RT_INDEX, MAP_INDEX));
		ReferenceTable rt = ReferenceTable.decode(rtContainer.getData());

		/* find possible landscape keys */
		Set<Key> possibleKeys = readPossibleKeys();

		/* find other caches */
		List<CacheRef> otherCaches = findOtherCaches();

		/* create blank map image */
		BufferedImage map = new BufferedImage(MAP_SIZE, MAP_SIZE, BufferedImage.TYPE_INT_ARGB);

		/* initialise counters */
		int total = 0, haveKey = 0, plaintextEmpty = 0, haveKeyFromOtherCache = 0;

		/* flag indicating if we need to rewrite the reference table */
		boolean rewriteReferenceTable = false;

		/* iterate through all maps */
		for (int x = 0; x < MAP_SIZE; x++) {
			for (int y = 0; y < MAP_SIZE; y++) {
				/* find the file for lX_Y */
				CacheRef cache = new CacheRef(store, rt, false);
				int id = cache.findLandscapeFile(x, y);

				if (id != -1) {
					/* read and process the file */
					ByteBuffer buf = store.read(MAP_INDEX, id).getByteBuffer();
					ProcessResult result = processLandscapeFile(store, rt, id, x, y, buf, possibleKeys, otherCaches);

					/* update rewriteReferenceTable flag appropriately */
					if (result == ProcessResult.PLAINTEXT_EMPTY || result == ProcessResult.HAVE_KEY_FROM_OTHER_CACHE)
						rewriteReferenceTable = true;

					/* set the result colour in the map */
					map.setRGB(x, MAP_SIZE - y, result.getRgb());

					/* increment counters */
					total++;

					if (result == ProcessResult.HAVE_KEY)
						haveKey++;

					if (result == ProcessResult.PLAINTEXT_EMPTY)
						plaintextEmpty++;

					if (result == ProcessResult.HAVE_KEY_FROM_OTHER_CACHE)
						haveKeyFromOtherCache++;
				}
			}
		}

		/* rewrite reference table */
		if (rewriteReferenceTable) {
			/* increment version */
			int version = rt.getVersion();
			rt.setVersion(version + 1);
			System.out.println("(Reference table version: " + version + " -> " + rt.getVersion() + ")");

			/* make container */
			rtContainer = new Container(rtContainer.getType(), rt.encode());

			/* write new container to cache */
			store.write(RT_INDEX, MAP_INDEX, rtContainer.encode());
		}

		/* write the map to disk */
		ImageIO.write(map, "png", new File("cache/data/map.png"));

		/* compute totals */
		int successful = haveKey + plaintextEmpty + haveKeyFromOtherCache;
		int failed = total - successful;

		/* output stats */
		System.out.println(successful + " / " + total + " files successful (" + failed + " failed)");
		System.out.println("    Have key for:                  " + haveKey + " files");
		System.out.println("    Plaintext looks empty for:     " + plaintextEmpty + " files");
		System.out.println("    Have key from other cache for: " + haveKeyFromOtherCache + " files");
	}

	private static ImmutableList<CacheRef> findOtherCaches() throws IOException {
		ImmutableList.Builder<CacheRef> caches = ImmutableList.builder();

		for (File f : new File("cache/data/other-caches").listFiles()) {
			if (!f.isDirectory())
				continue;

			FileStore store = FileStore.open(f.toPath()); // TODO
			ReferenceTable table = ReferenceTable.decode(Container.decode(store.read(RT_INDEX, MAP_INDEX)).getData());
			caches.add(new CacheRef(store, table, f.getName().equals("rs3")));
		}

		return caches.build();
	}

	private static boolean isEmpty(ByteBuffer buf) {
		/*
		 * Lots of the files we don't have keys for are sea, which contains no objects. Looking at the encrypted and
		 * compressed length we can guess if the file contains no objects.
		 */
		int type = buf.get(buf.position()) & 0xFF;
		if (type == Container.COMPRESSION_NONE) {
			/*
			 * A landscape file with no objects has a single zero-valued smart, which occupies 1 byte. We add on the 4
			 * bytes uncompressed length, the 1 byte type and the 2 byte version.
			 */
			return buf.remaining() == 8;
		} else if (type == Container.COMPRESSION_GZIP) {
			/*
			 * gzip-compressed in Java, a single byte occupies 21 bytes. We add the 4 byte uncompressed length, the 4
			 * byte compressed length, the 1 byte type and the 2 byte version.
			 */
			return buf.remaining() == 32;
		} else if (type == Container.COMPRESSION_BZIP2) {
			/*
			 * bzip2-compressed in Java excluding the header, a single byte occupies 33 bytes. We add the 4 byte
			 * uncompressed length, the 4 byte compressed length, the 1 byte type and the 2 byte version.
			 */
			return buf.remaining() == 44;
		} else {
			throw new IllegalArgumentException("Invalid container type.");
		}
	}

	private static boolean isKeyValid(ByteBuffer buf, Key key, boolean rs3) {
		/*
		 * Files in RS3 are not encrypted. Return true if it looks non-empty (i.e. has more than the container header
		 * and trailer) and if the current key we're using is the ZERO key indicating that decryption is not to be
		 * performed.
		 */
		if (rs3) {
			return key.equals(Key.ZERO) && buf.remaining() > 7;
		}

		/*
		 * This functions attempts to check if a key is probably valid whilst doing the absolute minimum amount of work
		 * by only examining the gzip/bzip2 headers, without actually decompressing anything.
		 *
		 * In both cases we want to decrypt two blocks of ciphertext.
		 *
		 * gzip: 4 byte client uncompressed len + 10 byte GZIP header bzip2: 4 byte client uncompressed len + 6 byte
		 * block magic
		 *
		 * Both cases round up to 16 bytes (two XTEA blocks).
		 */
		ByteBuffer clone = ByteBuffer.allocate(16);

		int type = buf.get(buf.position()) & 0xFF;
		if (type == Container.COMPRESSION_NONE) {
			throw new UnsupportedOperationException("Can't test uncompressed containers for key validity.");
		}

		int oldPosition = buf.position();
		int oldLimit = buf.limit();

		buf.position(buf.position() + 5); /* skip type and compressed length */
		buf.limit(buf.position() + clone.limit());

		clone.put(buf.slice());
		clone.rewind();

		buf.position(oldPosition);
		buf.limit(oldLimit);

		if (!key.isZero())
			Xtea.decipher(DataBuffer.wrap(clone), clone.position(), clone.limit(), key.k);

		int uncompressedLength = clone.getInt();
		if (uncompressedLength < 0)
			return false;

		if (type == Container.COMPRESSION_GZIP) {
			int magic = clone.getShort() & 0xFFFF;
			if (magic != 0x1F8B)
				return false;

			int compressionMethod = clone.get() & 0xFF;
			if (compressionMethod != 0x08)
				return false;

			int flags = clone.get() & 0xFF;
			if (flags != 0)
				return false;

			int time = clone.getInt();
			if (time != 0)
				return false;

			int extraFlags = clone.get() & 0xFF;
			if (extraFlags != 0)
				return false;

			int os = clone.get() & 0xFF;
			if (os != 0)
				return false;

			return true;
		} else if (type == Container.COMPRESSION_BZIP2) {
			byte[] magic = new byte[6];
			clone.get(magic);

			byte[] expectedMagic = { 0x31, 0x41, 0x59, 0x26, 0x53, 0x59 };

			if (!Arrays.equals(magic, expectedMagic))
				return false;

			return true;
		} else {
			throw new IllegalArgumentException("Invalid container type.");
		}
	}

	private static ProcessResult processLandscapeFile(FileStore store, ReferenceTable rt, int id, int x, int y, ByteBuffer buf,
	                                                  Set<Key> possibleKeys, List<CacheRef> otherCaches) throws IOException {
		/* calculate region id and central coordinates */
		int region = (x << 8) | y;
		int absX = x * 64 + 32;
		int absY = y * 64 + 32;

		/* first try all the possible keys */
		for (Key key : possibleKeys) {
			if (isKeyValid(buf, key, false)) {
				/* write the key we found */
				writeKey(region, key);
				return ProcessResult.HAVE_KEY;
			}
		}

		/* check if it looks like the plaintext is empty */
		if (isEmpty(buf)) {
			/* get reference table entry */
			Entry entry = rt.getEntry(id);

			/* increment version */
			int previousVersion = entry.getVersion();
			int version = previousVersion + 1;
			entry.setVersion(version);

			/* make landscape file */
			buf = ByteBuffer.allocate(1);
			buf.put((byte) 0);
			buf.flip();

			/* make and encode container */
			Container container = new Container(Container.COMPRESSION_GZIP, DataBuffer.wrap(buf), version);
			buf = container.encode().getByteBuffer();

			/* update CRC */
			int previousCrc = entry.getCrc();

			byte[] bytes = new byte[buf.limit() - 2]; /* exclude version from CRC */
			buf.slice().get(bytes);

			CRC32 crc = new CRC32();
			crc.update(bytes);
			entry.setCrc((int) crc.getValue());

			/* write new container to cache */
			store.write(MAP_INDEX, id, buf);

			/* write output table row (so we know what we've updated) */
			System.out.println(MAP_INDEX + "\t" + id + "\t" + previousVersion + "\t" + version + "\t" + previousCrc + "\t"
					+ entry.getCrc() + "\t(" + absX + ", " + absY + ")");

			/* write zero key (the container is not encrypted) */
			writeKey(region, Key.ZERO);
			return ProcessResult.PLAINTEXT_EMPTY;
		}

		/* check if we have a valid key for a version of the file from a different cache */
		for (CacheRef otherCache : otherCaches) {
			int otherId = otherCache.findLandscapeFile(x, y);
			if (otherId != -1) {
				/* read the file */
				buf = otherCache.store.read(MAP_INDEX, otherId).getByteBuffer();

				/* try all possible keys */
				for (Key key : possibleKeys) {
					if (isKeyValid(buf, key, otherCache.rs3)) {
						/* get reference table entry */
						Entry entry = rt.getEntry(id);

						/* increment version */
						int previousVersion = entry.getVersion();
						int version = previousVersion + 1;
						entry.setVersion(version);

						buf.putShort(buf.limit() - 2, (short) version);

						/* update CRC */
						int previousCrc = entry.getCrc();

						byte[] bytes = new byte[buf.limit() - 2];
						buf.slice().get(bytes);

						CRC32 crc = new CRC32();
						crc.update(bytes);
						entry.setCrc((int) crc.getValue());

						/* write new container to cache */
						store.write(MAP_INDEX, id, buf);

						/* write output table row (so we know what we've updated) */
						System.out.println(MAP_INDEX + "\t" + id + "\t" + previousVersion + "\t" + version + "\t" + previousCrc
								+ "\t" + entry.getCrc() + "\t(" + absX + ", " + absY + ")");

						/* write zero key (the container is not encrypted) */
						writeKey(region, key);

						// TODO copy the mX_Y file too?
						// TODO how to select the best file? in some cases we have a choice from multiple versions
						return ProcessResult.HAVE_KEY_FROM_OTHER_CACHE;
					}
				}
			}
		}

		/* give up :( */
		System.out.println("Missing: " + absX + ", " + absY + " (" + region + ".txt)");
		return ProcessResult.FAILED;
	}

	private static ImmutableSet<Key> readPossibleKeys() throws IOException {
		ImmutableSet.Builder<Key> builder = ImmutableSet.builder();
		builder.add(Key.ZERO); // zero key used to disable encryption

		Files.walkFileTree(Paths.get("cache/data/landscape-keys"), new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (file.getFileName().toString().endsWith(".jcm")) {
					try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
						String line;
						while ((line = reader.readLine()) != null) {
							if (!line.startsWith("--")) {
								String[] parts = line.split(" ");
								if (parts.length >= 4) {
									String[] k = parts[3].split("\\.");
									int[] ki = new int[4];
									for (int i = 0; i < ki.length; i++) {
										ki[i] = Integer.parseInt(k[i]);
									}
									builder.add(new Key(ki));
								}
							}
						}
					}
				} else if (file.getFileName().toString().endsWith(".txt")) {
					try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
						outer:
						for (; ; ) {
							int[] k = new int[4];
							for (int i = 0; i < k.length; i++) {
								String line = reader.readLine();
								if (line == null || line.isEmpty())
									break outer;
								k[i] = Integer.parseInt(line);
							}
							builder.add(new Key(k));
						}
					}
				} else if (file.getFileName().toString().endsWith(".dat") || file.getFileName().toString().endsWith(".bin")) {
					try (DataInputStream is = new DataInputStream(new BufferedInputStream(Files.newInputStream(file)))) {
						for (; ; ) {
							try {
								is.readShort(); // region id
								int[] k = new int[4];
								for (int i = 0; i < k.length; i++) {
									k[i] = is.readInt();
								}
								builder.add(new Key(k));
							} catch (EOFException ex) {
								break;
							}
						}
					}
				} else if (file.getFileName().toString().endsWith(".sql")) {
					String f = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
					Matcher matcher = Pattern.compile("\\([0-9]+,([0-9-]+),([0-9-]+),([0-9-]+),([0-9-]+)\\)").matcher(f);
					while (matcher.find()) {
						int[] k = { Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)),
								Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)) };
						builder.add(new Key(k));
					}
				}
				return FileVisitResult.CONTINUE;
			}
		});

		return builder.build();
	}

	private static void writeKey(int region, Key key) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("game/data/landscape-keys/" + region + ".txt"))) {
			for (int i = 0; i < key.k.length; i++) {
				writer.write(key.k[i] + "\n");
			}
		}
	}

}