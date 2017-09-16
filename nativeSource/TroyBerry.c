#include <stdio.h>      // Default System Calls
#include <stdlib.h>     // Needed for OS X

#include <winsock2.h>
#include <Ws2tcpip.h>


/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_troyberry_util_serialization_TroyBufferUnsafe */

#ifndef _Included_com_troyberry_util_serialization_TroyBufferUnsafe
#ifdef __cplusplus
extern "C" {
#endif

	/**
	* Reads length of a file into a buffer. Closes the file
	*/
	jlong readFromFile(FILE * file, void * address, long offset, size_t length) {
		fseek(file, offset, SEEK_SET);
		jlong readCount = (jlong) fread(address, 1, length, file);
		fclose(file);
		return readCount;
	}

	//Methods for manipulating java arrays

	//Theese methods copy bytes from an array to native memory
	JNIEXPORT void JNICALL Java_com_troyberry_util_serialization_NativeTroyBufferUtil_ncpyBytesFrom(JNIEnv * env, jclass class, jlong dest, jbyteArray src, jint offset, jint elements) {
		(*env)->GetByteArrayRegion(env, src, offset, elements, (jbyte*)dest);
	}

	JNIEXPORT void JNICALL Java_com_troyberry_util_serialization_NativeTroyBufferUtil_ncpyShortsFrom(JNIEnv * env, jclass class, jlong dest, jshortArray src, jint offset, jint elements, jboolean swapEndianness) {
		jshort* pointer = (jshort*)dest;
		(*env)->GetShortArrayRegion(env, src, offset, elements, pointer);
		if (swapEndianness) {
			for (int i = 0; i < elements; i++) {
				pointer[i] = (jshort)(((pointer[i] & 0xFF00) >> 8) | (pointer[i] << 8));
			}
		}
	}

	JNIEXPORT void JNICALL Java_com_troyberry_util_serialization_NativeTroyBufferUtil_ncpyCharsFrom(JNIEnv * env, jclass class, jlong dest, jcharArray src, jint offset, jint elements, jboolean swapEndianness) {
		jchar* pointer = (jchar*)dest;
		(*env)->GetCharArrayRegion(env, src, offset, elements, pointer);
		if (swapEndianness) {
			for (int i = 0; i < elements; i++) {
				pointer[i] = (jchar)(((pointer[i] & 0xFF00) >> 8) | (pointer[i] << 8));
			}
		}
	}

	JNIEXPORT void JNICALL Java_com_troyberry_util_serialization_NativeTroyBufferUtil_ncpyIntsFrom(JNIEnv * env, jclass class, jlong dest, jintArray src, jint offset, jint elements, jboolean swapEndianness) {
		jint* pointer = (jint*)dest;
		(*env)->GetIntArrayRegion(env, src, offset, elements, pointer);
		if (swapEndianness) {
			for (int i = 0; i < elements; i++) {
				jint element = pointer[i];
				pointer[i] = (jint)(element << 24) | ((element & 0xff00) << 8) | ((element >> 8) & 0xff00) | (element >> 24);
			}
		}
	}

	JNIEXPORT void JNICALL Java_com_troyberry_util_serialization_NativeTroyBufferUtil_ncpyLongsFrom(JNIEnv * env, jclass class, jlong dest, jlongArray src, jint offset, jint elements, jboolean swapEndianness) {
		(*env)->GetLongArrayRegion(env, src, offset, elements, (jlong*)dest);
	}

	JNIEXPORT void JNICALL Java_com_troyberry_util_serialization_NativeTroyBufferUtil_ncpyFloatsFrom(JNIEnv * env, jclass class, jlong dest, jfloatArray src, jint offset, jint elements, jboolean swapEndianness) {
		(*env)->GetFloatArrayRegion(env, src, offset, elements, (jfloat*)dest);
	}

	JNIEXPORT void JNICALL Java_com_troyberry_util_serialization_NativeTroyBufferUtil_ncpyDoublesFrom(JNIEnv * env, jclass class, jlong dest, jdoubleArray src, jint offset, jint elements, jboolean swapEndianness) {
		(*env)->GetDoubleArrayRegion(env, src, offset, elements, (jdouble*)dest);
	}
	

	//Theese methods copy bytes native memory to an array

	JNIEXPORT void JNICALL Java_com_troyberry_util_serialization_NativeTroyBufferUtil_ncpyBytesTo(JNIEnv * env, jclass class, jbyteArray dest, jlong src, jint offset, jint elements) {
		(*env)->SetByteArrayRegion(env, dest, offset, elements, (jbyte*)src);
	}

	JNIEXPORT void JNICALL Java_com_troyberry_util_serialization_NativeTroyBufferUtil_ncpyShortsTo(JNIEnv * env, jclass class, jshortArray dest, jlong src, jint offset, jint elements, jboolean swapEndianness) {
		(*env)->SetShortArrayRegion(env, dest, offset, elements, (jshort*)src);
	}
	
	JNIEXPORT void JNICALL Java_com_troyberry_util_serialization_NativeTroyBufferUtil_ncpyCharsTo(JNIEnv * env, jclass class, jcharArray dest, jlong src, jint offset, jint elements, jboolean swapEndianness) {
		(*env)->SetCharArrayRegion(env, dest, offset, elements, (jchar*)src);
	}

	JNIEXPORT void JNICALL Java_com_troyberry_util_serialization_NativeTroyBufferUtil_ncpyIntsTo(JNIEnv * env, jclass class, jintArray dest, jlong src, jint offset, jint elements, jboolean swapEndianness) {
		(*env)->SetIntArrayRegion(env, dest, offset, elements, (jint*)src);
	}

	JNIEXPORT void JNICALL Java_com_troyberry_util_serialization_NativeTroyBufferUtil_ncpyLongsTo(JNIEnv * env, jclass class, jlongArray dest, jlong src, jint offset, jint elements, jboolean swapEndianness) {
		(*env)->SetLongArrayRegion(env, dest, offset, elements, (jlong*)src);
	}

	JNIEXPORT void JNICALL Java_com_troyberry_util_serialization_NativeTroyBufferUtil_ncpyFloatsTo(JNIEnv * env, jclass class, jfloatArray dest, jlong src, jint offset, jint elements, jboolean swapEndianness) {
		(*env)->SetFloatArrayRegion(env, dest, offset, elements, (jfloat*)src);
	}

	JNIEXPORT void JNICALL Java_com_troyberry_util_serialization_NativeTroyBufferUtil_ncpyDoublesTo(JNIEnv * env, jclass class, jdoubleArray dest, jlong src, jint offset, jint elements, jboolean swapEndianness) {
		(*env)->SetDoubleArrayRegion(env, dest, offset, elements, (jdouble*)src);
	}




	//wrapper for memcpy
	JNIEXPORT void JNICALL Java_com_troyberry_util_serialization_NativeTroyBufferUtil_nmemcpy(JNIEnv * env, jclass class, jlong dest, jlong src, jlong bytes) {
		memcpy((jbyte*)dest, (jbyte*)src, (size_t)bytes);
	}

	//Writes a buffer to a file
	JNIEXPORT jboolean JNICALL Java_com_troyberry_util_serialization_NativeTroyBufferUtil_nwriteToFile(JNIEnv * env, jclass class, jstring name, jlong address, jlong length) {
		const char * nativeName = (*env)->GetStringUTFChars(env, name, NULL);
		if (nativeName == NULL)
			return JNI_FALSE;
		FILE * file = fopen(nativeName, "wb");
		if (file == NULL)
			return JNI_FALSE;

		size_t writtenCount = fwrite((jbyte*)address, 1, length, file);
		if ((jlong)writtenCount != length)
			return JNI_FALSE;

		(*env)->ReleaseStringUTFChars(env, name, nativeName);
		fclose(file);
		return JNI_TRUE;
	}

	//Copies a desired chunk from a file to native memory
	JNIEXPORT jboolean JNICALL Java_com_troyberry_util_serialization_NativeTroyBufferUtil_ncopyFileChunk(JNIEnv * env, jclass class, jlong longAddress, jstring name, jlong offset, jlong length) {
		const char * nativeName = (*env)->GetStringUTFChars(env, name, NULL);
		FILE * file = fopen(nativeName, "rb");
		if (file == NULL)
			return JNI_FALSE;
		jbyte * address = (jbyte*)longAddress;
		if (address == NULL)
			return JNI_FALSE;
		readFromFile(file, address, (long)offset, length);

		(*env)->ReleaseStringUTFChars(env, name, nativeName);
		return JNI_TRUE;
	}

	//Copies length of a file to native memory
	JNIEXPORT jboolean JNICALL Java_com_troyberry_util_serialization_NativeTroyBufferUtil_ncopyFileSubset(JNIEnv * env, jclass class, jlong longAddress, jstring name, jlong length) {
		return Java_com_troyberry_util_serialization_NativeTroyBufferUtil_ncopyFileChunk(env,class,longAddress,name,0,length);
	}

#ifdef __cplusplus
}
#endif
#endif