package hm.murdock.modules;

import hm.murdock.utils.Context;

import org.slf4j.Logger;

/**
 * 
 * @author Dario (i@dario.im)
 * 
 */
public abstract class Module {

	private Context context;

	public Module(Context context) {
		this.context = context;
	}

	public final Context getContext() {
		return this.context;
	}

	public final Logger getLogger() {
		return this.getContext().getLogger();
	}
}
