package com.troyberry.graphics;

public enum AspectRatio {
	SIZE16X9(16, 9), SIZE16X10(16, 10), SIZE4X3(4, 3), SIZE1X1(1, 1), SIZE5X4(5, 4), SIZE21X9(21, 9), SIZE3X2(3, 2), UNSUPPORTED(-1, -1);

	private final int width, height;
	private final String identifier;

	AspectRatio(int width, int height) {
		this.width = width;
		this.height = height;
		this.identifier = width + ":" + height;
	}

	public String getIdentifier() {
		return identifier;
	}

	public double getAspectRatio() {
		return width / (double) height;
	}

	public double getAspectRatioHeightOverWidth() {
		return height / (double) width;
	}

	public String toString() {
		if (width == -1 || height == -1) return "Aspect ratio unsupported";
		return identifier;
	}
}
