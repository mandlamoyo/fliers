package neuralnetwork;

import java.util.*;

public class NeuralNet {
	private int numInputs;
	private int numOutputs;
	private int numHiddenLayers;
	private int activationResponse;
	private int neuronsInputLayer;
	private int neuronsPerHiddenLayer;
	
	private int bias;
	private ArrayList<Double> outputs;
	private NeuronLayer[] layers;
	
	public NeuralNet( int inp, int out, int hidden, int neuroInit, int neuroHidden )
	{
		numInputs = inp;
		numOutputs = out;
		numHiddenLayers = hidden;
		activationResponse = 1;
		neuronsInputLayer = neuroInit;
		neuronsPerHiddenLayer = neuroHidden;
		
		bias = -1;
		outputs = new ArrayList<Double>(); 
		CreateNet();
	}
	
	private void CreateNet()
	{
		layers = new NeuronLayer[numHiddenLayers+2];
		
		//Input layer
		layers[0] = new NeuronLayer( neuronsInputLayer, numInputs );
		
		//Hidden layers
		for( int i=0; i < numHiddenLayers; i++ ) {
			int n = (i==0) ? numInputs : neuronsPerHiddenLayer;
			layers[i+1] = new NeuronLayer( neuronsPerHiddenLayer, n );
		}
		
		//Output layer
		layers[layers.length-1] = new NeuronLayer( numOutputs, neuronsInputLayer );
		
		/*
		if ( numHiddenLayers > 0 ) {
			
			layers = new NeuronLayer[numHiddenLayers+1];
			
			for( int i=0; i < numHiddenLayers; i++ ) {
				int n = (i==0) ? numInputs : neuronsPerHiddenLayer;
				layers[i] = new NeuronLayer( neuronsPerHiddenLayer, n );
			}
			
			layers[numHiddenLayers] = new NeuronLayer( numOutputs, neuronsPerHiddenLayer );
			
		} else {
			layers = new NeuronLayer[] { new NeuronLayer( neuronsInputLayer, numInputs ),
											new NeuronLayer( numOutputs, neuronsInputLayer )};
		}
		*/
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
	}
	
	private double Sigmoid( double activation, int response )
	{	return 1/ (1+Math.exp(-activation/response)); }
	*/
	
	public ArrayList<Double> update( ArrayList<Double> inputs )
	{
		if ( inputs.size() != numInputs ) {
			outputs.clear();
			return outputs;
		}
		
		for ( int i=0; i < layers.length; i++ ) {
			outputs.clear();
			outputs = layers[i].process( inputs );
			inputs = (ArrayList)outputs.clone();
		}
		
		return outputs;
		
		/*
		int currentWeight = 0;
		
		
		
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
		*/
	}
}
