package neuralnetwork;

public class NeuralNet {
	private int numInputs;
	private int numOutputs;
	private int numHiddenLayers;
	private int neuronsPerHiddenLayer;
	
	private NeuronLayer[] layers;
	
	public NeuralNet()
	{
		
	}
	
	private void CreateNet()
	{
		
		if ( numHiddenLayers > 0 ) {
			
			
			for( int i=0; i < numHiddenLayers; i++ ) {
				int n = (i==0) ? numInputs : neuronsPerHiddenLayer;
				layers[i] = new NeuronLayer( neuronsPerHiddenLayer, numInputs );
			}
			
			//layers
		} else {
			layers = new NeuronLayer[] { new NeuronLayer( numOutputs, numInputs )};
		}
		
	}
	
	public double[] GetWeights()
	{
		return new double[1];
	}
	
	
}
