package hm.murdock.cli;

import hm.murdock.exceptions.ActionException;
import hm.murdock.exceptions.ConfigurationException;
import hm.murdock.exceptions.MultipleRoutingException;
import hm.murdock.exceptions.RoutingException;
import hm.murdock.exceptions.RoutingException.RoutingExceptionType;
import hm.murdock.modules.Module;
import hm.murdock.modules.action.Action;
import hm.murdock.modules.annotations.Hook;
import hm.murdock.utils.Context;
import hm.murdock.utils.ContextProperty;
import hm.murdock.utils.Utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

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
		try {
			this.context.setProperty(ContextProperty.ROUTER, this);
		} catch (ConfigurationException e) {
			/*
			 * Yeah, this is impossible :) In setProperty method only can throw
			 * ConfigurationException if we write to disk and ROUTER property is
			 * a flash property, so it never will write to the damn disk!
			 */
			this.context.getLogger().debug("Impossible error", e);
		}

		String modulesPackage = Utils.getCurrentParentPackage(Router.class)
				+ ".modules";
		Reflections reflections = new Reflections(modulesPackage,
				new SubTypesScanner());

		Set<Class<? extends Module>> modules = reflections
				.getSubTypesOf(Module.class);

		Set<Method> notOverridableMethods = new HashSet<Method>();
		notOverridableMethods.addAll(Arrays.asList(Object.class.getMethods()));

		Map<String, Map<Hook, Method>> hooks = new HashMap<String, Map<Hook, Method>>();
		this.actions = new HashMap<String, Map<String, Action>>();

		for (Class<? extends Module> module : modules) {
			for (Method method : module.getMethods()) {
				if (notOverridableMethods.contains(method)) {
					continue;
				}

				Hook hookAnnotation = method.getAnnotation(Hook.class);

				// If it is not a hook...
				if (hookAnnotation == null) {
					String name = method.getName();
					Map<String, Action> nameActions = actions.get(name);

					if (nameActions == null) {
						nameActions = new HashMap<String, Action>();
					}

					try {
						Action action = new Action(module, method);
						nameActions.put(action.toString(), action);
						actions.put(name, nameActions);
					} catch (ActionException e) {
						this.context.getLogger().warn(
								"Ignoring action " + method.getName(), e);
					}
				} else {
					String actionName = hookAnnotation.action();
					Map<Hook, Method> actionHooks = hooks.get(actionName);

					if (actionHooks == null) {
						actionHooks = new HashMap<Hook, Method>();
					}

					actionHooks.put(hookAnnotation, method);
					hooks.put(actionName, actionHooks);
				}
			}
		}

		for (String actionName : hooks.keySet()) {
			Map<Hook, Method> actionHooks = hooks.get(actionName);
			for (Hook hook : actionHooks.keySet()) {
				Map<String, Action> actionsAvailable = this.actions
						.get(actionName);
				if (actionsAvailable == null) {
					this.context.getLogger().warn(
							"Ignoring hook " + hook.toString());
				} else {
					for (Action action : actionsAvailable.values()) {
						if (action.canApply(hook)) {
							action.addHook(hook, actionHooks.get(hook),
									this.context);
						}
					}
				}
			}
		}
	}

	/**
	 * Route to the module based in the first argument provided.
	 * 
	 * TODO Implement alias feature.
	 * 
	 * @param arguments
	 *            Command-line arguments.
	 * @throws RoutingException
	 * @throws ActionException
	 */
	public void route(String... arguments) throws RoutingException,
			ActionException {
		if (arguments == null) {
			// TODO Maybe route to Help:help action?
			throw new RoutingException(RoutingExceptionType.MISSING_ARGUMENTS);
		}

		if (arguments.length == 0) {
			// TODO Maybe route to Help:help action?
			throw new RoutingException(RoutingExceptionType.MISSING_ARGUMENTS);
		}

		if (arguments[0] == null) {
			// TODO Maybe route to Help:help action?
			throw new RoutingException(RoutingExceptionType.MISSING_ARGUMENTS);
		}

		String actionCommand = arguments[0].toLowerCase(Locale.US).trim();
		if (actionCommand.equals("")) {
			throw new RoutingException(RoutingExceptionType.FOOL_ME);
		}

		String[] actionArguments = new String[arguments.length - 1];
		System.arraycopy(arguments, 1, actionArguments, 0,
				actionArguments.length);

		Action action = getAction(actionCommand);
		action.invoke(context, actionArguments);
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
			throw new RoutingException(RoutingExceptionType.NOT_VALID_COMMAND,
					actionCommand);
		}

		Map<String, Action> availableActions = this.actions.get(actionName);
		if (availableActions == null) {
			throw new RoutingException(RoutingExceptionType.NOT_VALID_COMMAND,
					actionCommand);
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
			throw new RoutingException(RoutingExceptionType.NOT_VALID_COMMAND,
					actionCommand);
		}

		return action;
	}
}
