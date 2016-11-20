package com.troy.troyberry.utils.graphics;

import com.troy.troyberry.utils.*;

public class Resolution {

	public final int width, height;
	public final boolean fullscreen;
	public AspectRatio aspectRatio;

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
