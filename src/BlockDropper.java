import java.awt.Point;
import java.util.Random;

public class BlockDropper {
	private static final int LOWERLIMIT = 0;
	private static final int UPPERLIMIT = 40;
	private static final float CHANCE_OF_PERTURBATION = 0.2f;
	
	private int blockThreshold;
	private int perturbed;
	private Point body;
	private Random r;
	private Obstacles obs;
	
	private boolean barrier;
	
	public BlockDropper( int x, int y, int freq, boolean barr, Obstacles os )
	{
		obs = os;
		barrier = barr;
		r = new Random();
		perturbed = 0;
		body = new Point( x, y );
		blockThreshold = r.nextInt( freq );
		//blockThreshold = (int) Math.sqrt( r.nextInt( (int)Math.pow( freq, 2 )));
	}
	
	public int getThreshold()
	{	return blockThreshold; }
	
	public int getPerturbed()
	{	return perturbed; }
	
	public void resetPerturbed()
	{	perturbed = 0; }
	
	private void perturbThreshold()
	{
		blockThreshold += r.nextInt( 4 ) - 1;
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
		
			if ( r.nextInt(100*100) < (int) ( CHANCE_OF_PERTURBATION*100 )) {
				perturbed = blockThreshold;
				//System.out.print( "PTD: " + blockThreshold );
				perturbThreshold();
				//System.out.println( " -> " + blockThreshold );
			}
		}
	}
}
