package com.troy.troyberry.util;

import java.util.*;

public class IntArray {
	
	private Array<Integer> data = new Array<Integer>();

	public IntArray() {
	}
	
	public int get(int index) {
		Iterator it = data.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			if (((Integer) pair.getKey()).intValue() == index) {
				return (Integer)pair.getValue();
			}
		}
		return 0;
	}

	public void set(int index, int value) {
		if (data.containsKey(new Integer(index))) {
			data.remove(index);
		}
		data.put(new Integer(index), value);
	}

	public void add(int index, int amount) {
		int current = 0;
		if (data.containsKey(new Integer(index))) {
			current = data.get(index);
			data.remove(index);
		}
		data.put(new Integer(index), current + amount);
	}

}
