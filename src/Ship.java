import java.awt.*;
import java.awt.geom.*;
import java.util.Random;
import java.util.ArrayList;

import neuralnetwork.NeuralNet;

public class Ship {
	private static final int X = 0;
	private static final int Y = 1;
	private static final int LEFT = 0;
	private static final int RIGHT = 1;
	private static final int UP = 2;
	private static final int DOWN = 3;
	private static final int ALIVE = -1;
	
	private static final int INPUT_NEURONS = 4;
	private static final int HIDDEN_NEURONS = 3;
	private static final int OUTPUT_NEURONS = 3;
	private static final int HIDDEN_LAYERS = 1;
	
	private static final int MAX_VEL = 6;
	private static final int BODY_SIZE = 32;
	private static final int RADIUS = BODY_SIZE/2;
	
	// { SENSOR OUTPUT, SENSOR POSITIONS, BRAIN_OUTPUT, BRAIN_WEIGHTS, VELOCITY }
	private static final int SENSOR_OUT = 0;
	private static final int SENSOR_POS = 1;
	private static final int BRAIN_OUT = 2;
	private static final int BRAIN_WEIGHTS = 3;
	private static final int VELOCITY = 4;
	private static final boolean[] PRINTOUT = { true, false, true, false, true };
	
	private int id;
	private int pWidth, pHeight;
	private int lifespan;
	private int fitness;
	private boolean isSelected;
	
	private ArrayList<Double> brainOutput;
	private ArrayList<Double> sensorOutput;
	
	private Point body;
	private Point lastPos;
	private Point velocity;
	private Obstacles obs;
	private NeuralNet brain;
	private ShipGenome genome;
	
	private Sensor[] sensors;
	
	private Random rInt = new Random();
	

	public Ship( int sid, ShipGenome gnme, int pW, int pH, Obstacles os )
	{
		id = sid;
		obs = os;
		pWidth = pW;
		pHeight = pH;
		genome = gnme;
		
		fitness = 0;
		isSelected = false;
		lifespan = genome.getLifespan();
		
		body = new Point( pWidth/2, pHeight/2 );
		lastPos = new Point( pWidth/2, pHeight/2 );
		velocity = new Point( 0, 0 );
		
		sensorOutput = new ArrayList<Double>();
		brainOutput = new ArrayList<Double>();
		
		int[][] sensorGene = genome.getSensors();
		sensors = new Sensor[sensorGene.length];
		for ( int i=0; i < sensorGene.length; i++ ) {
			sensors[i] = new Sensor( sensorGene[i][X], 0-sensorGene[i][Y], this, obs );
		}
		
		// NeuralNet constructor: 
		//	( numInputs, numOutputs, numHiddenLayers, numNeuronsInInputLayer, neuronsPerHiddenLayer )
		brain = new NeuralNet( sensors.length, OUTPUT_NEURONS, HIDDEN_LAYERS, INPUT_NEURONS, HIDDEN_NEURONS );
		
		// Either set brain weights to those specified in the genome, or set genome weights to 
		//	brain's weights (random) if no weights have been specified
		ArrayList<Double> weights = genome.getWeights();
		if ( weights.isEmpty() ) genome.setWeights( brain.getWeights() );
		else brain.setWeights( weights );
		
	}
	
	public void move()
	{
		lastPos.x = body.x;
		lastPos.y = body.y;
		body.x += velocity.x;
		body.y += velocity.y;
	}
	
	public boolean outOfBounds( int x, int y )
	{
		if ( x <= 0 || x >= pWidth ) return true;
		if ( y <= 0 || y >= pHeight ) return true;
		return false;
	}
	
	public void select()
	{	isSelected = true; }
	
	public void deselect()
	{	isSelected = false; }
	
	public boolean isSelected()
	{	return isSelected; }
	
	//----- ACCESSOR FUNCTIONS -----//
	// SET
	public void setWeights( ArrayList<Double> wghts )
	{
		brain.setWeights( wghts );
		genome.setWeights( wghts );
	}
	
	public void setScore()
	{	genome.setScore( fitness ); }
	
	// GET
	public int[] getTop()
	{	return new int[] {body.x+RADIUS, body.y}; }
	
	public int[] getOrigin()
	{	return new int[] {body.x, body.y}; }
	
	public int getFitness()
	{	return fitness; }
	
	public int getLifespan()
	{	return lifespan; }
	
	public NeuralNet getBrain()
	{	return brain; }
	
	public ArrayList<Double> getWeights()
	{	return brain.getWeights(); }
	
	public ShipGenome getGenome()
	{	return genome; }
	
	//------------------------------//
	
	private void printOutput()
	{
		int printed = 0;

		
		if ( PRINTOUT[SENSOR_OUT] == true ) {
			System.out.print( "Sensor Output: " );
			for ( int i=0; i < sensorOutput.size(); i++ ) {
				System.out.print( sensorOutput.get(i) );
				System.out.print( ", " );
			}
			System.out.print( "\n" );
			printed++;
		}
		
		if ( PRINTOUT[SENSOR_POS] == true ) {
			int[][] gene = genome.getSensors();
			for ( int i=0; i < gene.length; i++ ) {
				System.out.print( "{" );
				for ( int j=0; j < 2; j++ ) {
					System.out.print( gene[i][j] );
					System.out.print( "," );
				}
				System.out.print( "}, " );
			}
			System.out.print( "\n" );
			printed++;
		}
		
		if ( PRINTOUT[BRAIN_OUT] == true ) {
			brainOutput = brain.update( sensorOutput );
			
			System.out.print( "Brain Output: ");
			for ( int i=0; i < brainOutput.size(); i++ ) {
				System.out.print( brainOutput.get(i) );
				System.out.print( ", " );
			}
			System.out.print( "\n" );
			printed++;
		}
		
		if ( PRINTOUT[BRAIN_WEIGHTS] == true ) {
			ArrayList<Double> weights = brain.getWeights();
			
			System.out.print( "Weights: " );
			for ( int i=0; i < weights.size(); i++ ) {
				System.out.print( weights.get(i) );
				System.out.print( ", " );
			}
			System.out.print( "\n" );
			printed++;
		}
		
		if ( PRINTOUT[VELOCITY] == true ) {
			System.out.println( "Velocity: (" + velocity.x + "," + velocity.y + ")" );
			printed++;
		}
		
		if ( printed > 0 ) {
			System.out.println( "SHIP ID: " + id );
			System.out.print( "\n" );
		}
	}
	
	public int update()
	{	
		//Check life
		if ( lifespan <= 0 ) return fitness;

		
		//Update sensors
		getSensorOutput();
		
		//Update velocity
		if (rInt.nextInt(100) < 20) {
			if (rInt.nextInt(100) < 20) thrust( rInt.nextInt(4) );
			printOutput();
			
		}
		
		
		if ( Math.abs( velocity.x ) > MAX_VEL ) velocity.x = MAX_VEL * velocity.x/Math.abs(velocity.x);
		if ( Math.abs( velocity.y ) > MAX_VEL ) velocity.y = MAX_VEL * velocity.y/Math.abs(velocity.y);
		
		//apply velocity
		move();
		
		//boundary checking
		if ( outOfBounds( body.x, body.y ) == true ) {
			body.x = lastPos.x;
			body.y = lastPos.y;
			//body = new Point( pWidth/2, pHeight/2 );
			//velocity = new Point( 0, 0 );
		}
		
		fitness++;
		lifespan--;
		return ALIVE;
	}
	
	private void thrust( int dir )
	{
		switch( dir ){ 
			case LEFT:
				velocity.x -= 1;
				break;
				
			case RIGHT:
				velocity.x += 1;
				break;
				
			case UP:
				velocity.y -= 1;
				break;
				
			case DOWN:
				velocity.y += 1;
				break;
				
			default:
				break;
		}
	}
	
	
	
	private void getSensorOutput()
	{	
		sensorOutput.clear();
		
		for ( int i=0; i< sensors.length; i++ ) {
			sensors[i].update();
			sensorOutput.add( sensors[i].getState() );
		}
	}
	
	public void draw( Graphics g )
	{
		//SHOW MAXIMUM SENSOR DISTANCE
		//int MAX_SENSOR_DIST = 100;
		//g.setColor( Color.yellow );
		//g.fillOval( body.x-MAX_SENSOR_DIST,  body.y-MAX_SENSOR_DIST, MAX_SENSOR_DIST*2, MAX_SENSOR_DIST*2 );
		
		if ( isSelected ) g.setColor( Color.yellow );
		else g.setColor( Color.red );
		g.fillOval( body.x-RADIUS,  body.y-RADIUS,  BODY_SIZE,  BODY_SIZE );
		
		g.setColor( Color.blue );
		g.fillOval( body.x-2, body.y-2, 4, 4 );
		g.setColor( Color.green );
		for ( Sensor s: sensors ) {
			if ( s.state == -1 ) continue;
			else {
				if ( s.state == 0 ) g.setColor( Color.green );
				else g.setColor( Color.orange );
				
				g.fillOval( s.pos[0],  s.pos[1],  s.size,  s.size);
			}
		}
	}
}
