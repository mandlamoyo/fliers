package neuralnetwork;

import java.util.Random;
import java.util.ArrayList;

public class Neuron {
	private int numInputs;
	private int activationResponse;
	
	private double output;
	public double[] inputWeights;
	private Random r = new Random();
	
	public Neuron( int numInp )
	{
		activationResponse = 1;
		inputWeights = new double[numInputs+1];
		for ( int i=0; i < inputWeights.length; i++ ) {
			inputWeights[i] = Math.random();
			if ( r.nextInt(2) == 1 ) inputWeights[i] *= -1;
		}
	}
	
	//public double[] getWeights()
	//{ return inputWeights; }
	
	private double sigmoid( double activation, int response )
	{	return 1/ (1+Math.exp(-activation/response)); }
	
	public double calculate( ArrayList<Double> inputs )
	{
		if ( inputs.size() != numInputs ) return 0.0;
		
		double netInput = 0;
		for ( int i=0; i < numInputs; i++ ) {
			netInput += inputs.get(i) * inputWeights[i];
		}
		
		netInput -= inputWeights[numInputs];
		output = sigmoid( netInput, activationResponse );
		return output;
	}
}
