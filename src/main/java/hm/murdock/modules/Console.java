package hm.murdock.modules;

import hm.murdock.Murdock;
import hm.murdock.exceptions.ConfigurationException;
import hm.murdock.modules.annotations.HelpDescription;
import hm.murdock.utils.Context;

@HelpDescription("Interactive console")
public class Console extends Module {

	public Console(Context context) {
		super(context);
	}

	public void start() throws ConfigurationException {
		java.io.Console console = System.console();
		if (console == null) {
			throw new RuntimeException(
					"Murdock is sad. No system console available :(");
		}

		Murdock murdock = new Murdock(getContext());

		boolean keepLooping = true;
		do {
			String command = console.readLine("# ");
			if (command != null) {
				command = command.trim();

				if (!command.equals("")) {
					String[] arguments = command.split(" ");
					if (arguments.length > 0 && arguments[0].equals("exit")) {
						keepLooping = false;
					} else {
						murdock.delegate(arguments);
					}
				}
			}
		} while (keepLooping);
	}
}
