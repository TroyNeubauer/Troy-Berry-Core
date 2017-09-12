package com.troyberry.util.serialization;

import com.troyberry.util.MiscUtil;

/**
 * Implements serializers and de-serializers for pre-existing classes so that they can be serialized 
 * (Ie. java.util.ArrayList)
 * @author Troy Neubauer
 *
 * @param <T> The type to be serialized Ie. java.util.ArrayList (assuming the sub class serialized ArrayLists)
 */
public abstract class TroySerializer<T> {

	private final Class<T> type;

	public TroySerializer() {
		try {
			this.type = (Class<T>) MiscUtil.getGenericType(this);
		} catch (Throwable e) {
			throw new IllegalStateException("Unable to get generic type of subclass!!!");
		}
	}

	public Class<T> getType() {
		return type;
	}

	public abstract T read(TroyBuffer buffer, TroySerializationFile file, Class<T> type);

	public abstract void write(T obj, TroySerializationFile file, TroyBuffer buffer);

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " for " + type;
	}
}
