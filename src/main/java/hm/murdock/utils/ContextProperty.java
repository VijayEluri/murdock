package hm.murdock.utils;

import hm.murdock.cli.Router;

/**
 * Enum-like class to handle all properties keys.
 * 
 * It can be used as parent class for inheritance and implement your own context
 * properties for your module.
 * 
 * @author Dario (i@dario.im)
 * 
 * @param <T>
 */
public class ContextProperty<T> {

	public static final ContextProperty<Router> ROUTER = new ContextProperty<Router>(
			"router", true);

	public static final ContextProperty<String> TEST = new ContextProperty<String>(
			"test");

	private static final String NAME = ContextProperty.class.getSimpleName();

	private final String id;

	private String fullId;

	private final boolean isFlash;

	private final boolean isPrivileged;

	protected ContextProperty(String id) {
		this(id, false);
	}

	protected ContextProperty(String id, boolean flash) {
		this(id, flash, false);
	}

	protected ContextProperty(String id, boolean flash, boolean privileged) {
		this.id = id;
		this.isFlash = flash;
		this.isPrivileged = privileged;
	}

	public final boolean isFlash() {
		return this.isFlash;
	}

	public final boolean isPrivileged() {
		return this.isPrivileged;
	}

	@Override
	public final String toString() {
		if (this.fullId == null) {
			String className = this.getClass().getSimpleName();
			if (!className.contains(NAME)) {
				throw new IllegalArgumentException(
						"Properties must be instaces of classes with names like Test"
								+ NAME);
			}

			String moduleName = className.substring(0, className.indexOf(NAME));
			if (moduleName.equals("")) {
				this.fullId = id;
			} else {
				this.fullId = moduleName + "." + id;
			}
		}

		return this.fullId;
	}
}
