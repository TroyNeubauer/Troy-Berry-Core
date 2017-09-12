package com.troyberry.color;

import com.troyberry.math.*;

/**
 * A static utility class for manipulating {@link TroyColor}.<br>
 * here you will find several methods for averaging, blending, adding, and multiplying colors together.
 * @author Troy Neubauer
 * @see TroyColor
 */
public class RGBColorUtils {
	
	/**
	 * Creates a integer representing a color with 8 bit color depth in a particular format.<br>
	 * <u>If alpha is not supported by the format, the alpha will be locked to max </u>(255, opaque)
	 * @param red The red component of the color in RGB 0-255
	 * @param green The green component of the color in RGB 0-255
	 * @param blue The blue component of the color in RGB 0-255
	 * @param alpha The alpha component of the color in RGB 0-255
	 * @param format The format to put the finished integer info
	 * @return The complete color
	 */
	public static int createColor(int red, int green, int blue, int alpha, RGBColorFormat format) {
		int result = 0;
		if(format.hasAlpha()) result |= (alpha << format.getAlphaOffset());
		result |= (red << format.getRedOffset());
		result |= (green << format.getGreenOffset());
		result |= (blue << format.getBlueOffset());
		
		return result;
	}
	
	public void checkColor(int red, int green, int blue, int alpha){
        String badComponentString = "";

        if (alpha < TroyColor.MIN_VALUE || alpha > TroyColor.MAX_VALUE) {
            badComponentString +=  " Alpha " + alpha;
        }
        if (red   < TroyColor.MIN_VALUE ||   red > TroyColor.MAX_VALUE) {
            badComponentString +=  " Red " +     red;
        }
        if (green < TroyColor.MIN_VALUE ||  green > TroyColor.MAX_VALUE) {
            badComponentString += " Green " +  green;
        }
        if (blue  < TroyColor.MIN_VALUE || blue > TroyColor.MAX_VALUE) {
            badComponentString += " Blue " +    blue;
        }
        if (!badComponentString.isEmpty()) {
        	throw new IllegalArgumentException("Color parameter outside of expected range:" + badComponentString);
        }
	}
	
	/**
	 * Converts a color in one format to a color in another format
	 * @param color The color to convert
	 * @param startingFormat The format that the color is currently in
	 * @param resultFormat The desired output format
	 * @return The converted color
	 */
	public static int convert(int color, RGBColorFormat startingFormat, RGBColorFormat resultFormat) {
		if(startingFormat.equals(resultFormat)) return color;
		int r = (color & startingFormat.getRedMask()) >> startingFormat.getRedOffset();
		int g = (color & startingFormat.getGreenMask()) >> startingFormat.getGreenOffset();
		int b = (color & startingFormat.getBlueMask()) >> startingFormat.getBlueOffset();
		int a = 0xff;
		if(startingFormat.hasAlpha()) {
			a = ((color & startingFormat.getAlphaMask()) >> startingFormat.getAlphaOffset()) & 0x000000ff;
		}
		int total = 0;
		if(resultFormat.hasAlpha()) {
			total |= (a << resultFormat.getAlphaOffset());
		}
		total |= (r << resultFormat.getRedOffset());
		total |= (g << resultFormat.getGreenOffset());
		total |= (b << resultFormat.getBlueOffset());
		return total;
	}
	
	/**
	 * Divides the components of the TroyColor color by divisor and returns a new TeoyColor object.<br>
	 * his method will not change any of the values of color
	 * @param color The color to divide
	 * @param divisor How much to divide by
	 * @return A new color that has been divided
	 */
	public static TroyColor divide(TroyColor color, double divisor) {
		int r = Maths.round(color.getRed() / divisor);
		int g = Maths.round(color.getGreen() / divisor);
		int b = Maths.round(color.getBlue() / divisor);
		return new TroyColor(r, g, b);
	}
	
	/**
	 * Combines the two colors such that the brightest channels of each color make up the final color.<br>
	 * Given two RBG colors (25, 250, 100) and (50, 125, 105), the output color would be (50, 250, 105).<br>
	 * The new color will have the alpha and format of color1.
	 * This method will not change any of the values of color1 or color2
	 * @param color1 The first color
	 * @param color2 The second color
	 * @return A new color with the brightest parts each channel
	 */
	public static TroyColor combineForBrightest(TroyColor color1, TroyColor color2){
		int r = Math.max(color1.getRed(), color2.getRed());
		int g = Math.max(color1.getGreen(), color2.getGreen());
		int b = Math.max(color1.getBlue(), color2.getBlue());
		return new TroyColor(r, g, b, color1.getAlpha(), color1.getFormat());
	}
	
	/**
	 * Combines the two colors such that the darkest channels of each color make up the final color.<br>
	 * Given two RBG colors (25, 250, 100) and (50, 125, 105), the output color would be (25, 125, 100).<br>
	 * The new color will have the alpha and format of color1.
	 * This method will not change any of the values of color1 or color2
	 * @param color1 The first color
	 * @param color2 The second color
	 * @return A new color with the brightest parts each channel
	 */
	public static TroyColor combineForDarkest(TroyColor color1, TroyColor color2){
		int r = Math.min(color1.getRed(), color2.getRed());
		int g = Math.min(color1.getGreen(), color2.getGreen());
		int b = Math.min(color1.getBlue(), color2.getBlue());
		return new TroyColor(r, g, b, color1.getAlpha(), color1.getFormat());
	}
	
	/**
	 * Increases the contrast of the desired color by the factor of contrast.<br>
	 * An increased contrast makes the dark colors darker and the light colors lighter.<br>
	 * A value of 0.1f would make all colors become 10% further away from gray (128)<br>
	 * The new color will have the same alpha and format of color.
	 * This method will not change any of the values of color.
	 * @param color1 The first color
	 * @param color2 The second color
	 * @param color The color to use (values wont be modified)
	 * @param contrast How much percent should the colors grow by
	 * @return The new color after the calculation
	 */
	public static TroyColor contrast(TroyColor color, float contrast) {
		float r = color.getRed();
		float g = color.getGreen();
		float b = color.getBlue();
		r /= TroyColor.MAX_VALUE;
		g /= TroyColor.MAX_VALUE;
		b /= TroyColor.MAX_VALUE;
		r -= 0.5f;
		g -= 0.5f;
		b -= 0.5f;
		r *= (1.0f + contrast);
		g *= (1.0f + contrast);
		b *= (1.0f + contrast);
		r += 0.5f;
		g += 0.5f;
		b += 0.5f;
		
		return new TroyColor((int)Maths.clamp(TroyColor.MIN_VALUE, TroyColor.MAX_VALUE, (r * TroyColor.MAX_VALUE)), 
				(int)Maths.clamp(TroyColor.MIN_VALUE, TroyColor.MAX_VALUE, (g * TroyColor.MAX_VALUE)), 
				(int)Maths.clamp(TroyColor.MIN_VALUE, TroyColor.MAX_VALUE, (b * TroyColor.MAX_VALUE)), 
				color.getAlpha(), color.getFormat());
	}
	
	/**
	 * Returns a new {@link TroyColor} which is the average of both input colors and has the format of the first color.<br>
	 * The new color will have averaged alpha of color1 and color2.
	 * This method will not change any of the values of color1 or color2
	 * @param color1 The first color
	 * @param color2 The second color
	 * @return The averaged color
	 */
	public static TroyColor average(TroyColor color1, TroyColor color2){
		return new TroyColor((color1.getRed() + color2.getRed()) / 2, (color1.getGreen() + color2.getGreen()) / 2, 
				(color1.getBlue() + color2.getBlue()) / 2, (color1.getAlpha() + color2.getAlpha()) / 2, color1.getFormat());
	}
	
	
	
	// Nobody can have an instance of this class
	private RGBColorUtils() {
	}

}
