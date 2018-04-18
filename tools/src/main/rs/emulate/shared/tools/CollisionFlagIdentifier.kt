package rs.emulate.shared.tools

import java.util.Scanner

/**
 * A command line tool to print the flags contained in a single value in a CollisionMap.
 */
class CollisionFlagIdentifier(private val values: List<Int>) {

    private enum class CollisionFlag(private val value: Int) {

        WALL_NORTH_WEST(0x1),
        WALL_NORTH(0x2),
        WALL_NORTH_EAST(0x4),
        WALL_EAST(0x8),
        WALL_SOUTH_EAST(0x10),
        WALL_SOUTH(0x20),
        WALL_SOUTH_WEST(0x40),
        WALL_WEST(0x80),
        OBJECT_TILE(0x100),

        /**
         * An unknown flag (not used in the cache, checked but never set in the client).
         */
        UNKNOWN(0x10_000),
        BLOCK_WALK(0x20_000),
        DECORATION_TILE(0x40_000),
        BOCKED_TILE(0x200_000),
        UNLOADED(0x1_000_000),
        ALLOW_RANGE(0x40_000_000);

        /**
         * Returns whether or not this [CollisionFlag] is set in the specified integer value.
         */
        fun setIn(value: Int): Boolean {
            return value and this.value != 0
        }

    }

    private fun getFlags(value: Int): List<CollisionFlag> {
        return CollisionFlag.values().filter { flag -> flag.setIn(value) }
    }

    private fun print() {
        values.map { value -> "${Integer.toString(value)} contains: ${getFlags(value).toSet()}" }
            .forEach { println(it) }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            if (args.isNotEmpty()) {
                val flags = parse(args)

                val parser = CollisionFlagIdentifier(flags)
                parser.print()
                return
            }

            println("Starting in interactive mode. Input values to decode, or 'q' to quit.")
            Scanner(System.`in`).use { scanner ->
                while (true) {
                    val input = scanner.nextLine()
                    if (input.equals("q", ignoreCase = true) || input.equals("quit", ignoreCase = true)) {
                        break
                    }

                    val values = input.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val flags: List<Int>
                    try {
                        flags = parse(values)
                    } catch (e: NumberFormatException) {
                        System.err.println("Input hex or decimal literals only.")
                        continue
                    }

                    val parser = CollisionFlagIdentifier(flags)
                    parser.print()
                }
            }
        }

        private fun parse(strings: Array<String>): List<Int> {
            return strings.map { string ->
                if (string.startsWith("0x")) {
                    Integer.parseInt(string.substring(2), 16)
                } else {
                    Integer.parseInt(string)
                }
            }
        }
    }

}
