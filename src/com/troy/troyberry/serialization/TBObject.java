package com.troy.troyberry.serialization;

import static com.troy.troyberry.serialization.SerializationUtils.*;

import java.util.*;

public class TBObject extends TBBase {

	public static final byte CONTAINER_TYPE = ContainerType.OBJECT;
	private short fieldCount;
	public List<TBField> fields = new ArrayList<TBField>();
	private short stringCount;
	public List<TBString> strings = new ArrayList<TBString>();
	private short arrayCount;
	public List<TBArray> arrays = new ArrayList<TBArray>();
	
	private TBObject() {
	}
	
	public TBObject(String name) {
		size += 1 + 2 + 2 + 2;
		setName(name);
	}
	
	public void addField(TBField field) {
		fields.add(field);
		size += field.getSize();
		
		fieldCount = (short)fields.size();
	}
	
	public void addString(TBString string) {
		strings.add(string);
		size += string.getSize();
		
		stringCount = (short)strings.size();
	}

	public void addArray(TBArray array) {
		arrays.add(array);
		size += array.getSize();
		
		arrayCount = (short)arrays.size();
	}
	
	public int getSize() {
		return size;
	}
	
	public TBField findField(String name) {
		for (TBField field : fields) {
			if (field.getName().equals(name))
				return field;
		}
		return null;
	}
	
	public TBString findString(String name) {
		for (TBString string : strings) {
			if (string.getName().equals(name))
				return string;
		}
		return null;
	}

	public TBArray findArray(String name) {
		for (TBArray array : arrays) {
			if (array.getName().equals(name))
				return array;
		}
		return null;
	}
	
	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);
		
		pointer = writeBytes(dest, pointer, fieldCount);
		for (TBField field : fields)
			pointer = field.getBytes(dest, pointer);
		
		pointer = writeBytes(dest, pointer, stringCount);
		for (TBString string : strings)
			pointer = string.getBytes(dest, pointer);
		
		pointer = writeBytes(dest, pointer, arrayCount);
		for (TBArray array : arrays)
			pointer = array.getBytes(dest, pointer);
		
		return pointer;
	}
	
	public static TBObject Deserialize(byte[] data, int pointer) {
		byte containerType = data[pointer++];
		assert(containerType == CONTAINER_TYPE);
		
		TBObject result = new TBObject();
		result.nameLength = readShort(data, pointer);
		pointer += 2;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.size = readInt(data, pointer);
		pointer += 4;
		
		// Early-out: pointer += result.size - sizeOffset - result.nameLength;
		
		result.fieldCount = readShort(data, pointer);
		pointer += 2;
		
		for (int i = 0; i < result.fieldCount; i++) {
			TBField field = TBField.Deserialize(data, pointer);
			result.fields.add(field);
			pointer += field.getSize();
		}
		
		result.stringCount = readShort(data, pointer);
		pointer += 2;

		for (int i = 0; i < result.stringCount; i++) {
			TBString string = TBString.Deserialize(data, pointer);
			result.strings.add(string);
			pointer += string.getSize();
		}

		result.arrayCount = readShort(data, pointer);
		pointer += 2;

		for (int i = 0; i < result.arrayCount; i++) {
			TBArray array = TBArray.Deserialize(data, pointer);
			result.arrays.add(array);
			pointer += array.getSize();
		}
		
		return result;
	}
	
}
