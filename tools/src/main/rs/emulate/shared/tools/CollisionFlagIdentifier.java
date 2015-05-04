package rs.emulate.shared.tools;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;

/**
 * A simple command line utility to print the flags contained in a single value in a CollisionMap.
 *
 * @author Major
 */
public final class CollisionFlagIdentifier {

	/**
	 * A collision flag.
	 */
	private enum CollisionFlag {

		/**
		 * The wall north-west flag.
		 */
		WALL_NORTH_WEST(0x1),

		/**
		 * The wall north flag.
		 */
		WALL_NORTH(0x2),

		/**
		 * The wall north-east flag.
		 */
		WALL_NORTH_EAST(0x4),

		/**
		 * The wall east flag.
		 */
		WALL_EAST(0x8),

		/**
		 * The wall south-east flag.
		 */
		WALL_SOUTH_EAST(0x10),

		/**
		 * The wall south flag.
		 */
		WALL_SOUTH(0x20),

		/**
		 * The wall south-west flag.
		 */
		WALL_SOUTH_WEST(0x40),

		/**
		 * The wall west flag.
		 */
		WALL_WEST(0x80),

		/**
		 * The object tile flag.
		 */
		OBJECT_TILE(0x100),

		/**
		 * An unknown flag (unused by the client).
		 */
		UNKNOWN(0x10_000),

		/**
		 * The block walk tile flag.
		 */
		BLOCK_WALK(0x20_000),

		/**
		 * The decoration tile flag.
		 */
		DECORATION_TILE(0x40_000),

		/**
		 * The blocked tile flag.
		 */
		BOCKED_TILE(0x200_000),

		/**
		 * The unloaded flag.
		 */
		UNLOADED(0x1_000_000),

		/**
		 * The allow range flag.
		 */
		ALLOW_RANGE(0x40_000_000);

		/**
		 * The ImmutableList of CollisionFlags.
		 */
		public static final List<CollisionFlag> FLAGS = ImmutableList.copyOf(values());

		/**
		 * The value of this CollisionFlag.
		 */
		private final int value;

		/**
		 * Creates the CollisionFlag.
		 * 
		 * @param value The value.
		 */
		CollisionFlag(int value) {
			this.value = value;
		}

		/**
		 * Returns whether or not this CollisionFlag is set in the specified integer value.
		 * 
		 * @param value The integer value.
		 * @return {@code true} if this CollisionFlag is set, {@code false} if not.
		 */
		public boolean setIn(int value) {
			return (value & this.value) != 0;
		}

	}

	/**
	 * The entry point for the printer.
	 * 
	 * @param args The application arguments.
	 */
	public static void main(String[] args) {
		if (args.length != 0) {
			IntStream flags = parse(args);

			CollisionFlagIdentifier parser = new CollisionFlagIdentifier(flags);
			parser.print();
			return;
		}

		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				String in = scanner.nextLine();
				if (in.equalsIgnoreCase("q") || in.equalsIgnoreCase("quit")) {
					break;
				}

				String[] values = in.split(" ");
				IntStream flags;
				try {
					flags = parse(values);
				} catch (NumberFormatException e) {
					System.err.println("Input hex or decimal literals only.");
					continue;
				}

				CollisionFlagIdentifier parser = new CollisionFlagIdentifier(flags);
				parser.print();
			}
		}
	}

	/**
	 * Parses the specified Strings, returning the parsed values in an {@link IntStream}.
	 * 
	 * @param strings The String array.
	 * @return The IntStream.
	 */
	private static IntStream parse(String[] strings) {
		return Arrays.stream(strings).mapToInt(
				string -> string.startsWith("0x") ? Integer.parseInt(string.substring(2), 16) : Integer.parseInt(string));
	}

	/**
	 * The IntStream of values to parse.
	 */
	private final IntStream values;

	/**
	 * Creates the CollisionFlagParser.
	 *
	 * @param values The {@link IntStream} of values to parse.
	 */
	private CollisionFlagIdentifier(IntStream values) {
		this.values = values;
	}

	/**
	 * Gets a {@link Stream} of {@link CollisionFlag} values the specified value contains.
	 * 
	 * @param value The value.
	 * @return The Stream of CollisionFlags.
	 */
	private Stream<CollisionFlag> getFlags(int value) {
		return CollisionFlag.FLAGS.stream().filter(flag -> flag.setIn(value));
	}

	/**
	 * Prints the {@link CollisionFlag}s stored in each of the values.
	 */
	private void print() {
		values.forEach(value -> {
			String message = Integer.toString(value) + " contains: ";
			message += getFlags(value).map(CollisionFlag::name).collect(Collectors.joining(", "));

			System.out.println(message);
		});
	}

}