package com.troyberry.util;

public class VersionManager {

	public static class DefaultVersion extends IVersion {

		public DefaultVersion() {
			super("Troy Berry Game", 0, 1, 0);
		}
	}

	private static IVersion currentVersion = new DefaultVersion();

	public static IVersion getCurentVersion() {
		return currentVersion;
	}

	public static void setVersion(IVersion version) {
		currentVersion = version;
	}

	private VersionManager() {
	}
}
