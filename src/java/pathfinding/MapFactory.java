package pathfinding;

import java.io.File;

public class MapFactory {
//	private static final Logger LOGGER = Logger.getLogger("MapFactory"); // TODO-
																			// logfilename
	protected NodeFactory nodeFactory;  // use to create nodes
	protected AbstractAlgorithm pathFinderAlgorithm; // pass in construct the implementation of 
	//path finder algorithm to use
	protected PropertyHelper propertyHelper;  // a singleton property helper

	/**
	 * 
	 *  pass in construct the implementation of path finder algorithm to use
	 *  pass in constructor the implementation of node factory to use
	 * @param nodeFactory
	 * @throws Exception
	 * 
	 * Constructor	 
	 */
	public MapFactory(NodeFactory nodeFactory, AbstractAlgorithm algorithm) throws Exception {
		super();
		this.nodeFactory = nodeFactory;
		this.pathFinderAlgorithm = algorithm;
		this.propertyHelper = PropertyHelper.getInstance();
	}	

	/**
	 * @param mapName
	 * @return
	 * @throws Exception
	 * 
	 * Create the terrain map from the input map file name
	 */
	public MyMap createMap(String mapName) throws Exception {
		MyMap map = new MyMap(nodeFactory, pathFinderAlgorithm);
		String resourcesPathName= propertyHelper.getResourcesPathName();
		System.out.println("1*****"+resourcesPathName);
		File file = new File(resourcesPathName+File.separator+mapName);
		if (!file.exists()) 
			resourcesPathName = PropertyHelper.DIST_PATH_NAME+resourcesPathName;
		System.out.println("2*****"+resourcesPathName);
		file = new File(resourcesPathName+File.separator+mapName);
		if (!file.exists())
			throw new Exception("Could not find file " + file.getAbsolutePath());
		SimpleFileReader reader = new SimpleFileReader(file);  // TODO - change to BufferedReader
		String line = reader.nextLine();
		int x = 0;
		while (line != null) {
			AbstractNode nodeToAdd;
			for (int y = 0; y < line.length(); y++) {
				nodeToAdd = nodeFactory.createNode(x, y, line.charAt(y)); //x,y,terrainSymbol);
				map.setNode(nodeToAdd);
			}  // line
			line = reader.nextLine();
			x++;
		} // row
		return map;
	}

}
