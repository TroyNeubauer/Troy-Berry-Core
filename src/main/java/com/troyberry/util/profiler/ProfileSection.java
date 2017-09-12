package com.troyberry.util.profiler;

public class ProfileSection extends ProfileData {

	/** Measured in milliseconds  NOT nanos*/
	private final long start;
	private long end = -1;

	public ProfileSection(String name) {
		super(name);
		this.start = System.currentTimeMillis();
	}

	public ProfileSection(String name, long start, long end) {
		super(name);
		this.start = start;
		this.end = end;
	}

	public void end() {
		this.end = System.currentTimeMillis();
	}

	public long length() {
		if (end == -1) return System.currentTimeMillis() - start;
		return end - start;
	}

	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}

	public boolean isComplete() {
		return end != -1;
	}

	@Override
	public String toString() {
		return "ProfileSection [" + name + ", " + (end == -1 ? "Currently Running " : "Took " + (end - start + " MS")) + "]";
	}

	@Override
	public byte getIdentifier() {
		return SECTION_ID;
	}

}
