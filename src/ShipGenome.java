import java.util.ArrayList;
import java.util.Random;

public class ShipGenome implements Comparable<ShipGenome>{
	private static final int X = 0;
	private static final int Y = 1;
	private static final int CHANCE_OF_MUTATION = 40; //out of 100 (actually 2/3 of this value)
	private static final int SENSOR_MOVEMENT_RANGE = 10;
	private static final int MAX_SENSOR_DISTANCE = 100;
	
	private Random r;
	private int score;
	private int lifespan;
	private int[][] sensors;
	private ArrayList<Double> weights;
	
	
	public ShipGenome( int numSensors, int life )
	{
		r = new Random();
		
		score = 0;
		lifespan = life;
		sensors = getSensorGene( numSensors );
	}
	
	//----- ACCESSOR FUNCTIONS -----//
	// SET
	public void setScore( int newScore )
	{	score = newScore; }
	
	public void setWeights( ArrayList<Double> wghts )
	{	weights = (ArrayList<Double>) wghts.clone(); }
	
	
	// GET
	public int getScore()
	{	return score; }
	
	public int getLifespan()
	{	return lifespan; }
	
	public int[][] getSensors()
	{	return sensors.clone(); }
	
	public ArrayList<Double> getWeights()
	{	
		if ( weights == null ) return new ArrayList<Double>();
		return (ArrayList<Double>) weights.clone(); }

	//------------------------------//

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
	
	public void mutate()
	{
		//Mutate weights
		for ( int i=0; i < weights.size(); i++ ) {
			if ( r.nextInt(100) < CHANCE_OF_MUTATION ) {
				double w = weights.get(i);
				double alteration = (Math.random()*0.2) * (r.nextInt(3)-1);
				if ( w + alteration > 0 && w + alteration < 1 ) weights.set(i, w + ( alteration ));
			}
		}
		
		//Mutate sensors
		for ( int i=0; i < sensors.length; i++ ) {
			//by position
			for ( int j=0; j < sensors[i].length; j++ ) {
				if ( r.nextInt(100) < CHANCE_OF_MUTATION ) {
					int newPos = sensors[i][j] + (int) Math.sqrt( r.nextInt( (int)Math.pow( SENSOR_MOVEMENT_RANGE, 2 ))) * (r.nextInt(3)-1);
					if ( Sensor.getDistance( newPos, sensors[i][1-j] ) < MAX_SENSOR_DISTANCE ) sensors[i][j] = newPos;
				}
			}
			
			//by rotation
			
		}
		//Mutate sensors by position
		
		//Mutate sensors by distance
		
		
	}
	
	public static ShipGenome crossover( ArrayList<ShipGenome> genomes )
	{
		//take two from genomes (biased to higher scores)
		//take some of first's genes, some of second's
		Random r = new Random();
		int range = genomes.size();
		ShipGenome parent1 = genomes.get( (int) Math.sqrt( r.nextInt( (int)Math.pow( range, 2 ))));
		ShipGenome parent2 = genomes.get( (int) Math.sqrt( r.nextInt( (int)Math.pow( range, 2 ))));
		
		//Implement the actual crossover (and move this code to outer "getOffspring" function?)
		return new ShipGenome( parent1.sensors.length, parent2.lifespan );
	}
	
	@Override
	public int compareTo( ShipGenome otherShipGenome ) {
		//Order scores from highest to lowest
		return otherShipGenome.getScore() - score;
	}
	
}
