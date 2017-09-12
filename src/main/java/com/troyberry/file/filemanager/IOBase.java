package com.troyberry.file.filemanager;

import java.io.IOException;

import com.troyberry.util.serialization.TroyBuffer;

public interface IOBase<FileType extends FileBase> {

	/**
	 * Returns the class object for the {@code FileBase} that represents this object
	 * @return The class object
	 */
	public Class<? extends FileType> getFileClass();

	/**
	 * Creates a {@code FileBase} object from an array of bytes
	 * @param bytes the array of bytes containing the file
	 * @return The 
	 * @throws InvalidFileException If the file is invalid or corrupt
	 */
	public FileType read(TroyBuffer buffer) throws InvalidFileException;

	/**
	 * Writes a {@code FileBase} object to the desired output stream
	 * @param stream The stream to write to
	 * @param data The data to write
	 * @param closeStream Weather or not the caller wants the stream to be closed at the end. If true, please close the stream
	 * @throws IOException If an I/O error occurs wile writing
	 */
	public void write(TroyBuffer buffer, FileType data) throws IOException;
	
	public void writeHeader(TroyBuffer writer, int major, int minor, int patch);

}
