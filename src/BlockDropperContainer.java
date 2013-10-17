import java.util.Random;

public class BlockDropperContainer {
	private static final int FREQ_THRESHOLD = 50;
	
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
			int freq = r.nextInt( FREQ_THRESHOLD );
			if (freq < 1) freq = 1;
			droppers[i] = new BlockDropper( i*blockSize + blockSize/2, 0, freq, os );
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
