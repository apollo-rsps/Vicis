# Vicis

Vicis is an editor for Runescape game resources. Currently it includes code for encoding and decoding all versions of the Runescape cache post-RS2 beta, although only some of these versions contain support for converting the raw file data into what they actually represent.

Vicis places significant emphasis on being extendable and easy to customise. Users who have already customised their legacy (2xx-3xx) game resources can substitute the default codec with a new [MutableConfigDefinition](https://github.com/Major-/Vicis/blob/master/legacy/src/main/rs/emulate/legacy/config/MutableConfigDefinition.java#L16) and [DefaultConfigDefinition](https://github.com/Major-/Vicis/blob/master/legacy/src/main/rs/emulate/legacy/config/DefaultConfigDefinition.java) (see [the example](https://github.com/Major-/Vicis/tree/master/legacy/src/example/rs/emulate/legacy/config)).

Vicis does not have a GUI, but is easy to use as a library to edit the game resources.

## License

See [LICENSE](#LICENSE).
