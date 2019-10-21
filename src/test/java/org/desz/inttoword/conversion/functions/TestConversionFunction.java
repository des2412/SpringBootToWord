package org.desz.inttoword.conversion.functions;

import static org.junit.Assert.assertEquals;

import org.desz.inttoword.IntToWordRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestConversionFunction {

	@Autowired
	ConversionFunction conversionFunction;

	@Test
	public void test() {
		assertEquals("zero", conversionFunction.apply(new IntToWordRequest(0, "UK")));
	}

}
