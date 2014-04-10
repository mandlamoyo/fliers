import java.util.Random;

public class BlockDropperContainer {
	private static final int FREQ_THRESHOLD = 20;
	
	private BlockDropper[] droppers;
	private int numBlocks;
	private int pWidth;
	private int period;
	private int count;
	private Random r;
	
	public BlockDropperContainer( int width, int blockSize, int pd, Obstacles os )
	{
		numBlocks = width/blockSize;
		droppers = new BlockDropper[numBlocks];
		r = new Random();
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
	
	public void update()
	{
		if ( count == period ) {
			for ( int i=0; i < numBlocks; i++ ) {
				droppers[i].update();
			}
			
			count = 0;
		} else {
			count++;
		}
	}
}
