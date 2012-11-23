package org.desz.language;

import java.util.HashMap;
import java.util.Map;

import org.desz.numbertoword.enums.EnumHolder.DE_ERRORS;
import org.desz.numbertoword.enums.EnumHolder.DE_FORMAT;
import org.desz.numbertoword.enums.EnumHolder.DE_UNITS;
import org.desz.numbertoword.enums.EnumHolder.DE_WORDS;
import org.desz.numbertoword.enums.EnumHolder.FR_ERRORS;
import org.desz.numbertoword.enums.EnumHolder.FR_FORMAT;
import org.desz.numbertoword.enums.EnumHolder.FR_UNITS;
import org.desz.numbertoword.enums.EnumHolder.FR_WORDS;
import org.desz.numbertoword.enums.EnumHolder.NL_ERRORS;
import org.desz.numbertoword.enums.EnumHolder.NL_FORMAT;
import org.desz.numbertoword.enums.EnumHolder.NL_UNITS;
import org.desz.numbertoword.enums.EnumHolder.NL_WORDS;
import org.desz.numbertoword.enums.EnumHolder.PROVISIONED_LN;
import org.desz.numbertoword.enums.EnumHolder.UK_ERRORS;
import org.desz.numbertoword.enums.EnumHolder.DEFAULT_FORMAT;
import org.desz.numbertoword.enums.EnumHolder.UK_UNITS;
import org.desz.numbertoword.enums.EnumHolder.UK_WORDS;

/**
 * Class that holds the enum value constants
 * 
 * Immutable
 * 
 * switches on PROVISIONED_LN
 * 
 * Used by IntegerToWordEnumFactory.
 * 
 * One-to-one relationship with NumberToWordMapper
 * 
 * @author des
 * 
 */
public final class EnumLanguageSupport implements ILanguageSupport {

	private String millUnit;
	private String thouUnit;
	private String hunUnit;
	private String and;

	private String invalidInput;

	private String nullInput;
	private String negativeInput;
	private String numberFormatErr;
	private String unknownErr;

	private Map<String, String> intToWordMap = new HashMap<String, String>();

	/**
	 * Construct state according to pl
	 * 
	 * This enables Factory instance configuration for specific languages.
	 * 
	 * 
	 * @param pl
	 */
	public EnumLanguageSupport(PROVISIONED_LN pl) {

		switch (pl) {
		case UK:
			this.millUnit = UK_UNITS.MILLS.val();
			this.thouUnit = UK_UNITS.THOUS.val();
			this.hunUnit = UK_UNITS.HUNS.val();
			this.and = DEFAULT_FORMAT.AND.val();
			this.invalidInput = UK_ERRORS.INVALID_INPUT.getError();
			this.nullInput = UK_ERRORS.NULL_INPUT.getError();
			this.negativeInput = UK_ERRORS.NEGATIVE_INPUT.getError();
			this.negativeInput = UK_ERRORS.NUMBERFORMAT.getError();
			this.unknownErr = UK_ERRORS.UNKNOWN.getError();

			for (UK_WORDS intToWord : UK_WORDS.values()) {
				intToWordMap.put(intToWord.getNum(), intToWord.getWord());
			}

			break;
		case FR:
			this.millUnit = FR_UNITS.MILLS.val();
			this.thouUnit = FR_UNITS.THOUS.val();
			this.hunUnit = FR_UNITS.HUNS.val();
			this.and = FR_FORMAT.AND.val();
			this.invalidInput = FR_ERRORS.INVALID_INPUT.getError();
			this.nullInput = FR_ERRORS.NULL_INPUT.getError();
			this.negativeInput = FR_ERRORS.NEGATIVE_INPUT.getError();
			this.negativeInput = FR_ERRORS.NUMBERFORMAT.getError();
			this.unknownErr = FR_ERRORS.UNKNOWN.getError();
			for (FR_WORDS intToWord : FR_WORDS.values()) {
				intToWordMap.put(intToWord.getNum(), intToWord.getWord());
			}

			break;

		case DE:
			this.millUnit = DE_UNITS.MILLS.val();
			this.thouUnit = DE_UNITS.THOUS.val();
			this.hunUnit = DE_UNITS.HUNS.val();
			this.and = DE_FORMAT.AND.val();
			this.invalidInput = DE_ERRORS.INVALID_INPUT.getError();
			this.nullInput = DE_ERRORS.NULL_INPUT.getError();
			this.negativeInput = DE_ERRORS.NEGATIVE_INPUT.getError();
			this.negativeInput = DE_ERRORS.NUMBERFORMAT.getError();
			this.unknownErr = DE_ERRORS.UNKNOWN.getError();
			for (DE_WORDS intToWord : DE_WORDS.values()) {
				intToWordMap.put(intToWord.getNum(), intToWord.getWord());
			}
			break;

		case NL:
			this.millUnit = NL_UNITS.MILLS.val();
			this.thouUnit = NL_UNITS.THOUS.val();
			this.hunUnit = NL_UNITS.HUNS.val();
			this.and = NL_FORMAT.AND.val();
			this.invalidInput = NL_ERRORS.INVALID_INPUT.getError();
			this.nullInput = NL_ERRORS.NULL_INPUT.getError();
			this.negativeInput = NL_ERRORS.NEGATIVE_INPUT.getError();
			this.negativeInput = NL_ERRORS.NUMBERFORMAT.getError();
			this.unknownErr = NL_ERRORS.UNKNOWN.getError();
			for (NL_WORDS intToWord : NL_WORDS.values()) {
				intToWordMap.put(intToWord.getNum(), intToWord.getWord());
			}
			break;

		default:
			break;

		}
	}

	
	public Map<String, String> getIntToWordMap() {
		return intToWordMap;
	}


	public String getNegativeInput() {
		return negativeInput;
	}

	public String getInvalidInput() {
		return invalidInput;
	}

	public String getHunUnit() {
		return hunUnit;
	}

	public String getMillUnit() {
		return millUnit;
	}

	public String getThouUnit() {
		return thouUnit;
	}

	public String getAnd() {
		return and;
	}

	public String getNullInput() {
		return this.nullInput;
	}

	public String getNumberFormatErr() {
		return this.numberFormatErr;
	}

	public String getUnkownErr() {
		return this.unknownErr;
	}

}