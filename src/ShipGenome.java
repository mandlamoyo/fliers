import java.util.ArrayList;

public class ShipGenome implements Comparable<ShipGenome>{
	private int score;
	private int lifespan;
	private int[][] sensors;
	private ArrayList<Double> weights;
	
	public ShipGenome( int[][] snsrs, int life )
	{
		score = 0;
		sensors = snsrs;
		lifespan = life;
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


	@Override
	public int compareTo( ShipGenome otherShipGenome ) {
		//Order scores from highest to lowest
		return otherShipGenome.getScore() - score;
	}
	
}
