import java.util.ArrayList;


public class Direction {
	private DataHandler dataHandler;
	private ArrayList<Way> ways = new ArrayList<Way>();
	private Node start;
	private Node end;
	
	public Direction(DataHandler d, Node start, Node end){
		dataHandler = d;
		this.start = start;
		this.end = end;
	}
	/**
	 * This will call the dataHandler to get all the ways
	 * then using an algorithm figure out the ways connectings
	 * the two nodes and return that
	 * @return
	 */
	public ArrayList<Way> calcDirection()
	{
		
		return ways;
	}

}
