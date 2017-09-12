package com.troyberry.util.profiler;

import java.io.*;

import com.troyberry.file.filemanager.*;
import com.troyberry.util.*;

public class ProfileFileManager {
	private static ProfileProvider instance = new ProfileProvider();

	public static Profiler read(byte[] bytes) throws InvalidFileException {
		return instance.read(bytes);
	}

	static ProfileProvider getInstance() {
		return instance;
	}

	public static Profiler read(File file) throws IOException, InvalidFileException {
		return instance.read(file);
	}

	public static Profiler read(MyFile file) throws IOException, InvalidFileException {
		return instance.read(file);
	}

	public static Profiler read(InputStream stream) throws IOException, InvalidFileException {
		return instance.read(stream);
	}

	public static void write(File dest, Profiler file) throws IOException {
		instance.write(dest, file);
	}

	public static void write(File dest, Profiler file, byte major, byte minor, byte patch) throws IOException {
		instance.write(dest, file, major, minor, patch);
	}

}
