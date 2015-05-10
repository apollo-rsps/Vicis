package rs.emulate.legacy.map;

import rs.emulate.legacy.archive.Archive;
import rs.emulate.shared.util.DataBuffer;

import java.io.FileNotFoundException;

/**
 * Decoder for the version lists map-index entry.
 */
public class MapIndexDecoder {
    private final DataBuffer data;

    /**
     * Construct a new MapIndexDecoder.
     *
     * @param versionListArchive The version list archive
     * @throws FileNotFoundException
     */
    public MapIndexDecoder(Archive versionListArchive) throws FileNotFoundException {
        this.data = versionListArchive.getEntry("map_index").getBuffer().asReadOnlyBuffer();
    }

    public MapIndex decode() {
        int areaCount = data.remaining() / 7;

        int[] areas = new int[areaCount];
        int[] landscapes = new int[areaCount];
        int[] mapFiles = new int[areaCount];
        int[] membersArea = new int[areaCount];

        for (int area = 0; area < areaCount; area++) {
            areas[area] = data.getUnsignedShort();
            mapFiles[area] = data.getUnsignedShort();
            landscapes[area] = data.getUnsignedShort();
            membersArea[area] = data.getUnsignedByte();
        }

        return new MapIndex(areas, landscapes, mapFiles, membersArea);
    }
}
