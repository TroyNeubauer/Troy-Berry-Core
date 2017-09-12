package com.troyberry.file.filemanager;

import com.troyberry.util.*;

public class VersionRange implements Comparable<VersionRange> {

	private byte minMajor, minMinor, minPatch;
	private byte maxMajor, maxMinor, maxPatch;

	public static final byte ALL = Byte.MAX_VALUE;

	public VersionRange(int minMajor, int minMinor, int minPatch, int maxMajor, int maxMinor, int maxPatch) {
		assert (minMajor >= Byte.MIN_VALUE && minMajor <= Byte.MAX_VALUE);
		assert (minMinor >= Byte.MIN_VALUE && minMinor <= Byte.MAX_VALUE);
		assert (minPatch >= Byte.MIN_VALUE && minPatch <= Byte.MAX_VALUE);

		assert (maxMajor >= Byte.MIN_VALUE && maxMajor <= Byte.MAX_VALUE);
		assert (maxMinor >= Byte.MIN_VALUE && maxMinor <= Byte.MAX_VALUE);
		assert (maxPatch >= Byte.MIN_VALUE && maxPatch <= Byte.MAX_VALUE);
		this.minMajor = (byte) minMajor;
		this.minMinor = (byte) minMinor;
		this.minPatch = (byte) minPatch;
		this.maxMajor = (byte) maxMajor;
		this.maxMinor = (byte) maxMinor;
		this.maxPatch = (byte) maxPatch;
		assert (minMajor <= maxMajor);
		assert (minMinor <= maxMinor);
		assert (minPatch <= maxPatch);
	}

	public boolean contains(IVersion version) {
		assert (version.getVersionMajor() <= Byte.MAX_VALUE && version.getVersionMajor() >= Byte.MIN_VALUE);
		assert (version.getVersionMinor() <= Byte.MAX_VALUE && version.getVersionMinor() >= Byte.MIN_VALUE);
		assert (version.getVersionPatch() <= Byte.MAX_VALUE && version.getVersionPatch() >= Byte.MIN_VALUE);
		return contains((byte) version.getVersionMajor(), (byte) version.getVersionMinor(), (byte) version.getVersionPatch());
	}


	public boolean contains(byte major, byte minor, byte patch) {
		return major >= minMajor && major <= maxMajor && minor >= minMinor && minor <= maxMinor && patch >= minPatch && patch <= maxPatch;
	}

	@Override
	public int compareTo(VersionRange other) {
		int sum = 0;
		sum += (this.minMajor - other.minMajor) * 100;
		sum += (this.minMinor - other.minMinor) * 10;
		sum += (this.minPatch - other.minPatch) * 1;
		sum += (this.maxMajor - other.maxMajor) * 100;
		sum += (this.maxMinor - other.maxMinor) * 10;
		sum += (this.maxPatch - other.maxPatch) * 1;
		return sum;
	}

	@Override
	public String toString() {
		return "VersionRange [min " + (minMajor == ALL ? "All" : minMajor) + "." + (minMinor == ALL ? "All" : minMinor) + "."
				+ (minPatch == ALL ? "All" : minPatch) + " to " + (maxMajor == ALL ? "All" : maxMajor) + "." + (maxMinor == ALL ? "All" : maxMinor) + "."
				+ (maxPatch == ALL ? "All" : maxPatch) + "]";
	}
}
