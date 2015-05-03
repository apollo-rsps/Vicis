package rs.emulate.util.shared.prop;

import static org.junit.Assert.*;

import org.junit.Test;

import rs.emulate.shared.property.DynamicPropertyType;

/**
 * Tests the caching used in {@link DynamicPropertyType}s to ensure that properties with the same name and opcode will
 * be the same object.
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
	 * Tests the {@link DynamicPropertyType} cache.
	 */
	@Test
	public void testCaching() {
		DynamicPropertyType first = DynamicPropertyType.valueOf(NAME, OPCODE);
		DynamicPropertyType second = DynamicPropertyType.valueOf(NAME, OPCODE);
		assertSame(first, second);
	}

	/**
	 * Tests the {@link DynamicPropertyType} constructor to ensure that it does not accept illegal arguments.
	 */
	@Test
	public void testConstructor() {
		int expected = 4;
		int exceptions = 0;

		for (int iteration = 0; iteration < expected; iteration++) {
			try {
				if (iteration == 0) {
					DynamicPropertyType.valueOf(null, OPCODE);
				} else if (iteration == 1) {
					DynamicPropertyType.valueOf("", OPCODE);
				} else if (iteration == 2) {
					DynamicPropertyType.valueOf(NAME, 0);
				} else if (iteration == 3) {
					DynamicPropertyType.valueOf(NAME, -1);
				}
			} catch (IllegalArgumentException e) {
				exceptions++;
				continue;
			}
		}

		assertEquals(expected, exceptions);
	}

}