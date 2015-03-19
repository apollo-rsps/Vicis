package rs.emulate.editor;

import static org.junit.Assert.assertSame;

import org.junit.Test;

import rs.emulate.shared.prop.DynamicPropertyType;

/**
 * Tests the caching used in {@link DynamicPropertyType}s to ensure that properties with the same name and opcode will
 * be the same object.
 * 
 * @author Major
 */
public final class DynamicPropertyNameTest {

	/**
	 * The name of the property name.
	 */
	private static final String NAME = "test";

	/**
	 * The opcode of the test property name.
	 */
	private static final int OPCODE = 1;

	/**
	 * Tests the {@link DynamicPropertyType} cache.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void test() {
		DynamicPropertyType first = DynamicPropertyType.valueOf(NAME, OPCODE);
		DynamicPropertyType second = DynamicPropertyType.valueOf(NAME, OPCODE);
		assertSame(first, second);
	}

}