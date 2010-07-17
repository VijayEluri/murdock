package hm.murdock.modules.action;

import hm.murdock.modules.Module;
import hm.murdock.utils.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ActionHook {

	private final Module moduleInstance;

	private final Method hook;

	public ActionHook(Method hook, Context context) {
		Module instance = null;
		try {
			Constructor<?> constructor = hook.getDeclaringClass()
					.getConstructor(Context.class);
			instance = (Module) constructor.newInstance(context);
		} catch (Exception e) {
			context.getLogger().warn("Ignoring hook " + hook.toString());
		}

		this.moduleInstance = instance;
		this.hook = hook;
	}

	public void invoke() {
		if (this.hook != null) {
			try {
				this.hook.invoke(this.moduleInstance);
			} catch (Exception e) {
				this.moduleInstance.getLogger().warn(
						"Error while invoking hook", e);
			}
		}
	}
}
