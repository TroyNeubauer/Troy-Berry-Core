package com.troyberry.util.interpolation;

import java.util.Map.Entry;
import java.util.Set;

import com.troyberry.math.Maths;
import com.troyberry.util.StringFormatter;
import com.troyberry.util.serialization.*;

public class DefaultKeyFrameManager extends KeyFrameMaster<Double> implements TroySerializable {
	
	public DefaultKeyFrameManager() {
		super();
	}

	public DefaultKeyFrameManager(InterpolationType type) {
		super(type);
	}
	
	@Override
	public Double getValue(double position) {
		Double positionObj = Double.valueOf(position);
		if (pairs.containsKey(positionObj)) return pairs.get(positionObj);
		Entry<Double, Double> min = pairs.lowerEntry(positionObj), max = pairs.higherEntry(positionObj);
		if (min != null && max != null) {
			double result = 0.0;
			double factor = Maths.normalize(min.getKey().doubleValue(), max.getKey().doubleValue(), position);
			switch (type) {
				case LINEAR:
					result = Maths.lerp(min.getValue(), max.getValue(), factor);
					break;
				case COSINE:
					result = Maths.cerp(min.getValue(), max.getValue(), factor);
					break;
			}
			return result;
		} else return (min != null) ? min.getValue().doubleValue() : max.getValue().doubleValue();
	}

	@Override
	public void write(AbstractTroyBuffer writer) {
		Set<Entry<Double, Double>> entrySet = pairs.entrySet();
		writer.writeInt(entrySet.size());
		writer.writeInt(type.getId());
		for (Entry<Double, Double> entry : entrySet) {
			writer.writeDouble(entry.getKey());
			writer.writeDouble(entry.getValue());
		}
	}
	
	

	public void read(AbstractTroyBuffer reader) {
		int entries = reader.readInt();
		this.type = InterpolationType.getType(reader.readInt());
		for(int i = 0; i < entries; i++) {
			this.pairs.put(reader.readDouble(), reader.readDouble());
		}
	}
	
	@Override
	public String toString() {
		return "DefaultKeyFrameManager Lerp type: " + StringFormatter.capitalizeFirstLetter(type.name()) + " paris " + pairs.toString();
	}

}
