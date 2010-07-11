package ba.stascus.test.utils;

import org.junit.Test;
import static org.junit.Assert.*;

import ba.stascus.cli.Router;
import ba.stascus.utils.Utils;

public class UtilsTest {

	@Test
	public void testGetCurrentPackage() {
		String parentPackage = Utils.getCurrentParentPackage(Router.class);
		assertEquals("ba.stascus", parentPackage);
	}
}
