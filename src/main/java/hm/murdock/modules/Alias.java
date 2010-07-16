package hm.murdock.modules;

import hm.murdock.modules.annotations.CanOmitFlag;
import hm.murdock.modules.annotations.HandlesLeftOverArgs;
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
	 * <li>left-over handling needs annotation on the last parameter</li>
	 * <li>first parameter can be without option</li>
	 * </ul>
	 */
	public void create(@CanOmitFlag String name,
			@HandlesLeftOverArgs String... command) {

	}

	public void delete(@CanOmitFlag String name) {

	}

	public void list() {

	}
}
