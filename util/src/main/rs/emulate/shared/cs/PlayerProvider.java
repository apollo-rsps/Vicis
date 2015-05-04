package rs.emulate.shared.cs;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

import rs.emulate.shared.world.Position;

/**
 * Provides player-related values used during the interpretation of a ClientScript.
 *
 * @author Major
 */
public class PlayerProvider {

	/**
	 * A builder class for {@link PlayerProvider}s.
	 */
	public static class Builder {

		/**
		 * The supplier of player run energy levels.
		 */
		private IntSupplier energies = () -> 100;

		/**
		 * The Supplier of Positions.
		 */
		private Supplier<Position> positions = () -> new Position(10, 10);

		/**
		 * The settings supplier.
		 */
		private IntUnaryOperator settings = setting -> 1;

		/**
		 * The SkillProvider.
		 */
		private SkillProvider skills = id -> new Skill(0, 1, 1);

		/**
		 * The Supplier of player weights.
		 */
		private IntSupplier weights = () -> 42;

		/**
		 * Builds the contents of this Builder into a {@link PlayerProvider}.
		 *
		 * @return The PlayerProvider.
		 */
		public PlayerProvider build() {
			return new PlayerProvider(skills, positions, settings, weights, energies);
		}

	}

	/**
	 * Returns the default {@link PlayerProvider}.
	 *
	 * @return The default.
	 */
	public static PlayerProvider defaultProvider() {
		return new Builder().build();
	}

	/**
	 * Returns a new {@link Builder}.
	 *
	 * @return The Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * A skill of a player.
	 */
	public static final class Skill {

		/**
		 * The current level of the Skill.
		 */
		private final int current;

		/**
		 * The experience of the Skill.
		 */
		private final double experience;

		/**
		 * The maximum level of the Skill.
		 */
		private final int maximum;

		/**
		 * Creates the Skill.
		 *
		 * @param experience The experience of the Skill.
		 * @param current The current level of the Skill.
		 * @param maximum The maximum level of the Skill.
		 */
		public Skill(double experience, int current, int maximum) {
			this.experience = experience;
			this.current = current;
			this.maximum = maximum;
		}

		/**
		 * Gets the current level of this Skill.
		 *
		 * @return The current level.
		 */
		public int getCurrentLevel() {
			return current;
		}

		/**
		 * Gets the experience of this Skill.
		 *
		 * @return The experience.
		 */
		public double getExperience() {
			return experience;
		}

		/**
		 * Gets the maximum level of this Skill.
		 *
		 * @return The maximum level.
		 */
		public int getMaximumLevel() {
			return maximum;
		}

	}

	/**
	 * Provides {@link Skill}s used during the execution of a ClientScript.
	 */
	@FunctionalInterface
	public interface SkillProvider extends Function<Integer, Skill> {

		/**
		 * Gets the {@link Skill} with the specified id.
		 *
		 * @param id The id.
		 * @return The Skill.
		 */
		default Skill get(int id) {
			return apply(id);
		}

		/**
		 * Gets an array containing each {@link Skill}.
		 *
		 * @param count The amount of Skills to get.
		 * @return The Skills.
		 */
		default Skill[] getSkills(int count) {
			Skill[] skills = new Skill[count];
			for (int id = 0; id < skills.length; id++) {
				skills[id] = get(id);
			}

			return skills;
		}

		/**
		 * Gets the total level of the {@link Skill}s in this SkillProvider.
		 *
		 * @param count The amount of Skills to get.
		 * @return The total.
		 */
		default int getTotal(int count) {
			return Arrays.stream(getSkills(count)).mapToInt(Skill::getMaximumLevel).sum();
		}
	}

	/**
	 * The supplier of player run energy levels.
	 */
	private final IntSupplier energies;

	/**
	 * The Supplier of Positions.
	 */
	private final Supplier<Position> positions;

	/**
	 * The settings supplier.
	 */
	private final IntUnaryOperator settings;

	/**
	 * The SkillProvider.
	 */
	private final SkillProvider skills;

	/**
	 * The Supplier of player weights.
	 */
	private final IntSupplier weights;

	/**
	 * Creates the PlayerProvider.
	 *
	 * @param skills The {@link SkillProvider}.
	 * @param positions The {@link Supplier} of {@link Position}s.
	 * @param settings The {@link IntFunction} that supplies settings.
	 * @param weights The {@link Supplier} of player weights.
	 * @param energies The {@link Supplier} of player run energy levels.
	 */
	public PlayerProvider(SkillProvider skills, Supplier<Position> positions, IntUnaryOperator settings,
			IntSupplier weights, IntSupplier energies) {
		this.skills = skills;
		this.positions = positions;
		this.settings = settings;
		this.weights = weights;
		this.energies = energies;
	}

	/**
	 * Gets the next {@link Position} from the {@link Supplier} used by this PlayerProvider.
	 *
	 * @return The Position.
	 */
	public Position getPosition() {
		return positions.get();
	}

	/**
	 * Gets the run energy level of the player.
	 *
	 * @return The run energy level.
	 */
	public int getRunEnergy() {
		return energies.getAsInt();
	}

	/**
	 * Gets the setting with the specified id.
	 *
	 * @param id The id.
	 * @return The setting value.
	 */
	public int getSetting(int id) {
		return settings.applyAsInt(id);
	}

	/**
	 * Gets the {@link Skill} with the specified id.
	 *
	 * @param id The id of the Skill.
	 * @return The Skill.
	 */
	public Skill getSkill(int id) {
		return skills.get(id);
	}

	/**
	 * Gets the combat level from the {@link SkillProvider} used by this PlayerProvider.
	 *
	 * @return The combat level.
	 */
	public int getCombatLevel() {
		Skill[] skills = this.skills.getSkills(21);
		int attack = skills[0].getMaximumLevel();
		int defence = skills[1].getMaximumLevel();
		int strength = skills[2].getMaximumLevel();
		int hitpoints = skills[3].getMaximumLevel();
		int prayer = skills[4].getMaximumLevel();
		int ranged = skills[5].getMaximumLevel();
		int magic = skills[6].getMaximumLevel();

		double base = (defence + hitpoints + Math.floor(prayer / 2)) * 0.25;
		double melee = (attack + strength) * 0.325;

		return (int) (base + Math.max(melee, Math.max(ranged, magic) * 0.4875));
	}

	/**
	 * Gets the total level from the {@link SkillProvider} used by this PlayerProvider.
	 *
	 * @return The total level.
	 */
	public int getTotalLevel() {
		return skills.getTotal(21);
	}

	/**
	 * Gets the weight of the player.
	 *
	 * @return The weight.
	 */
	public int getWeight() {
		return weights.getAsInt();
	}

}