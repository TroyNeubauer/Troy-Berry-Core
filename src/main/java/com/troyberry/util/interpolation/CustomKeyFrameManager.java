package com.troyberry.util.interpolation;

import java.util.Map.Entry;

import com.troyberry.math.Maths;
import com.troyberry.util.serialization.AbstractTroyBuffer;

public class CustomKeyFrameManager<T extends Interpolatable<T>> extends KeyFrameMaster<T> {
	
	public CustomKeyFrameManager() {
		super();
	}

	public CustomKeyFrameManager(InterpolationType type) {
		super(type);
	}

	public T getValue(double position) {
		Double positionObj = Double.valueOf(position);
		if (pairs.containsKey(positionObj)) return pairs.get(positionObj);
		Entry<Double, T> min = pairs.lowerEntry(positionObj), max = pairs.higherEntry(positionObj);
		if (min != null && max != null) {
			return (T) min.getValue().interpolate(min.getValue(), max.getValue(), Maths.normalize(min.getKey().doubleValue(), max.getKey().doubleValue(), position),
					getInterpolationType());
		} else return (min != null) ? min.getValue() : max.getValue();
	}

	@Override
	public void write(AbstractTroyBuffer writer) {
		//No op
	}

}
