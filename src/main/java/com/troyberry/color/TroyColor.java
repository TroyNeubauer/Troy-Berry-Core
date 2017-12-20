package com.troyberry.color;

import java.awt.Color;

import com.troyberry.math.*;
import com.troyberry.util.interpolation.*;
import com.troyberry.util.serialization.*;

/**
 * Represents an RGB color with or without transparency. The maxiumn depth for this representation of a color is 8 bits.<br>
 * The color can be converted to almost any color format in {@link RGBColorFormat }. If the format doesn't support alpha, 
 * like {@link RGBColorFormat#RGB }, the alpha will be locked to 255 (opaque). If this isan't desirable, all non alpha colors have an
 * alpha supported big brother like {@link RGBColorFormat#RGBA } or {@link RGBColorFormat#ARGB }<br>
 * <br>
 * The default color space for TroyColor is ARGB, a proposed
 * standard RGB color space with an alpha channel. For further information on ARGB,
 * see <A href="https://en.wikipedia.org/wiki/RGBA_color_space">
 * https://en.wikipedia.org/wiki/RGBA_color_space
 * </A>.
 * @see RGBColorUtils
 * @see RGBColorFormat
 * @author Troy Neubauer
 */
public class TroyColor implements Interpolatable<TroyColor>, TroySerializable {
	
	//format:off
    public final static TroyColor WHITE      = new TroyColor(255, 255, 255);
    public final static TroyColor LIGHT_GRAY = new TroyColor(192, 192, 192);
    public final static TroyColor Gray       = new TroyColor(128, 128, 128);
    public final static TroyColor DARK_GRAY  = new TroyColor( 64,  64,  64);
    public final static TroyColor BLACK      = new TroyColor(  0,   0,   0);
    public final static TroyColor RED        = new TroyColor(255,   0,   0);
    public final static TroyColor PINK       = new TroyColor(255, 175, 175);
    public final static TroyColor ORAGNE     = new TroyColor(255, 200,   0);
    public final static TroyColor YELLOW     = new TroyColor(255, 255,   0);
    public final static TroyColor GREEN      = new TroyColor(  0, 255,   0);
    public final static TroyColor MAGENTA    = new TroyColor(255,   0, 255);
    public final static TroyColor CYAN       = new TroyColor(  0, 255, 255);
    public final static TroyColor BLUE       = new TroyColor(  0,   0, 255);
	//format:on
	public static final int MIN_VALUE = 0;

	public static final int MAX_VALUE = Maths.pow(2, Byte.SIZE) - 1;// 255

	private int color;
	private RGBColorFormat format;

	/**
	 * Creates a new color object with the desired values for each of the color channels.<br> 
	 * This must <u>always be specified in RGBA order, even if the format is different.</u>
	 * The color values will be converted to the desired format automatically
	 * 
	 * @param red The value for the red channel (0 - 255)
	 * @param green The value for the green channel (0 - 255)
	 * @param blue The value for the blue channel (0 - 255)
	 * @param alpha The value for the alpha channel (0 - 255)
	 * @param format The format to store this color as
	 */
	public TroyColor(int red, int green, int blue, int alpha, RGBColorFormat format) {
		this.format = format;
		this.color = RGBColorUtils.createColor(red, green, blue, alpha, format);
	}

	public TroyColor(int color, RGBColorFormat format) {
		this.color = color;
		this.format = format;
	}

	/**
	 * Creates a new color object with the desired values for each of the color channels.<br> 
	 * This color will have a default format of RGB, with alpha disabled
	 * 
	 * @param red The value for the red channel (0 - 255)
	 * @param green The value for the green channel (0 - 255)
	 * @param blue The value for the blue channel (0 - 255)
	 */
	public TroyColor(int red, int green, int blue) {
		this(red, green, blue, RGBColorFormat.RGB);
	}

	public TroyColor(int red, int green, int blue, RGBColorFormat format) {
		this(red, green, blue, 255, format);
	}

	public TroyColor() {
		this(0, 0, 0, RGBColorFormat.RGB);
	}

	public TroyColor(double r, double g, double b, double a) {
		this(Maths.round(r), Maths.round(g), Maths.round(b), Maths.round(a), RGBColorFormat.RGBA);
	}

	public TroyColor(float r, float g, float b, float a) {
		this(Maths.round(r), Maths.round(g), Maths.round(b), Maths.round(a), RGBColorFormat.RGBA);
	}

	@Override
	public String toString() {
		return "TroyColor[R:" + getRed() + ", G:" + getGreen() + ", B:" + getBlue() + ", A:" + getAlpha() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj instanceof TroyColor) {
			TroyColor other = (TroyColor) obj;
			return this.color == other.color && this.format == other.format;
		}
		return false;
	}

	public Color toJavaColor() {
		return new Color(RGBColorUtils.convert(color, format, RGBColorFormat.ARGB), format.hasAlpha());
	}

	public Vector3f toVec3() {
		return new Vector3f(getRed(), getGreen(), getBlue());
	}

	public Vector4f toVec4() {
		return new Vector4f(getRed(), getGreen(), getBlue(), getAlpha());
	}

	public int getRed() {
		return (color >> format.getRedOffset()) & 0xff;
	}

	public int getGreen() {
		return (color >> format.getGreenOffset()) & 0xff;
	}

	public int getBlue() {
		return (color >> format.getBlueOffset()) & 0xff;
	}

	public void set(int red, int green, int blue, int alpha) {
		this.color = RGBColorUtils.createColor(red, green, blue, alpha, format);
	}

	public void set(int red, int green, int blue) {
		set(red, green, blue, alphaEnabled() ? getAlpha() : 255);
	}

	public void setFormat(RGBColorFormat newFormat) {
		if (newFormat != format) {
			this.format = newFormat;
			this.color = RGBColorUtils.createColor(getRed(), getGreen(), getBlue(), getAlpha(), newFormat);
		}
	}

	public int getAlpha() {
		if (format.hasAlpha()) { return (color >> format.getAlphaOffset()) & 0xff; }
		return 0xff;
	}

	public RGBColorFormat getFormat() {
		return format;
	}

	public boolean alphaEnabled() {
		return format.hasAlpha();
	}

	@Override
	public TroyColor interpolate(TroyColor a, TroyColor b, double f, InterpolationType type) {
		return new TroyColor(type.interpolate(a.getRed(), b.getRed(), f), type.interpolate(a.getGreen(), b.getGreen(), f),
				type.interpolate(a.getBlue(), b.getBlue(), f), type.interpolate(a.getAlpha(), b.getAlpha(), f));
	}

	public int getRawColor() {
		return color;
	}

	@Override
	public void read(TroyBuffer buffer) {
		this.format = RGBColorFormat.getFormat(buffer.readByte());
		this.color = buffer.readInt();
	}

	@Override
	public void write(TroyBuffer buffer) {
		buffer.writeByte((byte) format.ordinal());
		buffer.writeInt(color);
	}


}
