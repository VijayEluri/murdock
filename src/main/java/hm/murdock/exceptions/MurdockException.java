package hm.murdock.exceptions;

public abstract class MurdockException extends Exception {

	private static final long serialVersionUID = -951449883507589064L;

	private MurdockExceptionType type;

	protected MurdockException() {
		super();
	}

	public MurdockException(MurdockExceptionType type, Object... args) {
		super(type.getMessage(args));
		this.type = type;
	}

	public MurdockException(MurdockExceptionType type, Throwable cause,
			Object... args) {
		super(type.getMessage(args), cause);
		this.type = type;
	}

	protected final MurdockExceptionType getInnerType() {
		return this.type;
	}

	public abstract MurdockExceptionType getType();
}
