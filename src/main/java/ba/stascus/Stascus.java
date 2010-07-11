package ba.stascus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ba.stascus.cli.Router;
import ba.stascus.exceptions.ActionException;
import ba.stascus.exceptions.ConfigurationException;
import ba.stascus.exceptions.MultipleRoutingException;
import ba.stascus.exceptions.RoutingException;
import ba.stascus.modules.Action;
import ba.stascus.utils.Context;

/**
 * B.A. Stascus: I'm gonna end this, fool!
 * 
 * What's this 'bout? This is your fist against your overwhelming stream of
 * tasks, like your Twitter stream, you know.
 * 
 * Stascus is an app that aims to be easy and fast to use to keep track of your
 * tasks.
 * 
 * It features:
 * <ul>
 * <li>REST-like command line interface (Play! framework inspired)</li>
 * <li>Modules support for enhanced functionality</li>
 * </ul>
 * 
 * @author Dario (i@dario.im)
 * 
 */
public final class Stascus {

	public static final String VERSION = "0.0.1";

	public static final String NAME = Stascus.class.getSimpleName();

	private final Router router;

	private final Logger logger;

	public Stascus() throws ConfigurationException {
		Context context = new Context();
		this.router = new Router(context);

		this.logger = LoggerFactory.getLogger(Stascus.NAME);
	}

	/**
	 * Based in command-line arguments, delegates them to the corresponding
	 * module.
	 * 
	 * If multiple modules handle the same action name, it shows all
	 * possibilities.
	 * 
	 * @param args
	 */
	public void delegate(String... args) {
		try {
			this.router.route(args);
		} catch (MultipleRoutingException e) {
			logger.info("Your command matches various actions in different modules. Please, specify which one wants typing its full name or set up an alias:");
			for (Action action : e.getAvailableActions().values()) {
				logger.info("\t" + action.toString());
			}
		} catch (RoutingException e) {
			logger.error(e.getLocalizedMessage());
		} catch (ActionException e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	public static void main(String... args) throws ConfigurationException {
		Stascus stascus = new Stascus();
		stascus.delegate(args);
	}
}
