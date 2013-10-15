package neuralnetwork;

import java.util.*;

public class NeuralNet {
	private int numInputs;
	private int numOutputs;
	private int numHiddenLayers;
	private int activationResponse;
	private int neuronsPerHiddenLayer;
	
	private int bias;
	private ArrayList<Double> outputs;
	private NeuronLayer[] layers;
	
	public NeuralNet( int inp, int out, int hidden, int neurohidden )
	{
		numInputs = inp;
		numOutputs = out;
		numHiddenLayers = hidden;
		activationResponse = 1;
		neuronsPerHiddenLayer = neurohidden;
		
		bias = -1;
		outputs = new ArrayList<Double>(); 
		CreateNet();
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
	
	
	public ArrayList<Double> GetWeights()
	{
		ArrayList<Double> weights = new ArrayList<Double>();
		
		for ( int i=0; i < layers.length; i++ ) {
			for ( int j=0; j < layers[i].neuronList.length; j++ ) {
				for ( int k=0; k < layers[i].neuronList[j].inputWeights.length-1; k++ ) {
					weights.add( layers[i].neuronList[j].inputWeights[k] );
				}
			}
		}
		
		return weights;
	}
	
	public void PutWeights( ArrayList<Double> weights )
	{
		int currentWeight = 0;

		for ( int i=0; i < layers.length; i++ ) {
			for ( int j=0; j < layers[i].neuronList.length; j++ ) {
				for ( int k=0; k < layers[i].neuronList[j].inputWeights.length-1; k++ ) {
					layers[i].neuronList[j].inputWeights[k] = weights.get( currentWeight );
					currentWeight++;
				}
			}
		}
	}
	
	/*private int GetNumOfWeights()
	{
		return 0;
	}*/
	
	private double Sigmoid( double activation, int response )
	{	return 1/ (1+Math.exp(-activation/response)); }
	
	public ArrayList<Double> update( ArrayList<Double> inputs )
	{
		int currentWeight = 0;
		
		if ( inputs.size() != numInputs ) {
			outputs.clear();
			return outputs;
		}
		
		for ( int i=0; i < numHiddenLayers; i++ ) {
			if ( i > 0 ) inputs = (ArrayList<Double>)outputs.clone(); 
			
			outputs = new ArrayList<Double>(); //double[outputs.size()];
			currentWeight = 0;
			
			for ( int j=0; j < layers[i].numNeurons; j++ ) {
				double netInput = 0;
				int neuronInputs = layers[i].neuronList[j].numInputs; 
				
				for ( int k=0; k < neuronInputs; k++ ) {
					netInput += layers[i].neuronList[j].inputWeights[k] * inputs.get(currentWeight);
				}
				
				netInput += layers[i].neuronList[j].inputWeights[neuronInputs-1] * bias;
				
				outputs.add( Sigmoid( netInput, activationResponse ));
				currentWeight = 0;
			}
			
		}
		
		return outputs;
	}
}
