/**
 * 
 */
package org.desz.numbertoword.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.desz.numbertoword.IFNumberToWordMapper;
import org.desz.numbertoword.IntegerToWordMapper;
import org.desz.numbertoword.LanguageSupport;
import org.desz.numbertoword.enums.EnumHolder.FR_WORDS;
import org.desz.numbertoword.enums.EnumHolder.PROVISIONED_LANGUAGE;
import org.desz.numbertoword.enums.EnumHolder.UK_WORDS;
import org.desz.numbertoword.exceptions.FactoryMapperRemovalException;
import org.desz.numbertoword.exceptions.NumberToWordFactoryException;

/**
 * @author des
 * 
 *         Singleton enum NumberToWordMapper Factory for target languages
 *         Flyweight as initialised mappers are stored in a static List
 * 
 */
public enum IntegerToWordEnumFactory implements INumberToWordFactory {

	UK_MAPPER(), FR_MAPPER();

	private static Map<PROVISIONED_LANGUAGE, IntegerToWordEnumFactory> ENUMFACS = Collections
			.synchronizedMap(new HashMap<PROVISIONED_LANGUAGE, IntegerToWordEnumFactory>());

	private final static Logger LOGGER = Logger
			.getLogger(IntegerToWordEnumFactory.class.getName());

	private IFNumberToWordMapper integerToWordMapper;

	

	/**
	 * Invokes the private NumberToWordMapper constructor
	 * @param args
	 * @return
	 * @throws NumberToWordFactoryException
	 */
	private IFNumberToWordMapper instantiateInstance(final Object[] args)
			throws NumberToWordFactoryException {
		// access private Constructor of IntegerToWordMapper
		final Constructor<?>[] constructorRef = IntegerToWordMapper.class
				.getDeclaredConstructors();
		constructorRef[0].setAccessible(true);

		IFNumberToWordMapper integerToWordMapper = null;

		try {
			integerToWordMapper = (IntegerToWordMapper) constructorRef[0]
					.newInstance(args);
		} catch (InstantiationException e1) {
			LOGGER.severe(e1.getMessage());
		} catch (IllegalAccessException e1) {
			LOGGER.severe(e1.getMessage());
		} catch (IllegalArgumentException e1) {
			LOGGER.severe(e1.getMessage());
		} catch (InvocationTargetException e1) {
			LOGGER.severe(e1.getMessage());
		}
		if (integerToWordMapper == null) {
			throw new NumberToWordFactoryException(
					"Did not initialise NumberToWordMapper");
		}
		return integerToWordMapper;
	}
	
	/**
	 * 
	 * @param pl
	 * @return
	 */
	private boolean isCached(PROVISIONED_LANGUAGE pl){
			if(ENUMFACS.containsKey(pl)){
				LOGGER.info("Initialised instance of IntegerToWordMapper for language "
						+ pl.name() + " available");
				return true;
			}
		return false;
	}

	/**
	 * @param PROVISIONED_LANGUAGE
	 */
	@Override
	public IFNumberToWordMapper getIntegerToWordMapper() throws NumberToWordFactoryException {
			
		
		final Object[] args = new Object[1];

		LanguageSupport languageSupport = null;
		
		switch (this) {
		case UK_MAPPER:
			// check cache
			if(isCached(PROVISIONED_LANGUAGE.UK)){
				return ENUMFACS.get(PROVISIONED_LANGUAGE.UK).integerToWordMapper;
			}
			
			languageSupport = new LanguageSupport(PROVISIONED_LANGUAGE.UK);
			args[0] = languageSupport;
			// invoke private constructor
			this.integerToWordMapper = instantiateInstance(args);
			//cache 'this' in Map
			ENUMFACS.put(PROVISIONED_LANGUAGE.UK, this);
			break;

		case FR_MAPPER:
			if(isCached(PROVISIONED_LANGUAGE.FR)){
				return ENUMFACS.get(PROVISIONED_LANGUAGE.FR).integerToWordMapper;
			}
			
			languageSupport = new LanguageSupport(PROVISIONED_LANGUAGE.FR);
			args[0] = languageSupport;
			this.integerToWordMapper = instantiateInstance(args);
			ENUMFACS.put(PROVISIONED_LANGUAGE.FR, this);
			break;

		default:
			throw new NumberToWordFactoryException(
					languageSupport.getUnkownErr());

		}
		((IntegerToWordMapper) this.integerToWordMapper)
				.setMapping(initialiseMapping());
		
		LOGGER.info("Added "
				+ this.name()
				+ " to CONFIGURED_FACTORIES Map. Number of configured NumberToWordEnumFactories :"
				+ ENUMFACS.size());
		return this.integerToWordMapper;
	}

	/**
	 * Constructor
	 */
	private IntegerToWordEnumFactory() {
	}

	/**
	 * Provisions Map of Integer to language specific word
	 * 
	 * @return int to word Map
	 */
	private Map<String, String> initialiseMapping() {
		Map<String, String> intToWordMap = new HashMap<String, String>();
		switch (this) {
		case UK_MAPPER:
			for (UK_WORDS intToWord : UK_WORDS.values()) {
				intToWordMap.put(intToWord.getNum(), intToWord.getWord());
			}

			break;
		case FR_MAPPER:
			for (FR_WORDS intToWord : FR_WORDS.values()) {
				intToWordMap.put(intToWord.getNum(), intToWord.getWord());
			}
			break;
		default:
			break;

		}
		return intToWordMap;
	}

	/**
	 * 
	 * @param provLang
	 * @return
	 * @throws FactoryMapperRemovalException
	 */
	public static boolean removeNumberToWordEnumFactory(
			final PROVISIONED_LANGUAGE provLang)
			throws FactoryMapperRemovalException {

		if (ENUMFACS.containsKey(provLang)) {

			try {
				ENUMFACS.remove(provLang);
				LOGGER.info("Removal of " + provLang.name()
						+ " NumberToWordEnumFactory result:"
						+ !ENUMFACS.containsKey(provLang));

			} catch (Exception e) {
				throw new FactoryMapperRemovalException(e);
			}

		} else {
			LOGGER.info("References absent for " + provLang.name());
		}

		return !ENUMFACS.containsKey(provLang);
	}

}
