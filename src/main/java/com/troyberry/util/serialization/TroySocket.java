package com.troyberry.util.serialization;

import java.io.*;
import java.net.*;

import com.troyberry.internal.LibraryUtils;
import com.troyberry.util.*;

import sun.misc.*;

@SuppressWarnings("restriction")
public class TroySocket implements java.io.Closeable {

	private static final long DEFALUE_BUFFER_SIZE = 2048;

	private static Unsafe unsafe = MiscUtil.getUnsafe();

	/**
	 * Indicates that this is a client socket, or unbound server socket
	 */
	private int port = -1;

	private long fd;

	private TroyBufferUnsafe recieveBuffer;

	static {
		LibraryUtils.load();
		init();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			destroy();
		}));
	}

	public TroySocket() {
		this(DEFALUE_BUFFER_SIZE);
	}

	public TroySocket(long bufferSize) {
		this.recieveBuffer = (TroyBufferUnsafe) TroyBuffer.create(bufferSize);
		createSocket();
	}

	public TroySocket(int port) {
		this(port, DEFALUE_BUFFER_SIZE);
	}

	public TroySocket(int port, long bufferSize) {
		this.recieveBuffer = (TroyBufferUnsafe) TroyBuffer.create(bufferSize);
		createSocket();
		bind(port);
	}

	private void createSocket() {
		long result = socket();
		if (result < 0xFFFFFFFFL) {
			this.fd = (int) (result & 0xFFFFFFFF);
		} else {
			throw new RuntimeException("Unable to create socket!!! Error " + getError(result));
		}
	}

	public void bind(int port) {
		this.port = port;
		long result = bind(fd, port);
		int size = (int)(result & 0xFFFFFFFF);
		if (size == -1) {
			throw new RuntimeException("Unable to bind! Error code: " + getError(result));
		}
	}

	public TroyBuffer recieveRaw() {
		long result = recieve(fd, recieveBuffer.address(), recieveBuffer.capacity());
		System.out.println("result " + StringFormatter.toHexString(result));
		int size = (int)(result & 0xFFFFFFFF);
		if (size == -1) {
			throw new RuntimeException("Unable to recivec! Error code: " + getError(result));
		}
		recieveBuffer.size = size;
		System.out.println("done recieve!");
		return recieveBuffer;
	}
	
	public void send(TroyBuffer buffer, InetAddress address, int port) {
		byte[] addresses = address.getAddress();
		if(address.getClass() != Inet4Address.class || addresses.length != 4) throw new RuntimeException("Troy Socket does not support class " + address.getClass() + " for addressing");
		int finishedAddress = addresses[0] << 24 | addresses[1] << 16 | addresses[2] << 8 | addresses[3];
		send(fd, ((TroyBufferUnsafe)buffer).address, buffer.size, finishedAddress, port);
	}

	@Override
	public void close() throws IOException {
		closesocket(fd);
		recieveBuffer.free();
	}

	private int getError(long result) {
		return (int) (result >>> 32);
	}

	// Native methods

	/**
	 * Initializes the native socket library
	 * @return The return value from the C-call to WSAStartup();
	 */
	private static native void init();

	/**
	 * Shuts down the native socket library
	 * @return The return value from the C-call to WSACleanup();
	 */
	private static native void destroy();

	private static native long socket();

	private static native long bind(long fd, int port);

	private static native long recieve(long fd, long bufferAddress, long bufferCapacity);// Returns the length of the received packet

	private static native void send(long fd, long bufferAddress, long bufferCapacity, int destAddress, int destPort);
	
	private static native long closesocket(long fd);
}
