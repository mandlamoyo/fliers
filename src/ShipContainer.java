import java.util.*;
import java.awt.*;

public class ShipContainer {
	private static final int X = 0;
	private static final int Y = 1;
	private static final int SENSOR_COUNT = 3;
	private static final int MAX_SENSOR_DISTANCE = 50;
	
	private ArrayList<Ship> shipList;
	private int pWidth;
	private int pHeight;
	private Obstacles obs;
	private Random r;
	
	public ShipContainer( int pW, int pH, Obstacles os )
	{
		obs = os;
		pWidth = pW;
		pHeight = pH;
		r = new Random();
		shipList = new ArrayList<Ship>();
	}
	
	private int[][] genSensorGenome( int numSensors )
	{
		int[][] genome = new int[numSensors][2];
		
		
		for ( int i=0; i < numSensors; i++ ) {
			int[] gene = new int[] { MAX_SENSOR_DISTANCE, MAX_SENSOR_DISTANCE };
			
			do {	
				gene[0] = r.nextInt( MAX_SENSOR_DISTANCE );
				gene[1] = r.nextInt( MAX_SENSOR_DISTANCE );
				
				if ( r.nextInt(2) == 0 ) gene[0] *= -1;
				if ( r.nextInt(2) == 0 ) gene[1] *= -1;
			} while ( Sensor.getDistance( gene[X], gene[Y] ) > MAX_SENSOR_DISTANCE );
			genome[i][0] = gene[0];
			genome[i][1] = gene[1];
		}
		
		for ( int i=0; i < genome.length; i++ ) {
			System.out.print( "{" );
			for ( int j=0; j < 2; j++ ) {
				System.out.print( genome[i][j] );
				System.out.print( "," );
			}
			System.out.print( "}, " );
		}
		System.out.print( "\n" );
		
		return genome;
	}
	
	public void buildShip( int[][] genome )
	{	shipList.add( new Ship( genome, pWidth, pHeight, obs )); }
	
	public void buildRandom()
	{	buildShip( genSensorGenome( SENSOR_COUNT )); }
	
	public void buildShips( int n )
	{
		for ( int i=0; i < n; i++ ) buildRandom();
	}
	
	public void update()
	{
		for ( int i=0; i < shipList.size(); i++ ) {
			shipList.get(i).update();
		}
	}
	
	public void draw( Graphics g )
	{
		for ( int i=0; i < shipList.size(); i++ ) {
			shipList.get(i).draw( g );
		}
	}
}
