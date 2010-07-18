package hm.murdock.modules;

import hm.murdock.Murdock;
import hm.murdock.cli.Router;
import hm.murdock.exceptions.RoutingException;
import hm.murdock.modules.action.Action;
import hm.murdock.modules.annotations.CanOmitFlag;
import hm.murdock.modules.annotations.HelpDescription;
import hm.murdock.modules.annotations.Optional;
import hm.murdock.utils.Context;
import hm.murdock.utils.Utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * 
 * @author Dario (i@dario.im)
 * 
 */
@HelpDescription("Show this help or specific help for actions")
public final class Help extends Module {

	public Help(Context context) {
		super(context);
	}

	public void help(@Optional @CanOmitFlag String command)
			throws RoutingException {
		Router router = Router.getByContext(getContext());

		StringWriter writer = new StringWriter();
		if (command == null) {
			writer.append("usage: ");
			writer.append(Murdock.NAME);
			writer.append(" [module:]action [options]\n\nAvailable modules:\n");

			Set<Class<? extends Module>> rawModules = router.getModules();
			Class<?>[] aux = new Class<?>[rawModules.size()];
			rawModules.toArray(aux);

			AnnotatedElement[] modules = new AnnotatedElement[rawModules.size()];
			Utils.copyArray(modules, aux);

			printAnnotatedElements(modules, writer);
		} else {
			try {
				Action action = router.getAction(command);
				action.printHelp(new PrintWriter(writer));
			} catch (RoutingException e) {
				if (command.equals(getElementName(Hello.class))) {
					throw e;
				}

				for (Class<? extends Module> module : router.getModules()) {

					if (getElementName(module).equals(command)) {
						writer.append("usage: ");
						writer.append(Murdock.NAME);
						writer.append(" ");
						writer.append(command);
						writer.append(":action [options]\n\nAvailable actions:\n");
						Set<Method> rawMethods = new HashSet<Method>(
								Arrays.asList(module.getMethods()));
						Set<Method> notOverridable = router
								.getNotOverridableMethods();
						rawMethods.removeAll(notOverridable);

						AnnotatedElement[] aux = new AnnotatedElement[rawMethods
								.size()];
						rawMethods.toArray(aux);

						AnnotatedElement[] methods = new AnnotatedElement[rawMethods
								.size()];
						Utils.copyArray(methods, aux);

						printAnnotatedElements(methods, writer);
						break;
					}
				}
			}
		}

		this.getLogger().info(writer.getBuffer().toString());
	}

	private void printAnnotatedElements(AnnotatedElement[] modules,
			StringWriter writer) {
		int maxLength = 0;
		for (AnnotatedElement element : modules) {
			int length;

			length = getElementName(element).length();

			if (maxLength < length) {
				maxLength = length;
			}
		}

		for (AnnotatedElement element : modules) {
			if (element.equals(Hello.class)) {
				continue;
			}

			String name = getElementName(element).toLowerCase(Locale.US);

			writer.append("   ");
			writer.append(name);
			HelpDescription descriptionAnnotation = element
					.getAnnotation(HelpDescription.class);
			if (descriptionAnnotation != null) {
				byte[] buffer = new byte[maxLength - name.length() + 3];
				Arrays.fill(buffer, (byte) ' ');

				writer.append(new String(buffer));
				writer.append(descriptionAnnotation.value());
			}
			writer.append("\n");
		}
	}

	private String getElementName(AnnotatedElement element) {
		String name;

		if (element.getClass().equals(Class.class)) {
			name = ((Class<?>) element).getSimpleName();
		} else {
			name = ((Member) element).getName();
		}

		return name.toLowerCase(Locale.US);
	}

	public void version() {
		// TODO Allow to customize through properties.
		this.getLogger().info(Murdock.NAME + " version " + Murdock.VERSION);
	}
}
