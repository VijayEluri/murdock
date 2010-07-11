package ba.stascus.modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ba.stascus.Stascus;
import ba.stascus.utils.Context;

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
		logger.info("Help!");
	}

	public void version() {
		logger.info(Stascus.NAME + " version " + Stascus.VERSION);
	}
}
