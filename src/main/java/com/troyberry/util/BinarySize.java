package com.troyberry.util;

import com.troyberry.math.Maths;

/**
 * Represents binary prefixes up to 1024^8 (YottaByte)
 * @author Troy Neubauer
 *
 */
public enum BinarySize {
	BYTE(0), KILOBYTE(1), MEGABYTE(2), GIGABYTE(3), TERABYTE(4), PETABYTE(5), EXABYTE(6), ZETTABYTE(7), YOTTABYTE(8);

	final long value, siValue, exponent;
	final String fullName, shortName;

	BinarySize(long exp) {
		this.exponent = exp;
		this.value = Maths.pow(1024, exp);
		this.siValue = Maths.pow(1000, exp);
		
		String name = this.name();
		if (name.equals("BYTE")) this.fullName = StringFormatter.capitalizeFirstLetter(name);
		else this.fullName = StringFormatter.capitalizeFirstLetter(name.substring(0, name.indexOf("BYTE"))) + "Byte";
		
		String[] parts = fullName.split("B");
		this.shortName = ((exp != 0) ? parts[0].charAt(0) : "") + "B";
	}

	/**
	 * The "Full Name" for this prefix with no spaces<br>
	 * IE "Byte", "KiloByte", "TeraByte"
	 * @return The full name
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Returns the bytes in this prefix using a base of 1024<br>
	 * For example, {@link BinarySize#KILOBYTE}.{@link #getValue()} would return 1024.
	 * {@link BinarySize#MEGABYTE}.{@link #getValue()} would return 1048576.
	 * {@link BinarySize#BYTE}.{@link #getValue()} would return 1.
	 * @return The value for this prefix (1024^exponent)
	 */
	public long getValue() {
		return value;
	}
	
	/**
	 * Returns the bytes in this prefix using a base of 1024<br>
	 * For example, {@link BinarySize#KILOBYTE}.{@link #getValue()} would return 1024.
	 * {@link BinarySize#MEGABYTE}.{@link #getValue()} would return 1048576.
	 * {@link BinarySize#BYTE}.{@link #getValue()} would return 1.<br>
	 * Sometimes the value will be greater than {@link Integer#MAX_VALUE}, when this is the case, the number may flip the sign bit and
	 * return an unexpected result
	 * 
	 * @return The value for this prefix (1024^exponent)
	 */
	public int getIntValue() {
		return (int) value;
	}
	
	/**
	 * Returns a one or two length string representing the short hand version of this prefix.<br>
	 * IE "B" for Byte "MB" for Mega Byte etc.
	 * @return The short name
	 */
	public String getShortName() {
		return shortName;
	}
	
	/**
	 * Returns the SI value for this prefix.<br>
	 * IE (1000^exponent)
	 * @return The SI value
	 */
	public long getSiValue() {
		return siValue;
	}
	
	/**
	 * Returns the exponent of this 
	 * @return
	 */
	public long getExponent() {
		return exponent;
	}

}
