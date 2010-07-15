package hm.murdock.exceptions;

/**
 * Represents an exception while loading or calling a module.
 * 
 * @author Dario (i@dario.im)
 * 
 */
public final class ActionException extends MurdockException {

	private static final long serialVersionUID = -465468986842430155L;

	public ActionException(ActionExceptionType type, Object... args) {
		super(type, args);
	}

	public ActionException(ActionExceptionType type, Throwable cause,
			Object... args) {
		super(type, cause, args);
	}

	public enum ActionExceptionType implements MurdockExceptionType {
		UNABLE_INVOKE, UNABLE_ACCESS, CLASS_NOT_SUPPORTED, UNABLE_ASSIGN_SHORT_OPTION, INVOKING_ERROR;

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
			case CLASS_NOT_SUPPORTED:
				format = "Argument class %1$s is not supported because it is missing %1$s.valueOf(String) or <init>(String) constructor";
				break;
			case UNABLE_ASSIGN_SHORT_OPTION:
				format = "Unable to assign short option for '%s'";
			case INVOKING_ERROR:
				if (args.length == 0) {
					message = "Error while invoking";
				} else {
					format = "Error while invoking action '%s'";
				}
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
