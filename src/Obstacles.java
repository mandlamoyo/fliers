import java.awt.*;
import java.util.ArrayList;


public class Obstacles {
	private static final int BOX_WIDTH = 12;
	private static final int BOX_LENGTH = 12;
	
	private static final int PWIDTH = 500;
	private static final int PHEIGHT = 400;
	
	private ArrayList boxes;
	private Fliers flTop;
	private boolean isSelected = false;
	
	public Obstacles( Fliers fl )
	{
		boxes = new ArrayList();
		flTop = fl;
	}
	
	synchronized public void add( int x, int y )
	{
		boxes.add( new Rectangle( x-BOX_WIDTH/2, y-BOX_LENGTH/2, BOX_LENGTH, BOX_LENGTH ));
		flTop.setScore( boxes.size() );
	}
	
	synchronized public boolean isSelectedAt( int x, int y )
	{
		Rectangle box;
		if ( boxes.size() > 0 ) {
			for( int i=0; i < boxes.size(); i++) {
				box = (Rectangle) boxes.get(i);
				//g.fillRect( box.x, box.y, box.width, box.height );
				if( x >= box.x && x <= box.x+BOX_WIDTH && y >= box.y && y <= box.y+BOX_LENGTH ) {
				/*if( (Math.abs( box.x + box.width/2. - x) <= box.width) &&
						(Math.abs( box.y + box.height/2. - y) <= box.height) ) {*/
					isSelected = true;
					return true;
				}
			}
		}
		return false;
	}
	
	synchronized public void deSelect()
	{	isSelected = false; }
	
	synchronized public void update()
	{
		Rectangle box;
		ArrayList livingBoxes = new ArrayList();
		
		for( int i=0; i < boxes.size(); i++) {
			box = (Rectangle) boxes.get(i);
			box.y += 1;
			
			if ( box.y < PHEIGHT ) {
				livingBoxes.add( box );
			} else {
				System.out.println( "Box lost!" );
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
