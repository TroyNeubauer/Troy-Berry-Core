package com.troyberry.graphics;

import com.troyberry.color.*;

public interface IWindow<W, T> {
	
	public void center();
	
	public void clear();
	
	public void show();
	
	public void hide();

	public void update();

	public int getWidth();

	public int getHeight();

	public void setWidth(int height);

	public void setHeight(int height);

	public void setSize(int width, int height);

	public void setColor(TroyColor color);
	
	public boolean isCloseRequested();
	
	public void setTitle(String title);

	/**
	 * Moves the top left corner of the window to the specified position relative to the monitor's upper left corner. 
	 * @param x The x position
	 * @param y The y position
	 */
	public void move(int x, int y);

	public W getWrapper();

	public Class<T> getTextureType();

	public void destroy();

}
