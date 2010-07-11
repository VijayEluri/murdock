package ba.stascus.exceptions;

/**
 * Represents an exception while loading or calling a module.
 * 
 * @author Dario (i@dario.im)
 * 
 */
public final class ActionException extends StascusException {

	private static final long serialVersionUID = -465468986842430155L;

	public ActionException(ActionExceptionType type, Object... args) {
		super(type, args);
	}

	public ActionException(ActionExceptionType type, Throwable cause,
			Object... args) {
		super(type, cause, args);
	}

	public enum ActionExceptionType implements StascusExceptionType {
		UNABLE_INVOKE, UNABLE_ACCESS;

		public String getMessage(Object... args) {
			String format = null;
			String message = null;

			switch (this) {
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
				message = String.format(format, args);
			}

			return message;
		}
	}

	@Override
	public ActionExceptionType getType() {
		return (ActionExceptionType) this.getInnerType();
	}
}
