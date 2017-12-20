package com.troyberry.util.serialization;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Type.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

import org.objectweb.asm.*;

import com.troyberry.util.*;

public class TroySerializationBank {

	private static final HashMap<Class<?>, BaseSerializer<?>> map;

	static {
		map = new HashMap<Class<?>, BaseSerializer<?>>();
		Serializers.init();
	}

	public static void add(BaseSerializer<?> serializer) {
		map.put(serializer.getType(), serializer);
	}

	static <T> BaseSerializer<T> lookUp(Class<T> clazz) {
		BaseSerializer<T> fromMap = (BaseSerializer<T>) map.get(clazz);
		if (fromMap != null) {
			return fromMap;
		}
		for (Entry<Class<?>, BaseSerializer<?>> entry : map.entrySet()) {
			if (entry.getKey().isAssignableFrom(clazz)) {// We have one for the superclass
				map.put(clazz, entry.getValue());
				return (BaseSerializer<T>) entry.getValue();
			}
		}
		return createDynamicSerializer(clazz);
	}

	private static <T> TroySerializer<T> createDynamicSerializer(Class<T> clazz) {
		System.out.println("Creating dynamic serializer for class " + clazz);
		String name = clazz.getName();
		try {
			String classType = getInternalName(clazz);
			String newClassName = "TroySerializer$$" + clazz.getName().replace('.', '_');
			String newNameComplete = (TroySerializationBank.class.getPackage().getName()).replace('.', '/') + '/' + newClassName;

			ClassWriter cw = new ClassWriter(0);
			MethodVisitor mv;

			cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, newNameComplete, "Lcom/troyberry/util/serialization/TroySerializer<L" + classType + ";>;",
					"com/troyberry/util/serialization/TroySerializer", null);

			{
				mv = cw.visitMethod(ACC_PUBLIC, "read",
						"(L" + classType + ";Lcom/troyberry/util/serialization/TroySerializationFile;Lcom/troyberry/util/serialization/TroyBuffer;)V", null,
						null);
				mv.visitCode();
				new ClassReader(name).accept(new ClassAnalyzer(clazz.getName(), classType, mv, true), ClassReader.SKIP_CODE);
				mv.visitInsn(RETURN);
				mv.visitMaxs(3, 4);
				mv.visitEnd();
			}
			{
				mv = cw.visitMethod(ACC_PUBLIC, "write",
						"(L" + classType + ";Lcom/troyberry/util/serialization/TroySerializationFile;Lcom/troyberry/util/serialization/TroyBuffer;)V", null,
						null);
				mv.visitCode();
				new ClassReader(name).accept(new ClassAnalyzer(clazz.getName(), classType, mv, false), ClassReader.SKIP_CODE);
				mv.visitInsn(RETURN);
				mv.visitMaxs(3, 4);
				mv.visitEnd();
			}
			{
				mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "write",
						"(Ljava/lang/Object;Lcom/troyberry/util/serialization/TroySerializationFile;Lcom/troyberry/util/serialization/TroyBuffer;)V", null,
						null);
				mv.visitCode();
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitTypeInsn(CHECKCAST, classType);
				mv.visitVarInsn(ALOAD, 2);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitMethodInsn(INVOKEVIRTUAL, newNameComplete, "write",
						"(L" + classType + ";Lcom/troyberry/util/serialization/TroySerializationFile;Lcom/troyberry/util/serialization/TroyBuffer;)V", false);
				mv.visitInsn(RETURN);
				mv.visitMaxs(4, 4);
				mv.visitEnd();
			}
			{
				mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "read",
						"(Ljava/lang/Object;Lcom/troyberry/util/serialization/TroySerializationFile;Lcom/troyberry/util/serialization/TroyBuffer;)V", null,
						null);
				mv.visitCode();
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitTypeInsn(CHECKCAST, classType);
				mv.visitVarInsn(ALOAD, 2);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitMethodInsn(INVOKEVIRTUAL, newNameComplete, "read",
						"(L" + classType + ";Lcom/troyberry/util/serialization/TroySerializationFile;Lcom/troyberry/util/serialization/TroyBuffer;)V", false);
				mv.visitInsn(RETURN);
				mv.visitMaxs(4, 4);
				mv.visitEnd();
			}
			cw.visitEnd();

			byte[] bytes = cw.toByteArray();
			try {
				MiscUtil.writeByteArray(new File(newClassName + ".class"), bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}

			Class<?> newClass = new ByteClassLoader().defineClass(null, bytes);

			TroySerializer<T> result = (TroySerializer<T>) SerializationUtil.newInstance(newClass);
			map.put(clazz, result);
			return result;

		} catch (IOException e1) {
			throw new RuntimeException("Unable to locate class file " + name, e1);
		}
	}

	private static class ClassAnalyzer extends ClassVisitor {
		private static final String TROY_BUFFER_CLASS_TYPE = getInternalName(TroyBuffer.class);
		private static final String TROY_SERIALIZATION_FILE_CLASS_TYPE = getInternalName(TroyBuffer.class);

		private MethodVisitor mv;
		private boolean read;
		private String classType;

		public ClassAnalyzer(String name, String classType, MethodVisitor mv, boolean read) throws IOException {
			super(ASM5);
			this.classType = classType;
			this.mv = mv;
			this.read = read;
		}

		@Override
		public void visit(int arg0, int arg1, String arg2, String arg3, String arg4, String[] arg5) {
		}

		@Override
		public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
			return null;
		}

		@Override
		public void visitAttribute(Attribute arg0) {
		}

		@Override
		public void visitEnd() {
		}

		@Override
		public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
			if ((access & ACC_STATIC) == ACC_STATIC || (access & ACC_TRANSIENT) == ACC_TRANSIENT)
				return null;// We don't care about static fields or transient ones

			try {
				Class<?> clazz = MiscUtil.getClassFromSignature(desc);
				System.out.println("desc " +desc);
				if (clazz.isPrimitive()) {
					String longName = clazz.getName();
					if (read) {
						readTroyBuffer(longName, name, desc);
					} else {
						writeTroyBuffer(longName, name, desc);
					}
				} else {
					if (read) {
						readTroySerializationFile(name, desc);
					} else {
						writeTroySerializationFile(name, desc);
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		private void writeTroySerializationFile(String fieldName, String fieldType) {
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(GETFIELD, classType, fieldName, fieldType);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/troyberry/util/serialization/TroySerializationFile", "write", "(Ljava/lang/Object;)V", false);
		}

		private void readTroySerializationFile(String fieldName, String fieldType) {
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/troyberry/util/serialization/TroySerializationFile", "read", "()Ljava/lang/Object;", false);
			mv.visitTypeInsn(CHECKCAST, fieldType.substring(1, fieldType.length() - 1));
			mv.visitFieldInsn(PUTFIELD, classType, fieldName, fieldType);

		}

		private void readTroyBuffer(String methodName, String fieldName, String fieldType) {
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, TROY_BUFFER_CLASS_TYPE, "read" + StringFormatter.capitalizeFirstLetter(methodName), "()" + fieldType, false);
			mv.visitFieldInsn(PUTFIELD, classType, fieldName, fieldType);
		}

		private void writeTroyBuffer(String methodName, String fieldName, String fieldType) {
			mv.visitVarInsn(ALOAD, 3);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(GETFIELD, classType, fieldName, fieldType);
			mv.visitMethodInsn(INVOKEVIRTUAL, TROY_BUFFER_CLASS_TYPE, "write" + StringFormatter.capitalizeFirstLetter(methodName), "(" + fieldType + ")V",
					false);

		}

		@Override
		public void visitInnerClass(String arg0, String arg1, String arg2, int arg3) {
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			return null;
		}

		@Override
		public void visitOuterClass(String arg0, String arg1, String arg2) {
		}

		@Override
		public void visitSource(String arg0, String arg1) {
		}

	}

	public static void printAccess(String name, int access) {
		System.out.println("Access for:" + name + ":" + StringFormatter.toBinaryString(access));
		System.out.print('\t');
		boolean first = true;
		for (Field f : Opcodes.class.getFields()) {
			if (f.getName().startsWith("ACC")) {
				if (f.getType() == Integer.TYPE) {
					try {
						int value = f.getInt(null);
						if ((access & value) == value) {
							if (!first)
								System.out.print(", ");
							System.out.print(f.getName().replace("ACC_", "").toLowerCase());
							first = false;
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println();
	}
}