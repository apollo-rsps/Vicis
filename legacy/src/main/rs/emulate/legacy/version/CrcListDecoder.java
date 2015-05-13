package rs.emulate.legacy.version;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rs.emulate.legacy.archive.Archive;
import rs.emulate.legacy.archive.ArchiveEntry;
import rs.emulate.shared.util.DataBuffer;

import com.google.common.collect.ImmutableList;

/**
 * Decodes {@link CrcList}s from the {@code versionlist} {@link Archive}.
 *
 * @author Major
 */
public final class CrcListDecoder {

	/**
	 * The VersionListEntryType to decode.
	 */
	private final List<VersionEntryType> types;

	/**
	 * The version Archive.
	 */
	private final Archive version;

	/**
	 * Creates the CrcListDecoder which decodes the {@link StandardVersionEntryType}s.
	 *
	 * @param version The {@link Archive} containing the version data. Must not be {@code null}.
	 */
	public CrcListDecoder(Archive version) {
		this(version, Arrays.asList(StandardVersionEntryType.values()));
	}

	/**
	 * Creates the CrcListDecoder.
	 *
	 * @param version The {@link Archive} containing the version data. Must not be {@code null}.
	 * @param types The {@link VersionEntryType}s to decode. Must not be {@code null}.
	 */
	public CrcListDecoder(Archive version, List<VersionEntryType> types) {
		this.version = version;
		this.types = ImmutableList.copyOf(types);
	}

	/**
	 * Decodes the {@link CrcList}s.
	 *
	 * @return The {@link List} of CrcLists.
	 * @throws FileNotFoundException If any of the {@link ArchiveEntry ArchiveEntries} could not be found.
	 */
	public List<CrcList> decode() throws FileNotFoundException {
		List<CrcList> lists = new ArrayList<>(types.size());

		for (VersionEntryType type : types) {
			String name = type.asCrcList();
			ArchiveEntry entry = version.getEntry(name);
			DataBuffer data = entry.getBuffer();

			int count = data.limit() / Integer.BYTES;
			int[] crcs = new int[count];
			Arrays.setAll(crcs, index -> data.getInt());

			lists.add(new CrcList(type, crcs));
		}

		return ImmutableList.copyOf(lists);
	}

}