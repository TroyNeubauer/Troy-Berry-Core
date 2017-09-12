package com.troyberry.noise;

import java.util.*;

public class SimplexNoise {

	private final SimplexNoiseOctave[] octaves;
	private final double[] frequencys;
	private final double[] amplitudes;

	private final double largestFeature;
	private final double persistence;
	private final long seed;

	public SimplexNoise(double largestFeature, double persistence, long seed) {
		this.largestFeature = largestFeature;
		this.persistence = persistence;
		this.seed = seed;

		//recieves a number (eg 128) and calculates what power of 2 it is (eg 2^7)
		int numberOfOctaves = (int) Math.ceil(Math.log10(largestFeature) / Math.log10(2));
		numberOfOctaves = Math.max(numberOfOctaves, 1);
		octaves = new SimplexNoiseOctave[numberOfOctaves];
		frequencys = new double[numberOfOctaves];
		amplitudes = new double[numberOfOctaves];

		Random rnd = new Random(seed);

		for (int i = 0; i < numberOfOctaves; i++) {
			octaves[i] = new SimplexNoiseOctave(rnd.nextInt());

			frequencys[i] = Math.pow(2, i);
			amplitudes[i] = Math.pow(persistence, octaves.length - i);

		}

	}

	public double getNoise(double x, double y) {

		double result = 0;

		for (int i = 0; i < octaves.length; i++) {
			//double frequency = Math.pow(2,i);
			//double amplitude = Math.pow(persistence,octaves.length-i);

			result += octaves[i].noise(x / frequencys[i], y / frequencys[i]) * amplitudes[i];
		}

		return result;

	}

	public double getNoise(double x, double y, double z) {

		double result = 0;
		for (int i = 0; i < octaves.length; i++) {
			double frequency = Math.pow(2, i);
			double amplitude = Math.pow(persistence, octaves.length - i);
			result = result + octaves[i].noise(x / frequency, y / frequency, z / frequency) * amplitude;
		}

		return result;

	}
	
	public double getLargestFeature() {
		return largestFeature;
	}
	
	public double getPersistence() {
		return persistence;
	}
	
	public long getSeed() {
		return seed;
	}
}
