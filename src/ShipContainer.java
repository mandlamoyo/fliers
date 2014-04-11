import java.util.*;
import java.awt.*;
import neuralnetwork.NeuralNet;

public class ShipContainer {
	private static final int X = 0;
	private static final int Y = 1;
	private static final int ALIVE = -1;
	private static final int DELAY = 500;
	private static final int NEURONS = 0;
	private static final int WEIGHTS = 1;
	private static final int SENSOR_COUNT = 8;
	private static final int MAX_POP_SIZE = 200;
	private static final int SHIP_BODY_SIZE = 32;
	private static final int SHOW_BEST_LIMIT = 30;
	private static final int DEFAULT_LIFESPAN = 10000;
	private static final int MAX_SENSOR_DISTANCE = 70;
	private static final int MIN_GENOME_COLLECTION_SIZE = 50;
	private static final int MAX_GENOME_COLLECTION_SIZE = 200;
	
	private static final int CHANCE_MUTATION = 15; //Out of 100
	private static final int MAX_GENOME_PRINTOUT = 20;
	
	public static int gid_counter;
	
	private int start;
	private int pWidth;
	private int pHeight;
	private int shipIds;
	
	private int activeShips;
	private int shipsFromGenomes;
	private int totalShipsCreated;
	
	private int lifeExtended;
	
	private Obstacles obs;
	private Random r;
	private ArrayList<Ship> shipList;
	private ArrayList<ShipGenome> genomeList;
	private ShipGenome currentBest;
	
	public ShipContainer( int pW, int pH, Obstacles os )
	{
		lifeExtended = DEFAULT_LIFESPAN;
		gid_counter = 0;
		shipIds = 0;
		start = 0;
		obs = os;
		pWidth = pW;
		pHeight = pH;
		r = new Random();
		shipList = new ArrayList<Ship>();
		genomeList = new ArrayList<ShipGenome>();
		currentBest = new ShipGenome( SENSOR_COUNT, lifeExtended, gid_counter );
		//scoredWeightsList = new ArrayList<ScoredWeights>();
	}
	
	/*
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
	*/
	
	public void buildShip( ShipGenome gnme )
	{	
		shipList.add( new Ship( shipIds, gnme, pWidth, pHeight, obs ));
		shipIds++;
	}
	
	public void buildRandom()
	{	
		ShipGenome genome = new ShipGenome( SENSOR_COUNT, lifeExtended, gid_counter );
		buildShip( genome );
		gid_counter++;
	}
	
	public void buildFromGenomes()
	{
		if ( genomeList.size() > MIN_GENOME_COLLECTION_SIZE && shipList.size() < MAX_POP_SIZE ) {
		//if ( shipList.size() < MAX_POP_SIZE ) {
			int[][] layerInfo = Ship.getBrainStructure();
			ShipGenome genome = ShipGenome.crossover( genomeList, layerInfo[NEURONS], layerInfo[WEIGHTS] );
			genome.mutate();
			buildShip( genome );
		}
	}
	
	public void buildShips( int n )
	{
		for ( int i=0; i < n; i++ ) buildRandom();
	}
	
	
	private void populateGenomeList()
	{
		while ( genomeList.size() < MAX_GENOME_COLLECTION_SIZE ) genomeList.add( new ShipGenome( SENSOR_COUNT, lifeExtended, gid_counter ));
	}
	
	public int getBestScore( int n )
	{
		if( genomeList.size() > 0 ) {
			Collections.sort( genomeList );
			return genomeList.get(n).getScore();
		} else {
			return 0;
		}
	}
	
	public int getCurrentLifeSpan()
	{	return lifeExtended; }
	
	public void printGenomeScores()
	{
		Collections.sort( genomeList );
		System.out.print( "SCORELIST { " );
		for ( int i=0; i < genomeList.size(); i++ ) {
			System.out.print( genomeList.get(i).getScore() + ", " );
		}
		System.out.print( "}\n" );
		
		/*Collections.sort( genomeList );
		System.out.println( "GENOME SCORES: " );
		System.out.println( "CURRENT BEST: ");
		
		double pScore = currentBest.getScore()/(double)currentBest.getLifespan();
		System.out.print( "\tScore: " + currentBest.getScore() + " {" + pScore + "}");
		System.out.print( "\n\tWeights: " );
		for ( int j=0; j < currentBest.getWeights().size(); j++ ) {
			System.out.print( currentBest.getWeights().get(j) );
			System.out.print( ", " );
		}
		System.out.print( "\n\n\n" );
		
		for ( int i=0; i < Math.min( genomeList.size(), MAX_GENOME_PRINTOUT ); i++ ) {
			ShipGenome g = genomeList.get(i);
			ArrayList<Double> weights = g.getWeights();
			int score = g.getScore();
			double percScore = score/(double)g.getLifespan();
			System.out.print( "\tScore: " + score + " {" + percScore + "}");
			if ( g.gid == currentBest.gid ) System.out.print( "*" );
			System.out.print( "\n\tWeights: " );
			for ( int j=0; j < weights.size(); j++ ) {
				System.out.print( weights.get(j) );
				System.out.print( ", " );
			}
			System.out.print( "\n\n" );
		}*/
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
		lifeExtended = DEFAULT_LIFESPAN + getBestScore( genomeList.size()/2 );
		
		if ( genomeList.size() > 10 ) {
			ShipGenome g = new ShipGenome( SENSOR_COUNT, lifeExtended, gid_counter );
			g.setSensors( currentBest.getSensors() );
			g.setWeights( currentBest.getWeights() );
			if ( r.nextInt(100) < 15 ) buildShip( g );
			//if ( r.nextInt(100) < 5 ) buildShip( genomeList.get(r.nextInt(10)));
			for ( int i=0; i < 10; i++ ) {
				if ( r.nextInt(100) < 5 ) {
					ShipGenome g2 = new ShipGenome( SENSOR_COUNT, lifeExtended, gid_counter );
					g2.setSensors( genomeList.get(i).getSensors() );
					g2.setWeights( genomeList.get(i).getWeights() );
					buildShip( g2 );
				}
			}
			//shipList.remove( r.nextInt(shipList.size()) );
			//shipList.remove( r.nextInt(shipList.size()) );
			
			
		}
		
		
		
		
		//Fill up population in appropriate manner
		if ( start > DELAY ) {
			if ( shipList.size() < MAX_POP_SIZE && genomeList.size() < MIN_GENOME_COLLECTION_SIZE ) {
				buildRandom();
			} else if ( shipList.size() < MAX_POP_SIZE ) {
				buildFromGenomes();
			}
		}
		start++;
		
		//Remove dead ships from active population
		ArrayList<Ship> buffer = new ArrayList<Ship>();
		
		for ( int i=0; i < shipList.size(); i++ ) {
			Ship s = shipList.get(i);
			int result = s.update();
			if ( result == ALIVE ) {
				buffer.add( s );
				
			} else {
				// Score genome
				s.setScore();
				
				/*
				if ( s.getGenome().getScore() > currentBest.getScore() ) {
					System.out.println( s.getGenome().getScore() + " > " + currentBest.getScore() );
					currentBest.setSensors( s.getGenome().getSensors() ); //) = s.getGenome();
					currentBest.setWeights( s.getWeights() );
					currentBest.setScore( s.getGenome().getScore() );
				}
				*/
				
				// Update genomeList with fit Ships
				if ( genomeList.size() < MAX_GENOME_COLLECTION_SIZE ) genomeList.add( s.getGenome() );
				else {
					Collections.sort( genomeList );
					if ( s.getFitness() > genomeList.get( genomeList.size()-1 ).getScore() ) genomeList.set( genomeList.size()-1, s.getGenome() );
				}
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
	
	public void draw( Graphics g, boolean drawBest )
	{
		for ( int i=0; i < shipList.size(); i++ ) {
			Ship s = shipList.get(i);
			if( !drawBest || SHOW_BEST_LIMIT >= genomeList.size() || s.getFitness() > getBestScore( SHOW_BEST_LIMIT )) {
				s.draw( g );
			}
		}
	}

}
