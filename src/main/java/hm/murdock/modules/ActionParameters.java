package hm.murdock.modules;

import hm.murdock.exceptions.ActionException;
import hm.murdock.exceptions.ActionException.ActionExceptionType;

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

import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.Paranamer;

public final class ActionParameters {

	private final Map<String, ActionParameter> declaredParameters = new HashMap<String, ActionParameter>();

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

			String shortOption = parameter.getOverridenShortOption();
			if (shortOption == null) {
				shortOption = getShortOption(parameterName);
			}

			Option option = OptionBuilder.create(shortOption);
			options.addOption(option);

			this.declaredParameters.put(parameterName, parameter);
			this.shortOptions.add(option.getOpt());
		}
	}

	public Object[] handle(String[] arguments) throws ActionException {
		Object[] parameters = null;

		if (arguments.length > 0) {
			final String[] definitiveArguments = expandOmittedFirstArgument(arguments);

			parameters = new Object[this.declaredParameters.size()];

			GnuParser parser = new GnuParser();
			try {
				CommandLine line = parser.parse(options, definitiveArguments);
				for (Option option : line.getOptions()) {
					ActionParameter parameter = this.declaredParameters
							.get(option.getLongOpt());

					int parameterPosition = parameter.getPosition();
					if (option.hasArg()) {
						if (option.hasArgs()) {
							String[] optionArguments = line
									.getOptionValues(option.getOpt());
							Object array = deserializeArray(parameter,
									optionArguments);

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
						Object array = deserializeArray(lastParameter, leftOver);
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

			sanitizeNullBooleans(parameters);
		}

		return parameters;
	}

	private void sanitizeNullBooleans(Object[] finalParameters) {
		for (int i = 0; i < finalParameters.length; i++) {
			if (finalParameters[i] == null
					&& parametersByPosition[i].getDeclaredClass().equals(
							Boolean.class)) {
				finalParameters[i] = false;
			}
		}
	}

	private Object deserializeArray(ActionParameter parameter,
			String[] originalArray) {
		Object array = Array.newInstance(parameter.getDeclaredClass(),
				originalArray.length);

		for (int i = 0; i < originalArray.length; i++) {
			String arg = originalArray[i];
			Array.set(array, i, parameter.deserialize(arg));
		}

		return array;
	}

	private String[] expandOmittedFirstArgument(String[] arguments) {
		final String[] definitiveArguments;
		if (!arguments[0].startsWith("-")
				&& parametersByPosition[0].canOmitFlag()) {
			definitiveArguments = new String[arguments.length + 1];
			definitiveArguments[0] = "--" + parametersByPosition[0].getName();

			System.arraycopy(arguments, 0, definitiveArguments, 1,
					arguments.length);
		} else {
			definitiveArguments = arguments;
		}
		return definitiveArguments;
	}

	private String getShortOption(String name) throws ActionException {
		String letter;
		int i = 0;

		do {
			letter = name.substring(i++, i);
		} while (shortOptions.contains(letter) && i < name.length());

		if (i >= name.length()) {
			throw new ActionException(
					ActionExceptionType.UNABLE_ASSIGN_SHORT_OPTION, name);
		}

		return letter;
	}
}
