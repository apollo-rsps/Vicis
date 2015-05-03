package rs.emulate.legacy;

/**
 * A mode to open an {@link IndexedFileSystem} with.
 * 
 * @author Major
 */
public enum AccessMode {

	/**
	 * The read-only access mode.
	 */
	READ("r"),

	/**
	 * The read-write access mode.
	 */
	READ_WRITE("rw"),

	/**
	 * The write-only access mode.
	 */
	WRITE("w");

	/**
	 * The UNIX-style file system permission string of this access mode.
	 */
	private final String unix;

	/**
	 * Creates the access mode.
	 * 
	 * @param unix The UNIX-style file system permission string of this access mode.
	 */
	private AccessMode(String unix) {
		this.unix = unix;
	}

	/**
	 * Returns the UNIX-style file system permission string of this access mode.
	 * 
	 * @return The UNIX-style string.
	 */
	public String asUnix() {
		return unix;
	}

}