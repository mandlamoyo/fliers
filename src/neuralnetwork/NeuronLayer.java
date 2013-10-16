package neuralnetwork;

import java.util.ArrayList;

public class NeuronLayer {
	public int numNeurons;
	public Neuron[] neuronList;
	public ArrayList<Double> outputs;
	
	public NeuronLayer( int numOfNeurons, int numInputsPerNeuron )
	{
		numNeurons = numOfNeurons;
		neuronList = new Neuron[numNeurons];
		outputs = new ArrayList<Double>();
		
		for ( int i=0; i < numNeurons; i++ ) {
			neuronList[i] = new Neuron( numInputsPerNeuron );
		}
	}
	
	public ArrayList<Double> getWeights()
	{
		ArrayList<Double> weights = new ArrayList<Double>();
		
		for ( int i=0; i < numNeurons; i++ ) {
			double[] neuroWeights = neuronList[i].getWeights();
			for ( int j=0; j < neuroWeights.length; j++ ) {
				weights.add( neuroWeights[j] );
			}
		}
		
		return weights;
	}
	
	public ArrayList<Double> process( ArrayList<Double> inputs )
	{
		
		for ( int i=0; i < numNeurons; i++ ) {
			outputs.add( neuronList[i].calculate( inputs ));
		}
		
		return outputs;
	}
}
