package com.troyberry.util.serialization;

import java.io.*;
import java.util.HashMap;

@SuppressWarnings("rawtypes")
public class TroySerializationFile {

	private HashMap<Integer, Class> classesInt;
	private HashMap<Class, Integer> classesClass;
	private TroyBuffer objectBuffer, header;
	private int classCount = 1;// start at 1 because 0 is reserved for null references

	public TroySerializationFile() {
		init();
		this.objectBuffer = TroyBufferCreator.create();
		this.header = TroyBufferCreator.create();
		header.writeUnsignedShort(0);// The number of entries is 0
	}

	private void init() {
		this.classesInt = new HashMap<Integer, Class>();
		this.classesClass = new HashMap<Class, Integer>();
	}

	public TroySerializationFile(File file) throws IOException {
		init();
		TroyBuffer entireFile = TroyBufferCreator.create(file);
		int entriesCount = entireFile.readUnsignedShort();
		for (int i = 0; i < entriesCount; i++) {
			String className = entireFile.readString();
			int classId = entireFile.readUnsignedShort();
			try {
				Class clazz = Class.forName(className);
				classesInt.put(classId, clazz);
				classesClass.put(clazz, classId);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		header = TroyBufferCreator.create(entireFile, 0, entireFile.readPosition());
		objectBuffer = TroyBufferCreator.create(entireFile, entireFile.readPosition(), entireFile.remainingRead());
	}

	public <T> void write(T obj) {
		if (obj == null)
			writeNull();
		else {
			Class<T> clazz = (Class<T>) obj.getClass();
			writeClass(clazz);
			if (obj instanceof TroySerializable)
				((TroySerializable) obj).write(objectBuffer);
			else
				TroySerializationBank.lookUp(clazz).write(obj, this, objectBuffer);
		}

	}

	public boolean remaining() {
		return objectBuffer.remainingRead() > 0;
	}

	public Object read() {
		if (!remaining())
			throw new RuntimeException("End of read buffer");
		int clasId = objectBuffer.readUnsignedShort();
		if (clasId == 0)
			return null;
		Class<?> type = classesInt.get(clasId);
		if (type == null)
			throw new RuntimeException("Unable to find class with local id of: " + clasId);
		if(type.isAssignableFrom(TroySerializable.class)) {
			TroySerializable result = (TroySerializable) SerializationUtil.newInstance(type);
			result.read(objectBuffer);
			return result;
		}
		BaseSerializer<?> serializer = TroySerializationBank.lookUp(type);
		if (serializer instanceof TroySerializer) {
			Object result = SerializationUtil.newInstance(type);
			((TroySerializer) serializer).read(result, this, objectBuffer);
			return result;
		} else {
			return ((ReturningTroySerializer) serializer).read(type, this, objectBuffer);
		}
	}

	private void writeNull() {
		objectBuffer.writeUnsignedShort(0);
	}

	private void writeClass(Class<?> clazz) {
		objectBuffer.writeUnsignedShort(ensureCreated(clazz));
	}

	private int ensureCreated(Class clazz) {
		Integer result = classesClass.get(clazz);
		if (result != null)
			return result;
		int id = classCount++;
		classesInt.put(id, clazz);
		classesClass.put(clazz, id);
		header.writeString(clazz.getName());
		header.writeUnsignedShort(id);
		header.writeUnsignedShort(0, classesInt.size());
		return id;
	}

	public TroyBuffer getBuffer() {
		TroyBuffer result = TroyBufferCreator.create(header.getBytes(), header.limit() + objectBuffer.limit());
		result.copyFrom(objectBuffer, 0, result.limit(), objectBuffer.limit());
		return result;
	}

	public TroyBuffer getObjectBuffer() {
		return objectBuffer;
	}

	@Override
	public String toString() {
		return "TroySerializationFile [classes=" + classesClass + "]";
	}

}
