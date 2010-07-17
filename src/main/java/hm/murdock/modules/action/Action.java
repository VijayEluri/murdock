package hm.murdock.modules.action;

import hm.murdock.Murdock;
import hm.murdock.exceptions.ActionException;
import hm.murdock.exceptions.ActionException.ActionExceptionType;
import hm.murdock.modules.Module;
import hm.murdock.modules.Addon;
import hm.murdock.modules.annotations.Hook;
import hm.murdock.modules.annotations.Hook.HookType;
import hm.murdock.utils.Context;
import hm.murdock.utils.Utils;

import java.io.PrintWriter;
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

	private final ActionHooks hooks;

	public Action(Class<? extends Module> moduleClass, Method method)
			throws ActionException {
		this.hooks = new ActionHooks();
		this.parameters = new ActionParameters(method);

		this.method = method;
		this.moduleClass = moduleClass;
		this.name = buildName(this.moduleClass, this.method.getName());
	}

	// TODO Memoize created object?
	public void invoke(Context context, String... arguments)
			throws ActionException {
		try {
			this.hooks.invokeHooks(HookType.PRE);

			Constructor<? extends Module> constructor = this.moduleClass
					.getConstructor(Context.class);
			Addon module = constructor.newInstance(context);
			this.method.invoke(module, this.parameters.handle(arguments));

			this.hooks.invokeHooks(HookType.POST);
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

	public boolean canApply(Hook hook) {
		return this.name.equals(buildName(hook.module(), hook.action()));
	}

	private String buildName(Class<? extends Module> actionModule,
			String actionName) {
		return actionModule.getSimpleName().toLowerCase(Locale.US) + ":"
				+ actionName;
	}

	public void addHook(Hook hook, Method hookMethod, Context context) {
		this.hooks.addHook(hook, hookMethod, context);
	}

	public void printHelp(PrintWriter writer) {
		Utils.printHelp(writer, Murdock.NAME + " " + this.name,
				this.parameters.getOptions());
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
