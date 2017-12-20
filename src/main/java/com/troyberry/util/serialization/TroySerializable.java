package com.troyberry.util.serialization;

/**
 * Indicates that an implementing class can be serialized using troyberry's serialization.
 * @author Troy Neubauer
 */
public interface TroySerializable extends java.io.Serializable {

	/**
	* Retrieves (de-serializes) and sets the instance variables for this object using the serialized data in the desired Troy Buffer
	* @param buffer The buffer to read from
	*/
	public abstract void read(AbstractTroyBuffer buffer);

	/**
	 * Writes (serializes) the contents of this object into the desired Troy Buffer
	 * @param buffer The buffer to write to
	 */
	public abstract void write(AbstractTroyBuffer buffer);

}
