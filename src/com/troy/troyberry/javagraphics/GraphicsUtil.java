package com.troy.troyberry.javagraphics;

import java.awt.*;
import com.troy.troyberry.math.*;

public class GraphicsUtil {

	public static void drawCenteredString(Graphics g, String text, int x, int y, Font font) {
		int count = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == '\n') {
				count++;
			}
		}
		FontMetrics metrics = g.getFontMetrics(font);
		if (count == 0) {
			x -= metrics.stringWidth(text) / 2;
			y -= metrics.getHeight() / 2;
			y += metrics.getAscent();
			g.setFont(font);
			g.drawString(text, x, y);
		} else {
			int index = 0;
			System.out.println("count " + count);
			String[] lines = new String[count + 1];
			for (int i = 0; i < text.length(); i++) {
				char c = text.charAt(i);
				if (c == '\n') {
					index++;
				}else{
					String currentLine = lines[index];
					if(currentLine == null){
						lines[index] = "";
					}
					lines[index] = lines[index] + c;
				}
			}
			int height = 0;
			for (String s : lines) {
				
				System.out.println("drawing part of string " + s);
 				drawCenteredString(g, s, x, y + height * metrics.getHeight(), font);
 				height++;
			}
		}
	}

	private GraphicsUtil() {
	}

	public static void fillRect(Graphics g, double x, double y, double w, double h, Color color) {
		g.setColor(color);
		int intx = Maths.round(x), inty = Maths.round(y);
		int width = Maths.round(w), height = Maths.round(h);
		g.fillRect(intx, inty, width, height);
		System.out.println("fillin rect " + intx + ", " + inty + " w & h " + width + ", " + height);
	}

}
