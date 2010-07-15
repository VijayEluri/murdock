package hm.murdock.exceptions;

/**
 * Represents an exception while loading or calling a module.
 * 
 * @author Dario (i@dario.im)
 * 
 */
public class RoutingException extends MurdockException {

	private static final long serialVersionUID = -4855173328667224615L;

	protected RoutingException() {
		super();
	}

	public RoutingException(RoutingExceptionType type, Object... args) {
		super(type, args);
	}

	public RoutingException(RoutingExceptionType type, Throwable cause,
			Object... args) {
		super(type, cause, args);
	}

	public enum RoutingExceptionType implements MurdockExceptionType {
		FOOL_ME, MISSING_ARGUMENTS, NOT_VALID_COMMAND, MULTIPLE_ROUTES;

		public String getMessage(Object... args) {
			String format = null;
			String message = null;

			switch (this) {
			case FOOL_ME:
				message = "Are you trying to fool me?";
				break;
			case MISSING_ARGUMENTS:
				message = "Arguments not provided";
				break;
			case NOT_VALID_COMMAND:
				format = "'%s' is not a valid command";
				break;
			case MULTIPLE_ROUTES:
				format = "Your command matches various actions in different modules. Please, specify which one wants typing its full name or set up an alias";
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
	public final RoutingExceptionType getType() {
		return (RoutingExceptionType) this.getInnerType();
	}
}
