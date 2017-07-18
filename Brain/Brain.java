package Brain;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;


public class Brain implements Serializable {
	public Neuron [][] neurons = new Neuron[10][10];
	public Brain() {
		createNeurons();
		connectNeurons();
		begin();
	}
	public Neuron[][] getNeurons() {
		return neurons;
	}
	public void createNeurons() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				neurons[i][j] = new Neuron();
			}
		}
	}
	public void connectNeurons() {
		for (int i = 0; i < 10; i++) {
			for (int j0 = 0; j0 < 9; j0++) {
				neurons[i][j0].connect(neurons[i][j0+1]);
				neurons[i][j0+1].connect(neurons[i][j0]);
			}
			for (int j = 0; j < 10; j++) {
				for (int k = 0; k < 10; k++) {
					if (j != k) {
						neurons[i][j].connect(neurons[i][k]);
						neurons[i][k].connect(neurons[i][j]);
					}
				}
			}
		}
	}
	public void begin() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				neurons[i][j].begin();
			}
		}
	}
	public void halt() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				neurons[i][j].halt();
			}
		}
	}
}
