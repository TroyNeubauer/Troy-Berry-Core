package com.troy.troyberry.serialization;

import static com.troy.troyberry.serialization.SerializationUtils.*;

import java.io.*;
import java.util.*;

public class TBDatabase extends TBBase {

	public static final byte[] HEADER = "LD36".getBytes();
	public static final short VERSION = 0x0100;
	public static final byte CONTAINER_TYPE = ContainerType.DATABASE;
	private short objectCount;
	public List<TBObject> objects = new ArrayList<TBObject>();

	private TBDatabase() {
	}

	public TBDatabase(String name) {
		setName(name);

		size += HEADER.length + 2 + 1 + 2;
	}

	public void addObject(TBObject object) {
		objects.add(object);
		size += object.getSize();

		objectCount = (short) objects.size();
	}

	public int getSize() {
		return size;
	}

	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, HEADER);
		pointer = writeBytes(dest, pointer, VERSION);
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);

		pointer = writeBytes(dest, pointer, objectCount);
		for (TBObject object : objects)
			pointer = object.getBytes(dest, pointer);

		return pointer;
	}

	public static TBDatabase Deserialize(byte[] data) {
		int pointer = 0;
		assert (readString(data, pointer, HEADER.length).equals(HEADER));
		pointer += HEADER.length;

		if (readShort(data, pointer) != VERSION) {
			System.err.println("Invalid LD36 version!");
			return null;
		}
		pointer += 2;

		byte containerType = readByte(data, pointer++);
		assert (containerType == CONTAINER_TYPE);

		TBDatabase result = new TBDatabase();
		result.nameLength = readShort(data, pointer);
		pointer += 2;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;

		result.size = readInt(data, pointer);
		pointer += 4;

		result.objectCount = readShort(data, pointer);
		pointer += 2;

		for (int i = 0; i < result.objectCount; i++) {
			TBObject object = TBObject.Deserialize(data, pointer);
			result.objects.add(object);
			pointer += object.getSize();
		}

		return result;
	}

	public TBObject findObject(String name) {
		for (TBObject object : objects) {
			if (object.getName().equals(name))
				return object;
		}
		return null;
	}

	public static TBDatabase DeserializeFromFile(File saveDirectory) {
		byte[] buffer = null;
		try {
			BufferedInputStream stream = new BufferedInputStream(new FileInputStream(saveDirectory));
			buffer = new byte[stream.available()];
			stream.read(buffer);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Deserialize(buffer);
	}

	public void serializeToFile(File file) {
		byte[] data = new byte[getSize()];
		getBytes(data, 0);
		try {
			if (file.exists()){
				file.delete();
			}

			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
			stream.write(data);
			stream.close();
			System.out.print("saving file: " + file.getName());
			if (file.exists()) 
				System.out.println(" success!");
			else
				System.err.println(" failed!");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
