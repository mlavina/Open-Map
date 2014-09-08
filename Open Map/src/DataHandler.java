import java.util.ArrayList;
import java.util.HashMap;

/**
 * this class will handle all the data and store all the data
 * from the parser
 * @author Michael
 *
 */
public class DataHandler {
	
	 private HashMap<String, Node> nodes = new HashMap<String,Node>();
	    private HashMap<String,Way> ways = new HashMap<String,Way>();
	    private HashMap<String, String> wayNames = new HashMap<String,String>();
	    private HashMap<String, Relation> relations = new HashMap<String,Relation>();
	    private HashMap<String, String> relationNames = new HashMap<String,String>();
	    private int containRadius = 5;
	/*
	 * initalize the dataHandler with all the data parsed from the OSM file
	 */
	public DataHandler(HashMap<String, Way> ways, HashMap<String,String> wayNames, HashMap<String,Node> nodes , 
			HashMap<String, Relation> relations, HashMap<String, String> relationNames){
		this.ways = ways;
		this.wayNames = wayNames;
		this.nodes = nodes;
		this.relations = relations;
		this.relationNames = relationNames;
	}
	
	 /**
     * takes a name of a way and return all the nodes of the way 
     * @param name name of a way
     * @return ArrayList of nodes
     */
    public ArrayList<Node> findWay(String name)
    {
    	Way temp = ways.get(wayNames.get(name));
    	//System.out.println(temp);
    	ArrayList<Node> nodeInWay = new ArrayList<Node>();
    	for(String ref : temp.getNodes())
    	{
    		nodeInWay.add(nodes.get(ref));
    	}
    	return nodeInWay;
    }
    
    /*
     * takes in a coordinate and returns a node associated with that coordinate
     * or finds the closest match
     */
    public Node findNode(Coordinate coord,double radius){
    	for(Node node : nodes.values()){
    		if(node.getCoordinate().inRadius(coord, radius))
    		{
    			return node;
    		}
    	}
    	
    	return null;
    }
   
    
    
}