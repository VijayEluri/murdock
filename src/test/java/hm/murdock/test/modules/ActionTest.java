package hm.murdock.test.modules;

import static org.junit.Assert.assertTrue;

import hm.murdock.exceptions.ActionException;
import hm.murdock.exceptions.ConfigurationException;
import hm.murdock.modules.Action;
import hm.murdock.modules.Alias;
import hm.murdock.utils.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;


import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.Paranamer;

public class ActionTest {

	@Test
	public void testAction() throws SecurityException, NoSuchMethodException,
			ActionException, ConfigurationException {
		Action action = getTestingAction();
		action.invoke(new Context(), "-inew", "-c", "alias:create", "warcry",
				"--test", "alon");
	}

	@Test
	public void testActionNullParams() throws ActionException,
			NoSuchMethodException, ConfigurationException {
		Action action = getTestingAction();
		action.invoke(new Context(), "new", null);
	}

	@Test
	public void testParanamer() throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			ConfigurationException, IllegalAccessException,
			InvocationTargetException {
		Method method = Alias.class.getMethod("create", String.class,
				String[].class);

		Paranamer paranamer = new BytecodeReadingParanamer();
		String[] parameters = paranamer.lookupParameterNames(method);

		assertTrue(parameters.length > 0);
		Object[] arguments = new Object[parameters.length];

		arguments[0] = "new";
		arguments[1] = new String[] { "create", "alias" };

		method.invoke(new Alias(new Context()), arguments);
	}

	private Action getTestingAction() throws NoSuchMethodException,
			ActionException {
		Method method = Alias.class.getMethod("create", String.class,
				Boolean.class, String[].class);
		Action action = new Action(Alias.class, method);

		return action;
	}
}
