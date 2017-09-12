package com.troyberry.util.profiler;

import static com.troyberry.file.filemanager.VersionRange.*;
import static com.troyberry.util.profiler.Profiler.*;

import com.troyberry.file.filemanager.*;

class ProfileProvider extends FileProvider<ProfileIO, Profiler> {

	public ProfileProvider() {
		super();

		add(new VersionRange(1, 0, 0, ALL, ALL, ALL), new ProfileIO100());
	}
	
	@Override
	public boolean onIONotAvilable(byte requestedMajor, byte requestedMinor, byte requestedPatch, VersionRange rangeToUse, ProfileIO IOToUse) {
		return false;
	}

	@Override
	public boolean isValidFile(byte[] file) {
		if (file.length < 7 * Byte.BYTES) return false;
		return file[P_OFFSET] == 'P' && file[F_OFFSET] == 'F';
	}

	@Override
	public Version getVersion(byte[] file) {
		byte major = file[MAJOR_OFFSET];
		byte minor = file[MINOR_OFFSET];
		byte patch = file[PATCH_OFFSET];
		return new Version(major, minor, patch);
	}

}
