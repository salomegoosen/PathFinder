package pathfinding;

import java.io.File;

import org.apache.log4j.Logger;

public class MapFactory {
	protected NodeFactory nodeFactory;  // use to create nodes
	
	//  path finder algorithm to use
	protected AbstractAlgorithm pathFinderAlgorithm; 
		
	protected PropertyHelper propertyHelper;  // a singleton property helper

	public static Logger LOGGER = Logger.getLogger(MapFactory.class);
	
	/**
	 * 
	 *  pass in constructor the implementation of node factory to use
	 * @param nodeFactory
	 * @throws Exception
	 * 
	 * Constructor	 
	 */
	public MapFactory(NodeFactory nodeFactory) throws Exception {
		super();
		this.nodeFactory = nodeFactory;
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
		MyMap map = new MyMap(nodeFactory);
		String resourcesPathName= propertyHelper.getResourcesPathName();
		LOGGER.info("1*****"+resourcesPathName);
		File file = new File(resourcesPathName+File.separator+mapName);
		if (!file.exists()) 
			resourcesPathName = PropertyHelper.DIST_PATH_NAME+resourcesPathName;
		LOGGER.info("2*****"+resourcesPathName);
		file = new File(resourcesPathName+File.separator+mapName);
		if (!file.exists())
			throw new Exception("Could not find file " + file.getAbsolutePath());
		SimpleFileReader reader = new SimpleFileReader(file);  // TODO - change to BufferedReader
		String line = reader.nextLine();
		int x = 0;
		while (line != null) {
			AbstractNode nodeToAdd;
			for (int y = 0; y < line.length(); y++) {
				char terrainSymbol = line.charAt(y);
				nodeToAdd = nodeFactory.createNode(x, y, terrainSymbol ); 
				nodeToAdd.setWeightForSymbol(propertyHelper.weightForSymbol(terrainSymbol));
				map.setNode(nodeToAdd);
			}  // line
			line = reader.nextLine();
			x++;
		} // row
		try {
			reader.close();
		} catch (Exception e) {
		}
		return map;
	}

}
