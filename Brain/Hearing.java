package Brain;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class Hearing extends Thread {
	private Brain b;
	private AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
    private TargetDataLine microphone;
    private SourceDataLine speakers;
    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    private int numBytesRead;
    private int CHUNK_SIZE = 1024;
    private byte[] data;
    private int bytesRead = 0;
    private boolean running = true;
    private boolean monitor = false;
    public Hearing(Brain b) {
    	this.b = b;
    	try {
	    	microphone = AudioSystem.getTargetDataLine(format);
	        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
	        microphone = (TargetDataLine) AudioSystem.getLine(info);
	        microphone.open(format);
	        data = new byte[microphone.getBufferSize() / 5];
	        microphone.start();
	        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
	        speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
	        speakers.open(format);
	        speakers.start();
    	} catch (LineUnavailableException e) {
    		e.printStackTrace();
    	}
    }
    
    public void run() {
    	while (running) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
			numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
    		if (monitor) {
            	speakers.write(data, 0, numBytesRead);
            }
    		sendInputToBrain(convertBytesToALD(data));
    	}
    }
    public ArrayList <Double> convertBytesToALD(byte[] data) {
    	ArrayList <Double> al = new ArrayList <Double> ();
    	for (int i = 0; i < data.length; i++) {
    		al.add(convertByteToDouble(data[i]));
    	}
    	return al;
    }
    public void sendInputToBrain(ArrayList <Double> input) {
		Neuron[][] neurons = b.getNeurons();
		for (int i = 0; i < input.size(); i++) {
			for (int j = 0; j < 10; j++) {
				if (i*10 + j >= input.size() - 1)
					break;
				neurons[9][j].sendInputToNeuron(input.get(i*10 + j));
			}
		}
	}
    public double convertByteToDouble(byte b) {
    	int i = b + 129;
    	return i/257.0;
    }
    public void setRunning(boolean running) {
		this.running = running;
	}
	public void setMonitor(boolean monitor) {
		this.monitor = monitor;
	}
}
