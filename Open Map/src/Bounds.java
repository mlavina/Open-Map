/**
 * stores the bounds of the map
 * @author Michael
 *
 */
public class Bounds{
	private double minX;
	private double minY;
	private double maxX;
	private double maxY;
	
	public Bounds(double xMin, double yMin, double xMax, double yMax){
		minX = xMin;
		minY = yMin;
		maxX = xMax;
		maxY = yMax;
	}
	
	public double getXMin(){
		return minX;
	}
	
	public double getXMax(){
		return maxX;
	}
	public double getYMin(){
		return minY;
	}
	public double getYMax(){
		return maxY;
	}
}
