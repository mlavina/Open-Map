import java.util.ArrayList;
public class Relation {	
		private ArrayList<String> nodeRefs = new ArrayList<String>();
		private ArrayList<String> wayRefs = new ArrayList<String>();
		private ArrayList<String> relationRefs = new ArrayList<String>();
		private String id = "";
		private String name = "";
		
		public Relation(String id)
		{
			this.id = id;
		
		}
		
		public void setName(String name)
		{
			this.name = name;
		}
		
		public void add(String id, String type)
		{
			if("way".equals(type))
			{
				wayRefs.add(id);
			}
			else if("node".equals(type))
			{
				nodeRefs.add(id);
			}
			else if("relation".equals(type))
			{
				relationRefs.add(id);
			}
			
		}
		
		public String toString()
		{
			return "Name: " + name + "\tID: " +id + "\n" + wayRefs.toString();
		}
		
	
}
