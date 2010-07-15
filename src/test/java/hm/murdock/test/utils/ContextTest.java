package hm.murdock.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import hm.murdock.Murdock;
import hm.murdock.exceptions.ConfigurationException;
import hm.murdock.utils.Context;
import hm.murdock.utils.ContextProperty;

import java.io.File;

import org.junit.Test;

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
				+ Murdock.NAME.toLowerCase()), appHome);
	}

	@Test
	public void testIsFirstRun() throws ConfigurationException {
		File temporalDestination = new File(appHome.getParent()
				+ "/.murdock.test");
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
