package org.desz.numbertoword;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.desz.numbertoword.enums.EnumHolder.FR_ERRORS;
import org.desz.numbertoword.enums.EnumHolder.UK_FORMAT;

public abstract class NumberToWordMapperParent implements INumberToWordMapper {

	private static NumberFormat nf = NumberFormat.getInstance(Locale.UK);

	public volatile Map<String, String> numToWordMap = null;

	protected final static Logger LOGGER = Logger
			.getLogger(NumberToWordMapperParent.class.getName());

	private static final String FORMATTED_NUMBER_SEPARATOR = UK_FORMAT.UKSEP
			.val();

	private LanguageSupport languageSupport;

	/**
	 * 
	 * @return languageSupport
	 */
	public LanguageSupport getLanguageSupport() {
		return languageSupport;
	}

	public void setLanguageSupport(LanguageSupport languageSupport) {
		this.languageSupport = languageSupport;
	}

	public String getFormattedNumberSeparator() {
		return FORMATTED_NUMBER_SEPARATOR;
	}

	/**
	 * message: Typically reports an error condition. Used by Unit tests for
	 * assertions only.
	 */
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * validate and formats num
	 * 
	 * @param num
	 * @return
	 * @throws Exception
	 */
	protected String validateAndFormat(Number num) throws Exception {

		if (num == null) {
			LOGGER.info(FR_ERRORS.NULL_INPUT.val());
			setMessage(FR_ERRORS.NULL_INPUT.val());
			throw new Exception(FR_ERRORS.NULL_INPUT.val());
		}

		if ((Integer) num < 0) {
			LOGGER.info(FR_ERRORS.NEGATIVE_INPUT.val());
			setMessage(FR_ERRORS.NEGATIVE_INPUT.val());
			throw new Exception(FR_ERRORS.NEGATIVE_INPUT.val());
		}

		String formattedNumber = null;

		try {
			formattedNumber = convertToNumberFormat(Long.valueOf(String
					.valueOf(num)));
		} catch (NumberFormatException nfe) {
			setMessage(FR_ERRORS.NUMBERFORMAT.val());
			throw new Exception(FR_ERRORS.NUMBERFORMAT.val());
		}
		return formattedNumber;
	}


	/**
	 * set Level of Logging
	 * 
	 * @param newLevel
	 */
	protected static void setLoggingLevel(Level newLevel) {
		LOGGER.setLevel(newLevel);
	}

	/**
	 * 
	 * @param num
	 * @return
	 * @throws Exception
	 */
	protected String getWordForNum(Integer num) throws Exception {
		String numStr = String.valueOf(num);
		String indZero;
		String indOne;
		String indTwo;
		String result = null;
		Integer rem = null;
		switch (numStr.length()) {
		// 1,2 or 3 digits
		case 1:
			result = numToWordMap.get(numStr);
			break;
		case 2:
			indZero = String.valueOf(numStr.charAt(0));
			indOne = String.valueOf(numStr.charAt(1));
			rem = num % 10;

			if (num > 9 & num < 20) {// teenager
				result = numToWordMap.get(indZero + indOne);
			} else { // >= 20 & <= 99
				result = getDecimalPart(rem, indZero, indOne);
			}
			break;
		case 3:
			indZero = String.valueOf(numStr.charAt(0));
			indOne = String.valueOf(numStr.charAt(1));
			indTwo = String.valueOf(numStr.charAt(2));

			rem = num % 100;
			result = numToWordMap.get(indZero) + " "
					+ getLanguageSupport().getHunUnit();
			if (rem > 0) { // not whole hundredth
				String decs = getDecimalPart(rem, indOne, indTwo);
				result += getLanguageSupport().getAnd() + decs.toLowerCase();
			}
			break;

		default:
			setMessage(FR_ERRORS.NUMBERFORMAT.val());
			throw new Exception(FR_ERRORS.UNKNOWN.val());

		}

		return result;
	}

	/**
	 * 
	 * @param rem
	 * @param indZero
	 * @param indOne
	 * @return
	 */
	private String getDecimalPart(Integer rem, String indZero, String indOne) {
		String result;
		int decs = Integer.valueOf(indZero + indOne);

		if (rem == 0 | decs > 10 & decs < 20) {// teen
			result = numToWordMap.get(indZero + indOne);

		} else if (indZero.equals("0")) {
			result = numToWordMap.get(indOne);

		} else { // 20-99
			indZero += "0"; // add "0" to indZero so as to match key of whole
							// ten
			LOGGER.info("indZero:" + indZero + "indOne:" + indOne);
			if (!indOne.equals("0")) {
				result = numToWordMap.get(indZero) + UK_FORMAT.SPACE.val()
						+ numToWordMap.get(indOne);
			} else {
				result = numToWordMap.get(indZero);
			}
		}
		return result;
	}

	/**
	 * The formatted String that results has 1 to 3 parts.
	 * 
	 * @param num
	 * @return formatted String representation of num
	 */
	private String convertToNumberFormat(Long num)
			throws IllegalArgumentException {
		String s = null;
		try {
			s = nf.format(num);
		} catch (IllegalArgumentException e) {
			throw (e);
		}
		LOGGER.info("Formatted Number:" + s);
		return s;
	}

}