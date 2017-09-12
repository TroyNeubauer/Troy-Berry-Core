package com.troyberry.util.profiler;

import java.io.*;
import java.util.*;

import com.troyberry.file.filemanager.*;
import com.troyberry.util.data.*;
import com.troyberry.util.serialization.*;

public class ProfileIO100 implements ProfileIO {

	public ProfileIO100() {
	}

	@Override
	public Class<? extends Profiler> getFileClass() {
		return Profiler.class;
	}

	@Override
	public Profiler read(TroyBuffer buffer) throws InvalidFileException {
		try {

			buffer.readPosition(5 * Byte.BYTES);//Skip header and version
			long createTime = buffer.readLong();
			int dataCount = buffer.readInt();
			boolean usingByteForStringStorage = WriterUtils.byteToBoolean(buffer.readByte());
			short numberOfNameEntries = buffer.readShort();

			List<String> nameKeys = new ArrayList<String>(numberOfNameEntries);

			for (int i = 0; i < numberOfNameEntries; i++) {
				if (usingByteForStringStorage) nameKeys.add(buffer.readString());
				else nameKeys.add(buffer.readString());
			}

			List<ProfileData> data = new ArrayList<ProfileData>(dataCount);

			for (int i = 0; i < dataCount; i++) {
				byte id = buffer.readByte();
				short nameIndex = buffer.readShort();
				String name = nameKeys.get(nameIndex);
				switch (id) {
				case ProfileData.SELECTION_ID:
					long time = buffer.readLong();

					data.add(new ProfileSelection(name, time, 0));
					break;
				case ProfileData.SECTION_ID:
					long start = buffer.readLong();
					long end = buffer.readLong();

					data.add(new ProfileSection(name, start, end));
					break;
				case ProfileData.BREAK_ID:
					break;
				case ProfileData.ERROR_ID:
					break;

				}

			}
			return new Profiler(data, createTime);
		} catch (Exception e) {
			throw new InvalidFileException("Invalid Profile File!");
		}
	}

	@Override
	public void write(TroyBuffer writer, Profiler data) throws IOException {
		List<String> nameKeys = new ArrayList<String>(25);
		for (ProfileData d : data.getData())
			if (!nameKeys.contains(d.getName())) nameKeys.add(d.getName());

		boolean byteLengthForStrings = true;
		if (nameKeys.size() > 0) {
			int longestLength = nameKeys.get(0).length();
			for (int i = 1; i < nameKeys.size(); i++)
				if (nameKeys.get(i).length() > longestLength) longestLength = nameKeys.get(i).length();

			byteLengthForStrings = longestLength < Byte.MAX_VALUE;
		}

		//Write basic data
		writeHeader(writer, 1, 0, 0);
		writer.writeLong(data.getCreateTime());
		writer.writeInt(data.size());

		//Write the size of strings
		writer.writeByte(WriterUtils.booleanToByte(byteLengthForStrings));

		//Write the number of keys
		writer.writeShort((short) nameKeys.size());
		//Write each key as Signed byte, or signed int depending on byteLengthForStrings
		for (String name : nameKeys) {
			if (byteLengthForStrings) writer.writeString(name);
			else writer.writeString(name);
		}

		for (ProfileData d : data.getData()) {
			writer.writeByte(d.getIdentifier());
			writer.writeShort((short) nameKeys.indexOf(d.getName()));
			if (d instanceof ProfileSelection) {

				ProfileSelection dd = (ProfileSelection) d;

				writer.writeLong(dd.getLength());
			} else if (d instanceof ProfileSection) {

				ProfileSection dd = (ProfileSection) d;
				writer.writeLong(dd.getStart());
				writer.writeLong(dd.getEnd());
			}
		}
	}

}
