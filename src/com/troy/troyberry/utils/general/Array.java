package com.troy.troyberry.utils.general;

import java.util.*;

public class Array<T> {

	private Map data = new HashMap<Integer, T>();

	public Array(int min, int max, T initalValue) {
		for (int i = min; i <= max; i++) {
			data.put(i, initalValue);
		}
	}

	public Array() {

	}

	public Array(T initalValue) {
		this(-10, 10, initalValue);
	}

	public T get(int index) {
		Iterator it = data.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			if (((Integer) pair.getKey()).intValue() == index) {
				return (T) pair.getValue();
			}
		}
		return null;
	}

	public void set(int index, T value) {
		if (data.containsKey(new Integer(index))) {
			data.remove(new Integer(index));
		}
		data.put(new Integer(index), value);
	}

	protected Set<Map.Entry<Integer, T>> entrySet() {
		return data.entrySet();
	}

	public boolean containsKey(Integer integer) {
		return data.containsKey(integer);
	}

	public void remove(int integer) {
		data.remove(integer);
	}

	protected void put(Integer integer, T value) {
		data.put(integer, value);
		
	}

}
