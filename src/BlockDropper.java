import java.awt.Point;
import java.util.Random;

public class BlockDropper {
	private static final int LOWERLIMIT = 10;
	private static final int UPPERLIMIT = 60;
	
	private int blockThreshold;
	private Point body;
	private Random r;
	private Obstacles obs;
	
	private boolean barrier;
	
	public BlockDropper( int x, int y, int freq, boolean barr, Obstacles os )
	{
		obs = os;
		barrier = barr;
		r = new Random();
		body = new Point( x, y );
		blockThreshold = r.nextInt( freq );
		//blockThreshold = (int) Math.sqrt( r.nextInt( (int)Math.pow( freq, 2 )));
	}
	
	private void perturbThreshold()
	{
		blockThreshold += r.nextInt( 4 ) - 2;
		if ( blockThreshold < LOWERLIMIT ) blockThreshold = LOWERLIMIT;
		if ( blockThreshold > UPPERLIMIT ) blockThreshold = UPPERLIMIT;
	}
	
	public void update()
	{
		if ( barrier ) {//&& r.nextBoolean()) {
			obs.add( body.x, body.y );
			obs.add( body.x, body.y + FlierPanel.BOX_HEIGHT );
			
		} else {
			if ( r.nextInt(100) < blockThreshold ) {
				obs.add( body.x, body.y );
			}
		
			//if ( r.nextInt(100) < 5 ) perturbThreshold();
		}
	}
}
