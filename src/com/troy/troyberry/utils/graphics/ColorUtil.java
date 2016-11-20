package com.troy.troyberry.utils.graphics;

import java.awt.*;
import com.troy.troyberry.math.*;

public class ColorUtil {

	private static Color[] planetColors = new Color[] { Color.blue, Color.yellow, Color.red, Color.gray, Color.gray, Color.green, Color.gray,
		Color.gray, Color.LIGHT_GRAY, Color.gray, Color.lightGray, Color.gray, Color.gray, Color.LIGHT_GRAY, Color.gray, Color.gray, Color.gray,
		Color.LIGHT_GRAY, Color.gray, Color.gray, Color.gray, Color.LIGHT_GRAY, Color.gray, Color.gray, Color.gray, Color.LIGHT_GRAY, Color.gray,
		Color.gray, Color.gray, Color.LIGHT_GRAY, Color.gray, Color.orange, Color.orange };

	private ColorUtil() {

	}

	public static Color randomPlanetColor(int variability) {
		int colorIndex = Maths.randRange(0, planetColors.length - 1);
		int r = planetColors[colorIndex].getRed();
		int g = planetColors[colorIndex].getGreen();
		int b = planetColors[colorIndex].getBlue();
		r += Maths.randRange(-variability, variability);
		g += Maths.randRange(-variability, variability);
		b += Maths.randRange(-variability, variability);

		r = Maths.clamp(0, 255, r);
		g = Maths.clamp(0, 255, g);
		b = Maths.clamp(0, 255, b);
		Color color = new Color(r, g, b);
		return color;
	}

	public static Color lerp(Color colorA, Color colorB, double factor) {
		double r = Maths.lerp(colorA.getRed(), colorB.getRed(), factor);
		double g = Maths.lerp(colorA.getGreen(), colorB.getGreen(), factor);
		double b = Maths.lerp(colorA.getBlue(), colorB.getBlue(), factor);
		return new Color(Maths.round(r), Maths.round(g), Maths.round(b));
	}

	public static int divide(int color, double factor) {
		int a = (color & 0xff000000) >> 24;
		int r = (color & 0xff0000) >> 16;
		int g = (color & 0xff00) >> 8;
		int b = (color & 0xff);
		return (Maths.round(a / factor) << 24) | (Maths.round(r / factor) << 16) | (Maths.round(g / factor) << 8) | (Maths.round(b / factor) << 0);
	}

}
