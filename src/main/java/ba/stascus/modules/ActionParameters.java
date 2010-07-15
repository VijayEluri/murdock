package ba.stascus.modules;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import ba.stascus.exceptions.ActionException;
import ba.stascus.exceptions.ActionException.ActionExceptionType;

import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.Paranamer;

public class ActionParameters {

	private final Map<String, ActionParameter> parameters = new HashMap<String, ActionParameter>();

	private final Set<String> shortOptions = new HashSet<String>();

	private final ActionParameter[] parametersByPosition;

	private final Options options;

	public ActionParameters(Method method) throws ActionException {
		Paranamer paranamer = new BytecodeReadingParanamer();

		/* We assume this returns names in declaration order */
		String[] parameterNames = paranamer.lookupParameterNames(method);
		Class<?>[] parameterTypes = method.getParameterTypes();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();

		options = new Options();
		parametersByPosition = new ActionParameter[parameterNames.length];
		for (int i = 0; i < parameterNames.length; i++) {
			ActionParameter parameter = new ActionParameter(parameterNames[i],
					parameterTypes[i], parameterAnnotations[i], i);
			parametersByPosition[i] = parameter;

			String parameterName = parameterNames[i];
			OptionBuilder.withLongOpt(parameterName);
			OptionBuilder.withValueSeparator();

			if (parameterTypes[i].equals(Boolean.class)) {
				OptionBuilder.isRequired(false);
			} else {
				OptionBuilder.isRequired(!parameter.isOptional());

				if (parameterTypes[i].getComponentType() == null) {
					OptionBuilder.hasArg();
				} else {
					OptionBuilder.hasArgs();
					if (parameter.handlesLeftOverArgs()) {
						OptionBuilder.isRequired(false);
					}
				}
			}

			Option option = OptionBuilder.create(getShortOption(parameterName));
			options.addOption(option);

			this.parameters.put(parameterName, parameter);
			this.shortOptions.add(option.getOpt());
		}
	}

	public Object[] handle(String[] arguments) throws ActionException {
		Object[] parameters = null;

		if (arguments.length > 0) {
			if (!arguments[0].startsWith("-")
					&& parametersByPosition[0].canOmitFlag()) {
				String[] temp = new String[arguments.length + 1];
				temp[0] = "--" + parametersByPosition[0].getName();

				System.arraycopy(arguments, 0, temp, 1, arguments.length);
				arguments = temp;
			}

			parameters = new Object[this.parameters.size()];

			GnuParser parser = new GnuParser();
			try {
				CommandLine line = parser.parse(options, arguments);
				for (Option option : line.getOptions()) {
					ActionParameter parameter = this.parameters.get(option
							.getLongOpt());

					int parameterPosition = parameter.getPosition();
					if (option.hasArg()) {
						if (option.hasArgs()) {
							String[] optionArguments = line
									.getOptionValues(option.getOpt());
							Object array = Array.newInstance(
									parameter.getDeclaredClass(),
									optionArguments.length);

							for (int i = 0; i < optionArguments.length; i++) {
								String arg = optionArguments[i];
								Array.set(array, i, parameter.deserialize(arg));
							}

							parameters[parameterPosition] = array;
						} else {
							parameters[parameterPosition] = parameter
									.deserialize(option.getValue());
						}
					} else {
						parameters[parameterPosition] = line.hasOption(option
								.getOpt());
					}
				}

				ActionParameter lastParameter = parametersByPosition[parameters.length - 1];
				if (lastParameter.handlesLeftOverArgs()
						&& lastParameter.isArray()) {
					String[] leftOver = line.getArgs();

					int finalPosition = parameters.length - 1;
					if (parameters[finalPosition] == null) {
						// TODO Convert to declared class...
						parameters[finalPosition] = leftOver;

						Object array = Array.newInstance(
								lastParameter.getDeclaredClass(),
								leftOver.length);

						for (int i = 0; i < leftOver.length; i++) {
							String arg = leftOver[i];
							Array.set(array, i, lastParameter.deserialize(arg));
						}

						parameters[finalPosition] = array;
					} else {
						Object[] current = (Object[]) parameters[finalPosition];
						String[] all = new String[leftOver.length
								+ current.length];
						System.arraycopy(current, 0, all, 0, current.length);
						System.arraycopy(leftOver, 0, all, current.length,
								leftOver.length);

						parameters[finalPosition] = all;
					}
				}
			} catch (ParseException e) {
				throw new ActionException(ActionExceptionType.INVOKING_ERROR, e);
			}
		}

		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i] == null
					&& parametersByPosition[i].getDeclaredClass().equals(
							Boolean.class)) {
				parameters[i] = false;
			}
		}

		return parameters;
	}

	private char getShortOption(String name) throws ActionException {
		char letter;
		int i = 0;

		do {
			letter = name.charAt(i++);
		} while (shortOptions.contains(letter) && i < name.length());

		if (i >= name.length()) {
			throw new ActionException(
					ActionExceptionType.UNABLE_ASSIGN_SHORT_OPTION, name);
		}

		return letter;
	}
}
