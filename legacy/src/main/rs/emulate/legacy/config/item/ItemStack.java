package rs.emulate.legacy.config.item;

import rs.emulate.shared.util.DataBuffer;
import rs.emulate.util.Assertions;

/**
 * A utility class containing an item stack amount and its corresponding model id.
 *
 * @author Major
 */
public final class ItemStack {

	/**
	 * The empty ItemStack, used as the default value.
	 */
	public static final ItemStack EMPTY = new ItemStack();

	/**
	 * Decodes an ItemStack from the specified {@link DataBuffer}.
	 *
	 * @param buffer The Buffer.
	 * @return The ItemStack.
	 */
	public static ItemStack decode(DataBuffer buffer) {
		int amount = buffer.getUnsignedShort();
		int model = buffer.getUnsignedShort();

		return new ItemStack(amount, model);
	}

	/**
	 * Encodes the specified ItemStack into the specified {@link DataBuffer}.
	 *
	 * @param buffer The Buffer.
	 * @param stack The ItemStack.
	 */
	public static void encode(DataBuffer buffer, ItemStack stack) {
		buffer.putShort(stack.getAmount()).putShort(stack.getModel());
	}

	/**
	 * The stack amount.
	 */
	private final int amount;

	/**
	 * The model id.
	 */
	private final int model;

	/**
	 * Creates the ItemStack.
	 *
	 * @param amount The stack amount.
	 * @param model The model id.
	 */
	public ItemStack(int amount, int model) {
		Assertions.checkPositive(amount, "Amount must be greater than 0, received " + amount + ".");
		Assertions.checkNonNegative(model, "Model id must be greater than or equal to 0, received " + model + ".");
		this.amount = amount;
		this.model = model;
	}

	/**
	 * Creates an ItemStack with default values.
	 */
	private ItemStack() {
		amount = 0;
		model = -1;
	}

	/**
	 * Gets the amount of this ItemStack.
	 *
	 * @return The amount.
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Gets the model of this ItemStack.
	 *
	 * @return The model.
	 */
	public int getModel() {
		return model;
	}

}