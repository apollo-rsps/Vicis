package rs.emulate.tools;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

/**
 * A simple command line utility to print the adjacencies contained in a collision map adjacency value.
 *
 * @author Major
 */
public final class AdjacencyParser {

	/**
	 * An enum of adjacency flags and their numerical values.
	 */
	private static enum Adjacency {

		/**
		 * The blocked tile flag.
		 */
		BOCKED_TILE(0x200_000),

		/**
		 * The object tile flag.
		 */
		OBJECT_TILE(0x100),

		/**
		 * An unknown flag (unused by the client).
		 */
		UNKNOWN(0x10_000),

		/**
		 * The unloaded flag.
		 */
		UNLOADED(0x1_000_000),

		/**
		 * The wall east flag.
		 */
		WALL_EAST(0x8),

		/**
		 * The wall north flag.
		 */
		WALL_NORTH(0x2),

		/**
		 * The wall north-east flag.
		 */
		WALL_NORTH_EAST(0x4),

		/**
		 * The wall north-west flag.
		 */
		WALL_NORTH_WEST(0X1),

		/**
		 * The wall south flag.
		 */
		WALL_SOUTH(0x20),

		/**
		 * The wall south-east flag.
		 */
		WALL_SOUTH_EAST(0x10),

		/**
		 * The wall south-west flag.
		 */
		WALL_SOUTH_WEST(0x40),

		/**
		 * The wall west flag.
		 */
		WALL_WEST(0x80);

		/**
		 * The immutable list of adjacency flags.
		 */
		public static final List<Adjacency> FLAGS = ImmutableList.copyOf(Arrays.asList(values()));

		/**
		 * The value of this adjacency.
		 */
		private final int value;

		/**
		 * Creates the adjacency.
		 * 
		 * @param value The value.
		 */
		private Adjacency(int value) {
			this.value = value;
		}

	}

	/**
	 * The entry point for the printer.
	 * 
	 * @param args The application arguments.
	 */
	public static void main(String[] args) {
		if (args.length != 0) {
			List<Integer> flags = parse(args);
			print(flags);

			return;
		}

		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				String in = scanner.nextLine();
				if (in.equals("exit") || in.equals("q")) {
					System.exit(0);
				}

				String[] values = in.split(" ");
				List<Integer> flags = parse(values);
				print(flags);
			}
		}
	}

	/**
	 * Gets a {@link List} of {@link Adjacency} values the specified flag 'contains'.
	 * 
	 * @param flag The flag.
	 * @return The list of adjacency values.
	 */
	private static List<Adjacency> getAdjacencies(int flag) {
		return Adjacency.FLAGS.stream().filter(adjacency -> (flag & adjacency.value) != 0).collect(Collectors.toList());
	}

	/**
	 * Parses the string array, adding the values to the {@link List}.
	 * 
	 * @param strings The string array.
	 * @return The list of integers.
	 */
	private static List<Integer> parse(String[] strings) {
		return Arrays.stream(strings)
				.map(string -> string.startsWith("0x") ? Integer.parseInt(string.substring(2), 16) : Integer.parseInt(string))
				.collect(Collectors.toList());
	}

	/**
	 * Finds the adjacencies stored in each flag, and prints them
	 * 
	 * @param flags The {@link List} of flags.
	 */
	private static void print(List<Integer> flags) {
		for (int flag : flags) {
			StringBuilder builder = new StringBuilder("Flag=").append(flag).append(" contains: ");

			List<Adjacency> adjacencies = getAdjacencies(flag);
			adjacencies.forEach(value -> builder.append(value).append(", "));

			if (adjacencies.size() == 0) {
				builder.append("no adjacencies.");
			}

			System.out.println(builder.toString());
		}
	}

}