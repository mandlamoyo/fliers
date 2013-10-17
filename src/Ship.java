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
	
	private static final int BODY_SIZE = 32;
	private static final int RADIUS = BODY_SIZE/2;
	private static final int MAX_VEL = 4;
	private static final double FRICTION = 0.2;
	
	private int pWidth, pHeight;
	
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
	
	/*
	public Ship( int pW, int pH, Obstacles os )
	{
		pWidth = pW;
		pHeight = pH;
		obs = os;
		direction = 0;
		body = new Point( pWidth/2, pHeight/2 );
		sensors = new Sensor[] {new Sensor( 4, -12, this, obs )};
	}*/
	
	public Ship( int[][] gnme, int pW, int pH, Obstacles os )
	{
		
		pWidth = pW;
		pHeight = pH;
		obs = os;
		body = new Point( pWidth/2, pHeight/2 );
		lastPos = new Point( pWidth/2, pHeight/2 );
		
		velocity = new Point( 0, 0 );
		genome = gnme;
		sensors = new Sensor[genome.length];
		for ( int i=0; i < genome.length; i++ ) {
			sensors[i] = new Sensor( genome[i][X], 0-genome[i][Y], this, obs );
		}
		
		sensorOutput = new ArrayList<Double>();
		brainOutput = new ArrayList<Double>();
		
		// NeuralNet constructor: 
		//	( numInputs, numOutputs, numHiddenLayers, numNeuronsInInputLayer, neuronsPerHiddenLayer )
		brain = new NeuralNet( sensors.length, 3, 1, 4, 3 ); //check hiddenneurons
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
	
	public void update()
	{
		//if ( body.x <= 0 || body.x >= pWidth ) direction = Math.abs( direction-2 );
		//if ( outOfBounds( body.x, body.y ) == true ) direction = Math.abs( direction-2 );
		
		
		//Update sensors
		getSensorOutput();
		/*if (rInt.nextInt(600) < 10) {
			System.out.print( "Sensor output: ");
			for (int i : output) {
				System.out.print( i );
				System.out.print( ", " );
			}
			System.out.print( "\n" );
			//System.out.println( output );
		}*/
		
		//Update velocity
		if (rInt.nextInt(100) < 20) {
			if ( rInt.nextInt(2) == 0 ) thrust( LEFT );
			else thrust( RIGHT );
			
			System.out.print( "Sensor Output: " );
			for ( int i=0; i < sensorOutput.size(); i++ ) {
				System.out.print( sensorOutput.get(i) );
				System.out.print( ", " );
			}
			System.out.print( "\n" );
			
			/*
			ArrayList<Double> brainInput = (ArrayList)sensorOutput.clone();
			brainInput.add( (double)body.x / pWidth );
			brainInput.add( (double)body.y / pHeight );
			brainOutput = brain.update( brainInput );
			*/
			
			brainOutput = brain.update( sensorOutput );
			
			System.out.print( "Brain Output: ");
			for ( int i=0; i < brainOutput.size(); i++ ) {
				System.out.print( brainOutput.get(i) );
				System.out.print( ", " );
			}
			
			/* PRINT OUT WEIGHTS
			System.out.print( "\n" );
			ArrayList<Double> weights = brain.GetWeights();
			
			System.out.print( "Weights: " );
			for ( int i=0; i < weights.size(); i++ ) {
				System.out.print( weights.get(i) );
				System.out.print( ", " );
			}
			*/
			
			System.out.print( "\n\n" );
			
		}
		
		if (rInt.nextInt(100) < 20) thrust( rInt.nextInt(4) );
		
		if ( velocity.x > MAX_VEL ) velocity.x = MAX_VEL;
		if ( velocity.y > MAX_VEL ) velocity.y = MAX_VEL;
		
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
		//velocity.x += (dir == LEFT) ? -1 : 1;
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
			//output[i] = sensors[i].getState();
			
			sensorOutput.add( sensors[i].getState() );
		}
	}
	
	public void draw( Graphics g )
	{
		//int MAX_SENSOR_DIST = 100;
		//g.setColor( Color.yellow );
		//g.fillOval( body.x-MAX_SENSOR_DIST,  body.y-MAX_SENSOR_DIST, MAX_SENSOR_DIST*2, MAX_SENSOR_DIST*2 );
		
		g.setColor( Color.red );
		g.fillOval( body.x-RADIUS,  body.y-RADIUS,  BODY_SIZE,  BODY_SIZE );
		
		g.setColor( Color.blue );
		g.fillOval( body.x-2, body.y-2, 4, 4 );
		g.setColor( Color.green );
		for ( Sensor s: sensors ) {
			if ( s.state == -1 ) continue;//System.out.println( "OOB" );
			else {
				if ( s.state == 0 ) g.setColor( Color.green );
				else g.setColor( Color.orange );
				
				g.fillOval( s.pos[0],  s.pos[1],  s.size,  s.size);
			}
		}
	}
}
