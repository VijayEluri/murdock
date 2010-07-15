package hm.murdock.modules;

import hm.murdock.exceptions.ActionException;
import hm.murdock.exceptions.ActionException.ActionExceptionType;
import hm.murdock.utils.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Locale;


/**
 * Represents an action provided by a module. It is associated to a method,
 * which name is used to match with the first command-line argument.
 * 
 * @author Dario (i@dario.im)
 * 
 */
public final class Action implements Comparable<Object> {

	private final ActionParameters parameters;

	private final Method method;

	private final Class<? extends Module> moduleClass;

	private final String name;

	public Action(Class<? extends Module> moduleClass, Method method)
			throws ActionException {
		this.parameters = new ActionParameters(method);

		this.method = method;
		this.moduleClass = moduleClass;
		this.name = this.moduleClass.getSimpleName().toLowerCase(Locale.US)
				+ ":" + this.method.getName();
	}

	// TODO Memoize created object?
	public void invoke(Context context, String... arguments)
			throws ActionException {
		try {
			Constructor<? extends Module> constructor = moduleClass
					.getConstructor(Context.class);
			Module module = constructor.newInstance(context);
			method.invoke(module, parameters.handle(arguments));
		} catch (InstantiationException e) {
			throw new ActionException(ActionExceptionType.UNABLE_INVOKE, e,
					this.toString());
		} catch (IllegalAccessException e) {
			throw new ActionException(ActionExceptionType.UNABLE_ACCESS, e,
					this.toString());
		} catch (Exception e) {
			throw new ActionException(ActionExceptionType.INVOKING_ERROR, e,
					this.toString());
		}
	}

	@Override
	public String toString() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (this.getClass() != obj.getClass()) {
			return false;
		}

		Action other = (Action) obj;
		if (method == null) {
			if (other.method != null) {
				return false;
			}
		} else {
			if (!method.equals(other.method)) {
				return false;
			}
		}

		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else {
			if (!name.equals(other.name)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Compare actions by its full name.
	 * 
	 * We just assume that there is no possible way to have two classes with
	 * same name under the same package.
	 */
	public int compareTo(Object o) {
		return this.toString().compareTo(o.toString());
	}
}