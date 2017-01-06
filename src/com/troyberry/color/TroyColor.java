package com.troyberry.color;

import java.awt.*;

import com.troyberry.math.*;

/**
 * Represents an RGB color with or without transparency. The maxiumn depth for a color is 8 bits.<br>
 * The color can be converted to almost any image format in {@link ColorFormat }. If the format doesn't support alpha, 
 * like {@link ColorFormat#RGB }, the alpha will be locked to 255 (opaque). If this isan't desirable, all non alpha colors have an
 * alpha supported big brother like {@link ColorFormat#RGBA } or {@link ColorFormat#ARGB }<br>
 * <br>
 * The default color space for TroyColor is ARGB, a proposed
 * standard RGB color space with an alpha channel. For further information on ARGB,
 * see <A href="https://en.wikipedia.org/wiki/RGBA_color_space">
 * https://en.wikipedia.org/wiki/RGBA_color_space
 * </A>.
 * @see ColorUtils
 * @see ColorFormat
 * @author Troy Neubauer
 */
public class TroyColor {
	
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
	
    public static final int MIN_VALUE = 0;
    
    public static final int MAX_VALUE = Maths.pow(2, Byte.SIZE) - 1;// 255
    
    
	private int color;
	private ColorFormat format;
	
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
	public TroyColor(int red, int green, int blue, int alpha, ColorFormat format){
		this.format = format;
		this.color = ColorUtils.createColor(red, green, blue, alpha, format);
	}
	
	public TroyColor(int color, ColorFormat format){
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
		this(red, green, blue, ColorFormat.RGB);
	}
	
	public TroyColor(int red, int green, int blue, ColorFormat format){
		this(red, green, blue, 255, format);
	}
	
	public TroyColor() {
		this(0, 0, 0, ColorFormat.RGB);
	}

	@Override
	public String toString() {
		return "Color[R:" + getRed() + ", G:" + getGreen() + ", B:" + getBlue() + ", A:" + getAlpha() + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj instanceof TroyColor) {
			TroyColor other = (TroyColor)obj;
			return this.color == other.color && this.format.equals(other.format);
		}
		return false;
	}
	
	public Color toJavaColor() {
		return new Color(ColorUtils.convert(color, format, ColorFormat.ARGB), format.hasAlpha());
	}
	
	public Vector3f toVec3(){
		return new Vector3f(getRed(), getGreen(), getBlue());
	}
	
	public Vector4f toVec4(){
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
		this.color = ColorUtils.createColor(red, green, blue, alpha, format);
	}
	
	public void set(int red, int green, int blue) {
		set(red, green, blue, 255);
	}
	
	public int getAlpha() {
		if(format.hasAlpha()){
			return (color >> format.getAlphaOffset()) & 0xff;
		}
		return 0xff;
	}

	public ColorFormat getFormat() {
		return format;
	}
	
	public boolean alphaEnabled(){
		return format.hasAlpha();
	}

}
