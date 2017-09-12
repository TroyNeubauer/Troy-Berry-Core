package com.troyberry.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Represents a crash report in case that happens **/
public class CrashReport {

	/** Description of the crash report. */
	private final String description;

	/** File of crash report. */
	private File crashReportFile;

	/** Some information about the system. */
	private String info;

	/** The Throwable that is the "cause" for this crash and Crash Report. */
	private final Throwable cause;

	public CrashReport(String description, Throwable throwable) {
		this.description = description;
		this.cause = throwable;
		this.populateInfo();
	}

	/** Adds info such as Java version details and memory details **/
	private void populateInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("Java version: " + System.getProperty("java.version") + ", " + System.getProperty("java.vendor") + "\n");

		sb.append("Operating system: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version "
				+ System.getProperty("os.version") + "\n");

		sb.append("Java VM Version: " + System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), "
				+ System.getProperty("java.vm.vendor") + "\n");

		Runtime runtime = Runtime.getRuntime();
		long maxMemory = runtime.maxMemory();
		long totalMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();

		sb.append("Free Memory: " + freeMemory + " bytes (" + StringFormatter.formatBytesShort(freeMemory) + ") / Total Memory: " + totalMemory
				+ " bytes (" + StringFormatter.formatBytesShort(totalMemory) + ") Max Memory: " + maxMemory + " bytes ("
				+ StringFormatter.formatBytesShort(maxMemory) + ")");

		this.info = sb.toString();
	}

	/**
	 * @return The entire report containing time, Java version details, memory details, a description, as well as the cause and stack trace
	 **/
	public String getCompleteReport() {
		StringBuilder sb = new StringBuilder();
		sb.append("---- " + VersionManager.getCurentVersion().getName() + " Crash Report ----\n\n");
		sb.append("Time: ");
		sb.append((new SimpleDateFormat()).format(new Date()) + "\n");
		sb.append("\nVersion: " + VersionManager.getCurentVersion().getStringVersion() + "\n\n");
		sb.append("System Info:\n");
		sb.append(info + "\n\n");
		sb.append("Description: ");
		sb.append(this.description);
		sb.append("\n");
		sb.append(this.getCauseStackTraceOrString());
		return sb.toString();
	}

	/**
	 * @return The Cause or stack trace
	 **/
	public String getCauseStackTraceOrString() {
		StringWriter stringwriter = null;
		PrintWriter printwriter = null;
		Throwable throwable = this.cause;

		if (throwable.getMessage() == null) {
			if (throwable instanceof NullPointerException) {
				throwable = new NullPointerException(this.description);
			} else if (throwable instanceof StackOverflowError) {
				throwable = new StackOverflowError(this.description);
			} else if (throwable instanceof OutOfMemoryError) {
				throwable = new OutOfMemoryError(this.description);
			}

			throwable.setStackTrace(this.cause.getStackTrace());
		}

		// In case the printStackTrace fails, at least well have some information
		String s = throwable.toString();

		try {
			stringwriter = new StringWriter();
			printwriter = new PrintWriter(stringwriter);
			throwable.printStackTrace(printwriter);
			s = stringwriter.toString();
		} finally {
			try {
				stringwriter.close();
				printwriter.close();
			} catch (IOException ignore) {
			}
		}

		return s;
	}

	/**
	 * Gets the file this crash report is saved into. <br>
	 * This will return null until {@link CrashReport#saveToFile(File)} is called. After a call to saveToFile, this method will return the file
	 * it was saved to or null if an error occurred while saving
	 */
	public File getFile() {
		return this.crashReportFile;
	}

	/**
	 * @return true if and only if this crash report has been successfully saved to a file using {@link CrashReport#saveToFile(File)}
	 */
	public boolean hasBeenSaved() {
		return this.crashReportFile != null;
	}

	/**
	 * Saves this CrashReport to the given file and returns a value indicating whether or not saving was successful.<br>
	 * This method will return false immediately if the this crash report has already been saved. If this crash report has not been saved yet,
	 * the method will continue as such:
	 * <p>
	 * <ul>
	 * <li>Creating all the nonexistent parent directories for {@link file}
	 * <li>Deleting file if it exists and creating a new blank file
	 * <li>Writing the contents of this crash report to the file
	 * </ul>
	 * <p>
	 * 
	 * @returns True if and only if this crash report hasen't been saved to a file previously, and the saving operation completed without any
	 *          I/IO errors
	 * @throws IOException
	 *             If saving to the file fails
	 */
	public boolean saveToFile(File file) throws IOException {
		if (this.crashReportFile != null) {
			return false;
		} else {
			try {
				if (file.getParentFile() != null) {
					file.getParentFile().mkdirs();
				}
				if (file.exists())
					file.delete();

				file.createNewFile();

				FileWriter filewriter = new FileWriter(file);
				filewriter.write(this.getCompleteReport());
				filewriter.close();
				this.crashReportFile = file;
				return true;
			} catch (Throwable throwable) {
				throw new IOException(throwable);
			}
		}
	}

	/**
	 * @return The throwable responsible for the crash
	 **/
	public Throwable getCrashCause() {
		return this.cause;
	}

	/** Prints the crash report to System.err **/
	public CrashReport print() {
		System.err.println(this.getCompleteReport());
		return this;
	}

	/**
	 * Throws a Throwable created with all the data associated with this crash report.<br>
	 * In the format of:<br>
	 * <code>new Throwable("Crash Report Thrown!\n" + {@link CrashReport#getCompleteReport()} + "\nOrigionally thrown from:")</code>
	 */
	public void throwAsError() throws Throwable {
		throw new Throwable("Crash Report Thrown!\n" + getCompleteReport() + "\nOrigionally thrown from:");
	}

}
