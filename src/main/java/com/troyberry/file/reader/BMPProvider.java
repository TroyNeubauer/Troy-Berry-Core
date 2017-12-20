package com.troyberry.file.reader;

import com.troyberry.file.filemanager.*;

public class BMPProvider extends FileProvider<BMPIO, BMPFile> {

	@Override
	public boolean onIONotAvilable(byte requestedMajor, byte requestedMinor, byte requestedPatch, VersionRange rangeToUse, BMPIO IOToUse) {
		return false;
	}

	@Override
	public boolean isValidFile(byte[] file) {
		return (file[0] == 'B' && file[1] == 'M') || (file[0] == 'B' && file[1] == 'A') || (file[0] == 'C' && file[1] == 'I') || (file[0] == 'C' && file[1] == 'P')
				|| (file[0] == 'I' && file[1] == 'C') || (file[0] == 'P' && file[1] == 'T');
	}

	@Override
	public Version getVersion(byte[] file) {

		return new Version(0, 0, 0);
	}

}
