import java.awt.Point;


public class Sensor {
	private Point body;
	private int state;
	private int[] stateTypes;
	private int[] relativePos;
	private Ship owner;
	
	public Sensor( int rx, int ry, Ship s )
	{
		owner = s;
		state = 0;
		body = new Point();
		relativePos = new int[] {rx,ry};
		updatePos();
	}
	
	private void updatePos()
	{
		int[] ownerPos = owner.getPos();
		body.x = ownerPos[0] + relativePos[0];
		body.y = ownerPos[1] + relativePos[1];
	}
	
	public void update()
	{
		updatePos();
	}
}
