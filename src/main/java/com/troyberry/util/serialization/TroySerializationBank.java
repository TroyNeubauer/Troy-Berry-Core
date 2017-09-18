package com.troyberry.util.serialization;

import java.util.*;

public class TroySerializationBank {
	private static final ArrayList<TroySerializer<?>> serializers;
	private static final HashMap<Class<?>, TroySerializer<?>> map;
	static {
		serializers = new ArrayList<TroySerializer<?>>();
		map = new HashMap<Class<?>, TroySerializer<?>>();
		Serializers.init();
	}

	public static void add(TroySerializer<?> serializer) {
		serializers.add(serializer);
	}

	static <T> TroySerializer<T> lookUp(Class<T> clazz) {
		TroySerializer<T> fromMap = (TroySerializer<T>) map.get(clazz);
		if (fromMap != null)
			return fromMap;
		for (TroySerializer<?> ser : serializers) {
			if (clazz == ser.getType() ||clazz.isInstance(ser.getType())) {
				map.put(clazz, ser);
				return (TroySerializer<T>) ser;
			}
		}
		return genDynamicSerializer(clazz);
	}

	private static <T> TroySerializer<T> genDynamicSerializer(Class<T> clazz) {

		return null;
	}
}