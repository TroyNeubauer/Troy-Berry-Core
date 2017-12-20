package com.troyberry.util;

import java.io.*;
import java.lang.reflect.*;
import java.nio.*;

import com.troyberry.internal.*;

import sun.misc.Unsafe;

/**
 * A class that has some static miscellaneous utilities.
 * 
 * @author Troy Neubauer
 *
 */
@SuppressWarnings("restriction")
public class MiscUtil {

	private static final Unsafe unsafe = retriveUnsafe();

	private static final String UNSAFE_CLASS = "sun.misc.Unsafe", DISABLE_UNSAFE_ARG = "TBDisableUnsafe";

	private static final long NIO_BUFFER_ADDRESS_OFFSET = findBufferAddress();

	public static boolean isBigEndian() {
		return ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
	}

	public static boolean isLittleEndian() {
		return ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN;
	}

	public static Class<?> getGenericType(Object obj) {
		return (Class<?>) ((ParameterizedType) obj.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public static boolean isClassLoaded(String binaryName) {
		try {
			Method m = ClassLoader.class.getDeclaredMethod("findLoadedClass", new Class[] { String.class });
			m.setAccessible(true);

			return m.invoke(LibraryUtils.class.getClassLoader(), binaryName) != null;
		} catch (Exception e) {
			// Ignore
		}
		return false;
	}

	/**
	 * Returns an Enum object representing the enum declared in class {@code class} with the ordinal {@code ordinal}
	 * 
	 * @param clazz
	 *            The class to look in. Must be an enum class
	 * @param ordinal
	 *            The ordinal of the enum to look for
	 * @return the enum declared in class {@code class} with the ordinal {@code ordinal} {@link Enum#ordinal()}<br>
	 *         {@link Enum}
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getEnum(Class<T> clazz, int ordinal) {
		assert clazz.isEnum();
		try {
			Method m = clazz.getDeclaredMethod("values");
			Object values = m.invoke(null);
			Enum<?>[] thing = (Enum<?>[]) values;
			for (Enum<?> e : thing) {
				if (e.ordinal() == ordinal)
					return (T) e;
			}
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(clazz + " is not an enum!");
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			InternalLog.println("Illegal enum reflection! " + clazz + " requested ordinal " + ordinal);
		}
		return null;

	}

	/**
	 * Returns {@code true} if the class a contains b in the hierarchy
	 * 
	 * @param baseClass
	 *            The class's hierarchy to use
	 * @param lookFor
	 *            The class to compare to
	 * @return {@code true} if the class a contains b in a superclass, otherwise false
	 */
	public static boolean classSharesSuperClassOrInterface(Class<?> baseClass, Class<?> lookFor) {
		if (baseClass == lookFor)
			return true;
		Class<?> origionalBaseClass = baseClass;
		while ((baseClass = baseClass.getSuperclass()) != null)
			if (baseClass == lookFor)
				return true;
		return classImplementsInterface(origionalBaseClass, lookFor);
	}

	public static boolean classImplementsInterface(Class<?> baseClass, Class<?> lookFor) {
		for (Class<?> interfac : baseClass.getInterfaces()) {
			if (interfac == lookFor)
				return true;
			if (classImplementsInterface(interfac, lookFor))
				return true;
		}
		return false;
	}

	/**
	 * Returns the memory address of the given object
	 * 
	 * @param obj
	 *            The object to locate the address of
	 * @return The memory address of Object obj
	 */
	public static long memoryAddress(Object obj) {
		if (unsafe == null)
			throw new IllegalStateException("Unsafe is NOT unsupported!! So the memory address cannot be retrived!!!");
		Object helperArray[] = new Object[] { obj };
		long baseOffset = unsafe.arrayBaseOffset(Object[].class);
		long addressOfObject = unsafe.getLong(helperArray, baseOffset);
		return addressOfObject;
	}

	/**
	 * Returns JVM type signature for given class.
	 */
	public static String getClassSignature(Class<?> cl) {
		StringBuilder sbuf = new StringBuilder();
		while (cl.isArray()) {
			sbuf.append('[');
			cl = cl.getComponentType();
		}
		if (cl.isPrimitive()) {
			if (cl == Integer.TYPE) {
				sbuf.append('I');
			} else if (cl == Byte.TYPE) {
				sbuf.append('B');
			} else if (cl == Long.TYPE) {
				sbuf.append('J');
			} else if (cl == Float.TYPE) {
				sbuf.append('F');
			} else if (cl == Double.TYPE) {
				sbuf.append('D');
			} else if (cl == Short.TYPE) {
				sbuf.append('S');
			} else if (cl == Character.TYPE) {
				sbuf.append('C');
			} else if (cl == Boolean.TYPE) {
				sbuf.append('Z');
			} else if (cl == Void.TYPE) {
				sbuf.append('V');
			} else {
				throw new InternalError();
			}
		} else {
			sbuf.append('L' + cl.getName().replace('.', '/') + ';');
		}
		return sbuf.toString();
	}

	/**
	 * Gets the class for a signature (only handles one signature at a time)
	 * 
	 * @param signature
	 *            The signature
	 * @return The class representing the specified signature
	 * @throws ClassNotFoundException
	 */
	public static Class<?> getClassFromSignature(String signature) throws ClassNotFoundException {
		if (signature.isEmpty())
			throw new IllegalArgumentException("Signature cannot be empty!");
		if (signature.charAt(0) == '[') {
			if (signature.contains("L"))
				return Array.newInstance(Class.forName(signature.substring(1, signature.length()).replace("/", ".")), 1).getClass();
			int arrayDimensions = signature.lastIndexOf('[') + 1;
			char type = signature.charAt(signature.length() - 1);
			if (arrayDimensions == 1) {
				if (type == 'B')
					return byte[].class;
				if (type == 'S')
					return short[].class;
				if (type == 'C')
					return char[].class;
				if (type == 'I')
					return int[].class;
				if (type == 'J')
					return long[].class;
				if (type == 'F')
					return float[].class;
				if (type == 'D')
					return double[].class;
				if (type == 'Z')
					return boolean[].class;
			} else if (arrayDimensions == 2) {
				if (type == 'B')
					return byte[][].class;
				if (type == 'S')
					return short[][].class;
				if (type == 'C')
					return char[][].class;
				if (type == 'I')
					return int[][].class;
				if (type == 'J')
					return long[][].class;
				if (type == 'F')
					return float[][].class;
				if (type == 'D')
					return double[][].class;
				if (type == 'Z')
					return boolean[][].class;
			} else if (arrayDimensions == 3) {
				if (type == 'B')
					return byte[][][].class;
				if (type == 'S')
					return short[][][].class;
				if (type == 'C')
					return char[][][].class;
				if (type == 'I')
					return int[][][].class;
				if (type == 'J')
					return long[][][].class;
				if (type == 'F')
					return float[][][].class;
				if (type == 'D')
					return double[][][].class;
				if (type == 'Z')
					return boolean[][][].class;
			} else {
				throw new ClassNotFoundException("MiscUtil cannot find primitive array classes with more that 3 dimensions");
			}

		} else {
			if (signature.contains("L"))
				return Class.forName(signature.substring(1, signature.length() - 1).replace("/", "."));
			char type = signature.charAt(0);
			if (type == 'B')
				return byte.class;
			if (type == 'S')
				return short.class;
			if (type == 'C')
				return char.class;
			if (type == 'I')
				return int.class;
			if (type == 'J')
				return long.class;
			if (type == 'F')
				return float.class;
			if (type == 'D')
				return double.class;
			if (type == 'Z')
				return boolean.class;
			if (type == 'V')
				return void.class;
		}
		return null;
	}

	private static Unsafe retriveUnsafe() {
		String disableUnsafe = System.getProperty(DISABLE_UNSAFE_ARG, "false");
		if(disableUnsafe.equals("true") || disableUnsafe.equals("t") || disableUnsafe.equals("1")) {
			InternalLog.println(UNSAFE_CLASS + " Is diaabled because of the vm arg " + DISABLE_UNSAFE_ARG + " was set to \"" +System.getProperty(DISABLE_UNSAFE_ARG) + "\"");
			return null;
		}
		try {
			Class<?> unsafeClass = null;
			try {
				unsafeClass = Class.forName(UNSAFE_CLASS);
			} catch (ClassNotFoundException e) {
				InternalLog.println("Unable to find Unsafe Class \"" + UNSAFE_CLASS + "\"");
				return null;
			}
			Field[] fields = unsafeClass.getDeclaredFields();
			for (Field field : fields) {
				try {
					field.setAccessible(true);
					Unsafe cast = (Unsafe) field.get(null);
					InternalLog.println("Successfully retrived Unsafe instance. Avilable for use with MiscUtil.getUnsafe()");
					return cast;
				} catch (ClassCastException e) {
					// Ignore, there might be other static fields
				}
			}
		} catch (Exception e) {
		}
		InternalLog.println("Failed to retrive Unsafe instance");
		return null;
	}

	// 4MB buffer
	private static final byte[] BUFFER = new byte[(int) (BinarySize.MEGABYTE.getValue() * 4)];

	/**
	 * copy input to output stream
	 */
	public static void copy(InputStream input, OutputStream output) throws IOException {
		int bytesRead;
		while ((bytesRead = input.read(BUFFER)) != -1) {
			output.write(BUFFER, 0, bytesRead);
		}
	}

	/**
	 * Reads the desired file to a String assuming it exists
	 * 
	 * @param file
	 *            The file to read
	 * @return The String representing the file
	 * @throws IOException
	 *             If the file doen't not exist or if a I/O error occurs while reading the file
	 */
	public static String readToString(MyFile file) throws IOException {
		StringBuilder sb = new StringBuilder(512);
		BufferedReader reader = file.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

	/**
	 * Reads the desired file to a String assuming it exists
	 * 
	 * @param file
	 *            The file to read
	 * @return The String representing the file
	 * @throws IOException
	 *             If the file doen't not exist or if a IO error occurs while reading the file
	 */
	public static String readToString(File file) throws IOException {
		StringBuilder sb = new StringBuilder(512);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		reader.close();
		return sb.toString();
	}

	/**
	 * Reads the desired file to a byte array
	 * 
	 * @param file
	 *            The file to read
	 * @return The data in the file
	 * @throws IOException
	 *             If the file doen't not exist or if a IO error occurs while reading the file
	 */
	public static byte[] readToByteArray(File file) throws IOException {
		if (file == null)
			throw new NullPointerException("File can't be null!");
		byte[] data = new byte[(int) file.length()];
		FileInputStream stream = new FileInputStream(file);
		stream.read(data);
		stream.close();
		return data;
	}

	/**
	 * Reads the desired file to a byte array
	 * 
	 * @param file
	 *            The file to read
	 * @return The data in the file
	 * @throws IOException
	 *             If the file doen't not exist or if a IO error occurs while reading the file
	 */
	public static byte[] readToByteArray(MyFile file) throws IOException {
		return readToByteArray(new BufferedInputStream(file.getInputStream()));
	}

	/**
	 * Reads the all the available bytes from the desired input stream
	 * 
	 * @param stream
	 *            The stream to read from
	 * @return The data in the file
	 * @throws IOException
	 *             If the file doen't not exist or if a IO error occurs while reading the file
	 */
	public static byte[] readToByteArray(InputStream stream) throws IOException {
		byte[] bytes = new byte[stream.available()];
		stream.read(bytes);
		return bytes;
	}

	/**
	 * Generates a long from a string by adding the ASCII codes together. The long is more of less random but the same string will always return
	 * the same result.</br>
	 * This is useful for using a string to seed a random number generator. In that case, the user will be able to enter something like "Troy is
	 * cool" and the output of random numbers would always be the same as long as "Troy is cool" was the seed. However, as soon as the user
	 * changes what they entered to lets say "Troy is awesome", the seed would be different.
	 * 
	 * @param s
	 *            The string to convert
	 * @return A long representing the string that can be used scenarios
	 */
	public static long getlong(String s) {
		long number = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			number += ((int) c) * ((long) Math.pow(10, i));
		}
		return number;
	}

	public static String getStackTrace(Throwable error) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		error.printStackTrace(pw);
		return sw.toString();
	}

	public static String epochToString(long time) {
		return new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(time));
	}

	public static boolean isUnsafeSupported() {
		return unsafe != null;
	}

	public static Unsafe getUnsafe() {
		if (unsafe == null)
			throw new IllegalStateException("Unsafe is not supported");
		return unsafe;
	}

	private MiscUtil() {

	}

	public static String getExtension(String name) {
		int index = name.lastIndexOf('.');
		if (index == -1)
			return null;
		return name.substring(index + 1);
	}

	/**
	 * Attempts to parse a string into an int using {@link Integer#parseInt(String, int)}. If the string cannot be parsed, the error message
	 * along with the unparsable string will be printed to {@link System#err} and the default value will be returned. Otherwise string's parsed
	 * int value will be returned
	 * 
	 * @param str
	 *            The string to parse
	 * @param base
	 *            the base to parse the string into
	 * @param errorMessage
	 *            The error message to be printed if parsing fails
	 * @param defaultValue
	 *            The default value to be returned if parsing fails
	 * @return The parsed string, or the default value if parsing fails
	 */
	public static int getIntOrDefaultValue(String str, int base, String errorMessage, int defaultValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			System.err.println(errorMessage + " \"" + str + "\"");
		}
		return defaultValue;
	}

	/**
	 * Attempts to parse a string into an int using {@link Integer#parseInt(String, int)}. If the string cannot be parsed, then the runnable will
	 * be run and the default value will be returned. Otherwise string's parsed integer value will be returned
	 * 
	 * @param str
	 *            The string to parse
	 * @param base
	 *            the base to parse the string into
	 * @param runnable
	 *            The runnable to run if parsing fails
	 * @param defaultValue
	 *            The default value to be returned if parsing fails
	 * @return The parsed string, or the default value if parsing fails
	 */
	public static int getIntOrRunnableAndDefValue(String str, int base, Runnable runnable, int defaultValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			runnable.run();
		}
		return defaultValue;
	}

	/**
	 * Attempts to parse a string into an int using {@link Integer#parseInt(String)}. If the string cannot be parsed, the error message along
	 * with the unparsable string will be printed to {@link System#err} and the default value will be returned. Otherwise string's parsed int
	 * value will be returned.<br>
	 * This is equivalent to calling {@code MiscUtil.getIntOrDefaultValue(str, 10, errorMessage, defaultValue)}
	 * 
	 * @param str
	 *            The string to parse
	 * @param errorMessage
	 *            The error message to be printed if parsing fails
	 * @param defaultValue
	 *            The default value to be returned if parsing fails
	 * @return The parsed string, or the default value if parsing fails
	 */
	public static int getIntOrDefaultValue(String str, String errorMessage, int defaultValue) {
		return getIntOrDefaultValue(str, 10, errorMessage, defaultValue);
	}

	/**
	 * Attempts to parse a string into an int using {@link Integer#parseInt(String, int)}. If the string cannot be parsed, then the runnable will
	 * be run and the default value will be returned. Otherwise string's parsed integer value will be returned
	 * 
	 * @param str
	 *            The string to parse
	 * @param errorMessage
	 *            The error message to be printed if parsing fails
	 * @param defaultValue
	 *            The default value to be returned if parsing fails
	 * @return The parsed string, or the default value if parsing fails
	 */
	public static int getIntOrRunnableAndDefValue(String str, Runnable runnable, int defaultValue) {
		return getIntOrRunnableAndDefValue(str, 10, runnable, defaultValue);
	}

	public static final java.lang.reflect.Field getDeclaredField(Class<?> root, String fieldName) throws NoSuchFieldException {
		Class<?> type = root;
		do {
			try {
				java.lang.reflect.Field field = type.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {
				type = type.getSuperclass();
			} catch (SecurityException e) {
				type = type.getSuperclass();
			}
		} while (type != null);
		throw new NoSuchFieldException(fieldName + " does not exist in " + root.getName() + " or any of its superclasses.");
	}

	public static final long address(Buffer buffer) {
		if (!buffer.isDirect() || unsafe == null) {
			throw new UnsupportedOperationException(
					"Unable to get Nio Buffer address! " + (unsafe == null ? "Unsafe is not supported" : "Buffer is not direct!"));
		}
		return unsafe.getLong(buffer, NIO_BUFFER_ADDRESS_OFFSET);
	}

	private static long findBufferAddress() {
		try {
			return unsafe.objectFieldOffset(getDeclaredField(Buffer.class, "address"));
		} catch (Exception e) {
			throw new UnsupportedOperationException("Could not detect ByteBuffer.address offset", e);
		}
	}

	public static void writeByteArray(File file, byte[] bytes) throws IOException {
		FileOutputStream stream = new FileOutputStream(file);
		stream.write(bytes);
		stream.close();
	}
}
