//package io.skysail.server.weaving;
//
//import java.lang.reflect.Method;
//import org.osgi.framework.FrameworkUtil;
//
///**
// * @author arik
// */
//public class MethodEntry {
//	private final long id;
//
//	private final long moduleId;
//
//	private final Method method;
//
//	public MethodEntry(long id, Class<?> clazz, String methodName, String[] argumentTypeNames) {
//		this.id = id;
//		this.moduleId = FrameworkUtil.getBundle(clazz).getBundleId();
//
//		// resolve argument types
//		Class<?>[] argumentTypes = new Class<?>[argumentTypeNames.length];
//		for (int i = 0; i < argumentTypeNames.length; i++) {
//			String argumentTypeName = argumentTypeNames[i];
//			try {
//				argumentTypes[i] = resolveType(clazz.getClassLoader(), argumentTypeName);
//			} catch (ClassNotFoundException e) {
//				throw new IllegalStateException("Could not find argument type '" + argumentTypeName
//						+ "' for declaring method '" + clazz.getName() + "." + methodName + "': " + e.getMessage(), e);
//			}
//		}
//
//		// resolve method and cache it
//		try {
//			this.method = clazz.getDeclaredMethod(methodName, argumentTypes);
//		} catch (Throwable e) {
//			throw new IllegalStateException(
//					"Could not find method '" + clazz.getName() + "." + methodName + "': " + e.getMessage(), e);
//		}
//	}
//
//	public long getId() {
//		return this.id;
//	}
//
//	public long getModuleId() {
//		return this.moduleId;
//	}
//
//	public Method getMethod() {
//		return this.method;
//	}
//
//	@SuppressWarnings("RedundantIfStatement")
//	@Override
//	public boolean equals(Object o) {
//		if (this == o) {
//			return true;
//		}
//		if (o == null || getClass() != o.getClass()) {
//			return false;
//		}
//
//		MethodEntry that = (MethodEntry) o;
//
//		if (id != that.id) {
//			return false;
//		}
//
//		return true;
//	}
//
//	@Override
//	public int hashCode() {
//		return (int) (id ^ (id >>> 32));
//	}
//
//	@Override
//	public String toString() {
//		return "MethodEntry[id=" + this.id + ",moduleId=" + moduleId + ",method=" + this.method.toGenericString() + "]";
//	}
//
//	private Class<?> resolveType(ClassLoader classLoader, String parameterTypeName) throws ClassNotFoundException {
//		StringBuilder arrBuf = new StringBuilder(20);
//		while (parameterTypeName.endsWith("[]")) {
//			arrBuf.append("[");
//			parameterTypeName = parameterTypeName.substring(0, parameterTypeName.length() - 2);
//		}
//
//		boolean isArray = arrBuf.length() > 0;
//		switch (parameterTypeName) {
//		case "boolean":
//			return isArray ? Class.forName(arrBuf + "Z") : boolean.class;
//		case "byte":
//			return isArray ? Class.forName(arrBuf + "B") : byte.class;
//		case "char":
//			return isArray ? Class.forName(arrBuf + "C") : char.class;
//		case "double":
//			return isArray ? Class.forName(arrBuf + "D") : double.class;
//		case "float":
//			return isArray ? Class.forName(arrBuf + "F") : float.class;
//		case "int":
//			return isArray ? Class.forName(arrBuf + "I") : int.class;
//		case "long":
//			return isArray ? Class.forName(arrBuf + "J") : long.class;
//		case "short":
//			return isArray ? Class.forName(arrBuf + "S") : short.class;
//		default:
//			return isArray ? Class.forName(arrBuf + "L" + parameterTypeName + ";", true, classLoader)
//					: classLoader.loadClass(parameterTypeName);
//		}
//	}
//}