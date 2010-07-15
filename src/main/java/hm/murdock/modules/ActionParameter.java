package hm.murdock.modules;

import hm.murdock.exceptions.ActionException;
import hm.murdock.exceptions.ActionException.ActionExceptionType;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class ActionParameter {
	private AccessibleObject deserializer;

	private final String name;

	private boolean isArray = false;

	private boolean isConstructor = false;

	private final int position;

	private final Set<Annotation> annotations = new HashSet<Annotation>();

	private final Set<Class<? extends Annotation>> annotationClasses = new HashSet<Class<? extends Annotation>>();

	private final Class<?> klass;

	/**
	 * This constructor checks the next methods, using the first find, even if
	 * it is not public:
	 * <ul>
	 * <li>klass.valueOf(String)</li>
	 * <li>&lt;init&gt;(String) - Constructor</li>
	 * </ul>
	 * 
	 * @param parameterNames
	 * 
	 * @param klass
	 * @param annotations
	 * @param i
	 * @return
	 * @throws ActionException
	 */
	public ActionParameter(String name, Class<?> klass,
			Annotation[] annotations, int position) throws ActionException {
		this.annotations.addAll(Arrays.asList(annotations));
		this.position = position;
		this.name = name;

		for (Annotation annotation : this.annotations) {
			this.annotationClasses.add(annotation.annotationType());
		}

		Class<?> component = klass.getComponentType();
		if (component != null) {
			klass = component;
			this.isArray = true;
		}

		this.klass = klass;
		try {
			this.deserializer = klass
					.getDeclaredMethod("valueOf", String.class);
		} catch (Exception e) {
			try {
				this.deserializer = (AccessibleObject) klass
						.getDeclaredConstructor(String.class);
				this.isConstructor = true;
			} catch (Exception e1) {
				throw new ActionException(
						ActionExceptionType.CLASS_NOT_SUPPORTED,
						klass.getSimpleName());
			}
		}

		this.deserializer.setAccessible(true);
	}

	public Object deserialize(String value) {
		try {
			if (this.isConstructor) {
				Constructor<?> constructor = (Constructor<?>) deserializer;
				return constructor.newInstance(value);
			} else {
				Method method = (Method) deserializer;
				method.invoke(null, value);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}

		return null;
	}

	public Class<?> getDeclaredClass() {
		return this.klass;
	}

	public int getPosition() {
		return this.position;
	}

	public boolean isArray() {
		return this.isArray;
	}

	public boolean isOptional() {
		return this.annotationClasses.contains(Optional.class);
	}

	public boolean canOmitFlag() {
		return this.annotationClasses.contains(CanOmitFlag.class);
	}

	public boolean handlesLeftOverArgs() {
		return this.annotationClasses.contains(LeftOver.class);
	}

	public String getName() {
		return this.name;
	}
}
