package ba.stascus.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import ba.stascus.Stascus;
import ba.stascus.exceptions.ConfigurationException;
import ba.stascus.utils.Context;
import ba.stascus.utils.ContextProperty;

public class ContextTest {

	private final File appHome;
	private final Context context;

	public ContextTest() throws ConfigurationException {
		context = new Context();
		appHome = context.getAppHome();
	}

	@Test
	public void testGetAppHome() throws ConfigurationException {
		assertEquals(new File(System.getProperty("user.home") + "/."
				+ Stascus.NAME.toLowerCase()), appHome);
	}

	@Test
	public void testIsFirstRun() throws ConfigurationException {
		File temporalDestination = new File(appHome.getParent()
				+ "/.stascus.test");
		assertTrue(appHome.renameTo(temporalDestination));

		Context newContext = new Context();
		assertTrue(temporalDestination.renameTo(appHome));

		assertTrue(newContext.isFirstRun());
	}

	@Test
	public void testProperties() throws ConfigurationException {
		String value = "foo";

		context.setProperty(ContextProperty.TEST, value);
		String storedValue = context.getProperty(ContextProperty.TEST);

		assertEquals(value, storedValue);
	}
}
