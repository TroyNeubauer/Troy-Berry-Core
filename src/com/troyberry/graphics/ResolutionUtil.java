package com.troyberry.graphics;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.troyberry.math.*;

/**
 * A static utility class for managing resolutions.
 * @author Troy Neubauer
 * @see Resolution
 * @see AspectRatio
 */
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
	
	/**
	 * Returns weather or not two resolution's share the same aspect ratio
	 * @param a The first resolution
	 * @param b The second resolution
	 * @return weather or not two resolution's share the same aspect ratio
	 */
	public static boolean sameAspectRatio(Resolution a, Resolution b) {
		return a.aspectRatio.equals(b.aspectRatio);
	}
	
	/**
	 * Returns a list of all the aspect ratios supported by Troy Berry Core
	 * @return The list
	 * @see AspectRatio
	 */
	public static AspectRatio[] getAvilableAspectRatios() {
		AspectRatio[] ratios = new AspectRatio[aspectRatios.size()];
		Object[] objects = aspectRatios.toArray();
		for (int i = 0; i < ratios.length; i++) {
			ratios[i] = (AspectRatio) objects[i];
		}
		return ratios;
	}

	/**
	 * Creates a new JFrame with the size of the primary monitor * the size parameter.<br>
	 * IE 0.75 would create a window that is 3/4's the size of the primary monitor
	 * @param size The value to scale the size down by 0.01 - 1.0
	 * @return The JFrame new JFrame object that is the desired size
	 * @see JFrame
	 */
	public static JFrame createWindowOnPrimaryMonitor(double size) {
		size = Maths.clamp(0.00001, 1.0, size);
		JFrame frame = new JFrame();
		DisplayMode device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
		Resolution resolution = new Resolution((int) (device.getWidth() * size), (int) (device.getHeight() * size), (size == 1.0));
		frame.setSize(resolution.width, resolution.height);
		return frame;
	}

	/**
	 * Gets the resolution of the primary monitor * the size parameter.<br>
	 * IE 0.75 would return a resolution that is 3/4's the size of the primary monitor
	 * @param size The value to scale the size down by 0.01 - 1.0
	 * @return
	 */
	public static Resolution getscaledResolution(double size) {
		DisplayMode device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
		return new Resolution((int) (device.getWidth() * size), (int) (device.getHeight() * size), (size == 1.0));
	}

	/**
	 * Gets the resolution from a JFrame
	 * @param frame The frame to extract the resolution from
	 * @return The resolution of the JFrame
	 * @see Resolution
	 */
	public static Resolution getResolution(JFrame frame) {
		return new Resolution(frame.getWidth(), frame.getHeight(),
			frame.getWidth() == GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth()
				&& frame.getHeight() == GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight());
	}

	/**
	 * Gets the aspect ratio from a JFrame
	 * @param frame The frame to extract the resolution from
	 * @return The resolution of the JFrame
	 * @see Resolution
	 */
	public static AspectRatio getAspectRatio(JFrame frame) {
		return new Resolution(frame.getWidth(), frame.getHeight(),
			frame.getWidth() == GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth() && frame
				.getHeight() == GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight()).aspectRatio;
	}

}
