package com.troyberry.util.serialization;

import java.util.*;
import com.troyberry.util.MiscUtil;

@SuppressWarnings("rawtypes")
public class TroySerializationBank {
	private static final ArrayList<TroySerializer> serializers;
	private static final HashMap<Class, TroySerializer> map;
	static {
		serializers = new ArrayList<TroySerializer>();
		map = new HashMap<Class, TroySerializer>();
		Serializers.init();
	}

	public static void add(TroySerializer<?> serializer) {
		serializers.add(serializer);
	}

	static <T> TroySerializer<T> lookUp(Class<T> clazz) {
		TroySerializer<T> fromMap = map.get(clazz);
		if (fromMap != null)
			return fromMap;
		for (TroySerializer ser : serializers) {
			if (MiscUtil.classSharesSuperClassOrInterface(clazz, ser.getType())) {
				map.put(clazz, ser);
				return ser;
			}
		}
		return null;
	}
}