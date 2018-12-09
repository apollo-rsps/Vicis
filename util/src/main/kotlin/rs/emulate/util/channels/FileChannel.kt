package rs.emulate.util.channels

import java.io.EOFException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

fun FileChannel.readFully(buffer: ByteBuffer, ptrStart: Long) {
    var ptr = ptrStart
    while (buffer.remaining() > 0) {
        val read = read(buffer, ptrStart).toLong()
        if (read == -1L) {
            throw EOFException()
        } else {
            ptr += read
        }
    }
}

fun FileChannel.readFullyOrToEof(buffer: ByteBuffer, ptrStart: Long) {
    var ptr = ptrStart
    while (buffer.remaining() > 0) {
        val read = read(buffer, ptr).toLong()
        if (read == -1L) {
            break
        } else {
            ptr += read
        }
    }
}
