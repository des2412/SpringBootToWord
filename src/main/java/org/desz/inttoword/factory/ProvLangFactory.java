package org.desz.inttoword.factory;

import java.util.HashMap;
import java.util.Map;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

import java.util.Collections;
import java.util.stream.Stream;
import org.desz.inttoword.language.ILangProvider;
import org.desz.inttoword.language.IntWordMapping;
import org.desz.inttoword.language.ProvLang;
import org.desz.inttoword.language.ProvLangValues.DePair;
import org.desz.inttoword.language.ProvLangValues.DeUnit;
import org.desz.inttoword.language.ProvLangValues.FrPair;
import org.desz.inttoword.language.ProvLangValues.FrUnit;
import org.desz.inttoword.language.ProvLangValues.NlPair;
import org.desz.inttoword.language.ProvLangValues.NlUnit;
import org.desz.inttoword.language.ProvLangValues.UkPair;
import org.desz.inttoword.language.ProvLangValues.UkUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for numeric and error values associated to supported ProvLang.
 * 
 * @author des
 * 
 */
public final class ProvLangFactory implements ILangProvider {

	private final Map<ProvLang, IntWordMapping> provLangMappingCache = Collections
			.synchronizedMap(new HashMap<ProvLang, IntWordMapping>());

	private static Logger log = LoggerFactory.getLogger(ProvLangFactory.class);

	/**
	 * enforce singleton contract.
	 */
	private ProvLangFactory() {
	}

	/**
	 * static class not loaded until referenced.
	 * 
	 * @author des
	 *
	 */
	private static class ProvLangHolder {
		private static final ILangProvider provLangStore = new ProvLangFactory();
	}

	/**
	 * 
	 * @return singleton instance.
	 */
	public static ILangProvider getInstance() {
		return ProvLangHolder.provLangStore;
	}

	@Override
	public IntWordMapping getMapForProvLang(final ProvLang pl) {

		final ProvLang provLang = requireNonNull(pl);
		if (provLangMappingCache.containsKey(provLang))
			return provLangMappingCache.get(provLang);
		synchronized (provLangMappingCache) {
			IntWordMapping.Builder builder = new IntWordMapping.Builder();
			switch (provLang) {
			case UK:

				builder.withBilln(UkUnit.BILLS.val());
				builder.withMilln(UkUnit.MILLS.val());
				builder.withThoud(UkUnit.THOUS.val());
				builder.withHund(UkUnit.HUNS.val());
				builder.withAnd(UkUnit.AND.val());
				builder.withIntToWordMap(Stream.of(UkPair.values()).collect(toMap(UkPair::getNum, UkPair::getWord)));

				provLangMappingCache.put(ProvLang.UK, builder.build());

				break;
			case FR:

				builder.withBilln(FrUnit.BILLS.val());
				builder.withMilln(FrUnit.MILLS.val());
				builder.withThoud(FrUnit.THOUS.val());
				builder.withHund(FrUnit.HUNS.val());
				builder.withAnd(FrUnit.AND.val());
				builder.withIntToWordMap(Stream.of(FrPair.values()).collect(toMap(FrPair::getNum, FrPair::getWord)));
				provLangMappingCache.put(ProvLang.FR, builder.build());

				break;

			case DE:

				builder.withBilln(DeUnit.BILLS.val());
				builder.withMilln(DeUnit.MILLS.val());
				builder.withThoud(DeUnit.THOUS.val());
				builder.withHund(DeUnit.HUNS.val());
				builder.withAnd(DeUnit.AND.val());
				builder.withIntToWordMap(Stream.of(DePair.values()).collect(toMap(DePair::getNum, DePair::getWord)));
				provLangMappingCache.put(ProvLang.DE, builder.build());
				break;

			case NL:

				builder.withBilln(NlUnit.BILLS.val());
				builder.withMilln(NlUnit.MILLS.val());
				builder.withThoud(NlUnit.THOUS.val());
				builder.withHund(NlUnit.HUNS.val());
				builder.withAnd(NlUnit.AND.val());
				builder.withIntToWordMap(Stream.of(NlPair.values()).collect(toMap(NlPair::getNum, NlPair::getWord)));
				provLangMappingCache.put(ProvLang.NL, builder.build());

				break;

			default:
				log.info("Unsupported language");
				break;

			}
		}
		return provLangMappingCache.get(provLang);
	}

}
