package com.troyberry.collision;

import com.troyberry.math.*;

/**
 * Represents a 3D bounding box that is axis aligned, (cannot be rotated)<br>
 * AxisAlignedBB's are immutable
 * @author Troy Neubauer
 *
 */
public class AxisAlignedBB {
	protected final float minX, minY, minZ;
	protected final float maxX, maxY, maxZ;
	
	/**
	 * Constructs a new bounding box between the specified coordinates
	 */
	public AxisAlignedBB(Vector3f vec1, Vector3f vec2) {
		this.minX = Math.min(vec1.x, vec2.x);
		this.minY = Math.min(vec1.y, vec2.y);
		this.minZ = Math.min(vec1.z, vec2.z);
		this.maxX = Math.max(vec1.x, vec2.x);
		this.maxY = Math.max(vec1.y, vec2.y);
		this.maxZ = Math.max(vec1.z, vec2.z);
	}
	
	/**
	 * Constructs a new bounding box with the specified coordinates
	 */
	public AxisAlignedBB(float x1, float y1, float z1, float x2, float y2, float z2) {
		this.minX = Math.min(x1, x2);
		this.minY = Math.min(y1, y2);
		this.minZ = Math.min(z1, z2);
		this.maxX = Math.max(x1, x2);
		this.maxY = Math.max(y1, y2);
		this.maxZ = Math.max(z1, z2);
	}

	/**
	 * Adds the coordinates to the bounding box extending it if the point lies
	 * outside the current ranges.
	 */
	public AxisAlignedBB addCoord(float x, float y, float z) {
		float minX = this.minX;
		float minY = this.minY;
		float minZ = this.minZ;
		float maxX = this.maxX;
		float maxY = this.maxY;
		float maxZ = this.maxZ;

		if (x < 0.0f) {
			minX += x;
		} else if (x > 0.0f) {
			maxX += x;
		}

		if (y < 0.0f) {
			minY += y;
		} else if (y > 0.0f) {
			maxY += y;
		}

		if (z < 0.0f) {
			minZ += z;
		} else if (z > 0.0f) {
			maxZ += z;
		}

		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}

	/**
	 * Returns a bounding box expanded by the specified vector (if negative
	 * numbers are given it will shrink). Args: x, y, z
	 */
	public AxisAlignedBB expand(float x, float y, float z) {
		float minX = this.minX - x;
		float minY = this.minY - y;
		float minZ = this.minZ - z;
		float maxX = this.maxX + x;
		float maxY = this.maxY + y;
		float maxZ = this.maxZ + z;
		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}

	/**
	 * Returns a new AABB that encapsulates this AABB and the parameter AABB
	 */
	public AxisAlignedBB union(AxisAlignedBB other) {
		float minX = Math.min(this.minX, other.minX);
		float minY = Math.min(this.minY, other.minY);
		float minZ = Math.min(this.minZ, other.minZ);
		float maxX = Math.max(this.maxX, other.maxX);
		float maxY = Math.max(this.maxY, other.maxY);
		float maxZ = Math.max(this.maxZ, other.maxZ);
		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}

	/**
	 * Offsets the current bounding box by the specified coordinates
	 */
	public AxisAlignedBB offset(float x, float y, float z) {
		return new AxisAlignedBB(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
	}

	/**
	 * Returns whether the given bounding box intersects with this one.
	 * 
	 * @param thisPosition
	 *            the offset for this bounding box
	 * @param axisAlignedBB
	 *            the other bounding box to compare against
	 * @param otherPosition
	 *            the offset for other's bounding box
	 * 
	 */
	public boolean intersectsWith(Vector3f thisPosition, AxisAlignedBB other, Vector3f otherPosition) {

		return other.maxX + otherPosition.x > this.minX + thisPosition.x && other.minX + otherPosition.x < this.maxX + thisPosition.x
				? (other.maxY + otherPosition.y > this.minY + thisPosition.y && other.minY + otherPosition.y < this.maxY + thisPosition.y
						? other.maxZ + otherPosition.y > this.minZ + thisPosition.z
								&& other.minZ + otherPosition.z < this.maxZ + thisPosition.z
						: false)
				: false;
	}

	/**
	 * Returns if the supplied Vector3f is completely inside the bounding box
	 */
	public boolean isVecInside(Vector3f vec) {
		return vec.x > this.minX && vec.x < this.maxX
				? (vec.y > this.minY && vec.y < this.maxY ? vec.z > this.minZ && vec.z < this.maxZ : false) : false;
	}

	/**
	 * Returns the average length of the edges of the bounding box.
	 */
	public float getAverageEdgeLength() {
		float x = this.maxX - this.minX;
		float y = this.maxY - this.minY;
		float z = this.maxZ - this.minZ;
		return (x + y + z) / 3.0f;
	}

	/**
	 * Returns a bounding box that is inset by the specified amounts
	 */
	public AxisAlignedBB contract(float x, float y, float z) {
		float minX = this.minX + x;
		float minY = this.minY + y;
		float minZ = this.minZ + z;
		float maxX = this.maxX - x;
		float maxY = this.maxY - y;
		float maxZ = this.maxZ - z;
		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}

	/**
	 * Checks if the specified vector is within the YZ dimensions of the
	 * bounding box.
	 */
	public boolean isVecInYZ(Vector3f vec) {
		return vec == null ? false : vec.y >= this.minY && vec.y <= this.maxY && vec.z >= this.minZ && vec.z <= this.maxZ;
	}

	/**
	 * Checks if the specified vector is within the XZ dimensions of the
	 * bounding box.
	 */
	public boolean isVecInXZ(Vector3f vec) {
		return vec == null ? false : vec.x >= this.minX && vec.x <= this.maxX && vec.z >= this.minZ && vec.z <= this.maxZ;
	}

	/**
	 * Checks if the specified vector is within the XY dimensions of the
	 * bounding box.
	 */
	public boolean isVecInXY(Vector3f vec) {
		return vec == null ? false : vec.x >= this.minX && vec.x <= this.maxX && vec.y >= this.minY && vec.y <= this.maxY;
	}

	public String toString() {
		return "BoundingBox[" + this.minX + ", " + this.minY + ", " + this.minZ + " to " + this.maxX + ", " + this.maxY + ", " + this.maxZ
				+ "]";
	}

	public float getMinX() {
		return minX;
	}

	public float getMinY() {
		return minY;
	}

	public float getMinZ() {
		return minZ;
	}

	public float getMaxX() {
		return maxX;
	}

	public float getMaxY() {
		return maxY;
	}

	public float getMaxZ() {
		return maxZ;
	}
	
	
}
