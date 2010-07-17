package hm.murdock.modules.action;

import hm.murdock.modules.annotations.Hook;
import hm.murdock.modules.annotations.Hook.HookType;
import hm.murdock.utils.Context;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.TreeSet;

public class ActionHooks {

	private final Set<ActionHook> preHooks;

	private final Set<ActionHook> postHooks;

	public ActionHooks() {
		this.preHooks = new TreeSet<ActionHook>();
		this.postHooks = new TreeSet<ActionHook>();
	}

	public void addHook(Hook hook, Method hookMethod, Context context) {
		this.getHooks(hook.type()).add(new ActionHook(hookMethod, context));
	}

	public void invokeHooks(HookType type) {
		for (ActionHook hook : this.getHooks(type)) {
			hook.invoke();
		}
	}

	private Set<ActionHook> getHooks(HookType type) {
		Set<ActionHook> hooks;
		switch (type) {
		case PRE:
			hooks = this.preHooks;
			break;
		case POST:
			hooks = this.postHooks;
			break;
		default:
			hooks = new TreeSet<ActionHook>();
		}
		return hooks;
	}
}
