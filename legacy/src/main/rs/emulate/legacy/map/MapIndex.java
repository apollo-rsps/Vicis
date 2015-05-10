package rs.emulate.legacy.map;

/**
 * Represents the map_index entry in the versionlist archive, used for mapping region coordinates to landscape and map data files.
 */
public final class MapIndex {

    private int[] membersArea;
    private int[] areas;
    private int[] landscapes;
    private int[] mapFiles;

    public MapIndex(int[] areas, int[] landscapes, int[] mapFiles, int[] membersArea) {
        this.areas = areas;
        this.landscapes = landscapes;
        this.mapFiles = mapFiles;
        this.membersArea = membersArea;
    }

    public final int resolve(int regionX, int regionY, int type) {
        int code = (regionX << 8) + regionY;
        for (int area = 0; area < areas.length; area++) {
            if (areas[area] == code) {
                return type == 0 ? mapFiles[area] : landscapes[area];
            }
        }

        return -1;
    }

    /**
     * Lookup the file id for the landscape at coordinates (regionX, regionY)
     *
     * @param regionX The X coordinate of the region we want the landscape for.
     * @param regionY The Y coordinate of the region we want the landscape for.
     *
     * @return The file id of the regions landscape.
     */
    public final int resolveLandscape(int regionX, int regionY) {
        return resolve(regionX, regionY, 1);
    }

    /**
     * Lookup the file id for the map data at coordinates (regionX, regionY)
     *
     * @param regionX The X coordinate of the region we want the map data for.
     * @param regionY The Y coordinate of the region we want the map data for.
     *
     * @return The file id of the regions map data.
     */
    public final int resolveMapFile(int regionX, int regionY) {
        return resolve(regionX, regionY, 0);
    }

    /**
     * Get all of the file ids which represents a maps terrain.
     *
     * @return The array of map terrain file ids.
     */
    public final int[] getMapFiles() {
        return mapFiles;
    }
}
