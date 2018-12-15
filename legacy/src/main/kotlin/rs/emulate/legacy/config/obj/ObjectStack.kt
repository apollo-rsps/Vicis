package rs.emulate.legacy.config.obj

import io.netty.buffer.ByteBuf

/**
 * A utility class containing an item stack amount and its corresponding model id.
 */
class ObjectStack {

    /**
     * The stack amount.
     */
    val amount: Int

    /**
     * The model id.
     */
    val model: Int

    /**
     * Creates the ItemStack.
     */
    constructor(amount: Int, model: Int) {
        require(amount.toLong() >= 0) { "Amount must be greater than or equal to 0, received $amount." }
        require(model.toLong() >= 0) { "Model id must be greater than or equal to 0, received $model." }
        this.amount = amount
        this.model = model
    }

    /**
     * Creates an ItemStack with default values.
     */
    private constructor() {
        amount = 0
        model = -1
    }

    companion object {

        /**
         * The empty ItemStack, used as the default value.
         */
        val EMPTY = ObjectStack()

        /**
         * Decodes an ItemStack from the specified [ByteBuf].
         */
        fun decode(buffer: ByteBuf): ObjectStack {
            val amount = buffer.readUnsignedShort()
            val model = buffer.readUnsignedShort()

            return ObjectStack(amount, model)
        }

        /**
         * Encodes the specified ItemStack into the specified [ByteBuf].
         */
        fun encode(buffer: ByteBuf, stack: ObjectStack): ByteBuf {
            return buffer.writeShort(stack.amount)
                .writeShort(stack.model)
        }
    }

}
