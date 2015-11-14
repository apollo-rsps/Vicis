package rs.emulate.modern.def;

import rs.emulate.shared.util.DataBuffer;

import java.nio.ByteBuffer;

/**
 * A class that loads item/model information from the cache. TODO Get rid of this crap.
 *
 * @author Graham
 * @author `Discardedx2
 */
@SuppressWarnings("javadoc")
public final class ItemDefinition {

	/**
	 * Decodes an item definition from the specified {@link ByteBuffer}.
	 *
	 * @param buffer The buffer.
	 * @return The item definition.
	 */
	@SuppressWarnings("unused")
	public static ItemDefinition decode(DataBuffer buffer) {
		ItemDefinition def = new ItemDefinition();
		def.groundOptions = new String[]{ null, null, "take", null, null };
		def.inventoryOptions = new String[]{ null, null, null, null, "drop" };
		while (true) {
			int opcode = buffer.getByte() & 0xFF;
			if (opcode == 0) {
				break;
			}
			if (opcode == 1) {
				def.inventoryModelId = buffer.getUnsignedShort();
			} else if (opcode == 2) {
				def.name = buffer.getJagString();
			} else if (opcode == 4) {
				def.modelZoom = buffer.getUnsignedShort();
			} else if (opcode == 5) {
				def.modelRotation1 = buffer.getUnsignedShort();
			} else if (opcode == 6) {
				def.modelRotation2 = buffer.getUnsignedShort();
			} else if (opcode == 7) {
				def.modelOffset1 = buffer.getUnsignedShort();
				if (def.modelOffset1 > 32767) {
					def.modelOffset1 -= 65536;
				}
				def.modelOffset1 <<= 0;
			} else if (opcode == 8) {
				def.modelOffset2 = buffer.getUnsignedShort();
				if (def.modelOffset2 > 32767) {
					def.modelOffset2 -= 65536;
				}
				def.modelOffset2 <<= 0;
			} else if (opcode == 11) {
				def.stackable = true;
			} else if (opcode == 12) {
				def.value = buffer.getInt();
			} else if (opcode == 16) {
				def.membersOnly = true;
			} else if (opcode == 18) {
				buffer.getShort();
			} else if (opcode == 23) {
				def.maleWearModel1 = buffer.getUnsignedShort();
			} else if (opcode == 24) {
				def.femaleWearModel1 = buffer.getUnsignedShort();
			} else if (opcode == 25) {
				def.maleWearModel2 = buffer.getUnsignedShort();
			} else if (opcode == 26) {
				def.femaleWearModel2 = buffer.getUnsignedShort();
			} else if (opcode >= 30 && opcode < 35) {
				def.groundOptions[opcode - 30] = buffer.getJagString();
			} else if (opcode >= 35 && opcode < 40) {
				def.inventoryOptions[opcode - 35] = buffer.getJagString();
			} else if (opcode == 40) {
				int length = buffer.getByte() & 0xFF;
				def.originalModelColors = new short[length];
				def.modifiedModelColors = new short[length];
				for (int index = 0; index < length; index++) {
					def.originalModelColors[index] = (short) (buffer.getUnsignedShort());
					def.modifiedModelColors[index] = (short) (buffer.getUnsignedShort());
				}
			} else if (opcode == 41) {
				int length = buffer.getByte() & 0xFF;
				def.textureColour1 = new short[length];
				def.textureColour2 = new short[length];
				for (int index = 0; index < length; index++) {
					def.textureColour1[index] = (short) (buffer.getUnsignedShort());
					def.textureColour2[index] = (short) (buffer.getUnsignedShort());
				}
			} else if (opcode == 42) {
				int length = buffer.getByte() & 0xFF;
				for (int index = 0; index < length; index++) {
					buffer.getByte();
				}
			} else if (opcode == 65) {
				def.unnoted = true;
			} else if (opcode == 78) {
				def.colourEquip1 = buffer.getUnsignedShort();
			} else if (opcode == 79) {
				def.colourEquip2 = buffer.getUnsignedShort();
			} else if (opcode == 90) {
				buffer.getShort();
			} else if (opcode == 91) {
				buffer.getShort();
			} else if (opcode == 92) {
				buffer.getShort();
			} else if (opcode == 93) {
				buffer.getShort();
			} else if (opcode == 95) {
				buffer.getShort();
			} else if (opcode == 96) {
				buffer.getByte();
			} else if (opcode == 97) {
				def.notedId = buffer.getUnsignedShort();
			} else if (opcode == 98) {
				def.notedTemplateId = buffer.getUnsignedShort();
			} else if (opcode >= 100 && opcode < 110) {
				if (def.stackableIds == null) {
					def.stackableIds = new int[10];
					def.stackableAmounts = new int[10];
				}
				def.stackableIds[opcode - 100] = buffer.getUnsignedShort();
				def.stackableAmounts[opcode - 100] = buffer.getUnsignedShort();
			} else if (opcode == 110) {
				buffer.getShort();
			} else if (opcode == 111) {
				buffer.getShort();
			} else if (opcode == 112) {
				buffer.getShort();
			} else if (opcode == 113) {
				buffer.getByte();
			} else if (opcode == 114) {
				buffer.getByte();
			} else if (opcode == 115) {
				def.teamId = buffer.getUnsignedByte();
			} else if (opcode == 121) {
				def.lendId = buffer.getUnsignedShort();
			} else if (opcode == 122) {
				def.lendTemplateId = buffer.getUnsignedShort();
			} else if (opcode == 125) {
				buffer.getByte();
				buffer.getByte();
				buffer.getByte();
			} else if (opcode == 126) {
				buffer.getByte();
				buffer.getByte();
				buffer.getByte();
			} else if (opcode == 127) {
				buffer.getByte();
				buffer.getShort();
			} else if (opcode == 128) {
				buffer.getByte();
				buffer.getShort();
			} else if (opcode == 129) {
				buffer.getByte();
				buffer.getShort();
			} else if (opcode == 130) {
				buffer.getByte();
				buffer.getShort();
			} else if (opcode == 132) {
				int len = buffer.getByte() & 0xFF;
				for (int index = 0; index < len; index++) {
					buffer.getShort();
				}
			} else if (opcode == 249) {
				int length = buffer.getByte() & 0xFF;
				for (int index = 0; index < length; index++) {
					boolean stringInstance = buffer.getByte() == 1;
					buffer.getUnsignedTriByte();
					buffer.getJagString();
					buffer.getInt();
				}
			}
		}
		return def;
	}

	private int colourEquip1;

	private int colourEquip2;

	private int femaleWearModel1;

	private int femaleWearModel2;

	private String[] groundOptions;

	private int inventoryModelId;

	private String[] inventoryOptions;

	private int lendId;

	private int lendTemplateId;

	private int maleWearModel1 = -1;

	private int maleWearModel2;

	private boolean membersOnly;

	private int modelOffset1;

	private int modelOffset2;

	private int modelRotation1;

	private int modelRotation2;

	private int modelZoom;

	private short[] modifiedModelColors;

	private String name;

	private int notedId;

	private int notedTemplateId;

	private short[] originalModelColors;

	private boolean stackable;

	private int[] stackableAmounts;

	private int[] stackableIds;

	private int teamId;

	private short[] textureColour1;

	private short[] textureColour2;

	private boolean unnoted;

	private int value;

	public int getColourEquip1() {
		return colourEquip1;
	}

	public int getColourEquip2() {
		return colourEquip2;
	}

	public int getFemaleWearModel1() {
		return femaleWearModel1;
	}

	public int getFemaleWearModel2() {
		return femaleWearModel2;
	}

	public String[] getGroundOptions() {
		return groundOptions;
	}

	public int getInventoryModelId() {
		return inventoryModelId;
	}

	public String[] getInventoryOptions() {
		return inventoryOptions;
	}

	public int getLendId() {
		return lendId;
	}

	public int getLendTemplateId() {
		return lendTemplateId;
	}

	public int getMaleWearModel1() {
		return maleWearModel1;
	}

	public int getMaleWearModel2() {
		return maleWearModel2;
	}

	public int getModelOffset1() {
		return modelOffset1;
	}

	public int getModelOffset2() {
		return modelOffset2;
	}

	public int getModelRotation1() {
		return modelRotation1;
	}

	public int getModelRotation2() {
		return modelRotation2;
	}

	public int getModelZoom() {
		return modelZoom;
	}

	public short[] getModifiedModelColors() {
		return modifiedModelColors;
	}

	public String getName() {
		return name;
	}

	public int getNotedId() {
		return notedId;
	}

	public int getNotedTemplateId() {
		return notedTemplateId;
	}

	public short[] getOriginalModelColors() {
		return originalModelColors;
	}

	public int[] getStackableAmounts() {
		return stackableAmounts;
	}

	public int[] getStackableIds() {
		return stackableIds;
	}

	public int getTeamId() {
		return teamId;
	}

	public short[] getTextureColour1() {
		return textureColour1;
	}

	public short[] getTextureColour2() {
		return textureColour2;
	}

	public int getValue() {
		return value;
	}

	public boolean isMembersOnly() {
		return membersOnly;
	}

	public boolean isStackable() {
		return stackable;
	}

	public boolean isUnnoted() {
		return unnoted;
	}

}