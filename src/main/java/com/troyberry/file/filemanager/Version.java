package com.troyberry.file.filemanager;

public class Version {

	private final byte major, minor, patch;

	public Version(byte major, byte minor, byte patch) {
		this.major = major;
		this.minor = minor;
		this.patch = patch;
	}

	public Version(int major, int minor, int patch) {
		assert major <= Byte.MAX_VALUE && minor <= Byte.MAX_VALUE && patch <= Byte.MAX_VALUE;
		this.major = (byte) major;
		this.minor = (byte) minor;
		this.patch = (byte) patch;
	}

	public byte getMajor() {
		return major;
	}

	public byte getMinor() {
		return minor;
	}

	public byte getPatch() {
		return patch;
	}

}
