package rs.emulate.legacy.config.npc;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import rs.emulate.shared.util.DataBuffer;

/**
 * A wrapper class containing four movement animations used by an npc.
 *
 * @author Major
 */
public final class MovementAnimationSet {

	/**
	 * The empty MovementAnimationSet instance, to be used as the default value.
	 */
	public static final MovementAnimationSet EMPTY = new MovementAnimationSet(-1, -1, -1, -1);

	/**
	 * Decodes a MovementAnimationSet from the specified {@link DataBuffer}.
	 *
	 * @param buffer The Buffer.
	 * @return The MovementAnimationSet.
	 */
	public static MovementAnimationSet decode(DataBuffer buffer) {
		int walking = buffer.getUnsignedShort();
		int halfTurn = buffer.getUnsignedShort();
		int clockwiseQuarterTurn = buffer.getUnsignedShort();
		int anticlockwiseQuarterTurn = buffer.getUnsignedShort();

		return new MovementAnimationSet(walking, halfTurn, clockwiseQuarterTurn, anticlockwiseQuarterTurn);
	}

	/**
	 * Encodes the specified {@link MovementAnimationSet} into the specified {@link DataBuffer}.
	 *
	 * @param buffer The Buffer.
	 * @param set The MovementAnimationSet.
	 */
	public static void encode(DataBuffer buffer, MovementAnimationSet set) {
		buffer.putShort(set.getWalkingId()).putShort(set.getHalfTurnId());
		buffer.putShort(set.getClockwiseQuarterTurnId()).putShort(set.getAnticlockwiseQuarterTurnId());
	}

	/**
	 * The anticlockwise quarter turn (i.e. a anticlockwise rotation of {@code pi / 4}) id.
	 */
	private final int anticlockwiseQuarterTurn;

	/**
	 * The clockwise quarter turn (i.e. a clockwise rotation of {@code pi / 4}) id.
	 */
	private final int clockwiseQuarterTurn;

	/**
	 * The half turn (i.e. a rotation of {@code pi / 2}) animation id.
	 */
	private final int halfTurn;

	/**
	 * The walking animation id.
	 */
	private final int walking;

	/**
	 * Creates the MovementAnimationSet.
	 *
	 * @param walking The walking animation id.
	 * @param halfTurn The half turn animation id.
	 * @param clockwiseQuarterTurn The clockwise quarter turn animation id.
	 * @param anticlockwiseQuarterTurn The anticlockwise quarter turn animation id.
	 */
	public MovementAnimationSet(int walking, int halfTurn, int clockwiseQuarterTurn, int anticlockwiseQuarterTurn) {
		Preconditions.checkArgument(walking >= -1 && halfTurn >= -1 && clockwiseQuarterTurn >= -1
				&& anticlockwiseQuarterTurn >= -1, "Specified animation ids must be greater than or equal to -1.");

		this.walking = walking;
		this.halfTurn = halfTurn;
		this.clockwiseQuarterTurn = clockwiseQuarterTurn;
		this.anticlockwiseQuarterTurn = anticlockwiseQuarterTurn;
	}

	/**
	 * Gets the anticlockwise quarter turn animation id.
	 *
	 * @return The anticlockwise quarter turn animation id.
	 */
	public int getAnticlockwiseQuarterTurnId() {
		return anticlockwiseQuarterTurn;
	}

	/**
	 * Gets the clockwise quarter turn animation id.
	 *
	 * @return The clockwise quarter turn animation id.
	 */
	public int getClockwiseQuarterTurnId() {
		return clockwiseQuarterTurn;
	}

	/**
	 * Gets the half turn animation id.
	 *
	 * @return The half turn animation id.
	 */
	public int getHalfTurnId() {
		return halfTurn;
	}

	/**
	 * Gets the walking animation id.
	 *
	 * @return The walking animation id.
	 */
	public int getWalkingId() {
		return walking;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("Walking animation", walking).add("Half turn animation", halfTurn)
				.add("Clockwise quarter turn animation", clockwiseQuarterTurn)
				.add("Anticlockwise quarter turn animation", anticlockwiseQuarterTurn).toString();
	}

}