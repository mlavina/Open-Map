/**
 * a class that stores x and y points for a coordinate
 */
public class Coordinate {
	double x = 0;
	double y = 0;
	public Coordinate(double x, double y)
	{
		this.x= x;
		this.y=y;
	}
	
	public double getX()
	{
		return x;
		
	}
	public double getY()
	{
		return y;
	}
	
	public Boolean inRadius(Coordinate other, double radius){
		if(this.x == other.x && this.y == other.y){return true;}
		if( Math.sqrt((Math.pow((this.x-other.x),2) + Math.pow((this.y - other.y),2))) < radius){return true;}
		return false;
		
	}
	
	public String toString(){
		String s = super.toString();
		s += "\t X:" + this.x + "\t Y:" +this.y;
		return s;
	}
}
