package com.troy.troyberry.util;

public class VersionManager {

	private static IVersion currentVersion, defalutVersion = new DefalutVersion();

	public static IVersion getCurentVersion() {
		if (currentVersion != null) {
			return currentVersion;
		}
		return defalutVersion;
	}

	private VersionManager() {
	}

	public static void setVersion(IVersion version) {
		defalutVersion = version;
	}

}
