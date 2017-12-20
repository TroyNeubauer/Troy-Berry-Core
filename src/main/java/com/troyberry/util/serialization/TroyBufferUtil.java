package com.troyberry.util.serialization;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;

public class TroyBufferUtil {

	private static final int BUFFER_SIZE = 4096;

	protected static final ThreadLocal<byte[]> BUFFERS = new ThreadLocal<byte[]>() {
		protected byte[] initialValue() {
			return new byte[BUFFER_SIZE];
		}
	};

	public static void writeZipFile(TroyBuffer buffer, ZipFile file) throws IOException {
		long lengthIndex = buffer.writePosition();
		buffer.writeLong(0);
		long startIndex = buffer.writePosition();
		//ZipOutputStream stream = new ZipOutputStream(new BufferOutputStream(buffer));
		ZipOutputStream stream = new ZipOutputStream(new FileOutputStream(new File("testtesttest.zip")));
		Enumeration<? extends ZipEntry> e = file.entries();
		while (e.hasMoreElements()) {
			ZipEntry entry = e.nextElement();
			try {
				stream.putNextEntry(entry);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		stream.finish();
		stream.close();
		buffer.writeLong(lengthIndex, buffer.writePosition() - startIndex);
	}

	public static ZipFile readZipFile(TroyBuffer buffer) throws IOException {
		long length = buffer.readLong();
		File file = File.createTempFile("TempTroyBerryZipFile", Long.toString(System.nanoTime()));
		buffer.writeToFile(file);
		buffer.skipReader(length);
		return new ZipFile(file);
	}

	public static int writeToBuffer(AbstractTroyBuffer buffer, InputStream stream) throws IOException {
		int readTotal = 0, read;
		byte[] bytes = BUFFERS.get();
		while (stream.available() > 0) {
			read = stream.read(bytes);
			if (read == -1)
				break;
			buffer.writeBytesRaw(0, read, bytes);
			readTotal += read;
		}
		return readTotal;
	}

	public static int feedToOuputStream(AbstractTroyBuffer buffer, OutputStream stream) throws IOException {
		int written = 0;
		byte[] bytes = BUFFERS.get();
		for (long i = 0; i <= buffer.limit() / BUFFER_SIZE; i++) {
			long index = i * BUFFER_SIZE;
			int length = index + BUFFER_SIZE > buffer.limit() ? (int) (buffer.limit() % BUFFER_SIZE) : BUFFER_SIZE;
			buffer.readBytes(index, bytes, 0, length);
			stream.write(bytes, 0, length);
		}
		return written;
	}

	private TroyBufferUtil() {
	}

}
