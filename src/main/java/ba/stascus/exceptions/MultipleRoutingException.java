package ba.stascus.exceptions;

import java.util.Map;

import ba.stascus.modules.Action;

/**
 * Represents an exception while loading or calling a module.
 * 
 * @author Dario (i@dario.im)
 * 
 */
public final class MultipleRoutingException extends RoutingException {

	private static final long serialVersionUID = -6908667390347766062L;

	private Map<String, Action> actions;

	public MultipleRoutingException(Map<String, Action> availableActions) {
		super(RoutingExceptionType.MULTIPLE_ROUTES);
		this.actions = availableActions;
	}

	public Map<String, Action> getAvailableActions() {
		return this.actions;
	}
}
