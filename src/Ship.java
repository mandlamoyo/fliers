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
	private static final int MAX_SENSORS = 10;
	private static final int MAX_VEL = 4;
	private static final double FRICTION = 0.2;
	
	private int pWidth, pHeight, direction;
	private int[] output;
	private int[][] dirList = {{-1,0},{0,-1},{1,0},{0,1}};
	private int[][] genome;
	private ArrayList<Double> brainInput;
	private ArrayList<Double> brainOutput;
	private ArrayList<Double> sensorOutput;
	private ArrayList<Double> weights;
	
	private Obstacles obs;
	private Sensor[] sensors;
	private Point body;
	
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
	
	public Ship( int[][] g, int pW, int pH, Obstacles os )
	{
		
		pWidth = pW;
		pHeight = pH;
		obs = os;
		direction = 0;
		body = new Point( pWidth/2, pHeight/2 );
		velocity = new Point( 0, 0 );
		genome = g;
		output = new int[genome.length];
		sensors = new Sensor[genome.length];
		for ( int i=0; i < genome.length; i++ ) {
			sensors[i] = new Sensor( genome[i][X], 0-genome[i][Y], this, obs );
		}
		
		sensorOutput = new ArrayList<Double>();
		brainInput = new ArrayList<Double>();
		brainOutput = new ArrayList<Double>();
		weights = new ArrayList<Double>();
		
		// NeuralNet constructor: 
		//	( numInputs, numOutputs, numHiddenLayers, numNeuronsInInputLayer, neuronsPerHiddenLayer )
		brain = new NeuralNet( sensors.length+2, 3, 1, 4, 3 ); //check hiddenneurons
	}
	
	public void move( int dir )
	{
		//body.x += dirList[dir][X];
		//body.y += dirList[dir][Y];
		
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
			
			brainInput = (ArrayList)sensorOutput.clone();
			brainInput.add( (double)body.x );
			brainInput.add( (double)body.y );
			
			brainOutput = brain.update( brainInput );
			
			System.out.print( "Brain Output: ");
			for ( int i=0; i < brainOutput.size(); i++ ) {
				System.out.print( brainOutput.get(i) );
				System.out.print( ", " );
			}
			
			/* PRINT OUT WEIGHTS
			System.out.print( "\n" );
			weights = brain.GetWeights();
			
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
		move(direction);
		
		//boundary checking
		if ( outOfBounds( body.x, body.y ) == true ) {
			body = new Point( pWidth/2, pHeight/2 );
			velocity = new Point( 0, 0 );
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
		double normalizedOutput;
		sensorOutput.clear();
		
		for ( int i=0; i< sensors.length; i++ ) {
			sensors[i].update();
			//output[i] = sensors[i].getState();
			
			sensorOutput.add( sensors[i].getState() );
		}
	}
	
	public void draw( Graphics g )
	{
		
		g.setColor( Color.red );
		g.fillOval( body.x,  body.y,  BODY_SIZE,  BODY_SIZE );
		
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
