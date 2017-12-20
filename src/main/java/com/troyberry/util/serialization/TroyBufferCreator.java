package com.troyberry.util.serialization;

import java.io.File;
import java.nio.ByteBuffer;

import com.troyberry.util.MiscUtil;

public class TroyBufferCreator {
	
	public final static TroyBuffer create(long bytes) {
		if (MiscUtil.isUnsafeSupported())
			return new TroyBufferUnsafe(bytes);
		else
			return new TroyBufferStandard(bytes);
	}

	public final static TroyBuffer create(byte[] data) {
		if (MiscUtil.isUnsafeSupported()) {
			return TroyBufferUnsafe.createFromArray(data);
		}
		return null;
	}

	public final static TroyBuffer create(byte[] data, int length) {
		TroyBuffer result = create(length);
		for (int i = 0; i < length; i++) {
			result.writeByte(data[i]);
		}
		result.readPosition(0);
		result.writePosition(0);
		return result;
	}

	public final static TroyBuffer create(File file) {
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

	public static AbstractTroyBuffer create(AbstractTroyBuffer src, long offset, long length) {
		AbstractTroyBuffer result = null;
		if (MiscUtil.isUnsafeSupported()) {
			long bytes = length - offset;
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
		TroyBuffer result = new TroyBufferStandard(buffer.limit(), buffer.capacity());
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
		AbstractTroyBuffer result;
		if (MiscUtil.isUnsafeSupported()) {
			if (buffer.isDirect()) {
				result = new TroyBufferUnsafe(buffer.limit());
				NativeTroyBufferUtil.nmemcpy(result.address(), MiscUtil.address(buffer), buffer.limit());
				result.capacity = buffer.limit();
				result.limit = buffer.limit();
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
			result = new TroyBufferStandard(buffer.limit(), buffer.capacity());
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
