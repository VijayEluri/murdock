package hm.murdock;

import hm.murdock.cli.Router;
import hm.murdock.exceptions.ActionException;
import hm.murdock.exceptions.ConfigurationException;
import hm.murdock.exceptions.MultipleRoutingException;
import hm.murdock.exceptions.RoutingException;
import hm.murdock.modules.action.Action;
import hm.murdock.utils.Context;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Murdock is an easy and ligtweight framework for command line apps.
 * 
 * CLI apps have been with us a long time and they deserve a new brand framework
 * in these GUI times. BTW, GUI are evil, period.
 * 
 * This is the main class where all the magic starts. At this point, Murdock
 * comprises two parts:
 * 
 * <ul>
 * <li>The Context: single point to access Murdock's configuration and
 * environment (although there is nothing that keeps you from working your own
 * way).</li>
 * <li>The Router: main dispatcher which matches the given command (first
 * paramater) with the corresponding module/action.</li>
 * <ul>
 * <li>If only a word is provided, it tries to match an action but shows all the
 * matching actions if there is more than one.</li>
 * <li>If a command with module:action syntax is provided, it looks for its
 * corresponding module and action.</li>
 * </ul>
 * </ul>
 * 
 * We must keep in mind that module name used here is the name of the action's
 * containing class in lower case. Murdock does not handle CamelCase names so
 * (e.g.) it will be camelcase, not camel_case.
 * 
 * @author Dario (i@dario.im)
 * 
 */
public final class Murdock {

	/**
	 * Current version.
	 * 
	 * TODO Probably it should be overridable.
	 */
	public static final String VERSION = "0.0.1";

	/**
	 * Name of the framework. It started as Baracus so why keep hard-coded
	 * references to its name?
	 */
	public static final String NAME = Murdock.class.getSimpleName()
			.toLowerCase(Locale.US);

	private final Router router;

	private final Logger logger;

	public Murdock() throws ConfigurationException {
		this(new Context());
	}

	public Murdock(Context context) {
		this.router = new Router(context);
		this.logger = LoggerFactory.getLogger(Murdock.NAME);
	}

	/**
	 * Based in command-line arguments, delegates them to the corresponding
	 * module.
	 * 
	 * If multiple modules handle the same action name, it shows all
	 * possibilities, without executing them.
	 * 
	 * @param arguments
	 */
	public void delegate(String... arguments) {
		try {
			this.router.route(arguments);
		} catch (MultipleRoutingException e) {
			logger.info(e.getLocalizedMessage());
			for (Action action : e.getAvailableActions().values()) {
				logger.info("\t" + action.toString());
			}
		} catch (RoutingException e) {
			logger.error(e.getLocalizedMessage(), e);
		} catch (ActionException e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 
	 * @param args
	 * @throws ConfigurationException
	 *             If there is any problem while loading configuration.
	 */
	public static void main(String... args) throws ConfigurationException {
		Murdock murdock = new Murdock();
		murdock.delegate(args);
	}
}
