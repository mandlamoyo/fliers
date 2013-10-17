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
	
	private static final int INPUT_NEURONS = 4;
	private static final int HIDDEN_NEURONS = 3;
	private static final int OUTPUT_NEURONS = 3;
	private static final int HIDDEN_LAYERS = 1;
	
	private static final int BODY_SIZE = 32;
	private static final int RADIUS = BODY_SIZE/2;
	private static final int MAX_VEL = 6;
	private static final double FRICTION = 0.2;
	
	// { SENSOR OUTPUT, BRAIN_OUTPUT, BRAIN_WEIGHTS, VELOCITY }
	private static final int SENSOR_OUT = 0;
	private static final int BRAIN_OUT = 1;
	private static final int BRAIN_WEIGHTS = 2;
	private static final int VELOCITY = 3;
	private static final boolean[] PRINTOUT = { true, true, false, true };
	
	private int pWidth, pHeight;
	private int lifespan;
	
	private int[][] genome;
	private ArrayList<Double> brainOutput;
	private ArrayList<Double> sensorOutput;
	
	private Obstacles obs;
	private Sensor[] sensors;
	private Point body;
	private Point lastPos;
	
	private Point velocity;
	private Random rInt = new Random();
	private NeuralNet brain;

	
	public Ship( int[][] gnme, int pW, int pH, Obstacles os )
	{
		
		obs = os;
		pWidth = pW;
		pHeight = pH;
		genome = gnme;
		
		body = new Point( pWidth/2, pHeight/2 );
		lastPos = new Point( pWidth/2, pHeight/2 );
		velocity = new Point( 0, 0 );
		
		sensorOutput = new ArrayList<Double>();
		brainOutput = new ArrayList<Double>();
		
		sensors = new Sensor[genome.length];
		for ( int i=0; i < genome.length; i++ ) {
			sensors[i] = new Sensor( genome[i][X], 0-genome[i][Y], this, obs );
		}
		
		// NeuralNet constructor: 
		//	( numInputs, numOutputs, numHiddenLayers, numNeuronsInInputLayer, neuronsPerHiddenLayer )
		brain = new NeuralNet( sensors.length, OUTPUT_NEURONS, HIDDEN_LAYERS, INPUT_NEURONS, HIDDEN_NEURONS );
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
			ArrayList<Double> weights = brain.GetWeights();
			
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
		
		if ( printed > 0 ) System.out.print( "\n" );
	}
	
	public void update()
	{	
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
	
	public int[] getPos()
	{
		return new int[] {body.x+RADIUS, body.y};
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
		
		g.setColor( Color.red );
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
