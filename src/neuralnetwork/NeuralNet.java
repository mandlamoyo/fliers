package neuralnetwork;

import java.util.*;

public class NeuralNet {
	private int numInputs;
	private int numOutputs;
	private int numHiddenLayers;
	//private int activationResponse;
	private int neuronsInputLayer;
	private int neuronsPerHiddenLayer;
	
	//private int bias;
	private ArrayList<Double> outputs;
	private NeuronLayer[] layers;
	
	public NeuralNet( int inp, int out, int hidden, int neuroInit, int neuroHidden )
	{
		numInputs = inp;
		numOutputs = out;
		numHiddenLayers = hidden;
		//activationResponse = 1;
		neuronsInputLayer = neuroInit;
		neuronsPerHiddenLayer = neuroHidden;
		
		outputs = new ArrayList<Double>(); 
		createNet();
	}
	
	public int getNeuronCount()
	{	
		int total = 0;
		for ( NeuronLayer nl : layers ) {
			total += nl.numNeurons;
		}
		return total;
	}
	
	private void createNet()
	{
		layers = new NeuronLayer[numHiddenLayers+2];
		
		//Input layer
		layers[0] = new NeuronLayer( neuronsInputLayer, numInputs );
		int numInputs = neuronsInputLayer;
		
		//Hidden layers
		for( int i=0; i < numHiddenLayers; i++ ) {
			layers[i+1] = new NeuronLayer( neuronsPerHiddenLayer, numInputs );
			numInputs = neuronsPerHiddenLayer;
		}
		
		//Output layer
		layers[layers.length-1] = new NeuronLayer( numOutputs, numInputs );
	}
	
	
	public ArrayList<Double> getWeights()
	{
		ArrayList<Double> weights = new ArrayList<Double>();
		ArrayList<Double> buffer = new ArrayList<Double>();
		
		for ( int i=0; i < layers.length; i++ ) {
			buffer = layers[i].getWeights();
			for ( int j=0; j < buffer.size(); j++ ) {
				weights.add( buffer.get(j) );
			}
		}
		
		return weights;
	}
	
	public void setWeights( ArrayList<Double> weights ) //DOES THIS WORK?
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
	}
}
