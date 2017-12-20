package com.troyberry.util.serialization;

import java.io.*;
import java.nio.ByteBuffer;

import com.troyberry.util.MiscUtil;

public class TroyBufferCreator {

	private static final boolean USE_FAST = true;

	public final static TroyBuffer create(long bytes) {
		if (USE_FAST)
			return new TroyBufferFast(bytes);
		if (MiscUtil.isUnsafeSupported())
			return new TroyBufferUnsafe(bytes);
		return null;
	}

	public final static TroyBuffer create(byte[] data) {
		if (USE_FAST)
			return new TroyBufferFast(data);

		if (MiscUtil.isUnsafeSupported())
			return TroyBufferUnsafe.createFromArray(data);

		return null;
	}

	public final static TroyBuffer create(byte[] data, long length) {
		TroyBuffer result = create(length);
		result.setFromArray(data);
		return result;
	}

	public final static TroyBuffer create(File file) throws IOException {
		if (USE_FAST) {
			return new TroyBufferFast(MiscUtil.readToByteArray(file));
		}
		if (MiscUtil.isUnsafeSupported())
			return TroyBufferUnsafe.createFromFile(file);
		return null;
	}

	public final static AbstractTroyBuffer create(AbstractTroyBuffer buffer) {
		return create(buffer, buffer.capacity);
	}

	public final static AbstractTroyBuffer create(AbstractTroyBuffer buffer, long capacity) {
		if (MiscUtil.isUnsafeSupported()) {
			if (buffer instanceof TroyBufferUnsafe) {
				TroyBufferUnsafe result = new TroyBufferUnsafe(capacity);
				NativeTroyBufferUtil.nmemcpy(result.address, ((TroyBufferUnsafe) buffer).address, buffer.limit);
				result.limit = buffer.limit;
				result.positionRead = buffer.positionRead;
				result.positionWrite = buffer.positionWrite;
				return result;
			}
		}
		return null;
	}

	public static TroyBuffer create(TroyBuffer src, long offset, long length) {
		TroyBuffer result = null;
		long bytes = length - offset;
		if (USE_FAST) {
			result = new TroyBufferFast();
			if(src instanceof TroyBufferFast) {
				result.setFromArray(((TroyBufferFast)src).buffer);
			} else {
				for(long i = offset; i < length + offset; i++) {
					result.writeByte(i - offset, src.readByte(i));
				}
			}
		} else if (MiscUtil.isUnsafeSupported()) {
			result = new TroyBufferUnsafe(bytes, bytes);
			if (src instanceof TroyBufferUnsafe) {
				NativeTroyBufferUtil.nmemcpy(result.address(), src.address() + offset, bytes);
			} else {
				assert length <= (long) Integer.MAX_VALUE;
				result.setFromArray(src.getBytes(offset, (int) length));
			}
		}
		return result;
	}

	/**
	 * Copies the data from the desired ByteBuffer to a new TroyBuffer that doesn't use sun.misc.Unsafe and native memory. <br>
	 * All data from the index zero to the limit of the specified ByteBuffer will be copied
	 * 
	 * @param buffer
	 *            The buffer to copy
	 * @return A new TroyBuffer representing a copy of the desired ByteBuffer
	 */
	public static TroyBuffer createSafe(ByteBuffer buffer) {
		TroyBuffer result = new TroyBufferFast(buffer.limit(), buffer.capacity());
		if (buffer.hasArray()) {
			result.setFromArray(buffer.array());
		} else {
			for (int i = 0; i < buffer.limit(); i++) {
				result.writeByte(i, buffer.get(i));
			}
		}
		return result;
	}

	/**
	 * Copies the data from the desired ByteBuffer to a new TroyBuffer. <br>
	 * All data from the index zero to the limit of the specified ByteBuffer will be copied
	 * 
	 * @implSpec the data will be copied using the most efficient method available, for example if the ByteBuffer is direct and the system supports
	 *           unsafe, the memory will be copied directly.
	 * @param buffer
	 *            The buffer to copy
	 * @return A new TroyBuffer representing a copy of the desired ByteBuffer
	 */
	public static TroyBuffer create(ByteBuffer buffer) {
		TroyBuffer result;
		if (MiscUtil.isUnsafeSupported()) {
			if (buffer.isDirect()) {
				result = new TroyBufferUnsafe(buffer.capacity());
				NativeTroyBufferUtil.nmemcpy(result.address(), MiscUtil.address(buffer), buffer.limit());
			} else {
				result = new TroyBufferUnsafe(0);
				if (buffer.hasArray()) {
					result.setFromArray(buffer.array());
				} else {
					for (int i = 0; i < buffer.limit(); i++) {
						result.writeByte(i, buffer.get(i));
					}
				}
			}

		} else {
			result = new TroyBufferFast(buffer.limit(), buffer.capacity());
			if (buffer.hasArray()) {
				result.setFromArray(buffer.array());
			} else {
				for (int i = 0; i < buffer.limit(); i++) {
					result.writeByte(i, buffer.get(i));
				}
			}
		}
		return result;
	}

	public final static TroyBuffer create() {
		return create(TroyBuffer.DEFAULT_SIZE);
	}

}
