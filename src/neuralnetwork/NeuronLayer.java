package neuralnetwork;

public class NeuronLayer {
	public int numNeurons;
	public Neuron[] neuronList;
	
	public NeuronLayer( int numOfNeurons, int numInputsPerNeuron )
	{
		numNeurons = numOfNeurons;
		
		for ( int i=0; i < numInputsPerNeuron; i++ ) {
			neuronList[i] = new Neuron( numInputsPerNeuron );
		}
	}
}
