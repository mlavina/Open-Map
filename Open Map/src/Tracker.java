import java.util.ArrayList;

import com.starkeffect.highway.GPSDevice;
import com.starkeffect.highway.GPSEvent;
import com.starkeffect.highway.GPSListener;

public class Tracker implements GPSListener  {
	private GPSDevice gps;
	private Map map;
	private Direction direction;
	private DataHandler data;
	private ArrayList<Way> path;
	
	/**
	 * initialize a tracker with a GPS device and access to the map
	 * @param fileName
	 * @param map
	 */
	public Tracker(String fileName, Map map, DataHandler d){
		gps = new GPSDevice(fileName);
		this.map = map;
		gps.addGPSListener(this);
		data = d;
	}

	/**
	 * 
	 */
	public void onRoute(){
		//check to see if car is on route
		//if not directions = new Directions(where the user is now, previous end)
		//set the new Path
		// pass the path to the map to draw
	}
	
	/**
	 * set the directions based on pass parameters
	 * @param start
	 * @param end
	 */
	public void setDirections(Node start, Node end){
		direction = new Direction(data, start,end);
		path = direction.calcDirection();
		// pass the path to the map to draw
	}
	/*
	 * when the location is changed send the updated data t
	 * @see com.starkeffect.highway.GPSListener#processEvent(com.starkeffect.highway.GPSEvent)
	 */
	@Override
	public void processEvent(GPSEvent e) {
		
	}
}
