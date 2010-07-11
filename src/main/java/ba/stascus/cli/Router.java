package ba.stascus.cli;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import ba.stascus.exceptions.ActionException;
import ba.stascus.exceptions.ExceptionFactory;
import ba.stascus.exceptions.MultipleRoutingException;
import ba.stascus.exceptions.RoutingException;
import ba.stascus.exceptions.RoutingException.RoutingExceptionType;
import ba.stascus.modules.Action;
import ba.stascus.modules.Module;
import ba.stascus.utils.Context;
import ba.stascus.utils.Utils;

/**
 * Handles command-line args to the corresponding module's action.
 * 
 * @author Dario (i@dario.im)
 * 
 */
public final class Router {

	private final Context context;

	private final Map<String, Map<String, Action>> actions;

	/**
	 * Constructor.
	 * 
	 * Through Reflections it loads all the modules available in classpath and
	 * map their actions using their names.
	 * 
	 * TODO Memoize modules (only load if modules directory (or its jars) is
	 * newer than our cache).
	 * 
	 * @param context
	 */
	public Router(Context context) {
		this.context = context;

		String modulesPackage = Utils.getCurrentParentPackage(Router.class)
				+ ".modules";
		Reflections reflections = new Reflections(modulesPackage,
				new SubTypesScanner());

		Set<Class<? extends Module>> modules = reflections
				.getSubTypesOf(Module.class);

		this.actions = new HashMap<String, Map<String, Action>>();
		for (Class<? extends Module> module : modules) {
			for (Method method : module.getMethods()) {
				String name = method.getName();
				Map<String, Action> nameActions = actions.get(name);

				if (nameActions == null) {
					nameActions = new HashMap<String, Action>();
				}

				Action action = new Action(module, method);
				nameActions.put(action.toString(), action);
				actions.put(name, nameActions);
			}
		}
	}

	/**
	 * Route to the module based in the first argument provided.
	 * 
	 * TODO Implement alias feature.
	 * 
	 * @param args
	 *            Command-line arguments.
	 * @throws RoutingException
	 * @throws ActionException
	 */
	public void route(String... args) throws RoutingException, ActionException {
		if (args == null) {
			// TODO Maybe route to Help:help action?
			throw ExceptionFactory.get(RoutingException.class,
					RoutingExceptionType.MISSING_ARGUMENTS);
		}

		if (args.length == 0) {
			// TODO Maybe route to Help:help action?
			throw ExceptionFactory.get(RoutingException.class,
					RoutingExceptionType.MISSING_ARGUMENTS);
		}

		if (args[0] == null) {
			// TODO Maybe route to Help:help action?
			throw ExceptionFactory.get(RoutingException.class,
					RoutingExceptionType.MISSING_ARGUMENTS);
		}

		String actionCommand = args[0].toLowerCase(Locale.US).trim();
		if (actionCommand.equals("")) {
			throw ExceptionFactory.get(RoutingException.class,
					RoutingExceptionType.FOOL_ME);
		}

		Action action = getAction(actionCommand);
		action.invoke(context); // TODO Arguments handling
	}

	private Action getAction(String actionCommand) throws RoutingException {
		String actionName = null;
		String[] actionData = actionCommand.split(":");

		switch (actionData.length) {
		case 1:
			actionName = actionData[0];
			break;
		case 2:
			actionName = actionData[1];
			break;
		default:
			throw ExceptionFactory.get(RoutingException.class,
					RoutingExceptionType.NOT_VALID_COMMAND, actionCommand);
		}

		Map<String, Action> availableActions = this.actions.get(actionName);
		if (availableActions == null) {
			throw ExceptionFactory.get(RoutingException.class,
					RoutingExceptionType.NOT_VALID_COMMAND, actionCommand);
		}

		Action action = null;
		if (availableActions.size() > 1) {
			if (actionData.length > 1) {
				action = availableActions.get(actionCommand);
			} else {
				throw new MultipleRoutingException(availableActions);
			}
		} else {
			/*
			 * There is only one action available.
			 * 
			 * No need to check if we get zero actions, because it is impossible
			 * because we check first if there are actions related with
			 * actionCommand in the first level map.
			 */
			action = availableActions.values().iterator().next();
		}

		if (action == null) {
			throw ExceptionFactory.get(RoutingException.class,
					RoutingExceptionType.NOT_VALID_COMMAND, actionCommand);
		}

		return action;
	}
}
