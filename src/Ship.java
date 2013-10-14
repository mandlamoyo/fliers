import java.awt.*;
import java.awt.geom.*;
import java.util.Random;

public class Ship {
	private static final int X = 0;
	private static final int Y = 1;
	private static final int LEFT = 0;
	private static final int RIGHT = 1;
	private static final int BODY_SIZE = 32;
	private static final int RADIUS = BODY_SIZE/2;
	private static final int MAX_SENSORS = 10;
	private static final int MAX_VEL = 5;
	private static final double FRICTION = 0.2;
	
	private int pWidth, pHeight, direction;
	private int[][] dirList = {{-1,0},{0,-1},{1,0},{0,1}};
	private int[][] genome;
	private Obstacles obs;
	private Sensor[] sensors;
	private Point body;
	
	private Point velocity;
	private Random rInt = new Random();
	
	public Ship( int pW, int pH, Obstacles os )
	{
		pWidth = pW;
		pHeight = pH;
		obs = os;
		direction = 0;
		body = new Point( pWidth/2, pHeight/2 );
		sensors = new Sensor[] {new Sensor( 4, -12, this, obs )};
	}
	
	public Ship( int[][] g, int pW, int pH, Obstacles os )
	{
		
		pWidth = pW;
		pHeight = pH;
		obs = os;
		direction = 0;
		body = new Point( pWidth/2, pHeight/2 );
		velocity = new Point( 0, 0 );
		genome = g;
		sensors = new Sensor[genome.length];
		for ( int i=0; i < genome.length; i++ ) {
			sensors[i] = new Sensor( genome[i][X], 0-genome[i][Y], this, obs );
		}
	}
	
	public void move( int dir )
	{
		//body.x += dirList[dir][X];
		//body.y += dirList[dir][Y];
		
		body.x += velocity.x;
		body.y += velocity.y;
	}
	
	public boolean outOfBounds( int x, int y )
	{
		if ( x <= 0 || x >= pWidth ) return true;
		if ( y <= 0 || y >= pHeight ) return true;
		return false;
	}
	
	public void update()
	{
		//if ( body.x <= 0 || body.x >= pWidth ) direction = Math.abs( direction-2 );
		//if ( outOfBounds( body.x, body.y ) == true ) direction = Math.abs( direction-2 );
		move(direction);
		if ( outOfBounds( body.x, body.y ) == true ) body = new Point( pWidth/2, pHeight/2 );
		for ( Sensor s: sensors ) {
			s.update();
		}
		
		//VELOCITY STUFF
		if (rInt.nextInt(100) < 20) {
			if ( rInt.nextInt(2) == 0 ) thrust( LEFT );
			else thrust( RIGHT );
		}
		
		if ( velocity.x > MAX_VEL ) velocity.x = MAX_VEL;
		if ( velocity.y > MAX_VEL ) velocity.y = MAX_VEL;
	}
	
	private void thrust( int dir )
	{
		velocity.x += (dir == LEFT) ? -1 : 1;
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
			if ( s.state == -1 ) continue;//System.out.println( "OOB" );
			else {
				if ( s.state == 0 ) g.setColor( Color.green );
				else g.setColor( Color.orange );
				
				g.fillOval( s.pos[0],  s.pos[1],  s.size,  s.size);
			}
		}
	}
}
