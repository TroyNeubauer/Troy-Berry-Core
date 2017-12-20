package com.troyberry.file.reader;

import java.io.IOException;
import java.nio.ByteOrder;

import com.troyberry.file.filemanager.*;
import com.troyberry.util.PrimitiveUtils;
import com.troyberry.util.serialization.TroyBuffer;

public class BMPIO implements IOBase<BMPFile> {

	private static enum DIBType {
		BM, BA, CI, CP, IC, PT;
		byte first, second;

		private DIBType() {
			String name = this.name();
			assert name.length() == 2;
			this.first = (byte) name.charAt(0);
			this.second = (byte) name.charAt(1);
		}

		public void write(TroyBuffer buffer) {
			buffer.writeByte(first);
			buffer.writeByte(second);
		}

		public static DIBType get(TroyBuffer buffer) {
			byte first = buffer.readByte();
			byte second = buffer.readByte();
			for (DIBType dib : values()) {
				if (dib.first == first && dib.second == second) {
					return dib;
				}
			}
			return DIBType.BA;
		}

	}

	private static enum DIBHeaderType {
		BITMAPCOREHEADER(12), OS22XBITMAPHEADER_64(64), OS22XBITMAPHEADER_16(16), BITMAPINFOHEADER(40), BITMAPV2INFOHEADER(52), BITMAPV3INFOHEADER(56), BITMAPV4HEADER(108), BITMAPV5HEADER(124);
		private int bytes;

		private DIBHeaderType(int bytes) {
			this.bytes = bytes;
		}

		public void write(TroyBuffer buffer) {
			buffer.writeInt(bytes);
		}

		public static DIBHeaderType get(TroyBuffer buffer) {
			int bytes = buffer.readInt();
			for (DIBHeaderType header : values()) {
				if (header.bytes == bytes) {
					return header;
				}
			}
			return DIBHeaderType.BITMAPINFOHEADER;
		}

	}

	@Override
	public Class<? extends BMPFile> getFileClass() {
		return BMPFile.class;
	}

	@Override
	public BMPFile read(TroyBuffer buffer) throws InvalidFileException {
		buffer.setReadOrder(ByteOrder.LITTLE_ENDIAN);

		// BItmap file header
		DIBType dibType = DIBType.get(buffer);
		int size = buffer.readShort();
		buffer.skipReader(2);
		buffer.skipReader(2);
		int offset = buffer.readInt(0x0A);
		DIBHeaderType headerType = DIBHeaderType.get(buffer);
		int width, height;
		switch (headerType) {
		case BITMAPCOREHEADER:
			width = buffer.readUnsignedShort(0x12);
			height = buffer.readUnsignedShort(0x14);
			break;
		case OS22XBITMAPHEADER_64:
			break;
		case OS22XBITMAPHEADER_16:
			break;
		case BITMAPINFOHEADER:
			break;
		case BITMAPV2INFOHEADER:
			break;
		case BITMAPV3INFOHEADER:
			break;
		case BITMAPV4HEADER:
			break;
		case BITMAPV5HEADER:
			break;
		default:
			return null;

		}

		// DIB header

		return null;
	}

	@Override
	public void write(TroyBuffer buffer, BMPFile data) throws IOException {
		buffer.setWriteOrder(ByteOrder.LITTLE_ENDIAN);
	}

	@Override
	public void writeHeader(TroyBuffer buffer, int major, int minor, int patch) {
		buffer.setWriteOrder(ByteOrder.LITTLE_ENDIAN);
		DIBType.BA.write(buffer);
	}

}
