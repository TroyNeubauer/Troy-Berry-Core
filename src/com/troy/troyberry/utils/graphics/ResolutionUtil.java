package com.troy.troyberry.utils.graphics;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import com.troy.troyberry.math.*;
import com.troy.troyberry.utils.*;

public class ResolutionUtil {

	private static Map<String, Resolution> sizes = new HashMap<String, Resolution>();
	public static List<AspectRatio> aspectRatios = new ArrayList<AspectRatio>();

	static {
		init();
	}

	public static Resolution getResolutionFromArgs(String[] args) {
		int width = 600, height = 400;
		boolean fullscreen = false;
		for (int i = 0; i < args.length; i++) {
			try {
				String currentArg = args[i];
				String clippedArg = currentArg.replaceAll("-", "");
				String[] values = clippedArg.split(":");
				String key = values[0], value = values[1];
				System.out.println("key " + key);
				if (key.equalsIgnoreCase("width")) {
					width = Integer.parseInt(value);
				} else if (key.equalsIgnoreCase("height")) {
					height = Integer.parseInt(value);
				} else if (key.equalsIgnoreCase("fullscreen")) {
					fullscreen = Boolean.parseBoolean(value);
				}

			} catch (Exception e) {
				System.err.println("unable to load resolution " + args[i]);
			}

		}
		System.out.println("width " + width + " height " + height + " fullscreen " + fullscreen);

		return new Resolution(width, height, fullscreen);
	}

	public static String createLaunchArgs(Resolution resolution) {

		return "-width:" + resolution.width + " -height:" + resolution.height + " -fullscreen:" + resolution.fullscreen;

	}

	private ResolutionUtil() {
	}

	private static void init() {
		add(AspectRatio.SIZE16X9);
		add(AspectRatio.SIZE16X10);
		add(AspectRatio.SIZE4X3);
		add(AspectRatio.SIZE1X1);
		add(AspectRatio.SIZE5X4);
		add(AspectRatio.SIZE21X9);
		add(AspectRatio.SIZE3X2);
		GraphicsDevice[] sizes = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		for (int i = 0; i < sizes.length; i++) {
			GraphicsDevice device = sizes[i];
		}

	}

	private static void add(AspectRatio ratio) {
		if (aspectRatios.contains(ratio)) return;
		aspectRatios.add(ratio);
	}

	public static boolean sameAspectRatio(Resolution a, Resolution b) {
		return false;
	}

	public static AspectRatio getAspectRatio(Resolution resolution) {

		return AspectRatio.UNSUPPORTED;
	}

	public static AspectRatio[] getAvilableAspectRatios() {
		AspectRatio[] ratios = new AspectRatio[aspectRatios.size()];
		Object[] objects = aspectRatios.toArray();
		for (int i = 0; i < ratios.length; i++) {
			ratios[i] = (AspectRatio) objects[i];
		}
		return ratios;
	}

	public static JFrame createWindowOnPrimaryMonitor(double size) {
		size = Maths.clamp(0.00001, 1.0, size);
		JFrame frame = new JFrame();
		DisplayMode device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
		Resolution resolution = new Resolution((int) (device.getWidth() * size), (int) (device.getHeight() * size), (size == 1.0));
		frame.setSize(resolution.width, resolution.height);
		return frame;
	}

	public static Resolution getscaledResolution(double size) {
		DisplayMode device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
		return new Resolution((int) (device.getWidth() * size), (int) (device.getHeight() * size), (size == 1.0));
	}

	public static Resolution getResolution(JFrame frame) {
		return new Resolution(frame.getWidth(), frame.getHeight(),
			frame.getWidth() == GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth()
				&& frame.getHeight() == GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight());
	}

	public static AspectRatio getAspectRatio(JFrame frame) {
		return new Resolution(frame.getWidth(), frame.getHeight(),
			frame.getWidth() == GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth() && frame
				.getHeight() == GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight()).aspectRatio;
	}

	public static void printAvilableAspectRatios() {
		System.out.println("Supported aspect ratios are:");
		for (int i = 0; i < aspectRatios.size(); i++) {
			System.out.println(aspectRatios.get(i));
		}
	}

}
