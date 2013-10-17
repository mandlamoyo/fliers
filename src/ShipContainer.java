import java.util.*;
import java.awt.*;

public class ShipContainer {
	private static final int X = 0;
	private static final int Y = 1;
	private static final int ALIVE = -1;
	private static final int SHIP_BODY_SIZE = 32;
	private static final int SENSOR_COUNT = 5;
	private static final int DEFAULT_LIFESPAN = 1000;
	private static final int MAX_SENSOR_DISTANCE = 100;
	
	private int pWidth;
	private int pHeight;
	private int shipIds;
	
	private Obstacles obs;
	private Random r;
	private ArrayList<Ship> shipList;
	private ArrayList<ShipGenome> genomeList;
	
	public ShipContainer( int pW, int pH, Obstacles os )
	{
		shipIds = 0;
		obs = os;
		pWidth = pW;
		pHeight = pH;
		r = new Random();
		shipList = new ArrayList<Ship>();
		genomeList = new ArrayList<ShipGenome>();
		//scoredWeightsList = new ArrayList<ScoredWeights>();
	}
	
	
	private int[][] getSensorGene( int numSensors )
	{
		int[][] gene = new int[numSensors][2];
		
		
		for ( int i=0; i < numSensors; i++ ) {
			int[] pos = new int[] { MAX_SENSOR_DISTANCE, MAX_SENSOR_DISTANCE };
			
			do {	
				pos[X] = r.nextInt( MAX_SENSOR_DISTANCE );
				pos[Y] = r.nextInt( MAX_SENSOR_DISTANCE );
				
				if ( r.nextInt(2) == 0 ) pos[X] *= -1;
				if ( r.nextInt(2) == 0 ) pos[Y] *= -1;
			} while ( Sensor.getDistance( pos[X], pos[Y] ) > MAX_SENSOR_DISTANCE );
			gene[i][X] = pos[X];
			gene[i][Y] = pos[Y];
		}
		
		return gene;
	}
	
	public void buildShip( ShipGenome gnme )
	{	
		shipList.add( new Ship( shipIds, gnme, pWidth, pHeight, obs ));
		shipIds++;
	}
	
	public void buildRandom()
	{	
		ShipGenome genome = new ShipGenome( getSensorGene( SENSOR_COUNT ), DEFAULT_LIFESPAN );
		buildShip( genome );
	}
	
	public void buildShips( int n )
	{
		for ( int i=0; i < n; i++ ) buildRandom();
	}
	
	
	public void printGenomeScores()
	{
		Collections.sort( genomeList );
		System.out.println( "GENOME SCORES: " );
		
		for ( int i=0; i < genomeList.size(); i++ ) {
			ArrayList<Double> weights = genomeList.get(i).getWeights();
			int score = genomeList.get(i).getScore();
			
			System.out.print( "\tScore: " + score + "\n\tWeights: ");
			for ( int j=0; j < weights.size(); j++ ) {
				System.out.print( weights.get(j) );
				System.out.print( ", " );
			}
			System.out.print( "\n\n" );
		}
	}
	
	public void printShipScores()
	{
		System.out.print( "SHIP SCORES: " );
		for ( int i=0; i < shipList.size(); i++ ) {
			System.out.print( "+" );
		}
		System.out.print( "\n" );
	}
	
	public void update()
	{
		ArrayList<Ship> buffer = new ArrayList<Ship>();
		
		for ( int i=0; i < shipList.size(); i++ ) {
			Ship s = shipList.get(i);
			int result = s.update();
			if ( result == ALIVE ) {
				buffer.add( s );
				
			} else {
				s.setScore();
				genomeList.add( s.getGenome() );
			}
		}
		
		shipList = buffer;
	}
	
	public boolean isSelectedAt( int x, int y )
	{
		for( int i=0; i < shipList.size(); i++) {
			Ship s = shipList.get(i);
			int[] shipPos = s.getOrigin();
			if( x+SHIP_BODY_SIZE/2 >= shipPos[X] && x+SHIP_BODY_SIZE/2 <= shipPos[X]+SHIP_BODY_SIZE && y+SHIP_BODY_SIZE/2 >= shipPos[Y] && y+SHIP_BODY_SIZE/2 <= shipPos[Y]+SHIP_BODY_SIZE ) {
				if ( !s.isSelected() ) s.select();
				else s.deselect();
				
				return true;
				
			} else {
				s.deselect();
			}
		}
		return false;
	}
	
	public void draw( Graphics g )
	{
		for ( int i=0; i < shipList.size(); i++ ) {
			shipList.get(i).draw( g );
		}
	}
}
