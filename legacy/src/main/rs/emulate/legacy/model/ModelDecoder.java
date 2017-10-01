package rs.emulate.legacy.model;

import com.google.common.base.Preconditions;
import rs.emulate.shared.util.DataBuffer;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import static rs.emulate.legacy.model.ModelFeature.*;

/**
 * A decoder for {@link Model}s.
 */
public final class ModelDecoder {
    /**
     * The size in {@code byte}s of the model header block.
     */
    public static final int HEADER_SIZE = 18;

    /**
     * Bitmask for vertex data blocks that contain an X coordinate.
     */
    public static final int VERTEX_X_POSITION = 0x1;

    /**
     * Bitmask for vertex data blocks that contain a Y coordinate.
     */
    public static final int VERTEX_Y_POSITION = 0x2;

    /**
     * Bitmask for vertex data blocks that contain a Z coordinate.
     */
    public static final int VERTEX_Z_POSITION = 0x4;

    /**
     * The {@link Model} data to decode.
     */
    private final DataBuffer buffer;

    /**
     * An indexed array of {@link Vertex}es in the {@link Model} being decoded.
     */
    private Vertex[] vertices;

    /**
     * An indexed array of texture coordinates projected into viewspace.
     */
    private TexCoord[] texCoords;

    /**
     * The faces of the geometry in the {@link Model} being decoded.
     */
    private Face[] faces;

    /**
     * A set of model rendering features enabled for the {@link Model} being decoded.
     */
    private Set<ModelFeature> features = new HashSet<>();

    /**
     * Create a decoder for the {@link Model} data stored in {@code buffer}.
     *
     * @param buffer The uncompressed {@link DataBuffer} containing the encoded {@code Model}.
     */
    public ModelDecoder(DataBuffer buffer) {
        this.buffer = buffer;
    }

    /**
     * Decode a feature flag value using {@code flagDecoder} and add {@code feature} to the feature set if
     * it was enabled.
     *
     * @param featureSet The set that the feature will be added to if enabled.
     * @param feature The feature to check.
     * @param flagDecoder A supplier that decodes and returns the flag.
     */
    private static void checkFeatureFlag(Set<ModelFeature> featureSet, ModelFeature feature,
                                         Supplier<Boolean> flagDecoder) {

        boolean enabled = flagDecoder.get();
        if (enabled) {
            featureSet.add(feature);
        }
    }

    /**
     * Decode the {@link ModelFeature}s present given the attributes in the {@code header} block.
     *
     * @param header The data buffer containing the model header block.
     * @return A {@link Set} of {@link ModelFeature}s that are enabled.
     */
    private Set<ModelFeature> decodeFeatures(DataBuffer header) {
        Set<ModelFeature> features = EnumSet.noneOf(ModelFeature.class);

        checkFeatureFlag(features, FACE_TEXTURES, header::getBoolean);
        checkFeatureFlag(features, FACE_RENDER_PRIORITY, () -> header.getUnsignedByte() == 255);
        checkFeatureFlag(features, FACE_TRANSPARENCY, header::getBoolean);
        checkFeatureFlag(features, FACE_SKINNING, header::getBoolean);
        checkFeatureFlag(features, VERTEX_SKINNING, header::getBoolean);

        return features;
    }

    public Model decode() {
        DataBuffer header = buffer.asReadOnlyBuffer();
        header.position(buffer.capacity() - HEADER_SIZE);

        vertices = new Vertex[header.getUnsignedShort()];
        faces = new Face[header.getUnsignedShort()];
        texCoords = new TexCoord[header.getUnsignedByte()];
        features = decodeFeatures(header);

        int xDataLength = header.getUnsignedShort();
        int yDataLength = header.getUnsignedShort();
        int zDataLength = header.getUnsignedShort();
        int faceDataLength = header.getUnsignedShort();

        int offset = 0;
        offset += vertices.length;

        int faceTypesOffset = offset;
        offset += faces.length;

        int faceRenderPrioritiesOffset = offset;
        if (features.contains(FACE_RENDER_PRIORITY)) {
            offset += faces.length;
        }

        int faceBonesOffset = offset;
        if (features.contains(FACE_SKINNING)) {
            offset += faces.length;
        }

        int faceTexturePointersOffset = offset;
        if (features.contains(FACE_TEXTURES)) {
            offset += faces.length;
        }

        int vertexBonesOffset = offset;
        if (features.contains(VERTEX_SKINNING)) {
            offset += vertices.length;
        }

        int faceAlphasOffset = offset;
        if (features.contains(FACE_TRANSPARENCY)) {
            offset += faces.length;
        }

        int faceDataOffset = offset;
        offset += faceDataLength;

        int faceColoursOffset = offset;
        offset += faces.length * 2;

        int texCoordsOffset = offset;
        offset += texCoords.length * 6;

        int xDataOffset = offset;
        offset += xDataLength;

        int yDataOffset = offset;
        offset += yDataLength;

        int zDataOffset = offset;
        offset += zDataLength;

        decodeVertices(xDataOffset, yDataOffset, zDataOffset, vertexBonesOffset);
        decodeFaces(faceDataOffset, faceTypesOffset, faceColoursOffset, faceRenderPrioritiesOffset,
            faceAlphasOffset, faceBonesOffset, faceTexturePointersOffset);
        decodeTexCoords(texCoordsOffset);

        return new Model(features, faces, vertices, texCoords);
    }

    /**
     * Decode the projected texture coordinates for the {@link Model} being decoded.
     *
     * @param offset The offset into {@code buffer} of the texture coordinate data.
     */
    private void decodeTexCoords(int offset) {
        DataBuffer texCoordsBuffer = buffer.asReadOnlyBuffer();
        texCoordsBuffer.position(offset);

        for (int index = 0; index < texCoords.length; index++) {
            int origin = texCoordsBuffer.getUnsignedByte();
            int u = texCoordsBuffer.getUnsignedByte();
            int v = texCoordsBuffer.getUnsignedByte();
            texCoords[index] = new TexCoord(origin, u, v);
        }
    }

    /**
     * Decode the triangle {@link Face}s that make up the geometry of the {@link Model} being decoded.
     *
     * @param faceDataOffset The index into {@code buffer} of the face geometry data.
     * @param typesOffset The index into {@code buffer} of the face type data.
     * @param coloursOffset The index into {@code buffer} of the face colour data.
     * @param renderPrioritiesOffset The index into {@code buffer} of the render priority data.
     * @param alphasOffset The index into {@code buffer} of the alpha value data.
     * @param bonesOffset The index into {@code buffer} of the face skinning data.
     * @param texturePointersOffset The index into {@code buffer} of the the texture pointer data.
     */
    private void decodeFaces(int faceDataOffset, int typesOffset,
                             int coloursOffset, int renderPrioritiesOffset,
                             int alphasOffset, int bonesOffset, int texturePointersOffset) {
        Preconditions.checkState(faces.length > 0, "Must be 1 or more faces present");

        DataBuffer faceData = buffer.asReadOnlyBuffer();
        faceData.position(faceDataOffset);

        DataBuffer types = buffer.asReadOnlyBuffer();
        types.position(typesOffset);

        DataBuffer colours = buffer.asReadOnlyBuffer();
        colours.position(coloursOffset);

        DataBuffer renderPriorities = buffer.asReadOnlyBuffer();
        renderPriorities.position(renderPrioritiesOffset);

        DataBuffer alphas = buffer.asReadOnlyBuffer();
        alphas.position(alphasOffset);

        DataBuffer bones = buffer.asReadOnlyBuffer();
        bones.position(bonesOffset);

        DataBuffer texturePointers = buffer.asReadOnlyBuffer();
        texturePointers.position(texturePointersOffset);

        int faceA = 0;
        int faceB = 0;
        int faceC = 0;
        int offset = 0;

        for (int index = 0; index < faces.length; index++) {
            int type = types.getUnsignedByte();
            int colour = colours.getUnsignedShort();
            int renderPriority = features.contains(FACE_RENDER_PRIORITY) ? renderPriorities.getUnsignedByte() : -1;
            int alpha = features.contains(FACE_TRANSPARENCY) ? alphas.getUnsignedByte() : -1;
            int bone = features.contains(FACE_SKINNING) ? bones.getUnsignedByte() : -1;
            int texturePointer = features.contains(FACE_TEXTURES) ? bones.getUnsignedByte() : -1;

            if (type == 1) {
                faceA = faceData.getSignedSmart() + offset;
                offset = faceA;
                faceB = faceData.getSignedSmart() + offset;
                offset = faceB;
                faceC = faceData.getSignedSmart() + offset;
                offset = faceC;
            } else if (type == 2) {
                faceB = faceC;
                faceC = faceData.getSignedSmart() + offset;
                offset = faceC;
            } else if (type == 3) {
                faceA = faceC;
                faceC = faceData.getSignedSmart() + offset;
                offset = faceC;
            } else if (type == 4) {
                int temp = faceA;
                faceA = faceB;
                faceB = temp;

                faceC = faceData.getSignedSmart() + offset;
                offset = faceC;
            }

            faces[index] = new Face(faceA, faceB, faceC, colour, renderPriority, alpha, bone, texturePointer);
        }
    }

    /**
     * Decode a list of {@link Vertex} objects from the input {@link Model} file.
     *
     * @param xDataOffset the offset into {@link this#buffer} of the X coordinate buffer.
     * @param yDataOffset the offset into {@link this#buffer} of the Y coordinate buffer.
     * @param zDataOffset the offset into {@link this#buffer} of the Z coordinate bfufer.
     */
    private void decodeVertices(int xDataOffset, int yDataOffset, int zDataOffset, int vertexBonesOffset) {
        Preconditions.checkState(vertices.length > 0, "Vertex count must be greater than 0");

        DataBuffer directionBuffer = buffer.asReadOnlyBuffer();
        directionBuffer.position(0);
        DataBuffer verticesX = buffer.asReadOnlyBuffer();
        verticesX.position(xDataOffset);

        DataBuffer verticesY = buffer.asReadOnlyBuffer();
        verticesY.position(yDataOffset);

        DataBuffer verticesZ = buffer.asReadOnlyBuffer();
        verticesZ.position(zDataOffset);

        DataBuffer bones = buffer.asReadOnlyBuffer();
        bones.position(vertexBonesOffset);

        int baseX = 0;
        int baseY = 0;
        int baseZ = 0;

        for (int index = 0; index < vertices.length; index++) {
            int mask = directionBuffer.getUnsignedByte();
            int x = 0;
            if ((mask & VERTEX_X_POSITION) != 0) {
                x = verticesX.getSignedSmart();
            }

            int y = 0;
            if ((mask & VERTEX_Y_POSITION) != 0) {
                y = verticesY.getSignedSmart();
            }

            int z = 0;
            if ((mask & VERTEX_Z_POSITION) != 0) {
                z = verticesZ.getSignedSmart();
            }

            x += baseX;
            y += baseY;
            z += baseZ;
            baseX = x;
            baseY = y;
            baseZ = z;

            vertices[index] = new Vertex(x, y, z);
            //@todo
            int bone = features.contains(VERTEX_SKINNING) ? bones.getUnsignedByte() : -1;
        }
    }
}
