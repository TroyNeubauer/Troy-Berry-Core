package com.troyberry.color;

import com.troyberry.util.BitBuilder;

/**
 * Represents a particular format for an RGB color. <br>
 * Both alpha and non alpha formats are supported.
 * @author Troy Neubauer
 * @see RGBColorUtils
 * @see TroyColor
 */
public enum RGBColorFormat {
	RGBA(24, 16, 8, 0), ARGB(16, 8, 0, 24), ABGR(0, 8, 16, 24), RGB(16, 8, 0), BGR(0, 8, 16);
	private int rOffset, gOffset, bOffset, aOffset;
	private int rMask, gMask, bMask, aMask;
	private boolean hasAlpha;

	private RGBColorFormat(int rOffset, int gOffset, int bOffset, int aOffset) {
		BitBuilder b = new BitBuilder();
		this.rOffset = rOffset;
		this.rMask = b.set(rOffset, rOffset + 8, true).first32();

		b.clear();
		this.gOffset = gOffset;
		this.gMask = b.set(gOffset, gOffset + 8, true).first32();

		b.clear();
		this.bOffset = bOffset;
		this.bMask = b.set(bOffset, bOffset + 8, true).first32();

		b.clear();
		this.aOffset = aOffset;
		this.aMask = b.set(aOffset, aOffset + 8, true).first32();
		this.hasAlpha = true;
	}

	private RGBColorFormat(int rOffset, int gOffset, int bOffset) {
		BitBuilder b = new BitBuilder();
		this.rOffset = rOffset;
		this.rMask = b.set(rOffset, rOffset + 8, true).first32();

		b.clear();
		this.gOffset = gOffset;
		this.gMask = b.set(gOffset, gOffset + 8, true).first32();

		b.clear();
		this.bOffset = bOffset;
		this.bMask = b.set(bOffset, bOffset + 8, true).first32();

		this.hasAlpha = false;
	}

	public int getRedOffset() {
		return rOffset;
	}

	public int getGreenOffset() {
		return gOffset;
	}

	public int getBlueOffset() {
		return bOffset;
	}

	public int getAlphaOffset() {
		if (!hasAlpha) throw new IllegalStateException("Color Format has no alpha!");
		return aOffset;
	}

	public int getRedMask() {
		return rMask;
	}

	public int getGreenMask() {
		return gMask;
	}

	public int getBlueMask() {
		return bMask;
	}

	public int getAlphaMask() {
		if (!hasAlpha) throw new IllegalStateException("Color Format has no alpha!");
		return aMask;
	}

	public boolean hasAlpha() {
		return hasAlpha;
	}

	public static RGBColorFormat getFormat(byte id) {
		for (RGBColorFormat format : RGBColorFormat.values()) {
			if (format.ordinal() == id) return format;
		}
		return RGBColorFormat.RGBA;
	}

}
