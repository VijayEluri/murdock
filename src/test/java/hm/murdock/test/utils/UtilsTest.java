package hm.murdock.test.utils;

import hm.murdock.cli.Router;
import hm.murdock.utils.Utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class UtilsTest {

	@Test
	public void testGetCurrentPackage() {
		String parentPackage = Utils.getCurrentParentPackage(Router.class);
		assertEquals("hm.murdock", parentPackage);
	}
}
