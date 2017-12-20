package com.troyberry.file.reader;

import java.awt.image.BufferedImage;

import com.troyberry.file.filemanager.FileBase;

public class BMPFile implements FileBase {
	private BufferedImage image;

	public BMPFile(BufferedImage image) {
		this.image = image;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	
}
