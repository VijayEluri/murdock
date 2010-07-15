package hm.murdock.utils;

public final class Utils {

	private Utils() {
		// Unavailable constructor.
	}

	/**
	 * Extracts the current parent package for this class.
	 * 
	 * This is useful to don't have string constants with package info
	 * hard-coded.
	 * 
	 * @return App's package (e.g. hm.murdock) without final dot.
	 */
	public static String getCurrentParentPackage(Class<?> klass) {
		String routerClassName = klass.getCanonicalName();
		Integer lastDot = routerClassName.lastIndexOf('.');

		/**
		 * Our lastDot index points to the real last dot but we are interested
		 * in the previous one, so we search from lastDot - 1 position to get
		 * it.
		 * 
		 * <pre>
		 *  (e.g. hm.murdock.                cli.Router)
		 *                  ^ (we want that)    ^ (lastDot)
		 * </pre>
		 */
		return routerClassName.substring(0,
				routerClassName.lastIndexOf('.', lastDot - 1));
	}
}
