package com.troyberry.internal;

import java.io.*;

public class ErrorLoggingUtils {
	
	private static boolean error = false;
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){ public void run() {
            if (error) {
                File file = new File(System.getProperty("user.dir") + File.separatorChar + "Troy Berry Error Log.txt");
                try {
                    FileWriter writer = new FileWriter(file);
                    writer.write(InternalLog.getLog());
                    writer.close();
                } catch (Throwable t) {
                    return;
                }
                System.err.println("[Troy Berry] A fatal error was detected by Troy Berry durring the executition of this program.");
                System.err.println("[Troy Berry] A log file containing the internal log has been saved to: " + file);
            }
        }}));
	}
	
	public static void indicateError() {
		error = true;
	}

}
