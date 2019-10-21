package org.desz.inttoword.conversion.functions;

import static org.desz.inttoword.factory.ProvLangFactory.getInstance;
import static org.junit.Assert.assertNotNull;

import org.desz.inttoword.conversion.functions.HundredthConverter;
import org.desz.inttoword.conversion.functions.IHundConverter;
import org.desz.inttoword.language.IntWordMapping;
import org.desz.inttoword.language.ProvLang;
import org.junit.Test;

public class TestHundConverter {
	protected IHundConverter hundConverter = new HundredthConverter();

	IntWordMapping mapping = getInstance().getMapForProvLang(ProvLang.UK);

	@Test
	public void testMapToWord() throws Exception {

		assertNotNull(hundConverter.hundredthToWord("123", mapping));

	}

	@Test
	public void testMapToWordFail() throws Exception {
		hundConverter.hundredthToWord("1231", getInstance().getMapForProvLang(ProvLang.UK));

	}

}
