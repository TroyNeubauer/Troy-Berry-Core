package com.troyberry.util.serialization;

/**
 * Implements serializers and de-serializers for pre-existing classes so that they can be serialized 
 * (Ie. java.util.ArrayList)
 * @author Troy Neubauer
 *
 * @param <T> The type to be serialized Ie. java.util.ArrayList (assuming the sub class serialized ArrayLists)
 */
public abstract class TroySerializer<T> extends BaseSerializer<T> {

	
	public TroySerializer() {
		super();
	}

	public TroySerializer(Class<T> type) {
		super(type);
	}

	public abstract void read(T obj, TroySerializationFile file, TroyBuffer buffer);

	public abstract void write(T obj, TroySerializationFile file, TroyBuffer buffer);

}
