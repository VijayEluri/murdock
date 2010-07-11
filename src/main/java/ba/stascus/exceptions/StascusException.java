package ba.stascus.exceptions;

import java.util.Formatter;

public abstract class StascusException extends Exception {

	private final Enum<?> type;

	private static final long serialVersionUID = -951449883507589064L;

	private static final Formatter FMT = new Formatter();

	public StascusException() {
		super();
		this.type = null;
	}

	public StascusException(String message) {
		this(message, (Enum<?>) null);
	}

	public StascusException(String message, Enum<?> type) {
		super(message);
		this.type = type;
	}

	public StascusException(String message, Throwable cause) {
		this(message, cause, null);
	}

	public StascusException(String message, Throwable cause, Enum<?> type) {
		super(message, cause);
		this.type = type;
	}

	protected final Enum<?> getInnerType() {
		return this.type;
	}

	protected static String format(String format, Object... args) {
		String result = FMT.format(format, args).toString();
		FMT.flush();

		return result;
	}
}
