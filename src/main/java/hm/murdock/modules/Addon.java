package hm.murdock.modules;

import hm.murdock.utils.Context;

import org.slf4j.Logger;

public abstract class Addon {

	private final Context context;

	public Addon(Context context) {
		this.context = context;
	}

	public final Context getContext() {
		return this.context;
	}

	public final Logger getLogger() {
		return this.getContext().getLogger();
	}
}