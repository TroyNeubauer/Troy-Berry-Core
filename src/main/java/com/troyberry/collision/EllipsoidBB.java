package com.troyberry.collision;

import com.troyberry.math.*;

/**
 * Represents an ellipsoid shaped bounding box
 * EllipsoidBB are immutable
 * @author Troy Neubauer
 *
 */
public class EllipsoidBB {

	protected final float radiusX, radiusY, radiusZ;
	protected final AxisAlignedBB box;

	public EllipsoidBB(float radiusX, float radiusY, float radiusZ) {
		this.radiusX = radiusX;
		this.radiusY = radiusY;
		this.radiusZ = radiusZ;
		this.box = new AxisAlignedBB(-radiusX, -radiusY, -radiusZ, radiusX, radiusY, radiusZ);
	}

	/**
	 * Returns a bounding box expanded by the specified vector (if negative
	 * numbers are given it will shrink)
	 */
	public EllipsoidBB expand(float rx, float ry, float rz) {
		float newrx = radiusX + rx;
		float newry = radiusY + ry;
		float newrz = radiusZ + rz;
		return new EllipsoidBB(newrx, newry, newrz);
	}
	/**@deprecated
	 * Determines if a point intersects with this bounding box. <br><u>This method is a work in process AND WILL NOT WORK</u>
	 * @param thisPosition The position of this bounding box
	 * @param other The other ellipsoid bounding box to test agents
	 * @param otherPosition The position of the other bounding box
	 * @return Weather or not the two boxes intersect
	 */
	public boolean intersects(Vector3f thisPosition, EllipsoidBB other, Vector3f otherPosition) {
		if (this.box.intersectsWith(thisPosition, other.box, otherPosition)) {
			// TODO: This is to hard to calculate, will do later
		}
		return false;
	}

	/**
	 * Returns if the supplied Vector3f is completely inside the bounding box
	 */
	public boolean isVecInside(Vector3f thisPosition, Vector3f vec) {
		return Math.pow((vec.x - thisPosition.x) / radiusX, 2.0) + Math.pow((vec.y - thisPosition.y) / radiusY, 2.0)
				+ Math.pow((vec.z - thisPosition.z) / radiusZ, 2.0) < 1.0f;
	}

	public float getRadiusX() {
		return radiusX;
	}

	public float getRadiusY() {
		return radiusY;
	}

	public float getRadiusZ() {
		return radiusZ;
	}

	public AxisAlignedBB getBox() {
		return box;
	}
	
	

}
