package ba.stascus.test.cli;

import static org.junit.Assert.*;

import org.junit.Test;

import ba.stascus.cli.Router;
import ba.stascus.exceptions.ActionException;
import ba.stascus.exceptions.ConfigurationException;
import ba.stascus.exceptions.MultipleRoutingException;
import ba.stascus.exceptions.RoutingException;
import ba.stascus.exceptions.RoutingException.RoutingExceptionType;
import ba.stascus.utils.Context;

public class RouterTest {

	private final Context context;

	private final Router router;

	public RouterTest() throws ConfigurationException {
		context = new Context();
		router = new Router(context);
	}

	@Test
	public void testRoute() throws RoutingException, ActionException {
		router.route("help");
	}

	@Test
	public void testRouteWhenCommandIsEmpty() throws ActionException {
		try {
			router.route("");
		} catch (RoutingException e) {
			assertEquals(RoutingExceptionType.FOOL_ME, e.getType());
		}
	}

	@Test
	public void testRouteWhenCommandIsNull() throws ActionException {
		try {
			router.route((String) null);
		} catch (RoutingException e) {
			assertEquals(RoutingExceptionType.MISSING_ARGUMENTS, e.getType());
		}
	}

	@Test
	public void testRouteWhenCommandIsArrayNull() throws ActionException {
		try {
			router.route((String[]) null);
		} catch (RoutingException e) {
			assertEquals(RoutingExceptionType.MISSING_ARGUMENTS, e.getType());
		}
	}

	@Test
	public void testRouteUnknownCommand() throws RoutingException,
			ActionException {
		try {
			router.route("you", "fool");
		} catch (RoutingException e) {
			assertEquals(RoutingExceptionType.NOT_VALID_COMMAND, e.getType());
		}
	}

	/**
	 * This test will work always, no matter if version has multiple modules or
	 * not.
	 */
	@Test
	public void testRouteCommandWithMultipleModules() throws RoutingException,
			ActionException {
		try {
			router.route("version");
		} catch (MultipleRoutingException e) {
			assertTrue(e.getAvailableActions().size() > 0);
		}
	}

	@Test
	public void testRouteCommandWithFullName() throws RoutingException,
			ActionException {
		router.route("help:version");
	}
}
