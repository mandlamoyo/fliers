package neuralnetwork;

import java.util.Random;

public class Neuron {
	public int numInputs;
	public double[] inputWeights;
	private Random r = new Random();
	
	public Neuron( int numInp )
	{
		inputWeights = new double[numInputs+1];
		for ( int i=0; i < inputWeights.length; i++ ) {
			inputWeights[i] = Math.random();
			if ( r.nextInt(2) == 1 ) inputWeights[i] *= -1;
		}
	}
}
