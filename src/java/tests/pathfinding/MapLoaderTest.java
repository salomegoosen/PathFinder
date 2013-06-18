package tests.pathfinding;

import junit.framework.TestCase;
import pathfinding.MyMap;
import pathfinding.MyMapLoader;
import pathfinding.MyNodeFactory;

public class MapLoaderTest extends TestCase {

	public static final String MAP1_NAME = "test_map.txt";
	public static final String LARGE_MAP_NAME = "large_map.txt";
	private static final int MAP1_LENGTH = 5;
	private static final int MAP1_WIDTH = 5;
	
	public void testLoad() {
		try {
			MyMap map = createTestMapFromFile(MAP1_NAME);
			assertEquals(map.getNumRows(), MAP1_LENGTH);
			assertEquals(map.getNumColumns(), MAP1_WIDTH);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	public static MyMap createTestMapFromFile(String mapName) throws Exception {
		MyMapLoader mapLoader = new MyMapLoader(new MyNodeFactory());
		MyMap map = (MyMap) mapLoader.load(mapName);
		return map;
	}

}
