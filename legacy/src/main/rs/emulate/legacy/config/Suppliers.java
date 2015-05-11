package rs.emulate.legacy.config;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import rs.emulate.legacy.config.animation.AnimationDefinition;
import rs.emulate.legacy.config.animation.DefaultAnimationDefinition;
import rs.emulate.legacy.config.graphic.DefaultGraphicDefinition;
import rs.emulate.legacy.config.graphic.GraphicDefinition;
import rs.emulate.legacy.config.item.DefaultItemDefinition;
import rs.emulate.legacy.config.item.ItemDefinition;
import rs.emulate.legacy.config.kit.DefaultIdentikitDefinition;
import rs.emulate.legacy.config.kit.IdentikitDefinition;
import rs.emulate.legacy.config.npc.DefaultNpcDefinition;
import rs.emulate.legacy.config.npc.NpcDefinition;
import rs.emulate.legacy.config.object.DefaultObjectDefinition;
import rs.emulate.legacy.config.object.ObjectDefinition;
import rs.emulate.legacy.config.varbit.BitVariableDefinition;
import rs.emulate.legacy.config.varbit.DefaultBitVariableDefinition;
import rs.emulate.legacy.config.varp.DefaultParameterVariableDefinition;
import rs.emulate.legacy.config.varp.ParameterVariableDefinition;

/**
 * Contains {@link DefinitionSupplier}s.
 *
 * @author Major
 */
public final class Suppliers {

	/**
	 * Supplies material for config decoders to avoid reflection.
	 *
	 * @param <T> The definition type.
	 */
	public static final class DefinitionSupplier<T extends MutableConfigDefinition> {

		/**
		 * The BiFunction that returns a new MutableDefinition.
		 */
		private final BiFunction<Integer, ConfigPropertyMap, T> creator;

		/**
		 * The name of the ArchiveEntry.
		 */
		private final String name;

		/**
		 * The PropertyMap Supplier.
		 */
		private final Supplier<ConfigPropertyMap> supplier;

		/**
		 * Creates the definition supplier.
		 *
		 * @param name The name of the ArchiveEntry, <strong>without</strong> an extension.
		 * @param supplier A {@link Supplier} that returns a {@link DefaultConfigDefinition}.
		 * @param creator An (id, {@link ConfigPropertyMap}) {@link BiFunction} that creates a
		 *            {@link MutableConfigDefinition} .
		 */
		public DefinitionSupplier(String name, Supplier<ConfigPropertyMap> supplier,
				BiFunction<Integer, ConfigPropertyMap, T> creator) {
			this.name = name;
			this.supplier = supplier;
			this.creator = creator;
		}

		/**
		 * Creates a new {@link MutableConfigDefinition}.
		 *
		 * @param id The id.
		 * @param map The {@link ConfigPropertyMap}.
		 * @return The MutableDefinition.
		 */
		public T createDefinition(int id, ConfigPropertyMap map) {
			return creator.apply(id, map);
		}

		/**
		 * Gets the name of the ArchiveEntry.
		 *
		 * @return The name.
		 */
		public String getName() {
			return name;
		}

		/**
		 * Gets a default {@link ConfigPropertyMap} from the supplier.
		 *
		 * @return The PropertyMap.
		 */
		public ConfigPropertyMap supplyDefault() {
			return supplier.get();
		}

	}

	/**
	 * A {@link DefinitionSupplier} for {@link AnimationDefinition}s.
	 */
	public static final DefinitionSupplier<AnimationDefinition> ANIMATION_SUPPLIER = new DefinitionSupplier<>(
			AnimationDefinition.ENTRY_NAME, DefaultAnimationDefinition.SUPPLIER, AnimationDefinition::new);

	/**
	 * A {@link DefinitionSupplier} for {@link BitVariableDefinition}s.
	 */
	public static final DefinitionSupplier<BitVariableDefinition> BIT_VARIABLE_SUPPLIER = new DefinitionSupplier<>(
			BitVariableDefinition.ENTRY_NAME, DefaultBitVariableDefinition.SUPPLIER, BitVariableDefinition::new);

	/**
	 * A {@link DefinitionSupplier} for {@link GraphicDefinition}s.
	 */
	public static final DefinitionSupplier<GraphicDefinition> GRAPHIC_SUPPLIER = new DefinitionSupplier<>(
			GraphicDefinition.ENTRY_NAME, DefaultGraphicDefinition.SUPPLIER, GraphicDefinition::new);

	/**
	 * A {@link DefinitionSupplier} for {@link IdentikitDefinition}s.
	 */
	public static final DefinitionSupplier<IdentikitDefinition> IDENTIKIT_SUPPLIER = new DefinitionSupplier<>(
			IdentikitDefinition.ENTRY_NAME, DefaultIdentikitDefinition.SUPPLIER, IdentikitDefinition::new);

	/**
	 * A {@link DefinitionSupplier} for {@link ItemDefinition}s.
	 */
	public static final DefinitionSupplier<ItemDefinition> ITEM_SUPPLIER = new DefinitionSupplier<>(
			ItemDefinition.ENTRY_NAME, DefaultItemDefinition.SUPPLIER, ItemDefinition::new);

	/**
	 * A {@link DefinitionSupplier} for {@link NpcDefinition}s.
	 */
	public static final DefinitionSupplier<NpcDefinition> NPC_SUPPLIER = new DefinitionSupplier<>(
			NpcDefinition.ENTRY_NAME, DefaultNpcDefinition.SUPPLIER, NpcDefinition::new);

	/**
	 * A {@link DefinitionSupplier} for {@link ObjectDefinition}s.
	 */
	public static final DefinitionSupplier<ObjectDefinition> OBJECT_SUPPLIER = new DefinitionSupplier<>(
			ObjectDefinition.ENTRY_NAME, DefaultObjectDefinition.SUPPLIER, ObjectDefinition::new);

	/**
	 * A {@link DefinitionSupplier} for {@link ParameterVariableDefinition}s.
	 */
	public static final DefinitionSupplier<ParameterVariableDefinition> PARAMETER_VARIABLE_SUPPLIER = new DefinitionSupplier<>(
			ParameterVariableDefinition.ENTRY_NAME, DefaultParameterVariableDefinition.SUPPLIER,
			ParameterVariableDefinition::new);

}