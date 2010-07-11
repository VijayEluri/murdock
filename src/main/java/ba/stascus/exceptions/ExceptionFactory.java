package ba.stascus.exceptions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ba.stascus.Stascus;

/**
 * Represents an exception while loading or calling a module.
 * 
 * @author Dario (i@dario.im)
 * 
 */
public final class ExceptionFactory {

	private static final long serialVersionUID = 4816230884125915819L;

	private ExceptionFactory() {
		// Unavailable constructor.
	}

	public static <T> T get(Class<T> klass, Enum<?> type, String... args) {
		return get(klass, type, null, args);
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> klass, Enum<?> type, Throwable cause,
			String... args) {
		T exception = null;
		Class<?> exceptionClass = type.getClass().getEnclosingClass();

		if (exceptionClass == null) {
			throw new IllegalArgumentException(
					"Expected an enum of an StascusException subclass");
		}

		final Logger logger = LoggerFactory.getLogger(Stascus.NAME);
		try {
			Method factory = exceptionClass.getDeclaredMethod("factory",
					type.getClass(), Throwable.class, Object[].class);
			exception = (T) factory.invoke(null, type, cause, args);
		} catch (SecurityException e) {
			logger.debug(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			logger.debug(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			logger.debug(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.debug(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			logger.debug(e.getMessage(), e);
		}

		return exception;
	}
}
