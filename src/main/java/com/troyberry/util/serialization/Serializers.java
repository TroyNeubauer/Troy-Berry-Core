package com.troyberry.util.serialization;

import java.net.*;
import java.util.Collection;

import com.troyberry.util.MiscUtil;
import com.troyberry.util.data.WriterUtils;

import sun.misc.Unsafe;

@SuppressWarnings({"rawtypes", "restriction"})
public class Serializers {

	private static final Unsafe unsafe = MiscUtil.getUnsafe();

	static {
		// Primitives
		TroySerializationBank.add(new TroySerializer<String>() {

			@Override
			public String read(TroyBuffer buffer, TroySerializationFile file, Class<String> type) {
				return buffer.readString();
			}

			@Override
			public void write(String obj, TroySerializationFile file, TroyBuffer buffer) {
				buffer.writeString(obj);
			}

		});
		TroySerializationBank.add(new TroySerializer<Byte>() {

			@Override
			public Byte read(TroyBuffer buffer, TroySerializationFile file, Class<Byte> type) {
				return buffer.readByte();
			}

			@Override
			public void write(Byte obj, TroySerializationFile file, TroyBuffer buffer) {
				buffer.writeByte(obj);
			}
		});
		TroySerializationBank.add(new TroySerializer<Short>() {

			@Override
			public Short read(TroyBuffer buffer, TroySerializationFile file, Class<Short> type) {
				return buffer.readShort();
			}

			@Override
			public void write(Short obj, TroySerializationFile file, TroyBuffer buffer) {
				buffer.writeShort(obj);
			}
		});
		TroySerializationBank.add(new TroySerializer<Character>() {

			@Override
			public Character read(TroyBuffer buffer, TroySerializationFile file, Class<Character> type) {
				return buffer.readChar();
			}

			@Override
			public void write(Character obj, TroySerializationFile file, TroyBuffer buffer) {
				buffer.writeChar(obj);
			}
		});
		TroySerializationBank.add(new TroySerializer<Integer>() {

			@Override
			public Integer read(TroyBuffer buffer, TroySerializationFile file, Class<Integer> type) {
				return buffer.readInt();
			}

			@Override
			public void write(Integer obj, TroySerializationFile file, TroyBuffer buffer) {
				buffer.writeInt(obj);
			}
		});
		TroySerializationBank.add(new TroySerializer<Long>() {

			@Override
			public Long read(TroyBuffer buffer, TroySerializationFile file, Class<Long> type) {
				return buffer.readLong();
			}

			@Override
			public void write(Long obj, TroySerializationFile file, TroyBuffer buffer) {
				buffer.writeLong(obj);
			}
		});
		TroySerializationBank.add(new TroySerializer<Float>() {

			@Override
			public Float read(TroyBuffer buffer, TroySerializationFile file, Class<Float> type) {
				return buffer.readFloat();
			}

			@Override
			public void write(Float obj, TroySerializationFile file, TroyBuffer buffer) {
				buffer.writeFloat(obj);
			}
		});
		TroySerializationBank.add(new TroySerializer<Double>() {

			@Override
			public Double read(TroyBuffer buffer, TroySerializationFile file, Class<Double> type) {
				return buffer.readDouble();
			}

			@Override
			public void write(Double obj, TroySerializationFile file, TroyBuffer buffer) {
				buffer.writeDouble(obj);
			}
		});
		TroySerializationBank.add(new TroySerializer<Boolean>() {

			@Override
			public Boolean read(TroyBuffer buffer, TroySerializationFile file, Class<Boolean> type) {
				return WriterUtils.byteToBoolean(buffer.readByte());
			}

			@Override
			public void write(Boolean obj, TroySerializationFile file, TroyBuffer buffer) {
				buffer.writeByte(WriterUtils.booleanToByte(obj));
			}
		});
		TroySerializationBank.add(new TroySerializer<java.net.URL>() {

			@Override
			public URL read(TroyBuffer buffer, TroySerializationFile file, Class<URL> type) {
				try {
					return new URL(buffer.readString());
				} catch (MalformedURLException e) {

				}
				return null;
			}

			@Override
			public void write(URL obj, TroySerializationFile file, TroyBuffer buffer) {
				buffer.writeString(obj.toExternalForm());
			}
		});
		TroySerializationBank.add(new TroySerializer<java.util.Collection>() {
			public Collection<?> read(TroyBuffer buffer, TroySerializationFile file, Class<Collection> type) {
				Collection result = newInstance(type);
				int size = buffer.readInt();
				for (int i = 0; i < size; i++) {
					result.add(file.read());
				}
				return result;
			}

			public void write(Collection obj, TroySerializationFile file, TroyBuffer buffer) {

				buffer.writeInt(obj.size());
				if (obj.size() > 0) {
					Object[] elements = obj.toArray();
					Class<?> genericClass = null;
					int index = 0;
					do {
						if (elements[index] != null)
							genericClass = elements[index].getClass();
						else
							index++;
					} while (genericClass == null);
					for (int i = 0; i < obj.size(); i++) {
						file.write(elements[i]);
					}
				}
			}
		});

		TroySerializationBank.add(new TroySerializer<byte[]>() {

			@Override
			public byte[] read(TroyBuffer buffer, TroySerializationFile file, Class<byte[]> type) {
				return buffer.readBytesSigned();
			}

			@Override
			public void write(byte[] obj, TroySerializationFile file, TroyBuffer buffer) {
				buffer.writeBytesSigned(obj);
			}
		});

		TroySerializationBank.add(new TroySerializer<short[]>() {

			@Override
			public short[] read(TroyBuffer buffer, TroySerializationFile file, Class<short[]> type) {
				// return buffer.readShortsSigned();
				return null;
			}

			@Override
			public void write(short[] obj, TroySerializationFile file, TroyBuffer buffer) {
				// buffer.writeShortsSigned(obj);
			}
		});

		TroySerializationBank.add(new TroySerializer<char[]>() {

			@Override
			public char[] read(TroyBuffer buffer, TroySerializationFile file, Class<char[]> type) {
				// return buffer.readString().toCharArray();
				return null;
			}

			@Override
			public void write(char[] obj, TroySerializationFile file, TroyBuffer buffer) {
				// buffer.writeString(new String(obj));
			}
		});

		TroySerializationBank.add(new TroySerializer<int[]>() {

			@Override
			public int[] read(TroyBuffer buffer, TroySerializationFile file, Class<int[]> type) {
				// return buffer.readIntsSigned();
				return null;
			}

			@Override
			public void write(int[] obj, TroySerializationFile file, TroyBuffer buffer) {
				// buffer.writeIntsSigned(obj);
			}
		});

		TroySerializationBank.add(new TroySerializer<long[]>() {

			@Override
			public long[] read(TroyBuffer buffer, TroySerializationFile file, Class<long[]> type) {
				// return buffer.readLongsSigned();
				return null;
			}

			@Override
			public void write(long[] obj, TroySerializationFile file, TroyBuffer buffer) {
				// buffer.writeLongsSigned(obj);
			}
		});

		TroySerializationBank.add(new TroySerializer<float[]>() {

			@Override
			public float[] read(TroyBuffer buffer, TroySerializationFile file, Class<float[]> type) {
				// return buffer.readFloatsSigned();
				return null;
			}

			@Override
			public void write(float[] obj, TroySerializationFile file, TroyBuffer buffer) {
				// buffer.writeFloatsSigned(obj);
			}
		});

		TroySerializationBank.add(new TroySerializer<double[]>() {

			@Override
			public double[] read(TroyBuffer buffer, TroySerializationFile file, Class<double[]> type) {
				// return buffer.readDoublesSigned();
				return null;
			}

			@Override
			public void write(double[] obj, TroySerializationFile file, TroyBuffer buffer) {
				// buffer.writeDoublesSigned(obj);
			}
		});

		TroySerializationBank.add(new TroySerializer<boolean[]>() {

			@Override
			public boolean[] read(TroyBuffer buffer, TroySerializationFile file, Class<boolean[]> type) {
				int length = buffer.readInt();
				boolean[] result = new boolean[length];
				for (int i = 0; i < length; i++) {
					result[i] = WriterUtils.byteToBoolean(buffer.readByte());
				}
				return result;
			}

			@Override
			public void write(boolean[] obj, TroySerializationFile file, TroyBuffer buffer) {
				buffer.writeInt(obj.length);
				for (int i = 0; i < obj.length; i++) {
					buffer.writeByte(WriterUtils.booleanToByte(obj[i]));
				}
			}
		});
	}

	private static <T> T newInstance(Class<T> type) {
		try {
			return (T) unsafe.allocateInstance(type);
		} catch (InstantiationException e) {
			System.err.println("Unable to create ");
			e.printStackTrace();
		}
		return null;
	}

	public static void init() {
		// Blank to call static initializer
	}

}
