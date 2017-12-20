package com.troyberry.util.serialization;

import java.util.Collection;

@SuppressWarnings("rawtypes")
public class Serializers {

	static {
/*
		TroySerializationBank.add(new ReturningTroySerializer<ArrayList>() {

			@Override
			public ArrayList read(Class<ArrayList> type, TroySerializationFile file, TroyBuffer buffer) {
				int length = buffer.readInt();
				ArrayList list = new ArrayList(length);
				for (int i = 0; i < length; i++) {
					list.add(file.read());
				}
				return list;
			}

			@Override
			public void write(ArrayList obj, TroySerializationFile file, TroyBuffer buffer) {
				int size = obj.size();
				buffer.writeInt(size);
				for (int i = 0; i < size; i++) {
					file.write(obj.get(i));
				}
			}

		});*/
		TroySerializationBank.add(new ReturningTroySerializer<Collection>() {

			@Override
			public Collection read(Class<Collection> type, TroySerializationFile file, TroyBuffer buffer) {
				Collection result = SerializationUtil.newInstance(type);
				int length = buffer.readInt();
				for(int i = 0; i < length; i++) {
					result.add(file.read());
				}
				return result;
			}

			@Override
			public void write(Collection obj, TroySerializationFile file, TroyBuffer buffer) {
				buffer.writeInt(obj.size());
				for(Object o : obj) {
					file.write(o);
				}
			}
		});
		TroySerializationBank.add(new ReturningTroySerializer<String>() {

			@Override
			public String read(Class<String> type, TroySerializationFile file, TroyBuffer buffer) {
				return buffer.readString();
			}

			@Override
			public void write(String obj, TroySerializationFile troySerializationFile, TroyBuffer buffer) {
				buffer.writeString(obj);
			}
		});
		TroySerializationBank.add(new ReturningTroySerializer<Byte>() {

			@Override
			public Byte read(Class<Byte> type, TroySerializationFile file, TroyBuffer buffer) {

				return Byte.valueOf(buffer.readByte());
			}

			@Override
			public void write(Byte obj, TroySerializationFile troySerializationFile, TroyBuffer buffer) {
				buffer.writeByte(obj.byteValue());
			}

		});
		TroySerializationBank.add(new ReturningTroySerializer<Short>() {

			@Override
			public Short read(Class<Short> type, TroySerializationFile file, TroyBuffer buffer) {

				return Short.valueOf(buffer.readShort());
			}

			@Override
			public void write(Short obj, TroySerializationFile troySerializationFile, TroyBuffer buffer) {
				buffer.writeShort(obj.shortValue());
			}

		});
		TroySerializationBank.add(new ReturningTroySerializer<Character>() {

			@Override
			public Character read(Class<Character> type, TroySerializationFile file, TroyBuffer buffer) {

				return Character.valueOf(buffer.readChar());
			}

			@Override
			public void write(Character obj, TroySerializationFile troySerializationFile, TroyBuffer buffer) {
				buffer.writeChar(obj.charValue());
			}

		});
		TroySerializationBank.add(new ReturningTroySerializer<Integer>() {

			@Override
			public Integer read(Class<Integer> type, TroySerializationFile file, TroyBuffer buffer) {

				return Integer.valueOf(buffer.readInt());
			}

			@Override
			public void write(Integer obj, TroySerializationFile troySerializationFile, TroyBuffer buffer) {
				buffer.writeInt(obj.intValue());
			}

		});
		TroySerializationBank.add(new ReturningTroySerializer<Long>() {

			@Override
			public Long read(Class<Long> type, TroySerializationFile file, TroyBuffer buffer) {

				return Long.valueOf(buffer.readLong());
			}

			@Override
			public void write(Long obj, TroySerializationFile troySerializationFile, TroyBuffer buffer) {
				buffer.writeLong(obj.longValue());
			}

		});
		TroySerializationBank.add(new ReturningTroySerializer<Float>() {

			@Override
			public Float read(Class<Float> type, TroySerializationFile file, TroyBuffer buffer) {

				return Float.valueOf(buffer.readFloat());
			}

			@Override
			public void write(Float obj, TroySerializationFile troySerializationFile, TroyBuffer buffer) {
				buffer.writeFloat(obj.floatValue());
			}

		});
		TroySerializationBank.add(new ReturningTroySerializer<Double>() {

			@Override
			public Double read(Class<Double> type, TroySerializationFile file, TroyBuffer buffer) {

				return Double.valueOf(buffer.readDouble());
			}

			@Override
			public void write(Double obj, TroySerializationFile troySerializationFile, TroyBuffer buffer) {
				buffer.writeDouble(obj.doubleValue());
			}

		});
		TroySerializationBank.add(new ReturningTroySerializer<Boolean>() {

			@Override
			public Boolean read(Class<Boolean> type, TroySerializationFile file, TroyBuffer buffer) {

				return buffer.readByte() == 0 ? Boolean.FALSE : Boolean.TRUE;
			}

			@Override
			public void write(Boolean obj, TroySerializationFile troySerializationFile, TroyBuffer buffer) {
				buffer.writeByte((byte) (obj.booleanValue() ? 1 : 0));
			}
		});
		TroySerializationBank.add(new ReturningTroySerializer<Object[]>() {

			@Override
			public Object[] read(Class<Object[]> type, TroySerializationFile file, TroyBuffer buffer) {
				int length = buffer.readInt();
				Object[] result = new Object[length];
				for (int i = 0; i < length; i++) {
					result[i] = file.read();
				}
				return result;
			}

			@Override
			public void write(Object[] obj, TroySerializationFile file, TroyBuffer buffer) {
				int length = obj.length;
				for (int i = 0; i < length; i++) {
					file.write(obj[i]);
				}
			}
		});
	}

	public static void init() {
		// Blank to call static initializer
	}

}
