package hm.murdock.modules;

import hm.murdock.Murdock;
import hm.murdock.utils.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Dario (i@dario.im)
 * 
 */
public final class Help extends Module {

	private final Logger logger = LoggerFactory.getLogger(Help.class
			.getSimpleName());

	public Help(Context context) {
		super(context);
	}

	public void help() {
		// TODO Enhance with dynamic module help registration.
		logger.info("No help :)");
	}

	public void version() {
		logger.info(Murdock.NAME + " version " + Murdock.VERSION);
	}
}
