package Brain;

import java.io.Serializable;
import java.util.ArrayList;

public class Neuron implements Serializable, Runnable {
	public double temp = Math.random();
	public double nucleus = 0.0;
	public boolean running = false;
	public Message tempMessage = new Message();
	public Message receivedMessage;
	public Message tempReceivedMessage;
	public int counter = 0;
	public Message sendToNucleus;
	public ArrayList <Neuron> connectedNeurons = new ArrayList <Neuron> ();
	public CurrentState currentState = CurrentState.Receiving;
	public transient Thread nThread;
	public Neuron() {
	}
	public void connect(Neuron n) {
		connectedNeurons.add(n);
	}
	@Override
	public void run() {
		while (running) {
			try {
				if (currentState == CurrentState.Processing) {
					if (counter != 6) {
						Thread.sleep(10);
						thinkAbout(tempReceivedMessage.pattern[counter]);
						counter++;
					} else {
						currentState = currentState.Receiving;
					}
				} 
				if (currentState == CurrentState.Sending || currentState == currentState.Receiving) {
					Thread.sleep(10);
					think();
					if (!tempMessage.isFull()) {
						currentState = CurrentState.Receiving;
						tempMessage.addToPattern(nucleus);
					} else {
						currentState = CurrentState.Sending;
						sendMessage(tempMessage);
						tempMessage = new Message();
					}
				}
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
	public void think() {
		String tempStr = String.valueOf(temp);
		tempStr = tempStr.replaceAll("E-", "");
		tempStr = tempStr.substring(2);
		tempStr = remove0s(tempStr);
		if (tempStr.length() > 10) {
			tempStr = tempStr.substring(0, 10);
			tempStr = "0." + tempStr;
		} else {
			tempStr = fill(tempStr);
		}
		nucleus = Double.parseDouble(tempStr);
		temp = Math.sin(nucleus/7);
	}
	public void thinkAbout(double proposition) {
		String tempStr = String.valueOf(temp);
		tempStr = tempStr.replaceAll("E-", "");
		tempStr = tempStr.substring(2);
		tempStr = remove0s(tempStr);
		if (tempStr.length() > 10) {
			tempStr = tempStr.substring(0, 10);
			tempStr = "0." + tempStr;
		} else {
			tempStr = fill(tempStr);
		}
		nucleus = Double.parseDouble(tempStr);
		temp = Math.sin((nucleus + proposition)/7);
	}
	public String fill(String tempStr) {
		int length = tempStr.length();
		int repetitions = (int)Math.floor(10.0/(double)length);
		int tag = 10%length;
		for (int i = 0; i < repetitions-1; i++) {
			tempStr += tempStr;
		}
		tempStr += tempStr.substring(0, tag);
		return "0." + tempStr;
	}
	public String remove0s(String tempStr) {
		return tempStr.replaceAll("0", "");
	}
	public void begin() {
		running = true;
		nThread = new Thread(this);
		nThread.start();
	}
	public void halt() {
		running = false;
	}
	public void sendMessage(Message m) {
		ArrayList <Neuron> receivingNeurons = getListOfReceivingNeurons();
		for (int i = 0; i < receivingNeurons.size(); i++) {
			receivingNeurons.get(i).receiveMessage(m);
		}
	}
	public void receiveMessage(Message m) {
		if (receivedMessage == null) {
			receivedMessage = m;
		} else if (receivedMessage.sharesPattern(m)) {
			//System.out.println("Matched pattern!");
			//for (int i = 0; i < receivedMessage.pattern.length; i++) {
			//	System.out.print(receivedMessage.pattern[i] + ":  ");
			//}
			//System.out.println();
			tempReceivedMessage = m;
			receivedMessage = null;
			//for (int i = 0; i < tempReceivedMessage.pattern.length; i++) {
			//	System.out.print(tempReceivedMessage.pattern[i] + ":  ");
			//}
			//System.out.println();
			counter = 0;
			currentState = CurrentState.Processing;
		}
	}
	public void sendInputToNeuron(double input) {
		thinkAbout(input);
	}
	public CurrentState getCurrentState() {
		return currentState;
	}
	public ArrayList <Neuron> getListOfReceivingNeurons() {
		ArrayList <Neuron> toReturn = new ArrayList <Neuron> ();
		for (int i = 0; i < connectedNeurons.size(); i++) {
			if (connectedNeurons.get(i).getCurrentState() == CurrentState.Receiving) {
				toReturn.add(connectedNeurons.get(i));
			}
		}
		return toReturn;
	}
}

enum CurrentState {
	Receiving, Sending, Processing;
}