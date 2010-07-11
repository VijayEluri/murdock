package ba.stascus.exceptions;

/**
 * Represents an exception while loading or calling a module.
 * 
 * @author Dario (i@dario.im)
 * 
 */
public final class ActionException extends StascusException {

	private static final long serialVersionUID = -465468986842430155L;

	public enum ActionExceptionType {
		UNABLE_INVOKE, UNABLE_ACCESS
	}

	private ActionException(String message, ActionExceptionType type) {
		super(message, type);
	}

	private ActionException(String message, Throwable cause,
			ActionExceptionType type) {
		super(message, cause, type);
	}

	protected static ActionException factory(ActionExceptionType type,
			Throwable cause, Object... args) {
		String format = null;
		String message = null;

		switch (type) {
		case UNABLE_INVOKE:
			format = "Unable to invoke action '%s'";
			break;
		case UNABLE_ACCESS:
			format = "Unable to access action '%s'";
			break;
		default:
			message = "Unknown";
			break;
		}

		if (format != null) {
			message = format(format, args);
		}

		return (cause == null) ? new ActionException(message, type)
				: new ActionException(message, cause, type);
	}

	public ActionExceptionType getType() {
		return (ActionExceptionType) super.getInnerType();
	}
}
