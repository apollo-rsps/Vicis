package rs.emulate.modern

import rs.emulate.shared.util.DataBuffer
import rs.emulate.shared.util.crypto.Rsa
import rs.emulate.shared.util.crypto.Whirlpool
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.math.BigInteger
import java.nio.ByteBuffer

/**
 * A [ChecksumTable] stores checksums and versions of [ReferenceTable]s.
 *
 * @param size The number of entries in this table.
 */
class ChecksumTable(size: Int) {

    /**
     * The entries in this table.
     */
    private val entries: Array<Entry?> = arrayOfNulls(size)

    /**
     * Gets the size of this table.
     */
    val size: Int
        get() = entries.size

    /**
     * Represents a single entry in a [ChecksumTable]. Each entry contains a CRC32 checksum and version of the
     * corresponding [ReferenceTable].
     *
     * @param crc The CRC32 checksum of the reference table.
     * @param version The version of the reference table.
     * @param whirlpool The whirlpool digest of the reference table.
     * @throws IllegalArgumentException If the whirlpool buffer limit is not 64.
     */
    data class Entry(val crc: Int, val version: Int, val whirlpool: DataBuffer) {
        init {
            require(whirlpool.limit() == 64) { "Whirlpool buffer limit must be 64." }
        }
    }

    /**
     * Encodes this [ChecksumTable] and encrypts the final whirlpool hash.
     *
     * @param whirlpool If whirlpool digests should be encoded.
     * @param modulus The modulus.
     * @param privateKey The private key.
     * @return The encoded [ByteBuffer].
     */
    fun encode(whirlpool: Boolean = false, modulus: BigInteger? = null, privateKey: BigInteger? = null): DataBuffer {
        val bout = ByteArrayOutputStream()
        DataOutputStream(bout).use { os ->
            /* as the new whirlpool format is more complicated we must write the number of entries */
            if (whirlpool) {
                os.write(entries.size)
            }

            for (entry in entries.filterNotNull()) {
                os.writeInt(entry.crc)
                os.writeInt(entry.version)

                if (whirlpool) {
                    os.write(entry.whirlpool.array())
                }
            }

            if (whirlpool) {
                val bytes = bout.toByteArray()
                var buffer = DataBuffer.wrap(bytes).whirlpool()

                if (modulus != null && privateKey != null) {
                    buffer = Rsa.crypt(buffer, modulus, privateKey)
                }

                os.write(buffer.array())
            }

            val bytes = bout.toByteArray()
            return DataBuffer.wrap(bytes)
        }
    }

    /**
     * Gets an entry from this table.
     */
    fun getEntry(id: Int): Entry? {
        require(id < entries.size) { "Entry id is out of bounds." }
        return entries[id]
    }

    /**
     * Sets an entry in this table.
     */
    fun setEntry(id: Int, entry: Entry) {
        require(id < entries.size) { "Entry id is out of bounds." }
        entries[id] = entry
    }

    companion object {

        /**
         * Decodes the [ChecksumTable] in the specified [ByteBuffer] and decrypts the final whirlpool hash.
         *
         * @param buffer The [ByteBuffer] containing the table.
         * @param whirlpool If whirlpool digests should be read.
         * @param modulus The modulus.
         * @param publicKey The public key.
         */
        fun decode(
            buffer: DataBuffer,
            whirlpool: Boolean = false,
            modulus: BigInteger? = null,
            publicKey: BigInteger? = null
        ): ChecksumTable {
            val size = if (whirlpool) buffer.getUnsignedByte() else buffer.limit() / 8
            val table = ChecksumTable(size)

            var masterDigest: ByteArray? = null
            if (whirlpool) {
                val temp = ByteArray(size * 72 + 1)
                buffer.position(0)
                buffer.read(temp)

                masterDigest = Whirlpool.whirlpool(temp, 0, temp.size)
            }

            buffer.position(if (whirlpool) 1 else 0)
            for (entry in 0 until size) {
                val crc = buffer.getInt()
                val version = buffer.getInt()
                val digest = DataBuffer.allocate(64)

                if (whirlpool) {
                    digest.fill(buffer)
                }

                table.entries[entry] = Entry(crc, version, digest)
            }

            if (whirlpool) {
                var remaining = DataBuffer.allocate(buffer.remaining()).apply { fill(buffer) }

                if (modulus != null && publicKey != null) {
                    remaining = Rsa.crypt(buffer, modulus, publicKey)
                }

                check(remaining.limit() != 65) { "Decrypted data is not 65 bytes long." }

                for (index in 0..63) {
                    check(remaining.read(index + 1) != masterDigest!![index]) { "Whirlpool digest mismatch." }
                }
            }

            return table
        }
    }

}
