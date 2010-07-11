package ba.stascus.exceptions;

/**
 * Represents an exception while loading, updating or saving configuration.
 * 
 * @author Dario (i@dario.im)
 * 
 */
public final class ConfigurationException extends StascusException {

	private static final long serialVersionUID = -712453084854927282L;

	public enum ConfigurationExceptionType {
		UNABLE_CREATE_HOME, INVALID_FORMAT_CONFIG, UNABLE_ACCESS_CONFIG, UNABLE_READ_CONFIG, UNABLE_WRITE_CONFIG
	}

	private ConfigurationException(String message,
			ConfigurationExceptionType type) {
		super(message, type);
	}

	private ConfigurationException(String message, Throwable cause,
			ConfigurationExceptionType type) {
		super(message, cause, type);
	}

	protected static ConfigurationException factory(
			ConfigurationExceptionType type, Throwable cause, Object... args) {
		String format = null;
		String message = null;

		switch (type) {
		case INVALID_FORMAT_CONFIG:
			message = "Invalid format: expected XML Properties format";
			break;
		case UNABLE_CREATE_HOME:
			format = "Unable to create %s user directory at %s";
			break;
		case UNABLE_ACCESS_CONFIG:
			format = "Unable to access configuration file at %s";
			break;
		case UNABLE_READ_CONFIG:
			format = "Unable to read configuration file at %s";
			break;
		case UNABLE_WRITE_CONFIG:
			format = "Unable to write configuration file at %s";
			break;
		default:
			message = "Unknown";
			break;
		}

		if (format != null) {
			message = format(format, args);
		}

		return (cause == null) ? new ConfigurationException(message, type)
				: new ConfigurationException(message, cause, type);
	}

	public ConfigurationExceptionType getType() {
		return (ConfigurationExceptionType) super.getInnerType();
	}
}
