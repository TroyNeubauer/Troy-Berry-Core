package com.troy.troyberry.utils.general;

import java.io.*;
import java.text.*;
import java.util.*;

import com.troy.troyberry.utils.*;

/** Represents a crash report in case that happens **/
public class CrashReport {

	/** Description of the crash report. */
	private final String description;

	/** Some information about the system. */
	private String info;

	/** The Throwable that is the "cause" for this crash and Crash Report. */
	private final Throwable cause;

	public CrashReport(String decs, Throwable throwable) {
		this.description = decs;
		this.cause = throwable;
		this.populateInfo();
	}

	/** Adds info such as Java version details and memory details **/
	private void populateInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("Java version " + System.getProperty("java.version") + ", " + System.getProperty("java.vendor") + "\n");

		sb.append("Operating system " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version "
				+ System.getProperty("os.version") + "\n");

		sb.append("Java VM Version " + System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), "
				+ System.getProperty("java.vm.vendor") + "\n");

		Runtime runtime = Runtime.getRuntime();
		long maxMemory = runtime.maxMemory();
		long totalMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		double maxMemoryMB = (double) maxMemory / 1024.0 / 1024.0 / 1024.0;
		long totalMemoryMB = totalMemory / 1024L / 1024L;
		long freeMemoryMB = freeMemory / 1024L / 1024L;

		sb.append("Memory " + freeMemory + " bytes (" + freeMemoryMB + " MB) / " + totalMemory + " bytes (" + totalMemoryMB + " MB) up to "
				+ maxMemory + " bytes (" + StringFormatter.clip(maxMemoryMB, 2) + " GB)");

		this.info = sb.toString();
	}

	/**
	 * @return The entire report containing time, Java version details, memory
	 *         details, a description, and the cause and stack trace
	 **/
	public String getCompleteReport() {
		StringBuilder sb = new StringBuilder();
		sb.append("---- " + VersionManager.getCurentVersion().getName() + " Crash Report ----\n\n");
		sb.append("Time: ");
		sb.append((new SimpleDateFormat()).format(new Date()) + "\n");
		sb.append("Game: " + VersionManager.getCurentVersion().getName() + "\nVersion: "
				+ VersionManager.getCurentVersion().getStringVersion() + "\n\n");
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
	 * @return The throwable responsible for the crash
	 **/
	public Throwable getCrashCause() {
		return this.cause;
	}

	/** prints the crash report to System.err **/
	public CrashReport print() {
		System.err.println(this.getCompleteReport());
		return this;
	}

}
