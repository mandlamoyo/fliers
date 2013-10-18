import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;

public class ShipGenome implements Comparable<ShipGenome>{
	private static final int X = 0;
	private static final int Y = 1;
	private static final int CHANCE_OF_MUTATION = 40; //out of 100 (actually 2/3 of this value)
	private static final int SENSOR_MOVEMENT_RANGE = 10;
	private static final int MAX_SENSOR_DISTANCE = 70;
	
	private Random r;
	public int gid;
	private int score;
	private int lifespan;
	private int neuronCount;
	private int[][] sensors;
	private ArrayList<Double> weights;
	
	
	public ShipGenome( int numSensors, int life, int id )
	{
		r = new Random();
		
		gid = id;
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
	
	public void setSensors( int[][] snsrs )
	{
		sensors = snsrs;
	}
	
	public void setNeuronCount( int nc )
	{	neuronCount = nc; }
	
	// GET
	public int getScore()
	{	return score; }
	
	public int getLifespan()
	{	return lifespan; }
	
	public int getNeuronCount()
	{	return neuronCount; }
	
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
					int newPos = sensors[i][j] + (SENSOR_MOVEMENT_RANGE - (int) Math.sqrt( r.nextInt( (int)Math.pow( SENSOR_MOVEMENT_RANGE, 2 )))) * (r.nextInt(3)-1);
					if ( Sensor.getDistance( newPos, sensors[i][1-j] ) < MAX_SENSOR_DISTANCE ) sensors[i][j] = newPos;
				}
			}
			
			//by rotation
			
		}
		//Mutate sensors by position
		
		//Mutate sensors by distance
		
		
	}
	
	public static ShipGenome crossover( ArrayList<ShipGenome> genomes, int[] neuronsPerLayer, int[] weightsPerLayer )
	{
		//take two from genomes (biased to higher scores)
		//take some of first's genes, some of second's
		Random r = new Random();
		int range = genomes.size();
		//System.out.println( "RANGE: " + range );
		Collections.sort( genomes );
		
		// total-sqrt ->   0 BIAS <- 	   TOTAL
		// sqrt 	  ->   0	   -> BIAS TOTAL
		int v1 = Math.min( range - (int) Math.sqrt( r.nextInt( (int)Math.pow( range, 2 ))), genomes.size()-1 ); 
		int v2 = Math.min( range - (int) Math.sqrt( r.nextInt( (int)Math.pow( range, 2 ))), genomes.size()-1 );
		
		if ( r.nextInt(50) < 5 ) v1 = 0;
		if ( r.nextInt(50) < 5 ) v1 = Math.min( r.nextInt(10),  genomes.size()-1 );
		ShipGenome parent1 = genomes.get( v1 );
		ShipGenome parent2 = genomes.get( v2 );
		
		//System.out.println( v1 + ", " + v2 );
		
		//Crossover to create new
		ArrayList<Double> w1 = parent1.getWeights();
		ArrayList<Double> w2 = parent2.getWeights();
		ArrayList<Double> wc = new ArrayList<Double>();
		
		int[][] s1 = parent1.getSensors();
		int[][] s2 = parent2.getSensors();
		int[][] sc = new int[s2.length][2];

		
		//Crossover parent weight vectors on neuron input boundaries
		int[] segments = new int[parent1.getNeuronCount()];
		for ( int i=0; i < parent1.getNeuronCount(); i++ ) {
			int[] weightRange = getNeuronWeightRange( i, neuronsPerLayer, weightsPerLayer );
			ArrayList<Double> chosenList = (r.nextBoolean()) ? w1 : w2;
			
			for ( int j=weightRange[0]; j < weightRange[1]; j++ ) {
				wc.add( chosenList.get( j ));
			}
		}
		
		//Crossover parent sensor vectors
		for ( int i=0; i < s1.length; i++ ) {
			if (r.nextBoolean()) sc[i] = s1[i].clone();
			else sc[i] = s2[i].clone();
		}
		
		ShipGenome child =  new ShipGenome( parent1.sensors.length, parent2.lifespan, ShipContainer.gid_counter );
		ShipContainer.gid_counter++;
		child.setWeights( wc );
		child.setSensors( sc );
		return child;
	}
	
	private static int[] getNeuronWeightRange( int neuron, int[] lyrs, int[] wghts )
	{
		/* NEURON -> WEIGHT_RANGE
		def getRange( neuron, lyrs, wghts ):
			spread = []
			for i in range(len(lyrs)):
				for j in range(lyrs[i]):
					spread.append(wghts[i])
			li = spread[:n]
        	return [sum(li), sum(li)+spread[n]-1]
		*/
		
		int sum = 0;
		ArrayList<Integer> spread = new ArrayList<Integer>();
		
		for ( int i=0; i < lyrs.length; i++ ) {
			for ( int j=0; j < lyrs[i]; j++ ) {
				spread.add(wghts[i]);
			}
		}
		List<Integer> newList = spread.subList( 0, neuron );
		
		for ( int i=0; i < newList.size(); i++ ) sum += newList.get(i);
		return new int[] {sum, sum+spread.get(neuron)};
	}
	
	@Override
	public int compareTo( ShipGenome otherShipGenome ) {
		//Order scores from highest to lowest
		return otherShipGenome.getScore() - score;
	}
	
}
