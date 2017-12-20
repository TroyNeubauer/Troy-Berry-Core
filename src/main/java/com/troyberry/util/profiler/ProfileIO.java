package com.troyberry.util.profiler;

import com.troyberry.file.filemanager.IOBase;
import com.troyberry.util.serialization.TroyBuffer;

public abstract class ProfileIO implements IOBase<Profiler> {
	
	public void writeHeader(TroyBuffer writer, int major, int minor, int patch) {
		writer.writeByte((byte)'P');
		writer.writeByte((byte)'F');
		writer.writeByte((byte)major);
		writer.writeByte((byte)minor);
		writer.writeByte((byte)patch);
	}
	
}
