package com.troyberry.util;

public abstract class IVersion {

	private int major, minor, patch;
	private String name, extraInfo = "";

	public IVersion(String name, int major, int minor, int patch) {
		this.name = name;
		this.major = major;
		this.minor = minor;
		this.patch = patch;
	}

	/** @return The title used for the window **/
	public String getWindowTitle() {
		return name + " " + getStringVersion();
	}

	/** @return The version major, minor and patch with no additional extra info.<br>
	 * IE "1.1.2"
	 */
	public String getSimpleVersion() {
		return major + "." + minor + "." + patch;
	}

	/** @return The complete version as a String 
	 * IE "1.1.2 Beta Test" assuming Beta and Test were added as extra info via {@link IVersion#addInfo(String)}
	 */
	public String getStringVersion() {
		return major + "." + minor + "." + patch + ((extraInfo == null) ? "" : " " + extraInfo);
	}

	/** @return The major version */
	public final int getVersionMajor() {
		return major;
	}

	/** @return The minor version */
	public final int getVersionMinor() {
		return minor;
	}

	/** @return The patch version */
	public final int getVersionPatch() {
		return patch;
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
		} else {
			extraInfo = extraInfo + " " + info;
		}
	}

}
