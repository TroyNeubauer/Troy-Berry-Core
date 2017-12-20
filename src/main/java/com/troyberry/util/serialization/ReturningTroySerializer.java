package com.troyberry.util.serialization;

public abstract class ReturningTroySerializer<T> extends BaseSerializer<T> {

	public ReturningTroySerializer() {
		super();
	}

	public ReturningTroySerializer(Class<T> type) {
		super(type);
	}
	
	public abstract T read(Class<T> type, TroySerializationFile file, TroyBuffer buffer);
	
}
