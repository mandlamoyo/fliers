import java.awt.Point;
import java.util.Random;

public class BlockDropper {
	private static final int LOWERLIMIT = 10;
	private static final int UPPERLIMIT = 90;
	
	private int blockThreshold;
	private Point body;
	private Random r;
	private Obstacles obs;
	
	public BlockDropper( int x, int y, int freq, Obstacles os )
	{
		obs = os;
		r = new Random();
		body = new Point( x, y );
		blockThreshold = r.nextInt( freq );
	}
	
	private void perturbThreshold()
	{
		blockThreshold += r.nextInt( 4 ) - 2;
		if ( blockThreshold < LOWERLIMIT ) blockThreshold = LOWERLIMIT;
		if ( blockThreshold > UPPERLIMIT ) blockThreshold = UPPERLIMIT;
	}
	
	public void update()
	{
		if ( r.nextInt(100) < blockThreshold ) {
			obs.add( body.x, body.y );
		}
		
		if ( r.nextInt(100) < 5 ) perturbThreshold();
	}
}
