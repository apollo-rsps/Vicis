package rs.emulate.legacy.model

import rs.emulate.legacy.model.ModelFeature.*
import rs.emulate.shared.util.DataBuffer
import java.util.HashSet

/**
 * A decoder for [Model]s.
 *
 * @param buffer The uncompressed [DataBuffer] containing the encoded `Model`.
 */
class ModelDecoder(private val buffer: DataBuffer) {

    /**
     * An indexed array of [Vertex]es in the [Model] being decoded.
     */
    private lateinit var vertices: Array<Vertex?>

    /**
     * An indexed array of texture coordinates projected into viewspace.
     */
    private lateinit var texCoords: Array<TexCoord?>

    /**
     * The faces of the geometry in the [Model] being decoded.
     */
    private lateinit var faces: Array<Face?>

    /**
     * A set of model rendering features enabled for the [Model] being decoded.
     */
    private var features: Set<ModelFeature> = HashSet()

    fun decode(): Model {
        val header = buffer.asReadOnlyBuffer()
        header.position(buffer.capacity() - HEADER_SIZE)

        vertices = arrayOfNulls(header.getUnsignedShort())
        faces = arrayOfNulls(header.getUnsignedShort())
        texCoords = arrayOfNulls(header.getUnsignedByte())

        features = ModelFeature.decodeFrom(header)

        val xDataLength = header.getUnsignedShort()
        val yDataLength = header.getUnsignedShort()
        val zDataLength = header.getUnsignedShort()
        val faceDataLength = header.getUnsignedShort()

        var offset = vertices.size

        val faceTypesOffset = offset
        offset += faces.size

        val faceRenderPrioritiesOffset = offset
        if (features.none { it is GlobalFaceRenderPriority }) { // TODO full scan is stupid
            offset += faces.size
        }

        val faceBonesOffset = offset
        if (FaceSkinning in features) {
            offset += faces.size
        }

        val faceTexturePointersOffset = offset
        if (FaceTextures in features) {
            offset += faces.size
        }

        val vertexBonesOffset = offset
        if (VertexSkinning in features) {
            offset += vertices.size
        }

        val faceAlphasOffset = offset
        if (FaceTransparency in features) {
            offset += faces.size
        }

        val faceDataOffset = offset
        offset += faceDataLength

        val faceColoursOffset = offset
        offset += faces.size * 2

        val texCoordsOffset = offset
        offset += texCoords.size * 6

        val xDataOffset = offset
        offset += xDataLength

        val yDataOffset = offset
        offset += yDataLength

        val zDataOffset = offset
        offset += zDataLength

        decodeVertices(xDataOffset, yDataOffset, zDataOffset, vertexBonesOffset)
        decodeFaces(faceDataOffset, faceTypesOffset, faceColoursOffset, faceRenderPrioritiesOffset,
            faceAlphasOffset, faceBonesOffset, faceTexturePointersOffset)
        decodeTexCoords(texCoordsOffset)

        return Model(features, faces.requireNoNulls(), vertices.requireNoNulls(), texCoords.requireNoNulls())
    }

    /**
     * Decode the projected texture coordinates for the [Model] being decoded.
     *
     * @param offset The offset into `buffer` of the texture coordinate data.
     */
    private fun decodeTexCoords(offset: Int) {
        val texCoordsBuffer = buffer.asReadOnlyBuffer()
        texCoordsBuffer.position(offset)

        for (index in texCoords.indices) {
            val origin = texCoordsBuffer.getUnsignedByte()
            val u = texCoordsBuffer.getUnsignedByte()
            val v = texCoordsBuffer.getUnsignedByte()

            texCoords[index] = TexCoord(origin, u, v)
        }
    }

    /**
     * Decode the triangle [Face]s that make up the geometry of the [Model] being decoded.
     */
    private fun decodeFaces(
        faceDataOffset: Int,
        typesOffset: Int,
        coloursOffset: Int,
        renderPrioritiesOffset: Int,
        alphasOffset: Int,
        bonesOffset: Int,
        texturePointersOffset: Int
    ) {
        check(faces.isNotEmpty()) { "Must be 1 or more faces present." }

        val faceData = buffer.asReadOnlyBuffer()
        faceData.position(faceDataOffset)

        val types = buffer.asReadOnlyBuffer()
        types.position(typesOffset)

        val colours = buffer.asReadOnlyBuffer()
        colours.position(coloursOffset)

        val priorities = buffer.asReadOnlyBuffer()
        priorities.position(renderPrioritiesOffset)

        val alphas = buffer.asReadOnlyBuffer()
        alphas.position(alphasOffset)

        val bones = buffer.asReadOnlyBuffer()
        bones.position(bonesOffset)

        val texturePointers = buffer.asReadOnlyBuffer()
        texturePointers.position(texturePointersOffset)

        var faceA = 0
        var faceB = 0
        var faceC = 0
        var offset = 0

        val priorityFeature = features.find { it is GlobalFaceRenderPriority } as GlobalFaceRenderPriority?

        for (index in faces.indices) {
            val type = types.getUnsignedByte()
            val colour = colours.getUnsignedShort()

            val renderPriority = priorityFeature?.priority ?: priorities.getUnsignedByte()

            val alpha = if (FaceTransparency in features) alphas.getUnsignedByte() else -1
            val bone = if (FaceSkinning in features) bones.getUnsignedByte() else -1
            val texturePointer = if (FaceTextures in features) bones.getUnsignedByte() else -1

            if (type == 1) {
                faceA = faceData.getSignedSmart() + offset
                offset = faceA

                faceB = faceData.getSignedSmart() + offset
                offset = faceB

                faceC = faceData.getSignedSmart() + offset
                offset = faceC
            } else if (type == 2) {
                faceB = faceC
                faceC = faceData.getSignedSmart() + offset
                offset = faceC
            } else if (type == 3) {
                faceA = faceC
                faceC = faceData.getSignedSmart() + offset
                offset = faceC
            } else if (type == 4) {
                val temp = faceA
                faceA = faceB
                faceB = temp

                faceC = faceData.getSignedSmart() + offset
                offset = faceC
            }

            faces[index] = Face(faceA, faceB, faceC, colour, renderPriority, alpha, bone, texturePointer)
        }
    }

    /**
     * Decode a list of [Vertex] objects from the input [Model] file.
     */
    private fun decodeVertices(xDataOffset: Int, yDataOffset: Int, zDataOffset: Int, vertexBonesOffset: Int) {
        check(vertices.isNotEmpty()) { "Vertex count must be greater than 0." }

        val directionBuffer = buffer.asReadOnlyBuffer()
        directionBuffer.position(0)
        val verticesX = buffer.asReadOnlyBuffer()
        verticesX.position(xDataOffset)

        val verticesY = buffer.asReadOnlyBuffer()
        verticesY.position(yDataOffset)

        val verticesZ = buffer.asReadOnlyBuffer()
        verticesZ.position(zDataOffset)

        val bones = buffer.asReadOnlyBuffer()
        bones.position(vertexBonesOffset)

        var baseX = 0
        var baseY = 0
        var baseZ = 0

        for (index in vertices.indices) {
            val mask = directionBuffer.getUnsignedByte()
            var x = when {
                mask and VERTEX_X_POSITION != 0 -> verticesX.getSignedSmart()
                else -> 0
            }

            var y = when {
                mask and VERTEX_Y_POSITION != 0 -> verticesY.getSignedSmart()
                else -> 0
            }

            var z = when {
                mask and VERTEX_Z_POSITION != 0 -> verticesZ.getSignedSmart()
                else -> 0
            }

            val bone = when (VertexSkinning) {
                in features -> bones.getUnsignedByte()
                else -> -1
            }

            x += baseX
            y += baseY
            z += baseZ

            baseX = x
            baseY = y
            baseZ = z

            vertices[index] = Vertex(x, y, z, bone)
        }
    }

    companion object {

        /**
         * The size (in bytes) of the model header block.
         */
        private const val HEADER_SIZE = 18

        /**
         * The mask for vertex data blocks that contain an X coordinate.
         */
        private const val VERTEX_X_POSITION = 0x1

        /**
         * The mask for vertex data blocks that contain a Y coordinate.
         */
        private const val VERTEX_Y_POSITION = 0x2

        /**
         * The mask for vertex data blocks that contain a Z coordinate.
         */
        private const val VERTEX_Z_POSITION = 0x4

    }
}
