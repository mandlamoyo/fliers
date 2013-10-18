import java.awt.*;
import java.util.ArrayList;


public class Obstacles {
	//private static final int bWidth = 32;
	//private static final int bHeight = 32;
	
	private static final int PWIDTH = 800; //500;
	private static final int PHEIGHT = 700; //400;
	
	private int bWidth;
	private int bHeight;
	private ArrayList<Rectangle> boxes;
	private Fliers flTop;
	private boolean isSelected = false;
	
	public Obstacles( Fliers fl, int bW, int bH )
	{
		boxes = new ArrayList<Rectangle>();
		flTop = fl;
		bWidth = bW;
		bHeight = bH;
	}
	
	synchronized public void add( int x, int y )
	{
		boxes.add( new Rectangle( x-bWidth/2, y-bHeight/2, bWidth, bHeight ));
		flTop.setScore( boxes.size() );
	}
	
	synchronized public boolean isSelectedAt( int x, int y )
	{
		Rectangle box;
		if ( boxes.size() > 0 ) {
			for( int i=0; i < boxes.size(); i++) {
				box = (Rectangle) boxes.get(i);
				if( x >= box.x && x <= box.x+bWidth && y >= box.y && y <= box.y+bHeight ) {
					isSelected = true;
					return true;
				}
			}
		}
		return false;
	}
	
	synchronized public ArrayList<Rectangle> inRadius( int x, int y, int radius )
	{
		ArrayList<Rectangle> inRange = new ArrayList<Rectangle>();
		
		for( int i=0; i < boxes.size(); i++) {
			Rectangle box = boxes.get(i);
			if( box.x >= x-radius && box.x <= x+radius && box.y >= y-radius && box.y <= y+radius ) {
				inRange.add( box );
			}
		}
		return inRange;
	}
	
	synchronized public boolean checkCollision( Rectangle ship )
	{
		for( int i=0; i < boxes.size(); i++) {
			Rectangle box = boxes.get(i);
			if ( box.intersects( ship )) return true;
		}
		return false;
	}
	
	synchronized public void deSelect()
	{	isSelected = false; }
	
	synchronized public void update()
	{
		Rectangle box;
		ArrayList<Rectangle> livingBoxes = new ArrayList<Rectangle>();
		
		for( int i=0; i < boxes.size(); i++) {
			box = (Rectangle) boxes.get(i);
			box.y += 1;
			
			if ( box.y < PHEIGHT ) {
				livingBoxes.add( box );
			}
		}
		
		boxes = livingBoxes;
	}
	
	synchronized public void draw( Graphics g )
	{
		Rectangle box;
		g.setColor( Color.blue );
		for( int i=0; i < boxes.size(); i++) {
			box = (Rectangle) boxes.get(i);
			g.fillRect( box.x, box.y, box.width, box.height );
		}
	}
	
	synchronized public int getNumObstacles()
	{ return boxes.size(); }
}
