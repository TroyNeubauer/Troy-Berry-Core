package com.troyberry.file.filemanager;

public class Version {

	private final byte major, minor, patch;

	public Version(byte major, byte minor, byte patch) {
		this.major = major;
		this.minor = minor;
		this.patch = patch;
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
