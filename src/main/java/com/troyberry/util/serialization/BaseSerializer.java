package com.troyberry.util.serialization;

import com.troyberry.util.MiscUtil;

public abstract class BaseSerializer<T> {

	private final Class<T> type;
	
	public BaseSerializer(Class<T> type) {
		this.type = type;
	}

	public BaseSerializer() {
		try {
			this.type = (Class<T>) MiscUtil.getGenericType(this);
		} catch (Throwable e) {
			throw new IllegalStateException("Unable to get generic type of subclass!!!");
		}
	}
	
	public Class<T> getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " for " + type;
	}

	public abstract void write(T obj, TroySerializationFile file, TroyBuffer buffer);
	
}
