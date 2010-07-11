package ba.stascus.exceptions;

/**
 * Represents an exception while loading or calling a module.
 * 
 * @author Dario (i@dario.im)
 * 
 */
public class RoutingException extends StascusException {

	public enum RoutingExceptionType {
		FOOL_ME, MISSING_ARGUMENTS, NOT_VALID_COMMAND, UNKNOWN
	}

	private static final long serialVersionUID = -4855173328667224615L;

	protected RoutingException() {
		super();
	}

	private RoutingException(String message, RoutingExceptionType type) {
		super(message, type);
	}

	private RoutingException(String message, Throwable cause,
			RoutingExceptionType type) {
		super(message, cause, type);
	}

	protected static RoutingException factory(RoutingExceptionType type,
			Throwable cause, Object... args) {
		String format = null;
		String message = null;

		switch (type) {
		case FOOL_ME:
			message = "Are you trying to fool me?";
			break;
		case MISSING_ARGUMENTS:
			message = "Arguments not provided";
			break;
		case NOT_VALID_COMMAND:
			format = "'%s' is not a valid command";
			break;
		default:
			message = "Unknown";
			break;
		}

		if (format != null) {
			message = format(format, args);
		}

		return (cause == null) ? new RoutingException(message, type)
				: new RoutingException(message, cause, type);
	}

	public final RoutingExceptionType getType() {
		return (RoutingExceptionType) super.getInnerType();
	}
}
