package hm.murdock.exceptions;

/**
 * Represents an exception while loading, updating or saving configuration.
 * 
 * @author Dario (i@dario.im)
 * 
 */
public final class ConfigurationException extends MurdockException {

	private static final long serialVersionUID = -712453084854927282L;

	public ConfigurationException(ConfigurationExceptionType type,
			Object... args) {
		super(type, args);
	}

	public ConfigurationException(ConfigurationExceptionType type,
			Throwable cause, Object... args) {
		super(type, cause, args);
	}

	public enum ConfigurationExceptionType implements MurdockExceptionType {
		UNABLE_CREATE_HOME, INVALID_FORMAT_CONFIG, UNABLE_ACCESS_CONFIG, UNABLE_READ_CONFIG, UNABLE_WRITE_CONFIG;

		public String getMessage(Object... args) {
			String format = null;
			String message = null;

			switch (this) {
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
				message = String.format(format, args);
			}

			return message;
		}
	}

	@Override
	public ConfigurationExceptionType getType() {
		return (ConfigurationExceptionType) this.getInnerType();
	}
}
