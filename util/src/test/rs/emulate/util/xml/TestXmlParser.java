package rs.emulate.util.xml;

import org.apollo.util.xml.XmlNode;
import org.apollo.util.xml.XmlParser;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * A test for the {@link XmlParser} class.
 * 
 * @author Graham
 */
public final class TestXmlParser {

	/**
	 * A test for the {@link XmlParser#parse(java.io.InputStream)} method.
	 * 
	 * @throws SAXException if a SAX error occurs.
	 * @throws IOException if an I/O error occurs.
	 */
	@Test
	public void testParseInputStream() throws SAXException, IOException {
		XmlParser parser = new XmlParser();
		InputStream input = new ByteArrayInputStream("<root a='1' b='2' c='3'><z><y><x></x></y></z></root>".getBytes());
		XmlNode root = parser.parse(input);

		assertEquals(root.getName(), "root");
		assertEquals(root.getAttributeCount(), 3);
		assertEquals(root.getChildCount(), 1);
		assertFalse(root.hasValue());

		Set<String> names = root.getAttributeNames();
		assertTrue(names.contains("a"));
		assertTrue(names.contains("b"));
		assertTrue(names.contains("c"));
		assertFalse(names.contains("z"));
		assertFalse(names.contains("y"));
		assertFalse(names.contains("x"));

		assertEquals(Optional.of("1"), root.getAttribute("a"));
		assertEquals(Optional.of("2"), root.getAttribute("b"));
		assertEquals(Optional.of("3"), root.getAttribute("c"));

		XmlNode[] first = root.getChildren().toArray(new XmlNode[1]);
		assertEquals(1, first.length);
		assertEquals("z", first[0].getName());

		XmlNode[] second = first[0].getChildren().toArray(new XmlNode[1]);
		assertEquals(1, second.length);
		assertEquals("y", second[0].getName());

		XmlNode[] third = second[0].getChildren().toArray(new XmlNode[1]);
		assertEquals(1, third.length);
		assertEquals("x", third[0].getName());

		assertEquals(0, third[0].getChildCount());
	}

	/**
	 * A test for the {@link XmlParser#parse(java.io.Reader)} method.
	 * 
	 * @throws SAXException if a SAX error occurs.
	 * @throws IOException if an I/O error occurs.
	 */
	@Test
	public void testParseReader() throws SAXException, IOException {
		XmlParser parser = new XmlParser();
		Reader reader = new StringReader("<alphabet><a>1</a><b>2</b><c>3</c></alphabet>");
		XmlNode root = parser.parse(reader);

		assertEquals(root.getName(), "alphabet");
		assertEquals(root.getAttributeCount(), 0);
		assertEquals(root.getChildCount(), 3);
		assertFalse(root.hasValue());

		XmlNode[] children = root.getChildren().toArray(new XmlNode[3]);

		assertEquals(children[0].getName(), "a");
		assertEquals(children[1].getName(), "b");
		assertEquals(children[2].getName(), "c");

		assertEquals(children[0].getValue(), "1");
		assertEquals(children[1].getValue(), "2");
		assertEquals(children[2].getValue(), "3");

		for (int index = 0; index < 3; index++) {
			assertTrue(children[index].hasValue());
			assertEquals(children[index].getAttributeCount(), 0);
		}
	}

}