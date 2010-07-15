package hm.murdock.modules;

import hm.murdock.utils.Context;

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
}
