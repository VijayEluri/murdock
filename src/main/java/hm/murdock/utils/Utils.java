package hm.murdock.utils;

import java.io.PrintWriter;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

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

	public static void printHelp(PrintWriter writer, String commandLineSyntax,
			Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(writer, HelpFormatter.DEFAULT_WIDTH,
				commandLineSyntax, null, options,
				HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD,
				null, true);
	}

	public static <E, T extends E> void copyArray(E[] dest, T[] src) {
		if (dest.length < src.length) {
			throw new IndexOutOfBoundsException("Source does not fit in dest");
		}

		System.arraycopy(src, 0, dest, 0, src.length);
	}
}
