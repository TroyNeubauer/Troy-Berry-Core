package com.troyberry.util.serialization;

public class ByteClassLoader extends ClassLoader {

	public Class<?> defineClass(String name, byte[] bytes) {
		
		return defineClass(name, bytes, 0, bytes.length);
	}

}
