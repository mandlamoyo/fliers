import java.util.ArrayList;
import java.util.Random;

public class ShipGenome implements Comparable<ShipGenome>{
	private static final int CHANCE_OF_MUTATION = 40; //out of 100 (actually 2/3 of this value)
	
	private Random r;
	private int score;
	private int lifespan;
	private int[][] sensors;
	private ArrayList<Double> weights;
	
	
	public ShipGenome( int[][] snsrs, int life )
	{
		score = 0;
		sensors = snsrs;
		lifespan = life;
		r = new Random();
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

	public void mutate()
	{
		for ( int i=0; i < weights.size(); i++ ) {
			if ( r.nextInt(100) < CHANCE_OF_MUTATION ) {
				double w = weights.get(i);
				double alteration = (Math.random()*0.2) * (r.nextInt(3)-1);
				if ( w + alteration > 0 && w + alteration < 1 ) weights.set(i, w + ( alteration ));
			}
		}
	}

	
	@Override
	public int compareTo( ShipGenome otherShipGenome ) {
		//Order scores from highest to lowest
		return otherShipGenome.getScore() - score;
	}
	
}
