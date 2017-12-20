package com.troyberry.file.filemanager;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import com.troyberry.internal.InternalLog;
import com.troyberry.util.*;
import com.troyberry.util.serialization.*;

@SuppressWarnings("rawtypes")
public abstract class FileProvider<IOType extends IOBase, FileType extends FileBase> {

	private static final Comparator<VersionRange> comparator = new Comparator<VersionRange>() {

		@Override
		public int compare(VersionRange o1, VersionRange o2) {
			return -o1.compareTo(o2);
		}

	};

	private final Map<VersionRange, IOType> versions = new TreeMap<VersionRange, IOType>(comparator);

	public FileProvider() {
	}

	public void add(VersionRange version, IOType type) {
		versions.put(version, type);
	}

	public void write(File dest, FileType file, byte major, byte minor, byte patch) throws IOException {
		TroyBuffer buffer = TroyBufferCreator.create();
		IOType io = getIO(major, minor, patch);
		if (io == null)
			return;
		io.write(buffer, file);
		buffer.writeToFile(dest);
	}

	public void write(File dest, FileType file) throws IOException {
		TroyBuffer buffer = TroyBufferCreator.create();
		getMostRecentIO().write(buffer, file);
		buffer.writeToFile(dest);
	}

	public FileType read(byte[] file) throws InvalidFileException {
		if (!isValidFile(file))
			throw new InvalidFileException("Invalid file!");
		Version version = getVersion(file);
		return (FileType) getIO(version).read(TroyBufferCreator.create(file));
	}

	public FileType read(File file) throws IOException, InvalidFileException {
		return read(MiscUtil.readToByteArray(file));
	}

	public FileType read(MyFile file) throws IOException, InvalidFileException {
		return read(MiscUtil.readToByteArray(file));
	}

	public FileType read(InputStream stream) throws IOException, InvalidFileException {
		return read(MiscUtil.readToByteArray(stream));
	}

	public IOType getIO(Version version) {
		return getIO(version.getMajor(), version.getMinor(), version.getPatch());
	}

	public IOType getIO(byte major, byte minor, byte patch) {
		Iterator<Entry<VersionRange, IOType>> i = versions.entrySet().iterator();
		while (i.hasNext()) {
			Entry<VersionRange, IOType> e = i.next();
			if (e.getKey().contains(major, minor, patch))
				return e.getValue();

		}

		Entry<VersionRange, IOType> mostRecentEntry = getMostRecentEntry();

		if (onIONotAvilable(major, minor, patch, mostRecentEntry.getKey(), mostRecentEntry.getValue()))
			return null;
		return mostRecentEntry.getValue();
	}

	public IOType getMostRecentIO() {
		Iterator<Entry<VersionRange, IOType>> i = versions.entrySet().iterator();

		IOType mostRecentVersion = null;
		while (i.hasNext()) {
			Entry<VersionRange, IOType> e = i.next();
			mostRecentVersion = e.getValue();
			break;
		}
		return mostRecentVersion;
	}

	public Entry<VersionRange, IOType> getMostRecentEntry() {
		Iterator<Entry<VersionRange, IOType>> i = versions.entrySet().iterator();

		Entry<VersionRange, IOType> mostRecentEntry = null;
		while (i.hasNext()) {
			Entry<VersionRange, IOType> e = i.next();
			mostRecentEntry = e;
			break;
		}
		return mostRecentEntry;
	}

	/**
	 * Returns the IOType object that matches the version of the argument model if it exists. Otherwise, the most recent IOType object is
	 * returned
	 * 
	 * @param file
	 *            The file type to use to get the matching version
	 * @return The matching version for file type, or the most recent one if none is found
	 */
	public IOType getIO(FileType file) {
		Iterator<Entry<VersionRange, IOType>> i = versions.entrySet().iterator();
		while (i.hasNext()) {
			Entry<VersionRange, IOType> e = i.next();
			if (file.getClass().equals(e.getValue().getFileClass()))
				return e.getValue();
		}

		i = versions.entrySet().iterator();
		InternalLog.println("ERROR: No TBMO IO found for the TBMO type " + file.getClass());
		InternalLog.println("Avilable versions are:");
		Entry<VersionRange, IOType> mostRecentEntry = getMostRecentEntry();

		String version = file.getClass().getSimpleName();
		version = version.substring(version.length() - 3);// Grab the last 3 chars IE "TestFile100" -> "100"

		try {
			byte major = Byte.parseByte(version.substring(0, 1));
			byte minor = Byte.parseByte(version.substring(1, 2));
			byte patch = Byte.parseByte(version.substring(2, 3));
			onIONotAvilable(major, minor, patch, mostRecentEntry.getKey(), mostRecentEntry.getValue());
		} catch (Exception e) {
			System.err.println("Class " + file.getClass().getName()
					+ " doesn't follow the naming conventition of the version at the end of the class name!");
		}
		return mostRecentEntry.getValue();
	}

	/**
	 * Called when a IO object is attempted to be retrieved, but the requested version is unavailable.
	 * 
	 * @param requestedMajor
	 *            The major component of the requested version
	 * @param requestedMinor
	 *            The minor component of the requested version
	 * @param requestedPatch
	 *            The patch component of the requested version
	 * @param rangeToUse
	 *            The {@code VersionRange} of the most recent version
	 * @param IOToUse
	 *            The {@code FileIO} most recent reader and writer that will be used if this method returns false
	 * @return A flag indicating if the write should be called off or not. {@code true} indicates to cancel the write. {@code false} indicates
	 *         that the write can continue with the most recent IO object / writer
	 */
	public abstract boolean onIONotAvilable(byte requestedMajor, byte requestedMinor, byte requestedPatch, VersionRange rangeToUse,
			IOType IOToUse);

	public abstract boolean isValidFile(byte[] file);

	public abstract Version getVersion(byte[] file);

}
