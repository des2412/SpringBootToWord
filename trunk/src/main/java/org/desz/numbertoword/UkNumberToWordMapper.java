package org.desz.numbertoword;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.desz.numbertoword.enums.EnumHolder.ERRORS;
import org.desz.numbertoword.enums.EnumHolder.FORMAT;
import org.desz.numbertoword.enums.EnumHolder.UKINTEGERTOWORD;
import org.desz.numbertoword.enums.EnumHolder.UNITS;
import org.desz.numbertoword.factory.NumberToWordFactory;

/**
 * 
 * @author des: des_williams_2000@yahoo.com
 * 
 */
public class UkNumberToWordMapper implements INumberToWordMapper {

	private static Map<String, String> numToWordMap = new HashMap<String, String>();
	private INumberToWordMapper numberToWordMapper = null;

	private static UkNumberFormatHelper ukEngNumberFormatter = null;

	private final static Logger LOGGER = Logger
			.getLogger(UkNumberToWordMapper.class.getName());

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
	 * Constructor
	 * @throws Exception 
	 */
	public UkNumberToWordMapper() throws Exception {
		/*this.numberToWordMapper = NumberToWordFactory.UK_SINGLETON
				.getNumberToWordMapper();*/
		initialiseMapping();
	}

	/**
	 * 
	 * @return numberToWordMapper
	 * @throws Exception
	 *
	public static INumberToWordMapper getInstance() throws Exception {
		return numberToWordMapper = NumberToWordFactory.UK_SINGLETON
				.getNumberToWordMapper();
	}*/

	public static void setLoggingLevel(Level newLevel) {
		LOGGER.setLevel(newLevel);
	}

	/**
	 * initialise map of number to corresponding word
	 */
	public void initialiseMapping() {
		LOGGER.setLevel(Level.INFO);
		ukEngNumberFormatter = UkNumberFormatHelper.getInstance();

		for (UKINTEGERTOWORD intToWord : UKINTEGERTOWORD.values()) {
			numToWordMap.put(intToWord.getNum(), intToWord.getWord());
		}
	}

	/**
	 * validate and formats num
	 * 
	 * @param num
	 * @return
	 * @throws Exception
	 */
	public String validateAndFormat(Number num) throws Exception {

		if (num == null) {
			LOGGER.info(ERRORS.NULL_INPUT.val());
			setMessage(ERRORS.NULL_INPUT.val());
			throw new Exception(ERRORS.NULL_INPUT.val());
		}

		if ((Integer) num < 0) {
			LOGGER.info(ERRORS.NEGATIVE_INPUT.val());
			setMessage(ERRORS.NEGATIVE_INPUT.val());
			throw new Exception(ERRORS.NEGATIVE_INPUT.val());
		}

		String formattedNumber = null;

		try {
			formattedNumber = ukEngNumberFormatter.convertToNumberFormat(Long
					.valueOf(String.valueOf(num)));
		} catch (NumberFormatException nfe) {
			setMessage(ERRORS.NUMBERFORMAT.val());
			throw new Exception(ERRORS.NUMBERFORMAT.val());
		}
		return formattedNumber;
	}

	/**
	 * 
	 * @param num
	 * @return
	 * @throws Exception
	 *             if validate throws Exception type
	 */

	public String getWord(Integer num) throws Exception {

		String formattedNumber = null;

		try {
			formattedNumber = validateAndFormat(num);
			if (LOGGER.isLoggable(Level.INFO))
				LOGGER.info("Formatted Number:" + formattedNumber);

		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			throw (e);
		}

		String[] components = formattedNumber.split(",");
		final int nComps = components.length;
		if (nComps > 3) {
			LOGGER.info(ERRORS.INVALID_INPUT_NUMBER.val() + num);
			setMessage(ERRORS.INVALID_INPUT_NUMBER.val() + num);
			throw new Exception(ERRORS.INVALID_INPUT_NUMBER.val() + num);
		}

		if (formattedNumber.equals("0")) {
			return numToWordMap.get("0");
		}

		String mills = FORMAT.EMPTY.val();
		String thous = FORMAT.EMPTY.val();
		String huns = FORMAT.EMPTY.val();

		Map<UNITS, Integer> numAtIndex = new HashMap<UNITS, Integer>();

		StringBuffer result = new StringBuffer();

		Integer val = null;
		if (nComps == 3) {

			val = Integer.valueOf(components[0]);
			mills = getWordForNum(val);
			numAtIndex.put(UNITS.MILLS, val);

			val = Integer.valueOf(components[1]);
			thous = getWordForNum(val);
			numAtIndex.put(UNITS.THOUS, val);

			val = Integer.valueOf(components[2]);
			huns = getWordForNum(val);
			numAtIndex.put(UNITS.HUNS, val);

		} else if (nComps == 2) {
			val = Integer.valueOf(components[0]);
			thous = getWordForNum(val);
			numAtIndex.put(UNITS.MILLS, -1);
			numAtIndex.put(UNITS.THOUS, val);

			val = Integer.valueOf(components[1]);
			huns = getWordForNum(val);
			numAtIndex.put(UNITS.HUNS, val);
		} else {
			val = Integer.valueOf(components[0]);
			huns = getWordForNum(val);
			numAtIndex.put(UNITS.MILLS, -1);
			numAtIndex.put(UNITS.THOUS, -1);
			numAtIndex.put(UNITS.HUNS, val);
		}

		// Concatenate the units of the number

		if (numAtIndex.get(UNITS.MILLS) > 0) {
			String mn = mills + FORMAT.SPACE.val() + UNITS.MILLS.val();
			result.append(mn);
		}
		if (numAtIndex.get(UNITS.THOUS) > 0) {
			if (mills == FORMAT.EMPTY.val()) {
				result.append(thous + FORMAT.SPACE.val() + UNITS.THOUS.val());
			} else if (numAtIndex.get(UNITS.THOUS) < 100) {
				result.append(FORMAT.AND.val() + thous.toLowerCase()
						+ FORMAT.SPACE.val() + UNITS.THOUS.val());

			} else {
				result.append(FORMAT.SPACE.val() + thous.toLowerCase()
						+ FORMAT.SPACE.val() + UNITS.THOUS.val());
			}
		}
		if (numAtIndex.get(UNITS.HUNS) > 0) {
			if (numAtIndex.get(UNITS.THOUS) < 0
					& numAtIndex.get(UNITS.MILLS) < 0) {
				result.append(huns);
			} else {
				if (numAtIndex.get(UNITS.HUNS) < 100) {
					result.append(FORMAT.AND.val() + huns.toLowerCase());
				} else {
					result.append(FORMAT.SPACE.val() + huns.toLowerCase());
				}
			}
		}

		return result.toString();
	}

	/**
	 * 
	 * @param num
	 * @return
	 * @throws Exception
	 */
	private String getWordForNum(Integer num) throws Exception {
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
			result = numToWordMap.get(indZero) + " " + UNITS.HUNS.val();
			if (rem > 0) { // not whole hundredth
				String decs = getDecimalPart(rem, indOne, indTwo);
				result += FORMAT.AND.val() + decs.toLowerCase();
			}
			break;

		default:
			setMessage(ERRORS.NUMBERFORMAT.val());
			throw new Exception(ERRORS.UNKNOWN.val());

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
			if (LOGGER.isLoggable(Level.INFO))
				LOGGER.info("indZero:" + indZero + "indOne:" + indOne);
			if (!indOne.equals("0")) {
				result = numToWordMap.get(indZero) + FORMAT.SPACE.val()
						+ numToWordMap.get(indOne);
			} else {
				result = numToWordMap.get(indZero);
			}
		}
		return result;
	}
}
