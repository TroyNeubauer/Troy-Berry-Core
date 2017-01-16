package com.troyberry.graphics;

/**
 * Represents the ration between the width and height of a rectangle. (Usually a computer monitor)
 * @author Troy Neubauer
 *
 */
public enum AspectRatio {
	SIZE16X9(16, 9), SIZE16X10(16, 10), SIZE4X3(4, 3), SIZE1X1(1, 1), SIZE5X4(5, 4), SIZE21X9(21, 9), SIZE3X2(3, 2), UNSUPPORTED(-1, -1);

	private final int width, height;
	private final String identifier;
	
	/**
	 * Creates a new aspect ratio.<br>
	 * The width to height ration should be in simplest form IE 16:9 would be acceptable where as 48:27 wouldn't be.
	 * @param width The width for this aspect ratio
	 * @param height The height for this aspect ratio
	 */
	AspectRatio(int width, int height) {
		this.width = width;
		this.height = height;
		this.identifier = width + ":" + height;
	}
	
	/**
	 * Returns a String representing this aspect ratio IE "16:9", "4:3" 
	 * @return The identifier
	 */
	public String getIdentifier() {
		return identifier;
	}
	
	/**
	 * Returns the aspect ratio as a double (width / height)
	 * @return The aspect ratio
	 */
	public double getAspectRatio() {
		return width / (double) height;
	}

	/**
	 * Returns the aspect ratio using the not normally used formula (height / width)<br>
	 * To get the default aspect ratio (width / height) {@link AspectRatio#getAspectRatio()}
	 * @return The inverted aspect ratio
	 */
	public double getAspectRatioHeightOverWidth() {
		return height / (double) width;
	}

	public String toString() {
		if (width == -1 || height == -1) return "Aspect ratio unsupported";
		return identifier;
	}
}
