package com.troy.troyberry.collision;

import com.troy.troyberry.math.*;

public class AxisAlignedBB {
	public final float minX;
	public final float minY;
	public final float minZ;
	public final float maxX;
	public final float maxY;
	public final float maxZ;

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
	 * outside the current ranges. Args: x, y, z
	 */
	public AxisAlignedBB addCoord(float x, float y, float z) {
		float minX = this.minX;
		float minY = this.minY;
		float minZ = this.minZ;
		float maxX = this.maxX;
		float maxY = this.maxY;
		float maxZ = this.maxZ;

		if (x < 0.0D) {
			minX += x;
		} else if (x > 0.0D) {
			maxX += x;
		}

		if (y < 0.0D) {
			minY += y;
		} else if (y > 0.0D) {
			maxY += y;
		}

		if (z < 0.0D) {
			minZ += z;
		} else if (z > 0.0D) {
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
	 * */
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
	 * @param axisAlignedBB the other bounding box to compare against
	 */
	public boolean intersectsWith(AxisAlignedBB other) {
		return other.maxX > this.minX && other.minX < this.maxX
				? (other.maxY > this.minY && other.minY < this.maxY ? other.maxZ > this.minZ && other.minZ < this.maxZ : false) : false;
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
		return "BoundingBox[" + this.minX + ", " + this.minY + ", " + this.minZ + " to " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
	}
}
