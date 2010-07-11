package ba.stascus.test.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import ba.stascus.exceptions.ActionException;
import ba.stascus.exceptions.ActionException.ActionExceptionType;
import ba.stascus.exceptions.ConfigurationException;
import ba.stascus.exceptions.ConfigurationException.ConfigurationExceptionType;
import ba.stascus.exceptions.ExceptionFactory;
import ba.stascus.exceptions.MultipleRoutingException;

public class ExceptionFactoryTest {

	@Test
	public void testGet() {
		ActionException ae = ExceptionFactory.get(ActionException.class,
				ActionExceptionType.UNABLE_ACCESS, "action");
		assertEquals(ActionExceptionType.UNABLE_ACCESS, ae.getType());

		try {
			ExceptionFactory.get(MultipleRoutingException.class,
					ActionExceptionType.UNABLE_ACCESS, "action");
		} catch (ClassCastException e) {
			assertNotNull(e);
		}

		ConfigurationException ce = ExceptionFactory.get(
				ConfigurationException.class,
				ConfigurationExceptionType.INVALID_FORMAT_CONFIG);
		assertEquals(ConfigurationExceptionType.INVALID_FORMAT_CONFIG,
				ce.getType());
	}
}
