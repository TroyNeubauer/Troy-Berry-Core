package com.troyberry.util.serialization;

import com.troyberry.util.MiscUtil;

import sun.misc.Unsafe;

@SuppressWarnings("restriction")
public class SerializationUtil {
	private static final Unsafe unsafe = MiscUtil.getUnsafe();
	
	public static <T> T newInstance(Class<T> clazz) {
		if (MiscUtil.isUnsafeSupported()) {
			try {
				return (T) unsafe.allocateInstance(clazz);// Casting is ok since allocateInstance always returns an instance of clazz, or fails
			} catch (InstantiationException e) {
				return null;// If we cannot instantiate a new object using unsafe, we have no hope...

			}
		} else {// No unsafe... So use reflection
			try {
				return clazz.newInstance();
			} catch (Exception e) {

			}
			return null;//No hope for us...
		}
	}

}
