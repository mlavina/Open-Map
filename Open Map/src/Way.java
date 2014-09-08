import java.util.ArrayList;


public class Way {
	private ArrayList<String> refs = new ArrayList<String>();
	private String id = "";
	private String name = "";
	
	public Way(String id)
	{
		this.id = id;
	
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public ArrayList<String> getNodes(){
		return refs;
	}
	public void add(String nodeID)
	{
		refs.add(nodeID);
	}
	
	public String toString()
	{
		return "Name: " + name + "\tID: " +id + "\n"+refs.toString();
	}
	
}
