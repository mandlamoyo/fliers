import java.awt.*;
import java.awt.geom.*;

public class Ship {
	private static final int X = 0;
	private static final int Y = 1;
	private static final int BODY_SIZE = 32;
	private static final int RADIUS = BODY_SIZE/2;
	
	private int pWidth, pHeight, direction;
	private int[][] dirList = {{-1,0},{0,-1},{1,0},{0,1}};
	private Obstacles obs;
	private Point body;
	
	public Ship( int pW, int pH, Obstacles os )
	{
		pWidth = pW;
		pHeight = pH;
		obs = os;
		direction = 0;
		body = new Point( pWidth/2, pHeight/2 );
	}
	
	public void move( int dir )
	{
		body.x += dirList[dir][X];
		body.y += dirList[dir][Y];
	}
	
	public void update()
	{
		if ( body.x <= 0 || body.x >= pWidth ) direction = Math.abs( direction-2 );
		move(direction);
		
		
		//code here
	}
	
	public int[] getPos()
	{
		return new int[] {body.x, body.y};
	}
	
	public void draw( Graphics g )
	{
		g.setColor( Color.red );
		g.fillOval( body.x,  body.y,  BODY_SIZE,  BODY_SIZE );
	}
}
