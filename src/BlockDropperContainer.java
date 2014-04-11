import java.util.Random;

public class BlockDropperContainer {
	private static final int FREQ_THRESHOLD = 20;
	
	private BlockDropper[] droppers;
	private int numBlocks;
	private int pWidth;
	private int period;
	private int count;
	private int avCount;
	private float lastAverage;
	private Random r;
	
	public BlockDropperContainer( int width, int blockSize, int pd, Obstacles os )
	{
		numBlocks = width/blockSize;
		droppers = new BlockDropper[numBlocks];
		lastAverage = 0;
		r = new Random();
		avCount = 90;
		period = pd;
		count = 0;
		
		for ( int i=0; i < numBlocks; i++ ) {
			boolean barrier = false;
			int freq = r.nextInt( FREQ_THRESHOLD );
			if (freq < 1) freq = 1;
			if ( i == 0 || i == numBlocks-1 || i == numBlocks/2 ) barrier = true;
			droppers[i] = new BlockDropper( i*blockSize + blockSize/2, 0, freq, barrier, os );
		}
		
	}
	
	public float getThresholdAverage()
	{	return lastAverage; }
	
	private void setThresholdAverage()
	{
		avCount = 0;
		float sum = 0;
		for ( int i=0; i < numBlocks; i++ ) sum += droppers[i].getThreshold();
		lastAverage = sum/droppers.length;
	}
	
	private void unPerturb( BlockDropper bd )
	{
		//System.out.println( "Block dropper threshold perturbed: " + bd.getPerturbed() + " -> " + bd.getThreshold() );
		if( avCount == 100 ) {
			setThresholdAverage();
			//System.out.println( "Threshold average: " + getThresholdAverage() );
		} else {
			avCount++;
		}
		//System.out.println();
		bd.resetPerturbed();
	}
	
	public void update()
	{
		if ( count == period ) {
			for ( int i=0; i < numBlocks; i++ ) {
				droppers[i].update();
				if ( droppers[i].getPerturbed() == 0 ) unPerturb( droppers[i] );
			}
			
			count = 0;
		} else {
			count++;
		}
	}
}
