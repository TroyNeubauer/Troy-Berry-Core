package com.troyberry.internal;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.*;

import com.troyberry.util.MiscUtil;

public class LibraryUtils {

	public static final String LIBRARY_NAME = System.getProperty("os.arch").contains("64") ? "troyberry" : "troyberry32";
	public static final String REAL_LIBRARY_NAME = System.mapLibraryName(LIBRARY_NAME);
	private static boolean loaded = false;

	static {

		InternalLog.println("\tOS: " + System.getProperty("os.name") + " v" + System.getProperty("os.version"));
		InternalLog.println("\tJRE: " + System.getProperty("java.version") + " " + System.getProperty("os.arch"));
		InternalLog.println("\tJVM: " + System.getProperty("java.vm.name") + " v" + System.getProperty("java.vm.version") + " by "
				+ System.getProperty("java.vm.vendor"));

		InternalLog.println("Attempting to load TroyBerry native library");
		try {
			loadFromJavaLibPath();
			loadFromClassPath();
			if(true) loadFromStream(new FileInputStream(new File("D:/Java/Current Projects/Troy Berry Core/src/main/resources/troyberry.dll")), "raw (bad)");
			try {
				File file = new File(LibraryUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
				if (file != null && file.isFile()) {
					InternalLog.println("Troy Berry Core is running inside jar file: " + file);
					loadFromJarFile(file);
				}
			} catch (Throwable t) {
				InternalLog.println("Failed to load TroyBerry native library from jar file!\n" + MiscUtil.getStackTrace(t));
			}
		} catch (Throwable t) {
			loaded = false;
		}
		if(!loaded) {
			InternalLog.println("[SEVERE] Failed to load TroyBerry native library!!!!");
			InternalLog.println("[SEVERE] This will most likley result in errors later when native methods are called!!!");
			System.err.println("[Troy Berry] [SEVERE] Failed to load TroyBerry native library");
			ErrorLoggingUtils.indicateError();
		}

	}

	private static boolean loadFromClassPath() {
		if (loaded)
			return true;
		InternalLog.println("Attempting to load TroyBerry native library from classpath");
		InputStream stream = Class.class.getResourceAsStream(REAL_LIBRARY_NAME);
		loadFromStream(stream, "Classpath");
		return loaded;
	}

	private static void loadFromStream(InputStream stream, String method) {
		if (loaded)
			return;
		if (stream != null) {
			File libraryFile;
			try {
				libraryFile = File.createTempFile(LIBRARY_NAME, null);// Create a temp file to copy the native lib inside the jar to
			} catch (Throwable t) {
				throw new UnsatisfiedLinkError("Unable to load libary " + REAL_LIBRARY_NAME
						+ " from stream because the temp directory creation failed(" + method + ")\"\n" + MiscUtil.getStackTrace(t));
			}
			try {
				FileOutputStream dest = new FileOutputStream(libraryFile);
				MiscUtil.copy(stream, dest);// Copy from the jar file to the temp file
				dest.flush();
				dest.getChannel().force(true);// To ensure that System.load() doesn't get angry that "another process is using the file..."
				dest.close();
			} catch (Throwable t) {
				throw new UnsatisfiedLinkError("Unable to load libary " + REAL_LIBRARY_NAME
						+ " from stream because copying to the temp file failed!(" + method + ")\"\n" + MiscUtil.getStackTrace(t));
			}
			try {
				System.load(libraryFile.getAbsolutePath());// Give the temp file to System.load to attempt to load it
			} catch (Throwable t) {
				throw new UnsatisfiedLinkError("Unable to load libary " + REAL_LIBRARY_NAME + " from stream!\n" + MiscUtil.getStackTrace(t));
			}
			loaded = true;
			InternalLog.println("Successfully loaded TroyBerry native library from input stream, (" + method + ")");
			libraryFile.deleteOnExit();// Delete the temp file when we exit because we don't want to delete it when System.load() is reading from
										// it
		} else {
			InternalLog.println("Failed to load TroyBerry native library from stream because the stream was null (" + method + ")");
		}
	}

	private static void loadFromJarFile(File file) {
		if (loaded)
			return;
		InternalLog.println("Attempting to load TroyBerry native library from jar file");
		try {
			ZipFile zip = new ZipFile(file);// Create a zip file object from the file so we can look at the entries
			ZipEntry entry = zip.getEntry(REAL_LIBRARY_NAME);// Grab the entry with the file name we want
			if (entry == null) {// zip.getEntry will return null if the desired file name is not found so throw an exception if the entry is null
				zip.close();//Close the zip file because the other line wont get executed
				throw new FileNotFoundException("Unable to find file " + REAL_LIBRARY_NAME + " in zip file " + file);
			}
			InternalLog.println("Found TroyBerry native library in zip file (" + entry + ") last modified " + new SimpleDateFormat().format(new Date(entry.getLastModifiedTime().toMillis())));
			loadFromStream(zip.getInputStream(entry), "Jar File");// Now get the input stream for reading the library and use it to load it
			zip.close();
		} catch (Throwable e) {
			InternalLog.println("Failed to load TroyBerry native library from jar file " + file + "\n" + MiscUtil.getStackTrace(e));
		}
	}

	private static boolean loadFromJavaLibPath() {
		if (loaded)
			return true;
		InternalLog.println("Attempting to load TroyBerry native library from java.library.path");
		String value = System.getProperty("java.library.path");
		System.setProperty("java.library.path", "D:\\Java\\Current Projects\\Troy Berry Core\\src\\main\\resources\\");
		boolean change = true;
		try {
			System.loadLibrary(LIBRARY_NAME);
			loaded = true;
			InternalLog.println("Successfully loaded TroyBerry native library from java.library.path");
		} catch (Throwable t) {
			InternalLog.println("Failed to load TroyBerry native library from java.library.path");
		} finally {
			if(change) {
				System.setProperty("java.library.path", value);
			}
		}
		return loaded;
	}

	/**
	 * Loads all required native libraries for Troy Berry. Can be called as many times without harm
	 */
	public static void load() {
		// Empty to call static initializer
	}

}