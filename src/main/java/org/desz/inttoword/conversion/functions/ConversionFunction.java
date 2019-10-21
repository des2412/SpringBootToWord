package org.desz.inttoword.conversion.functions;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;
import static org.desz.inttoword.factory.ProvLangFactory.getInstance;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import static java.util.Objects.requireNonNull;
import static java.util.Objects.nonNull;
import java.util.function.BiFunction;
import java.util.function.Function;
import static java.util.stream.IntStream.range;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import org.desz.inttoword.IntToWordRequest;
import org.desz.inttoword.exceptions.AppConversionException;
import org.desz.inttoword.language.IntWordMapping;
import org.desz.inttoword.language.ProvLang;
import org.desz.inttoword.output.DeDecorator;
import org.desz.inttoword.output.WordResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author des ConversionFunction: Function interface -> Integer to
 *         corresponding word specified by ProvLang
 * 
 */

public class ConversionFunction implements Function<IntToWordRequest, String> {
	private static final Logger logger = LoggerFactory.getLogger(ConversionFunction.class);

	private HundredthConverter hundredthConverter;

	/**
	 * 
	 * @param hundConverter the parameterised instance.
	 */
	@Autowired
	public ConversionFunction(HundredthConverter hundredthConverter) {
		this.hundredthConverter = hundredthConverter;
	}

	/**
	 * BiFunction funcHunConv.
	 */
	private BiFunction<String, IntWordMapping, String> funcHunConv = (x, y) -> {
		return hundredthConverter.hundredthToWord(x, y).orElse(EMPTY);
	};

	/**
	 * 
	 * @param n the integer.
	 * @return the word for n including units.
	 * @throws AppConversionException
	 */

	public String convertIntToWord(Integer n, ProvLang pl) throws AppConversionException {

		n = requireNonNull(n, "Integer parameter required to be non-null");
		final ProvLang provLang = requireNonNull(pl);
		if (provLang.equals(ProvLang.EMPTY))
			throw new AppConversionException();
		final List<String> numUnits = asList(NumberFormat.getIntegerInstance(Locale.UK).format(n).split(","));
		DeDecorator deDecorator = null;
		final int sz = numUnits.size();
		// save last element of numUnits..
		final int prmLastHun = Integer.parseInt(numUnits.get(numUnits.size() - 1));
		// singleton IntWordMapping per ProvLang.
		final IntWordMapping langMap = getInstance().getMapForProvLang(provLang);
		// convert each hundredth to word.
		final Map<Integer, String> wordMap = range(0, sz).boxed()
				.collect(toMap(Function.identity(), i -> funcHunConv.apply(numUnits.get(i), langMap)));

		final WordResult.Builder wordBuilder = new WordResult.Builder();

		// build with UNIT added to each part if applicable.
		switch (sz) {
		case 1:
			// result returned.
			if (provLang.equals(ProvLang.DE)) {
				WordResult deRes = wordBuilder.withHund(wordMap.get(0)).build();
				deRes = new DeDecorator(deRes).restructureHundrethRule();
				return new DeDecorator(deRes).pluraliseOneRule(prmLastHun).toString();

			}
			return wordMap.get(0);
		case 4:

			wordBuilder.withBill(wordMap.get(0) + langMap.getBilln()).withMill(wordMap.get(1) + langMap.getMilln())
					.withThou(wordMap.get(2) + langMap.getThoud());
			break;

		case 3:
			wordBuilder.withMill(wordMap.get(0) + langMap.getMilln()).withThou(wordMap.get(1) + langMap.getThoud());

			break;

		case 2:
			wordBuilder.withThou(wordMap.get(0) + langMap.getThoud());
			break;
		default:
			break;
		}
		if (IHundConverter.inRange(prmLastHun))
			// 1 to 99 -> prepend with AND.
			wordBuilder.withHund(langMap.getAnd() + wordMap.get(sz - 1));

		else
			wordBuilder.withHund(wordMap.get(sz - 1));

		// wordResult output for non DE case.
		final WordResult wordResult = wordBuilder.build();
		// decorate DE word.
		if (provLang.equals(ProvLang.DE)) {
			WordResult.Builder deBuilder = new WordResult.Builder();
			if (nonNull(wordResult.getBill()))
				deBuilder.withBill(wordResult.getBill().trim());
			if (nonNull(wordResult.getMill()))
				deBuilder.withMill(wordResult.getMill().trim());
			if (nonNull(wordResult.getThou()))
				deBuilder.withThou(wordResult.getThou());
			if (nonNull(wordResult.getHund()))
				deBuilder.withHund(wordMap.get(sz - 1));
			deDecorator = new DeDecorator(deBuilder.build());
			WordResult deRes = deDecorator.pluraliseUnitRule();
			deDecorator = new DeDecorator(deRes);
			deRes = deDecorator.pluraliseOneRule(prmLastHun);
			deDecorator = new DeDecorator(deRes);
			deRes = deDecorator.restructureHundrethRule();
			deDecorator = new DeDecorator(deRes);
			deRes = deDecorator.combineThouHundRule();
			// trim, multi to single whitespace.
			return normalizeSpace(deRes.toString());

		}

		return wordResult.toString();

	}

	@Override
	public String apply(IntToWordRequest req) {
		final int num = req.getNumber();
		final String ln = req.getLang();
		logger.info("number {}, lang {}", num, ln);
		final ProvLang pl = ProvLang.valueOf(ln);
		String result = null;
		try {
			result = this.convertIntToWord(num, pl);
		} catch (NumberFormatException | AppConversionException e) {
			logger.error(e.getMessage());
			result = e.getMessage();
		}
		logger.info("function calc. {}", result);
		return result;
	}

}
