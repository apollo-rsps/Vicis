package rs.emulate.legacy.prop;

import org.junit.Test;
import rs.emulate.legacy.config.DynamicConfigPropertyType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests the caching used in {@link DynamicConfigPropertyType}s to ensure that properties with the same name and opcode
 * will be the same object.
 *
 * @author Major
 */
public final class DynamicPropertyTypeTest {

	/**
	 * The name of the DynamicPropertyType.
	 */
	private static final String NAME = "test";

	/**
	 * The opcode of the test DynamicPropertyType.
	 */
	private static final int OPCODE = 1;

	/**
	 * Tests the {@link DynamicConfigPropertyType} cache.
	 */
	@Test
	public void testCaching() {
		DynamicConfigPropertyType first = DynamicConfigPropertyType.valueOf(NAME, OPCODE);
		DynamicConfigPropertyType second = DynamicConfigPropertyType.valueOf(NAME, OPCODE);
		assertSame(first, second);
	}

	/**
	 * Tests the {@link DynamicConfigPropertyType} constructor to ensure that it does not accept illegal arguments.
	 */
	@Test
	public void testConstructor() {
		final int expected = 4;
		int exceptions = 0;

		for (int iteration = 0; iteration < expected; iteration++) {
			try {
				if (iteration == 0) {
					DynamicConfigPropertyType.valueOf(null, OPCODE);
				} else if (iteration == 1) {
					DynamicConfigPropertyType.valueOf("", OPCODE);
				} else if (iteration == 2) {
					DynamicConfigPropertyType.valueOf(NAME, 0);
				} else if (iteration == 3) {
					DynamicConfigPropertyType.valueOf(NAME, -1);
				}
			} catch (IllegalArgumentException e) {
				exceptions++;
			}
		}

		assertEquals(expected, exceptions);
	}

}