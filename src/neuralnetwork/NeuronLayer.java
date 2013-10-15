package neuralnetwork;

public class NeuronLayer {
	public int numNeurons;
	public Neuron[] neuronList;
	
	public NeuronLayer( int numOfNeurons, int numInputsPerNeuron )
	{
		numNeurons = numOfNeurons;
		neuronList = new Neuron[numNeurons];
		
		for ( int i=0; i < numNeurons; i++ ) {
			neuronList[i] = new Neuron( numInputsPerNeuron );
		}
	}
}
