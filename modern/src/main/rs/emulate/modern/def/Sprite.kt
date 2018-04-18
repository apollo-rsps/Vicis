package rs.emulate.modern.def

import rs.emulate.shared.util.DataBuffer

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.util.ArrayList

/**
 * Represents a sprite that may contain one or more frames.
 *
 * @param width The width of the sprite in pixels.
 * @param height The height of the sprite in pixels.
 * @param size The number of animation frames.
 */
class Sprite(val width: Int, val height: Int, size: Int = 1) {

    /**
     * The array of animation frames in this sprite.
     */
    private val frames: Array<BufferedImage?> = arrayOfNulls(size)

    /**
     * Encodes this [Sprite] into a [ByteBuffer].
     *
     * Please note that this is a fairly simple implementation which only supports vertical encoding. It does not
     * attempt to use the offsets to save space.
     */
    fun encode(): ByteBuffer {
        ByteArrayOutputStream().use { bout ->
            DataOutputStream(bout).use { os ->
                val palette = ArrayList<Int>()
                palette.add(0) /* transparent colour */

                for (image in frames.filterNotNull()) {
                    if (image.width != width || image.height != height) {
                        throw IOException("All frames must be the same size.")
                    }

                    var flags = FLAG_VERTICAL // TODO: do we need to support horizontal encoding?
                    for (x in 0 until width) {
                        for (y in 0 until height) {
                            val argb = image.getRGB(x, y)
                            val alpha = argb shr 24 and 0xFF
                            var rgb = argb and 0xFFFFFF
                            if (rgb == 0) {
                                rgb = 1
                            }

                            if (alpha != 0 && alpha != 255) {
                                flags = flags or FLAG_ALPHA
                            }

                            if (!palette.contains(rgb)) {
                                if (palette.size >= 256) {
                                    throw IOException("Too many colours in this sprite!")
                                }
                                palette.add(rgb)
                            }
                        }
                    }

                    os.write(flags)
                    for (x in 0 until width) {
                        for (y in 0 until height) {
                            val argb = image.getRGB(x, y)
                            val alpha = argb shr 24 and 0xFF
                            var rgb = argb and 0xFFFFFF
                            if (rgb == 0) {
                                rgb = 1
                            }

                            if (flags and FLAG_ALPHA == 0 && alpha == 0) {
                                os.write(0)
                            } else {
                                os.write(palette.indexOf(rgb))
                            }
                        }
                    }

                    /* write the alpha channel if this sprite has one */
                    if (flags and FLAG_ALPHA != 0) {
                        for (x in 0 until width) {
                            for (y in 0 until height) {
                                val argb = image.getRGB(x, y)
                                val alpha = argb shr 24 and 0xFF
                                os.write(alpha)
                            }
                        }
                    }
                }

                for (i in 1 until palette.size) {
                    val rgb = palette[i]
                    os.write((rgb shr 16).toByte().toInt())
                    os.write((rgb shr 8).toByte().toInt())
                    os.write(rgb.toByte().toInt())
                }

                os.writeShort(width)
                os.writeShort(height)
                os.write(palette.size - 1)

                for (i in frames.indices) {
                    os.writeShort(0) // offset X
                    os.writeShort(0) // offset Y
                    os.writeShort(width)
                    os.writeShort(height)
                }

                os.writeShort(frames.size)

                val bytes = bout.toByteArray()
                return ByteBuffer.wrap(bytes)
            }
        }
    }

    /**
     * Gets the frame with the specified id.
     */
    fun getFrame(id: Int): BufferedImage? {
        return frames[id]
    }

    /**
     * Sets the frame with the specified id.
     */
    fun setFrame(id: Int, frame: BufferedImage) {
        if (frame.width != width || frame.height != height) {
            throw IllegalArgumentException("The frame's dimensions do not match the sprite's dimensions.")
        }

        frames[id] = frame
    }

    /**
     * Gets the number of frames in this set.
     */
    fun size(): Int {
        return frames.size
    }

    companion object {

        /**
         * This flag indicates that every pixel has an alpha, as well as red, green and blue, component.
         */
        const val FLAG_ALPHA = 0x02

        /**
         * This flag indicates that the pixels should be read vertically instead of horizontally.
         */
        const val FLAG_VERTICAL = 0x01

        /**
         * Decodes the [Sprite] from the specified [ByteBuffer].
         */
        fun decode(buffer: DataBuffer): Sprite {
            buffer.position(buffer.limit() - 2)
            val size = buffer.getShort() and 0xFFFF

            val offsetsX = IntArray(size)
            val offsetsY = IntArray(size)
            val subWidths = IntArray(size)
            val subHeights = IntArray(size)

            buffer.position(buffer.limit() - size * 8 - 7)
            val width = buffer.getShort() and 0xFFFF
            val height = buffer.getShort() and 0xFFFF
            val palette = IntArray((buffer.getByte() and 0xFF) + 1)

            val set = Sprite(width, height, size)

            for (i in 0 until size) {
                offsetsX[i] = buffer.getShort() and 0xFFFF
            }
            for (i in 0 until size) {
                offsetsY[i] = buffer.getShort() and 0xFFFF
            }
            for (i in 0 until size) {
                subWidths[i] = buffer.getShort() and 0xFFFF
            }
            for (i in 0 until size) {
                subHeights[i] = buffer.getShort() and 0xFFFF
            }

            buffer.position(buffer.limit() - size * 8 - 7 - (palette.size - 1) * 3)
            palette[0] = 0 /* transparent colour (black) */
            for (index in 1 until palette.size) {
                palette[index] = buffer.getUnsignedTriByte()
                if (palette[index] == 0) {
                    palette[index] = 1
                }
            }

            buffer.position(0)
            for (id in 0 until size) {
                val subWidth = subWidths[id]
                val subHeight = subHeights[id]
                val offsetX = offsetsX[id]
                val offsetY = offsetsY[id]

                set.frames[id] = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
                val image = set.frames[id]!!

                val indices = Array(subWidth) { IntArray(subHeight) }

                val flags = buffer.getByte() and 0xFF

                if (flags and FLAG_VERTICAL != 0) {
                    for (x in 0 until subWidth) {
                        for (y in 0 until subHeight) {
                            indices[x][y] = buffer.getByte() and 0xFF
                        }
                    }
                } else {
                    for (y in 0 until subHeight) {
                        for (x in 0 until subWidth) {
                            indices[x][y] = buffer.getByte() and 0xFF
                        }
                    }
                }

                /* read the alpha (if there is alpha) and convert values to ARGB */
                if (flags and FLAG_ALPHA != 0) {
                    if (flags and FLAG_VERTICAL != 0) {
                        for (x in 0 until subWidth) {
                            for (y in 0 until subHeight) {
                                val alpha = buffer.getByte() and 0xFF
                                image.setRGB(x + offsetX, y + offsetY, alpha shl 24 or palette[indices[x][y]])
                            }
                        }
                    } else {
                        for (y in 0 until subHeight) {
                            for (x in 0 until subWidth) {
                                val alpha = buffer.getByte() and 0xFF
                                image.setRGB(x + offsetX, y + offsetY, alpha shl 24 or palette[indices[x][y]])
                            }
                        }
                    }
                } else {
                    for (x in 0 until subWidth) {
                        for (y in 0 until subHeight) {
                            val index = indices[x][y]
                            if (index == 0) {
                                image.setRGB(x + offsetX, y + offsetY, 0)
                            } else {
                                image.setRGB(x + offsetX, y + offsetY, -0x1000000 or palette[index])
                            }
                        }
                    }
                }
            }
            return set
        }
    }

}
