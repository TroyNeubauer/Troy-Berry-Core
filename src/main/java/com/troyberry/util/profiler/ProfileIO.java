package com.troyberry.util.profiler;

import com.troyberry.file.filemanager.IOBase;
import com.troyberry.util.serialization.TroyBuffer;

public interface ProfileIO extends IOBase<Profiler> {
	
	public default void writeHeader(TroyBuffer writer, int major, int minor, int patch) {
		writer.writeByte((byte)'P');
		writer.writeByte((byte)'F');
		writer.writeByte((byte)major);
		writer.writeByte((byte)minor);
		writer.writeByte((byte)patch);
	}
	
}
