package ba.stascus.cli;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class Router {

	public Router() {
		String prefix = getCurrentPackage() + ".modules";
		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.filterInputsBy(
						new FilterBuilder().include(FilterBuilder
								.prefix(prefix)))
				.setUrls(ClasspathHelper.getUrlsForPackagePrefix(prefix))
				.setScanners(new TypeAnnotationsScanner()));

		// reflections.getTypesAnnotatedWith(null);
	}

	/**
	 * Extracts the current package for this class, assuming it is placed one
	 * sub-package below app's package.
	 * 
	 * This is useful to don't have string constants with package info
	 * hard-coded.
	 * 
	 * @return App's package (e.g. im.dario.stascus) without final dot.
	 */
	private String getCurrentPackage() {
		String routerClassName = Router.class.getCanonicalName();
		Integer lastDot = routerClassName.lastIndexOf('.');

		return routerClassName.substring(0, lastDot);
	}
}
