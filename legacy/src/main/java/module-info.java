module rs.emulate.legacy {
	requires kotlin.stdlib;
	requires rs.emulate.util;

	// Export utils and helpers in root package.
	exports rs.emulate.legacy;

	exports rs.emulate.legacy.archive;

	// Configuration decoders, encoders, data models.
	exports rs.emulate.legacy.config;
	exports rs.emulate.legacy.config.floor;
	exports rs.emulate.legacy.config.kit;
	exports rs.emulate.legacy.config.location;
	exports rs.emulate.legacy.config.npc;
	exports rs.emulate.legacy.config.obj;
	exports rs.emulate.legacy.config.sequence;
	exports rs.emulate.legacy.config.spotanim;
	exports rs.emulate.legacy.config.varbit;
	exports rs.emulate.legacy.config.varp;

	exports rs.emulate.legacy.frame;
	exports rs.emulate.legacy.graphics;
	exports rs.emulate.legacy.graphics.image;
	exports rs.emulate.legacy.graphics.sprite;
	exports rs.emulate.legacy.map;
	exports rs.emulate.legacy.model;
	exports rs.emulate.legacy.title.font;
	exports rs.emulate.legacy.version;
	exports rs.emulate.legacy.version.crc;
	exports rs.emulate.legacy.version.map;

	// Widget and ClientScript codecs/data models.
	exports rs.emulate.legacy.widget;
	exports rs.emulate.legacy.widget.script;
	exports rs.emulate.legacy.widget.type;
}
