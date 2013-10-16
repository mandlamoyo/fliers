import java.awt.Point;


public class Sensor {
	private static final int OOB = 2;
	private static final int INACTIVE = 0;
	private static final int ACTIVE = 1;
	private static final int SIZE = 5;
	
	private Point body;
	public int state;
	private int id;
	private int[] stateTypes;
	private int[] relativePos;
	private Ship owner;
	private Obstacles obs;
	
	public int size;
	public int[] pos = new int[2];
	
	public Sensor( int rx, int ry, Ship s, Obstacles os )
	{
		owner = s;
		obs = os;
		size = SIZE;
		state = INACTIVE;
		body = new Point();
		relativePos = new int[] {rx,ry};
		updatePos();
	}
	
	private void updatePos()
	{
		int[] ownerPos = owner.getPos();
		body.x = ownerPos[0] + relativePos[0];
		body.y = ownerPos[1] + relativePos[1];
		
		pos[0] = body.x;
		pos[1] = body.y;
	}
	
	private void setState()
	{
		//if ( body.x <= 0 || body.x >= pWidth ) direction = Math.abs( direction-2 );
		if ( owner.outOfBounds(body.x, body.y )) {
			state = OOB;
		} 
		else {
			if ( obs.isSelectedAt( body.x, body.y )) {
				state = ACTIVE;
			} else {
				state = INACTIVE;
			}
		}
	}
	
	public double getState()
	{ return state / 10.0; }
	
	public void update()
	{
		updatePos();
		//Should sensor updating consume energy?
		setState();


	}
}
