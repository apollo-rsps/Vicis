package rs.emulate.legacy;

/**
 * A class which points to a file in the cache.
 * 
 * @author Graham
 */
public final class FileDescriptor {

	/**
	 * The file id.
	 */
	private final int file;

	/**
	 * The file type.
	 */
	private final int type;

	/**
	 * Creates the file descriptor.
	 * 
	 * @param type The file type.
	 * @param file The file id.
	 */
	public FileDescriptor(int type, int file) {
		this.type = type;
		this.file = file;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FileDescriptor) {
			FileDescriptor other = (FileDescriptor) obj;
			return file == other.file && type == other.type;
		}

		return false;
	}

	/**
	 * Gets the file id.
	 * 
	 * @return The file id.
	 */
	public int getFile() {
		return file;
	}

	/**
	 * Gets the file type.
	 * 
	 * @return The file type.
	 */
	public int getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 59;
		return prime * file + type;
	}

}