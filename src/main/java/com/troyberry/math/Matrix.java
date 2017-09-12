package com.troyberry.math;

import java.nio.*;

public interface Matrix {

	/**
	 * Sets this matrix to the identity matrix
	 */
	public void setIdentity();

	/**
	 * Sets this matrix to the zero matrix
	 */
	public void setZero();

	/**
	 * Copies the contents of matrix source to this matrix
	 * @param src The matrix to copy from
	 */
	public void load(Matrix src);

	/**
	 * Copies the contents of the float buffer to this matrix
	 * @param buf The buffer to copy from
	 */
	public void load(FloatBuffer buf);

	/**
	 * Copies the contents of the float buffer to this matrix transposing them
	 * @param buf The buffer to copy from
	 */
	public void loadTranspose(FloatBuffer buf);
	
	/**
	 * Copies the contents of the double buffer to this matrix
	 * @param buf The buffer to copy from
	 */
	public void load(DoubleBuffer buf);

	/**
	 * Copies the contents of the double buffer to this matrix transposing them
	 * @param buf The buffer to copy from
	 */
	public void loadTranspose(DoubleBuffer buf);

	/**
	 * Sets this matrix to the transpose of itself
	 */
	public void transpose();
	
	/**
	 * Translates this matrix the desired direction and magnitude
	 * @param vec The vector to use to translate
	 */
	public void translate(Vector2f vec);

	/**
	 * Translates this matrix the desired direction and magnitude
	 * @param vec The vector to use to translate
	 */
	public void translate(Vector3f vec);
	
	/**
	 * Translates this matrix the desired direction and magnitude, storing the result in dest<br>
	 * <p>This method will not modify this matrix<p>
	 * @param vec The vector to use to translate
	 * @param dest The matrix to store the result in
	 */
	public void translate(Vector2f vec, Matrix dest);
	
	/**
	 * Translates this matrix the desired direction and magnitude, storing the result in dest<br>
	 * <p>This method will not modify this matrix<p>
	 * @param vec The vector to use to translate
	 * @param dest The matrix to store the result in
	 */
	public void translate(Vector3f vec, Matrix dest);
	
	/**
	 * Translates this matrix the desired direction and magnitude
	 * @param vec The vector to use to translate
	 */
	public void translate(Vector2d vec);

	/**
	 * Translates this matrix the desired direction and magnitude
	 * @param vec The vector to use to translate
	 */
	public void translate(Vector3d vec);
	
	/**
	 * Translates this matrix the desired direction and magnitude, storing the result in dest<br>
	 * <p>This method will not modify this matrix<p>
	 * @param vec The vector to use to translate
	 * @param dest The matrix to store the result in
	 */
	public void translate(Vector2d vec, Matrix dest);
	
	/**
	 * Translates this matrix the desired direction and magnitude, storing the result in dest<br>
	 * <p>This method will not modify this matrix<p>
	 * @param vec The vector to use to translate
	 * @param dest The matrix to store the result in
	 */
	public void translate(Vector3d vec, Matrix dest);
	
	/**
	 * Scales this matrix using the vector vec
	 * @param vec The vector to use to scale this matrix
	 */
	public void scale(Vector3f vec);
	
	/**
	 * Scales this matrix using the vector vec
	 * @param vec The vector to use to scale this matrix
	 */
	public void scale(Vector3d vec);
	
	/**
	 * Stores the scale of this matrix using a scalar in the matrix dest
	 * @param vec The vector to use to scale this matrix
	 * @param dest The matrix to store the scale of this matrix in
	 */
	public void scale(Vector3f vec, Matrix dest);
	
	/**
	 * Stores the scale of this matrix using a scalar in the matrix dest
	 * @param vec The vector to use to scale this matrix
	 * @param dest The matrix to store the scale of this matrix in
	 */
	public void scale(Vector3d vec, Matrix dest);

	/**
	 * Rotates this matrix the specified number of degrees along a desired axis
	 * @param degrees The number of degrees to rotate
	 * @param axis The axis to rotate on
	 */
	public void rotate(double degrees, Vector3f axis);
	
	/**
	 * Rotates the matrix dest using a specified number of degrees, a desired axis, and this matrix as a source
	 * @param degrees The number of degrees to rotate
	 * @param axis The axis to rotate on
	 * @param dest The matrix to store the result in
	 */
	public void rotate(double degrees, Vector3f axis, Matrix dest);
	/**
	 * Rotates this matrix the specified number of degrees along a desired axis
	 * @param degrees The number of degrees to rotate
	 * @param axis The axis to rotate on
	 */
	public void rotate(double degrees, Vector3d axis);

	/**
	 * Rotates the matrix dest using a specified number of degrees, a desired axis, and this matrix as a source
	 * @param degrees The number of degrees to rotate
	 * @param axis The axis to rotate on
	 * @param dest The matrix to store the result in
	 */
	public void rotate(double degrees, Vector3d axis, Matrix dest);
	
	/**
	 * Rotates this matrix the specified number of radians along a desired axis
	 * @param radians The number of radians to rotate
	 * @param axis The axis to rotate on
	 */
	public void rotateRadians(double radians, Vector3f axis);

	/**
	 * Rotates the matrix dest using a specified number of radians, a desired axis, and this matrix as a source
	 * @param radians The number of radians to rotate
	 * @param axis The axis to rotate on
	 * @param dest The matrix to store the result in
	 */
	public void rotateRadians(double radians, Vector3f axis, Matrix dest);
	
	/**
	 * Rotates this matrix the specified number of radians along a desired axis
	 * @param radians The number of radians to rotate
	 * @param axis The axis to rotate on
	 */
	public void rotateRadians(double radians, Vector3d axis);

	/**
	 * Rotates the matrix dest using a specified number of radians, a desired axis, and this matrix as a source
	 * @param radians The number of radians to rotate
	 * @param axis The axis to rotate on
	 * @param dest The matrix to store the result in
	 */
	public void rotateRadians(double radians, Vector3d axis, Matrix dest);

	/**
	 * Sets the desired matrix to the transpose of this one
	 * @param dest The matrix to store the transpose of this matrix in
	 */
	public void transpose(Matrix dest);

	/**
	 * Sets this matrix to the inverse of itself
	 */
	public void invert();

	/**
	 * Sets this matrix to the negated version of itself
	 */
	public void negate();

	/**
	 * Sets the desired matrix to the negated version of this matrix
	 * @param dest The matrix to store the the negated version of this matrix in
	 */
	public void negate(Matrix dest);
	
	/**
	 * Stores this matrix into the float buffer in column major ordering
	 * @param buf The buffer to write this matrix into
	 */
	public void store(FloatBuffer buf);
	
	/**
	 * Stores this matrix into the float buffer in row major ordering
	 * @param buf The buffer to write this matrix into
	 */
	public void storeTranspose(FloatBuffer buf);
	
	/**
	 * Stores this matrix into the float buffer in column major ordering, ignoring the last row and column (as a 3x3 matrix)
	 * @param buf The buffer to write this matrix into
	 */
	public void store3f(FloatBuffer buf);
	
	/**
	 * Stores this matrix into the double buffer in column major ordering
	 * @param buf The buffer to write this matrix into
	 */
	public void store(DoubleBuffer buf);

	/**
	 * Stores this matrix into the double buffer in row major ordering
	 * @param buf The buffer to write this matrix into
	 */
	public void storeTranspose(DoubleBuffer buf);

	/**
	 * Stores this matrix into the double buffer in column major ordering, ignoring the last row and column (as a 3x3 matrix)
	 * @param buf The buffer to write this matrix into
	 */
	public void store3f(DoubleBuffer buf);
	
	/**
	 * Returns the string representation of this matrix
	 * @return The string representation of this matrix
	 */
	public String toString();

}
