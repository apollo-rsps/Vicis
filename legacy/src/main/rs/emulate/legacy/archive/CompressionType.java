package rs.emulate.legacy.archive;

/**
 * The two types of archive compression support by the client.
 *
 * @author Major
 */
public enum CompressionType {

	/**
	 * The compression type where entire archive is compressed with bzip2, with only the 6 header bytes uncompressed.
	 */
	ARCHIVE_COMPRESSION,

	/**
	 * The compression type where meta information (e.g. archive entry identifiers) is not compressed, and only the
	 * actual entry data is.
	 */
	ENTRY_COMPRESSION,

	/**
	 * The compression type where neither the archive nor the entries are compressed. This is not used by the client,
	 * and should only be used for testing purposes.
	 */
	NO_COMPRESSION;

}