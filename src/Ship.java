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
	private Sensor[] sensors;
	private Point body;
	
	public Ship( int pW, int pH, Obstacles os )
	{
		pWidth = pW;
		pHeight = pH;
		obs = os;
		direction = 0;
		body = new Point( pWidth/2, pHeight/2 );
		sensors = new Sensor[] {new Sensor( 4, -12, this, obs )};
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
		
		for ( Sensor s: sensors ) {
			s.update();
		}
	}
	
	public int[] getPos()
	{
		return new int[] {body.x+RADIUS, body.y};
	}
	
	private int[] getSensorOutput()
	{
		//code goes here
		return new int[3];
	}
	
	public void draw( Graphics g )
	{
		
		g.setColor( Color.red );
		g.fillOval( body.x,  body.y,  BODY_SIZE,  BODY_SIZE );
		
		g.setColor( Color.green );
		for ( Sensor s: sensors ) {
			if ( s.state == 0 ) g.setColor( Color.green );
			else g.setColor( Color.orange );
			
			g.fillOval( s.pos[0],  s.pos[1],  s.size,  s.size);
		}
	}
}
