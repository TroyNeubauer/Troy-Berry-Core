package com.troyberry.graphics;

/**
 * Represents the resolution for a window with an aspect ratio, width & height and fullscreen
 * @author Troy Neubauer
 * @see AspectRatio
 *
 */
public class Resolution {

	public final int width, height;
	public final boolean fullscreen;
	public AspectRatio aspectRatio;
	
	/**
	 * Creates a new resolution, with a desired with height and full screen
	 * @param width The width of the window in pixels
	 * @param height The height of the window in pixels
	 * @param fullscreen Weather or not the window should be fullscreen
	 */
	public Resolution(int width, int height, boolean fullscreen) {
		this.width = width;
		this.height = height;
		this.fullscreen = fullscreen;
		double ratio = width / (double) height;
		this.aspectRatio = AspectRatio.UNSUPPORTED;
		for (int i = 0; i < ResolutionUtil.aspectRatios.size(); i++) {
			AspectRatio currentRatio = ResolutionUtil.aspectRatios.get(i);
			double otherRatio = currentRatio.getAspectRatio();
			if (ratio == otherRatio) this.aspectRatio = currentRatio;
		}
	}
	
	/**
	 * Creates a new resolution with the width and height getting set to size, making a square
	 * @param size The length of the square in pixels
	 */
	public Resolution(int size) {
		this.width = size;
		this.height = size;
		this.fullscreen = false;
		this.aspectRatio = AspectRatio.SIZE1X1;
	}

	public boolean isKnownAspectRatio() {
		return this.aspectRatio != AspectRatio.UNSUPPORTED;
	}

}
