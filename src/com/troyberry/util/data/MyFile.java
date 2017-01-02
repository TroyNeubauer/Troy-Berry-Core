package com.troyberry.util.data;

import java.io.*;

public class MyFile {

	private static final String FILE_SEPARATOR = "/";
	private static final char CHAR_FILE_SEPARATOR = (FILE_SEPARATOR.length() > 0) ? FILE_SEPARATOR.charAt(0) : '/';

	private String path;
	private String name;

	public MyFile(String path) {
		if (path.length() > 0 && path.charAt(0) != CHAR_FILE_SEPARATOR) {
			this.path = FILE_SEPARATOR + path;
		}else{
			this.path = path;
		}
		String[] dirs = path.split(FILE_SEPARATOR);
		this.name = dirs[dirs.length - 1];
	}

	public MyFile(String... paths) {
		this.path = "";
		for (String part : paths) {
			this.path += (FILE_SEPARATOR + part);
		}
		String[] dirs = path.split(FILE_SEPARATOR);
		this.name = dirs[dirs.length - 1];
	}

	public MyFile(MyFile file, String subFile) {
		this.path = file.path + FILE_SEPARATOR + subFile;
		this.name = subFile;
	}

	public MyFile(MyFile file, String... subFiles) {
		this.path = file.path;
		for (String part : subFiles) {
			this.path += (FILE_SEPARATOR + part);
		}
		String[] dirs = path.split(FILE_SEPARATOR);
		this.name = dirs[dirs.length - 1];
	}

	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return getPath();
	}

	public InputStream getInputStream() {
		return Class.class.getResourceAsStream(path);
	}

	public BufferedReader getReader() throws Exception {
		try {
			InputStreamReader isr = new InputStreamReader(getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			return reader;
		} catch (Exception e) {
			throw new IOException("Unable to find file " + path);
		}
	}

	public String getName() {
		return name;
	}

}
