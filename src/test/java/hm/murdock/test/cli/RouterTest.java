package hm.murdock.test.cli;

import static org.junit.Assert.*;
import hm.murdock.cli.Router;
import hm.murdock.exceptions.ActionException;
import hm.murdock.exceptions.ConfigurationException;
import hm.murdock.exceptions.MultipleRoutingException;
import hm.murdock.exceptions.RoutingException;
import hm.murdock.exceptions.RoutingException.RoutingExceptionType;
import hm.murdock.utils.Context;

import org.junit.Test;


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
