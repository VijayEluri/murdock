package ba.stascus.exceptions;

public abstract class StascusException extends Exception {

	private static final long serialVersionUID = -951449883507589064L;

	private StascusExceptionType type;

	protected StascusException() {
		super();
	}

	public StascusException(StascusExceptionType type, Object... args) {
		super(type.getMessage(args));
		this.type = type;
	}

	public StascusException(StascusExceptionType type, Throwable cause,
			Object... args) {
		super(type.getMessage(args), cause);
		this.type = type;
	}

	protected final StascusExceptionType getInnerType() {
		return this.type;
	}

	public abstract StascusExceptionType getType();
}
