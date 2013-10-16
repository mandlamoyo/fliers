import java.awt.Point;
import java.util.Random;

public class BlockDropper {
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
	
	public void update()
	{
		if ( r.nextInt(100) < blockThreshold ) {
			obs.add( body.x, body.y );
		}
	}
}
