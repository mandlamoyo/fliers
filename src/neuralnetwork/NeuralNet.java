package neuralnetwork;

import java.util.*;

public class NeuralNet {
	private int numInputs;
	private int numOutputs;
	private int numHiddenLayers;
	private int neuronsPerHiddenLayer;
	
	private int bias;
	private ArrayList<Double> outputs;
	private NeuronLayer[] layers;
	
	public NeuralNet()
	{
		bias = -1;
		outputs = new double[numOutputs];
	}
	
	private void CreateNet()
	{
		
		if ( numHiddenLayers > 0 ) {
			
			layers = new NeuronLayer[numHiddenLayers+1];
			
			for( int i=0; i < numHiddenLayers; i++ ) {
				int n = (i==0) ? numInputs : neuronsPerHiddenLayer;
				layers[i] = new NeuronLayer( neuronsPerHiddenLayer, n );
			}
			
			layers[numHiddenLayers] = new NeuronLayer( numOutputs, neuronsPerHiddenLayer );
			
		} else {
			layers = new NeuronLayer[] { new NeuronLayer( numOutputs, numInputs )};
		}
		
	}
	
	
	public double[] GetWeights()
	{
		return new double[1];
	}
	
	public void PutWeights()
	{
		
	}
	
	private int GetNumOfWeights()
	{
		return 0;
	}
	
	private double Sigmoid()
	{
		return 0.0;
	}
	
	private double[] update( double[] inputs )
	{
		int currentWeight = 0;
		
		if ( inputs.length != numInputs ) return (outputs = new double[outputs.length]);
		
		for ( int i=0; i < numHiddenLayers; i++ ) {
			if ( i > 0 ) System.arraycopy( outputs, 0, inputs, 0, outputs.length );
			
			outputs = new double[outputs.length];
			currentWeight = 0;
			
			for ( int j=0; j < layers[i].numNeurons; j++ ) {
				double netInput = 0;
				int neuronInputs = layers[i].neuronList[j].numInputs; 
				
				for ( int k=0; k < neuronInputs; k++ ) {
					netInput += layers[i].neuronList[j].inputWeights[k] * inputs[currentWeight];
				}
				
				netInput += layers[i].neuronList[j].inputWeights[neuronInputs-1] * bias;
				
				outputs.add( Sigmoid( netInput, activationResponse ));
			}
			
		}
		
		return outputs;
	}
}
