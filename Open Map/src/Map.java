import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * JPanel that draws all the points on the graph
 * Handles zooming and panning
 * Also converts points from map points to pixels
 * @author Michael
 *
 */
public class Map extends JPanel implements MouseListener, MouseMotionListener{
	int x = 0;
	int y = 0;

	
	double pressedX = 0;
	double pressedY = 0;
	//the amount you offSet when panning
	double offSetX = 0;
	double offSetY = 0;
	double xMax;
	double yMax;
	double xMin;
	double yMin;
	//how much you scale the points i.e. convert from map points to pixels 
	double XSCALAR;
	double YSCALAR;
	double zoom = 1;
	private DataHandler dataHandler;
	ArrayList<String> drawingWays = new ArrayList<String>();
	ArrayList<Coordinate> drawPoints = new ArrayList<Coordinate>();
	Node hoverPoint = null;
	Node clickedPoint = null;
	Coordinate car = null;
	private double mapPointRadius = 0;
	private double pixelRadius = 1;
	
	public Map(Bounds b, DataHandler d){
		super();
		this.setSize(600,750);
		this.setVisible(true);
		setFocusable(true); 
	    requestFocusInWindow();
	   
	    xMax = (b.getXMax());
	    yMax =  (b.getYMax());
	    xMin = (b.getXMin());
	    yMin =  (b.getYMin());
	    dataHandler = d;
	    //calculate the scale 
	    XSCALAR = (double) (this.getWidth()/((xMax - xMin)));
		YSCALAR = (double) (this.getHeight()/(yMax - yMin));
		mapPointRadius = 1.0/XSCALAR;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	    this.repaint();
	 
	}
	
	public void drawWays(ArrayList<String> drawingWays){
		this.drawingWays = drawingWays;
		this.repaint();
	}
	public void drawPoint(double x, double y)
	{	
		this.repaint();
	}
	
	public void updateCar(double x, double y){
		car = new Coordinate(x,y);
		this.repaint();
	}
	//method to zoom in
	public void zoomIn(){
		zoom *= 1.5;
	}
	//method to zoom out
	public void zoomOut(){
		zoom /= 1.5;
	}
	
	public void resetZoom(){
		zoom =1;
	}
	
	public void reCenter(){
		offSetX = 0;
		offSetY = 0;
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		 XSCALAR = (double) (this.getWidth()/((xMax - xMin)));
		  YSCALAR = (double) (this.getHeight()/(yMax - yMin));
		
		for(int i = 0; i< drawingWays.size() ; i++){
			ArrayList<Node> nodes = dataHandler.findWay(drawingWays.get(i));
			int endLoop = nodes.size();
			if(nodes.size()%2 != 0){endLoop--;}
			
			for(int j = 0; j < endLoop; j++){
				try{
					if(nodes.get(j) == null || nodes.get(j+1) == null){break;}
				}
				catch(IndexOutOfBoundsException e){
					break;
				}
				
				Coordinate start = nodes.get(j).getCoordinate();
				Coordinate end = nodes.get(j+1).getCoordinate();
				//multiplies points by scalar to convert, then offsets for panning and then zooms based on a certain value
				this.x = toXPixels(start.getX());
				this.y = toYPixels(start.getY());
				int x2 = toXPixels(end.getX());
				int y2 = toYPixels(end.getY());
//				this.x = (int) ((((Math.abs((int) (start.getX() * XSCALAR) - (xMin * XSCALAR))) + offSetX)) * zoom); 
//				this.y = (int) (((Math.abs((int) (start.getY() * YSCALAR) - (yMax * YSCALAR))) + offSetY) * zoom); 
//				int x2 = (int) ((((Math.abs((int) (end.getX() * XSCALAR) - (xMin * XSCALAR))) + offSetX)) * zoom); 
//				int y2 = (int) (((Math.abs((int) (end.getY() * YSCALAR) - (yMax * YSCALAR))) + offSetY) * zoom);
				
				
				g.drawLine(x, y, x2, y2);
				this.x = 0;
				this.y = 0;
				x2 = 0;
				y2 = 0;
			}
		}
		if(hoverPoint != null){
			Color color = g.getColor();
			g.setColor(Color.red);
			int x1 = toXPixels(hoverPoint.getCoordinate().getX());
			int y1 = toYPixels(hoverPoint.getCoordinate().getY());
			g.fillOval(x1, y1, 5, 5);
			g.setColor(color);
		}
		
		if(clickedPoint != null){
			Color color = g.getColor();
			g.setColor(Color.red);
			int x1 = toXPixels(clickedPoint.getCoordinate().getX());
			int y1 = toYPixels(clickedPoint.getCoordinate().getY());
			g.fillOval(x1, y1, 5, 5);
			g.setColor(color);
		}
		
		if(car != null){
			Color color = g.getColor();
			g.setColor(Color.blue);
			int x1 = toXPixels(car.getX());
			int y1 = toYPixels(car.getY());
			g.fillOval(x1, y1, 8, 8);
			g.setColor(color);
		}
	}
	
	public int toXPixels(double x){
	
		return (int) ((((Math.abs((int) (x * XSCALAR) - (xMin * XSCALAR))) + offSetX)) * zoom); 
	}
	
	public int toYPixels(double y){
	
		return (int) ((((Math.abs((int) (y * YSCALAR) - (yMin * YSCALAR))) + offSetY)) * zoom); 
	}
		
	public double toXMapPoints(double x){
		double test = (((double) x / XSCALAR) + (xMin) - (offSetX/XSCALAR)) ;
		return (((double) x/zoom) / XSCALAR) + (xMin) - (offSetX/XSCALAR);
	}
	
	public double toYMapPoints(double y){
		
		return (((double) y/zoom) / YSCALAR) + (yMin) - (offSetY/YSCALAR);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		clickedPoint = new Node(hoverPoint.getCoordinate().getY(), hoverPoint.getCoordinate().getX(), "-1");
		this.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		pressedX = e.getX();
		pressedY = e.getY();
	//	System.out.println("pressedX: " +pressedX + "  pressedY: " + pressedY);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		double x = Math.abs(e.getX() - pressedX);
		double y = Math.abs(e.getY() - pressedY);
		if(pressedX < e.getX())
		{
			offSetX  += x;
		}
		else if(pressedX > e.getX())
		{
			offSetX  -= x;
		}
		if(pressedY < e.getY())
		{
			offSetY  += y;
		}
		else if(pressedY > e.getY())
		{
			offSetY  -= y;
		}

		repaint();
	}

	@Override
	//calculates the offset and sets those numbers
	public void mouseDragged(MouseEvent e) {
		double x = Math.abs(e.getX() - pressedX);
		double y = Math.abs(e.getY() - pressedY);
		if(pressedX < e.getX())
		{
			offSetX  += x;
		}
		else if(pressedX > e.getX())
		{
			offSetX  -= x;
		}
		if(pressedY < e.getY())
		{
			offSetY  += y;
		}
		else if(pressedY > e.getY())
		{
			offSetY  -= y;
		}
		pressedX = e.getX();
		pressedY = e.getY();
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Coordinate coord = new Coordinate(toXMapPoints(e.getX()), toYMapPoints(e.getY()));
		//System.out.println(toXMapPoints(e.getX()) + "\t" + toYMapPoints(e.getY()));
		Node tmp = dataHandler.findNode(coord, mapPointRadius);
		if(tmp != null)
		{
			hoverPoint = tmp;
		}
		this.repaint();
	}
}

	
	
	

