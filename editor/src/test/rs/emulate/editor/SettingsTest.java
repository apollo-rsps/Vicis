package rs.emulate.editor;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import rs.emulate.editor.util.Settings;

/**
 * Tests the application {@link Settings}.
 * 
 * @author Major
 */
public final class SettingsTest {

	/**
	 * The settings used in this test.
	 */
	private final Settings settings = new Settings();

	/**
	 * Tests the code that verifies there is no infinite recursion when interpolating.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void infiniteInterpolation() {
		settings.put("first", "$second$");
		settings.put("second", "$third$");
		settings.put("third", "$first$");

		settings.get("first");
	}

	/**
	 * Tests the string interpolation code.
	 */
	@Test
	public void interpolation() {
		settings.put("test", "abc/$user-home$/a");
		String expected = "abc/" + System.getProperty("user.home") + "/a";
		assertEquals(settings.get("test"), expected);
	}

	/**
	 * Tests that retrieving an invalid setting throws an {@link IllegalArgumentException}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void invalidName() {
		settings.get("qwertyuiop");
	}

}