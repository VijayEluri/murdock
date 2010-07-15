package hm.murdock.modules;

import hm.murdock.utils.Context;

/**
 * TODO
 * 
 * @author Dario (i@dario.im)
 * 
 */
public final class Alias extends Module {

	public Alias(Context context) {
		super(context);
	}

	/**
	 * Default behavior:
	 * 
	 * <ul>
	 * <li>all mandatory (@Optional needed to set a parameter as optional) but
	 * booleans</li>
	 * <li>all match --name</li>
	 * <li>all match too -initial_letter_name but if duplicate it will be one of
	 * the next letters. Exception otherwise.</li>
	 * <li>"resto" needs annotation</li>
	 * <li>first parameter can be without option</li>
	 * </ul>
	 */
	public void create(@CanOmitFlag String name, Boolean test,
			@LeftOver String... command) {
		System.out.println(name);
		if (command != null) {
			for (String word : command) {
				System.out.println("\t" + word);
			}
		}
		if (test) {
			System.out.println("==> test");
		}
	}
}
