
public class Node {
	private Coordinate coord ;
	private String id;
	private String name;
	
	public Node(double lat, double lon, String id)
	{
		 coord = new Coordinate(lon, lat);
		this.id = id;
		name = "";
	
	}
	
	public void setName(String Name)
	{
		this.name = name;
	}
	public Coordinate getCoordinate()
	{
		return coord;
	}
	
	
}
