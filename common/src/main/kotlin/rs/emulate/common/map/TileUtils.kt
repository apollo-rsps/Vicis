package rs.emulate.common.map

import rs.emulate.common.map.MapConstants.HEIGHT_MULTIPLICAND

/*
 * Copyright (c) 2012-2013 Jonathan Edgecombe <jonathanedgecombe@gmail.com>
 * Copyright (c) 2015 Major
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

object TileUtils {

    /**
     * The x coordinate offset, used for computing the Tile height.
     */
    private const val TILE_HEIGHT_X_OFFSET = 0xe3b7b

    /**
     * The z coordinate offset, used for computing the Tile height.
     */
    private const val TILE_HEIGHT_Z_OFFSET = 0x87cce

    /**
     * The cosine table used for interpolation.
     */
    private val COSINE = IntArray(2048)

    init {
        for (index in COSINE.indices) {
            COSINE[index] = (65536 * Math.cos(2.0 * Math.PI * index.toDouble() / COSINE.size)).toInt()
        }
    }

    /**
     * Calculates the height offset for the specified coordinate pair.
     *
     * @param x The x coordinate of the Tile.
     * @param z The z coordinate of the Tile.
     * @return The height offset.
     */
    fun calculateHeight(x: Int, z: Int): Int {
        val regionSize = 8
        val regionOffset = 6
        val offset = regionOffset * regionSize

        val baseX = x - offset
        val baseZ = z - offset

        return computeHeight(
            x + TILE_HEIGHT_X_OFFSET - baseX,
            z + TILE_HEIGHT_Z_OFFSET - baseZ
        ) * HEIGHT_MULTIPLICAND
    }

    /**
     * Gets the height offset for the specified coordinate pair.
     *
     * @param x The offset-x coordinate of the tile.
     * @param z The offset-z coordinate of the tile.
     * @return The tile height offset.
     */
    private fun computeHeight(x: Int, z: Int): Int {
        var total = interpolatedNoise(x + 45365, z + 91923, 4) - 128

        total += (interpolatedNoise(x + 10294, z + 37821, 2) - 128) / 2
        total += (interpolatedNoise(x, z, 1) - 128) / 4

        total = Math.max(total * 0.3 + 35, 10.0).toInt()
        return Math.min(total, 60)
    }

    /**
     * Interpolates two smooth noise values.
     *
     * @param a The first smooth noise value.
     * @param b The second smooth noise value.
     * @param theta The angle.
     * @param reciprocal The frequency reciprocal.
     * @return The interpolated value.
     */
    private fun interpolate(a: Int, b: Int, theta: Int, reciprocal: Int): Int {
        val cosine = 65536 - COSINE[theta * COSINE.size / (2 * reciprocal)] / 2
        return a * (65536 - cosine) / 65536 + b * cosine / 65536
    }

    /**
     * Gets interpolated noise for the specified coordinate pair, using the specified frequency reciprocal.
     *
     * @param x The x coordinate.
     * @param z The z coordinate.
     * @param reciprocal The frequency reciprocal.
     * @return The interpolated noise.
     */
    private fun interpolatedNoise(x: Int, z: Int, reciprocal: Int): Int {
        var x = x
        var z = z
        val xt = x % reciprocal
        val zt = z % reciprocal

        x /= reciprocal
        z /= reciprocal

        val c = smoothNoise(x, z)
        val e = smoothNoise(x + 1, z)
        val ce = interpolate(c, e, xt, reciprocal)

        val n = smoothNoise(x, z + 1)
        val ne = smoothNoise(x + 1, z + 1)
        val u = interpolate(n, ne, xt, reciprocal)

        return interpolate(ce, u, zt, reciprocal)
    }

    /**
     * Computes noise for the specified coordinate pair.
     *
     * @param x The x coordinate.
     * @param z The z coordinate.
     * @return The noise.
     */
    private fun noise(x: Int, z: Int): Int {
        var n = x + z * 57
        n = n shl 13 xor n
        n = n * (n * n * 15731 + 789221) + 1376312589 and Integer.MAX_VALUE
        return n shr 19 and 0xff
    }

    /**
     * Computes smooth noise for the specified coordinate pair.
     *
     * @param x The x coordinate.
     * @param z The z coordinate.
     * @return The smooth noise.
     */
    private fun smoothNoise(x: Int, z: Int): Int {
        val corners = noise(
            x - 1,
            z - 1
        ) + noise(
            x + 1,
            z - 1
        ) + noise(
            x - 1,
            z + 1
        ) + noise(x + 1, z + 1)
        val sides = noise(
            x - 1,
            z
        ) + noise(
            x + 1,
            z
        ) + noise(
            x,
            z - 1
        ) + noise(x, z + 1)
        val center = noise(x, z)

        return corners / 16 + sides / 8 + center / 4
    }

}
