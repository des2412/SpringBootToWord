package org.desz.numbertoword;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.desz.numbertoword.enums.EnumHolder.FR_ERRORS;
import org.desz.numbertoword.enums.EnumHolder.PROVISIONED_LANGUAGE;
import org.desz.numbertoword.factory.NumberToWordFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FrNumberToWordMapperTest {
	final private static String[] decimals = { "One", "Two", "Three", "Four",
			"Five", "Six", "Seven", "Eight", "Nine" };
	static List<String> l = Arrays.asList(decimals);

	NumberToWordMapper numberToWordMapper = null;

	@Before
	public void init() throws Exception {
		numberToWordMapper = (NumberToWordMapper) NumberToWordFactory.FR_MAPPER
				.getNumberToWordMapper();

		NumberToWordMapperParent.setLoggingLevel(Level.ALL);

	}
	
	@After
	public void clean() throws Exception{
		NumberToWordFactory.removeNumberToWordMapper(PROVISIONED_LANGUAGE.FR);
	}

	@Test
	public void testNotNull() {
		assertNotNull(numberToWordMapper);
	}

	@Test
	public void testIsSingleton() throws Exception {
		NumberToWordMapper.setLoggingLevel(Level.INFO);
		assertNotSame(numberToWordMapper,
				NumberToWordFactory.UK_MAPPER.getNumberToWordMapper());

		assertSame(numberToWordMapper,
				NumberToWordFactory.FR_MAPPER.getNumberToWordMapper());
	}

	@Test(expected = Exception.class)
	public void testNegativeInputMessage() throws Exception {
		((NumberToWordMapperParent) numberToWordMapper).validateAndFormat(-100);
		assertEquals(FR_ERRORS.NEGATIVE_INPUT,
				((NumberToWordMapperParent) numberToWordMapper).getMessage());
	}

	@Test(expected = Exception.class)
	public void testNumberFormatMessage() throws Exception {
		((NumberToWordMapperParent) numberToWordMapper)
				.validateAndFormat(1.234);
		assertEquals(FR_ERRORS.NUMBERFORMAT,
				((NumberToWordMapperParent) numberToWordMapper).getMessage());
	}

	@Test(expected = Exception.class)
	public void testNullInputMessage() throws Exception {
		numberToWordMapper.validateAndFormat(null);
		assertEquals(FR_ERRORS.NULL_INPUT,
				numberToWordMapper.getMessage());
	}

	@Test
	public void testZero() throws Exception {
		assertEquals("Zéro", numberToWordMapper.getWord(0));
	}

	@Test
	public void testDecimals() throws Exception {

		assertEquals("Un", numberToWordMapper.getWord(1));
		assertEquals("Dix", numberToWordMapper.getWord(10));

	}

	@Test
	public void testTeens() throws Exception {
		assertEquals("Onze", numberToWordMapper.getWord(11));
		assertEquals("Quatorze", numberToWordMapper.getWord(14));
	}

	@Test
	public void testGetWords() throws Exception {

		assertEquals("Onze million un cent et onze mille un cent et onze",
				numberToWordMapper.getWord(11111111));

		/*
		 * assertEquals("One thousand and nine",
		 * numberToWordMapper.getWord(1009));
		 * assertEquals("One thousand and ninety nine",
		 * numberToWordMapper.getWord(1099));
		 * 
		 * assertEquals("One thousand and one",
		 * numberToWordMapper.getWord(1001));
		 * assertEquals("One thousand and eleven",
		 * numberToWordMapper.getWord(1011)); assertEquals("Ten thousand",
		 * numberToWordMapper.getWord(10000));
		 * assertEquals("Ten thousand and one",
		 * numberToWordMapper.getWord(10001));
		 * assertEquals("Ten thousand one hundred",
		 * numberToWordMapper.getWord(10100));
		 * 
		 * assertEquals("One hundred thousand",
		 * numberToWordMapper.getWord(100000));
		 * 
		 * assertEquals("One million", numberToWordMapper.getWord(1000000));
		 * 
		 * assertEquals("One million one hundred thousand",
		 * numberToWordMapper.getWord(1100000));
		 */
	}

}
