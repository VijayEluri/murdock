package hm.murdock.modules;

import hm.murdock.modules.annotations.Hook;
import hm.murdock.modules.annotations.Hook.HookType;
import hm.murdock.utils.Context;

public class HelloHook extends Helper {

	public HelloHook(Context context) {
		super(context);
	}

	@Hook(module = Hello.class, action = "world", type = HookType.PRE)
	public void hello() {
		this.getLogger().debug("Hello from a pre-hook!");
	}

	@Hook(module = Hello.class, action = "world", type = HookType.POST)
	public void bye() {
		this.getLogger().debug("Bye from a post-hook!");
	}
}
