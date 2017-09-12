package com.troyberry.util.profiler;

public abstract class ProfileData {
	
	protected static final byte SELECTION_ID = 0x1, SECTION_ID = 0x2, BREAK_ID = 0x3, ERROR_ID = 0x4;

	protected final String name;

	public ProfileData(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public abstract byte getIdentifier();

}
