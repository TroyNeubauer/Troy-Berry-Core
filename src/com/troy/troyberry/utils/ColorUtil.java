package com.troy.troyberry.utils;

import java.awt.*;
import com.troy.troyberry.math.*;

public class ColorUtil {

	private static Color[] planetColors = new Color[] { Color.blue, Color.yellow, Color.red, Color.gray, Color.gray, Color.green, Color.gray,
		Color.gray, Color.LIGHT_GRAY, Color.gray, Color.lightGray, Color.gray, Color.gray, Color.LIGHT_GRAY, Color.gray, Color.gray, Color.gray,
		Color.LIGHT_GRAY, Color.gray, Color.gray, Color.gray, Color.LIGHT_GRAY, Color.gray, Color.gray, Color.gray, Color.LIGHT_GRAY, Color.gray,
		Color.gray, Color.gray, Color.LIGHT_GRAY, Color.gray, Color.orange, Color.orange };

	private ColorUtil() {
		Maths.randRange(1, 1);
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

}
