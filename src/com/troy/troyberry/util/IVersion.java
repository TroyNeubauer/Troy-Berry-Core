package com.troy.troyberry.util;

public abstract class IVersion {

	private int VERSION_MAJOR, VERSION_MINOR, VERSION_PATCH;
	private String name, extraInfo = "";

	public IVersion(String name, int major, int minor, int patch) {
		this.name = name;
		this.VERSION_MAJOR = major;
		this.VERSION_MINOR = minor;
		this.VERSION_PATCH = patch;
	}
	
	/** @return The title used for the window **/
	public String getWindowTitle() {
		return name + " " + getStringVersion();
	}

	/** @return The complete version as a String */
	public String getStringVersion() {
		return VERSION_MAJOR + "." + VERSION_MINOR + "." + VERSION_PATCH + " " + extraInfo;
	}

	/** @return The major version */
	public final int getVersionMajor() {
		return VERSION_MAJOR;
	}

	/** @return The minor version */
	public final int getVersionMinor() {
		return VERSION_MINOR;
	}

	/** @return The patch version */
	public final int getVersionPatch() {
		return VERSION_PATCH;
	}

	/** @return The name of the version */
	public final String getName() {
		return name;
	}

	/** Adds a string containing information about this version such as beta, pre-release etc. to the list of info about this version.
	 *  This new information will be seen when getStringInfo or getWindowTitle is called
	 * @param info the information to be added
	 * */
	public final void addInfo(String info) {
		if (extraInfo.length() == 0 || extraInfo == null) {
			extraInfo = info;
		}else{
			extraInfo = extraInfo + " " + info;
		}
	}

}
