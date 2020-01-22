import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Starter implements java.util.function.Consumer<String> {

	@Override
	public void accept(String t) {
		try {
			Class<?> c = Class.forName(t);
			Constructor<?> constructor = c.getConstructor();
			try {
				Object o = ((Constructor<?>) constructor).newInstance();
				Method[] m = o.getClass().getDeclaredMethods();

				for (Method method : m) {
					Class<?>[] cc = method.getParameterTypes();
					if (Modifier.isPublic(method.getModifiers())) {
						if (cc.length == 0 || (cc[0] == String.class && cc.length == 1)) {
							if (method.isAnnotationPresent(MethodToStart.class)
									&& !method.isAnnotationPresent(MethodDisabled.class)) {
								int count = method.getAnnotation(MethodToStart.class).value();
								for (int i = 0; i < count; i++) {
									if (method.isAnnotationPresent(StringParameter.class)) {
										String s = method.getAnnotation(StringParameter.class).value();
										method.invoke(o, s);
									}
									else
										method.invoke(o);
								}
							}
						}
					}
			}
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

}
