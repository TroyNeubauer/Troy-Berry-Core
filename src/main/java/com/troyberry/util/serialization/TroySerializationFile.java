package com.troyberry.util.serialization;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

@SuppressWarnings("rawtypes")
public class TroySerializationFile {

	private HashMap<Integer, Class> classes;
	private TroyBuffer objectBuffer, header;
	private int classCount = 0;

	public TroySerializationFile() {
		this.classes = new HashMap<Integer, Class>();
		this.objectBuffer = TroyBuffer.create();
		this.header = TroyBuffer.create();

		header.writeUnsignedShort(0);// The number of entries is 0
	}

	public TroySerializationFile(File file) {
		this.classes = new HashMap<Integer, Class>();
		this.objectBuffer = TroyBuffer.create();
		this.header = TroyBuffer.create();
		TroyBuffer entireFile = TroyBuffer.create(file);
		int entriesCount = entireFile.readUnsignedShort();
		for (int i = 0; i < entriesCount; i++) {
			String className = entireFile.readString();
			int classId = entireFile.readUnsignedShort();
			try {
				classes.put(classId, Class.forName(className));
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		header.copyFrom(entireFile, 0, 0, entireFile.readPosition());
		objectBuffer.copyFrom(entireFile, entireFile.readPosition(), 0, entireFile.remaining());
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
		return objectBuffer.remaining() > 0;
	}

	public Object read() {
		if (!remaining())
			throw new RuntimeException("End of read buffer");
		int clasId = objectBuffer.readUnsignedShort();
		Class type = classes.get(clasId);
		if (type == null)
			throw new RuntimeException("Unable to find class with local id of: " + clasId);
		return TroySerializationBank.lookUp(type).read(objectBuffer, this, type);
	}

	private void writeNull() {
		objectBuffer.writeUnsignedShort(0);
	}

	private void writeClass(Class<?> clazz) {
		objectBuffer.writeUnsignedShort(ensureCreated(clazz));
	}

	private int ensureCreated(Class clazz) {
		if (!classes.containsValue(clazz)) {
			classes.put(++classCount, clazz);
			header.writeString(clazz.getName());
			header.writeUnsignedShort(classCount);
			header.writeUnsignedShort(0, classes.size());
			return classCount;
		} else {
			Iterator<Entry<Integer, Class>> it = classes.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Integer, Class> entry = it.next();
				if (entry.getValue() == clazz)
					return entry.getKey();
			}
			System.err.println("Warning! Unable to find class " + clazz.getName() + " Shouldnt reach here!");
			return 0;// Will never reach here
		}

	}

	public TroyBuffer getBuffer() {
		TroyBuffer result = TroyBuffer.create(header, header.limit() + objectBuffer.limit());
		result.copyFrom(objectBuffer, 0, result.limit(), objectBuffer.limit());
		return result;
	}

	@Override
	public String toString() {
		return "TroySerializationFile [classes=" + classes + "]";
	}

}
