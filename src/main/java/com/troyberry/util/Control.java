package com.troyberry.util;

public abstract class Control {

	/** The current key code of this keybinding **/
	public volatile int keyId;

	/** The default key code of this keybinding **/
	public final int defaultKeyId;

	/** weather or not the key has just been presses **/
	protected volatile boolean pressed, isPressedNow;

	/** Was this key pressed last update **/
	protected volatile boolean lastPress;

	public Control(int id) {
		this.keyId = id;
		this.defaultKeyId = id;
		this.pressed = false;
		this.lastPress = false;
		this.isPressedNow = false;
		addToHandler(this);
	}

	public boolean isPressedUpdateThread() {
		return isPressedNow;
	}

	/**
	 * @return true if this is the first time that this method has been called
	 *         since the control was pressed
	 */
	public boolean hasBeenPressed() {
		if (pressed) {
			pressed = false;
			return true;
		}
		return false;
	}

	/** Resets this control to the default key **/
	public void reset() {
		this.keyId = defaultKeyId;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("KeyBinding:\n");
		sb.append("current key " + getName(keyId) + " code is " + this.keyId + "\n");
		sb.append("default key " + getName(defaultKeyId) + " code is " + this.keyId + "\n");

		return sb.toString();
	}

	public abstract String getName(int keyId);

	public abstract void addToHandler(Control control);

	/**
	 * @return true if the key on the keyboard is physically down otherwise,
	 *         false
	 */
	public abstract boolean isPressed();

}
