package hm.murdock.modules;

import hm.murdock.utils.Context;

public class Hello extends Module {

	public Hello(Context context) {
		super(context);
	}

	public void world() {
		this.getLogger().info("Hello from a module!");
	}
}
