package rs.emulate.legacy.archive;

import com.google.common.collect.ImmutableList;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * An archive in the RuneScape cache. An archive is a set of files which can be completely compressed, or each
 * individual file can be compressed. This implementation is immutable (i.e. calling any of the
 * {@code add/replace/remove} methods will return a new archive with the specified modification performed on it).
 *
 * @author Graham
 * @author Major
 */
public final class Archive {

	/**
	 * The empty Archive.
	 */
	public static final Archive EMPTY_ARCHIVE = new Archive(Collections.emptyList());

	/**
	 * The entries in this archive.
	 */
	private final List<ArchiveEntry> entries;

	/**
	 * The size of this archive.
	 */
	private final int size;

	/**
	 * Creates a new archive.
	 *
	 * @param entries The entries in this archive.
	 */
	public Archive(List<ArchiveEntry> entries) {
		this.entries = ImmutableList.copyOf(entries);
		this.size = ArchiveUtils.sizeOf(entries);
	}

	/**
	 * Returns a new archive consisting of the current {@link ArchiveEntry} objects, and the specified entries. This
	 * will replace any entries with the same identifier as any of the specified ones, if present.
	 *
	 * @param entries The entries.
	 * @return The new archive.
	 */
	public Archive addEntries(ArchiveEntry... entries) {
		List<ArchiveEntry> current = new ArrayList<>(this.entries);
		List<ArchiveEntry> additions = Arrays.asList(entries);

		current.removeIf(entry -> additions.stream().anyMatch(e -> e.getIdentifier() == entry.getIdentifier()));
		current.addAll(additions);

		return new Archive(current);
	}

	/**
	 * Returns a new archive consisting of the current {@link ArchiveEntry} objects, and the added one. This will
	 * replace the entry with the same identifier as the specified one, if it exists.
	 *
	 * @param entry The entry to add.
	 * @return The new archive.
	 */
	public Archive addEntry(ArchiveEntry entry) {
		return addEntries(entry);
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Archive) {
			Archive other = (Archive) object;
			return size == other.size && entries.equals(other.entries);
		}

		return false;
	}

	/**
	 * Gets a shallow copy of the {@link List} of {@link ArchiveEntry} objects.
	 *
	 * @return The list of entries.
	 */
	public List<ArchiveEntry> getEntries() {
		return ImmutableList.copyOf(entries);
	}

	/**
	 * Gets the {@link ArchiveEntry} with the specified name.
	 *
	 * @param name The name of the entry.
	 * @return The entry.
	 * @throws FileNotFoundException If the entry could not be found.
	 */
	public ArchiveEntry getEntry(String name) throws FileNotFoundException {
		int hash = ArchiveUtils.hash(name);
		Optional<ArchiveEntry> optional = entries.stream().filter(entry -> entry.getIdentifier() == hash).findFirst();

		return optional.orElseThrow(() -> new FileNotFoundException("Could not find entry: " + name + "."));
	}

	/**
	 * Gets the {@link ArchiveEntry} with the specified hash.
	 *
	 * @param hash The hash of the entry.
	 * @return The entry.
	 * @throws FileNotFoundException If the entry could not be found.
	 */
	public ArchiveEntry getEntry(int hash) throws FileNotFoundException {
		Optional<ArchiveEntry> optional = entries.stream().filter(entry -> entry.getIdentifier() == hash).findFirst();

		return optional.orElseThrow(() -> new FileNotFoundException("Could not find entry: " + hash + "."));
	}

	/**
	 * Gets the size of this archive, in bytes.
	 *
	 * @return The size.
	 */
	public int getSize() {
		return size;
	}

	@Override
	public int hashCode() {
		return entries.hashCode();
	}

	/**
	 * Removes the {@link ArchiveEntry} with the specified name.
	 *
	 * @param name The name of the entry.
	 * @return An archive containing all of the entries in this archive, except for the one removed.
	 * @throws FileNotFoundException If the entry could not be found.
	 */
	public Archive removeEntry(String name) throws FileNotFoundException {
		int hash = ArchiveUtils.hash(name);
		List<ArchiveEntry> entries = new ArrayList<>(this.entries);

		for (Iterator<ArchiveEntry> iterator = entries.iterator(); iterator.hasNext(); ) {
			ArchiveEntry entry = iterator.next();

			if (entry.getIdentifier() == hash) {
				iterator.remove();
				return new Archive(entries);
			}
		}

		throw new FileNotFoundException("Could not find entry: " + name + ".");
	}

	/**
	 * Replaces the {@link ArchiveEntry} with the specified name (shorthand for {@link #removeEntry} and then
	 * {@link #addEntry}.
	 *
	 * @param name The name of the entry to replace.
	 * @param entry The entry.
	 * @return The archive with the replaced entry.
	 * @throws FileNotFoundException If the entry with the specified name is not part of this archive.
	 */
	public Archive replaceEntry(String name, ArchiveEntry entry) throws FileNotFoundException {
		return removeEntry(name).addEntry(entry); // TODO rewrite to avoid needless copying
	}

}