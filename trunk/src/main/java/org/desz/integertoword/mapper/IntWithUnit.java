package org.desz.integertoword.mapper;

/**
 * Simple convenience class to fulfil DRY. Associates int to be converted with
 * the unit to append.
 *
 */
public final class IntWithUnit {
	private final int i;
	private String unit;
	private final String and;

	/**
	 * 
	 * @param i
	 *            int to convert
	 * @param unit
	 *            language specific value
	 * @param and
	 *            language specific value
	 */
	public IntWithUnit(int i, String unit, String and) {
		this.i = i;
		this.unit = unit;
		this.and = and;
	}

	public String getAnd() {
		return and;
	}

	public int getI() {
		return i;
	}

	public String getUnit() {
		return unit;
	}

}