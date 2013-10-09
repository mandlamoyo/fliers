import java.awt.*;
import java.awt.geom.*;

public class Ship {
	private static final int X = 0;
	private static final int Y = 1;
	private static final int BODY_SIZE = 32;
	private static final int RADIUS = BODY_SIZE/2;
	
	private int pWidth, pHeight;
	private int[][] dirList = {{-4,0},{0,-4},{4,0},{0,4}};
	private Obstacles obs;
	private Point body;
	
	public Ship( int pW, int pH, Obstacles os )
	{
		pWidth = pW;
		pHeight = pH;
		obs = os;
		body = new Point( pWidth/2, pHeight/2 );
	}
	
	public void move( int dir )
	{
		body.x += dirList[dir][X];
		body.y += dirList[dir][Y];
	}
	
	public void draw( Graphics g )
	{
		g.setColor( Color.red );
		g.fillOval( body.x,  body.y,  BODY_SIZE,  BODY_SIZE );
	}
}
