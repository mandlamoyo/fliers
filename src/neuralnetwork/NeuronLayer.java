package neuralnetwork;

public class NeuronLayer {
	private int numNeurons;
	private Neuron[] neuronList;
	
	public NeuronLayer( int numOfNeurons, int numInputsPerNeuron )
	{
		numNeurons = numOfNeurons;
		
		for ( int i=0; i < numInputsPerNeuron; i++ ) {
			neuronList[i] = new Neuron( numInputsPerNeuron );
		}
	}
}
