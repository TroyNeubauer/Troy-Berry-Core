package com.troyberry.util;

import java.nio.*;

import com.troyberry.math.*;

public class TroyBufferUtils {
	public static ByteBuffer createByteBuffer(byte[] array) {
		ByteBuffer result = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
		result.put(array).flip();
		return result;
	}

	public static FloatBuffer createFloatBuffer(float[] array) {
		FloatBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
		result.put(array).flip();
		return result;
	}
	
	public static FloatBuffer createFloatBuffer(double[] array) {
		FloatBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
		float[] floats = new float[array.length];
		for(int i = 0; i < array.length; i++) {
			floats[i] = Maths.toFloat(array[i]);
		}
		result.put(floats).flip();
		return result;
	}

	public static IntBuffer createIntBuffer(int[] array) {
		IntBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
		result.put(array).flip();
		return result;
	}

	public static DoubleBuffer createDoubleBuffer(double[] array) {
		DoubleBuffer result = ByteBuffer.allocateDirect(array.length << 3).order(ByteOrder.nativeOrder()).asDoubleBuffer();
		result.put(array).flip();
		return result;
	}
}
