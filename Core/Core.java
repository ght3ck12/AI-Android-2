package Core;

import java.util.ArrayList;

import Brain.Brain;
import Brain.Hearing;
import Brain.Neuron;
import Brain.Screen;
import Utilities.FileOperations;
import Vocabulary.FormEditor;
import Vocabulary.Generator;
import Vocabulary.Vocabulary;

public class Core extends Thread {
	FileOperations fo = new FileOperations();
	ArrayList <Vocabulary> vocabulary;
	Brain b;
	Hearing h;
	Screen s;
	Main m;
	Vocabulary currentVocabulary;
	Generator g;
	boolean phraseConstruction = false;
	double speechThreshold = 43;
	boolean running = false;
	boolean speaking = false;
	public Core(Main m) {
		this.m = m;
		Object o;
		if ((o = fo.load("./brain.txt")) == null) {
			b = new Brain();
		} else {
			b = (Brain)o;
		}
		if ((o = fo.load("./vocabulary.txt")) == null) {
			vocabulary = new ArrayList <Vocabulary> ();
			Vocabulary main = new Vocabulary();
			main.name = "Main";
			vocabulary.add(main);
		} else {
			vocabulary = (ArrayList <Vocabulary>)o;
		}
		currentVocabulary = vocabulary.get(0);
		h = new Hearing(b);
		h.start();
		s = new Screen(b);
		s.start();
		g = new Generator(this, b);
		setRunning(true);
		start();
	}
	public void run() {
		while (running) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
			if (speaking && thresholdPassed(speechThreshold)) {
				speak();
			}
		}
	}
	public boolean thresholdPassed(double threshold) {
		double total = 0.0;
		for (int i = 0; i < b.getNeurons().length; i++) {
			for (int j = 0; j < b.getNeurons()[0].length; j++) {
				total += b.getNeurons()[i][j].nucleus;
			}
		}
		//System.out.println(total >= threshold);
		return total >= threshold;
	}
	public void speak() {
		process("Your choices of response are: ");
		processAL("Choice", currentVocabulary.v);
		String output = "";
		if (phraseConstruction) {
			if (c(2) == 0) {
				output = g.construct();
			} else {
				output = currentVocabulary.v.get(c(currentVocabulary.v.size()));
			}
		} else {
			output = currentVocabulary.v.get(c(currentVocabulary.v.size()));
		}
		process("Your output: " + output);
		m.speak(output);
	}
	public void setPhraseConstruction(boolean phraseConstruction) {
		this.phraseConstruction = phraseConstruction;
	}
	public String[] getNames() {
		String[] names = new String[vocabulary.size()];
		for (int i = 0; i < vocabulary.size(); i++) {
			names[i] = vocabulary.get(i).name;
		}
		return names;
	}
	public String makeDecision(String question, ArrayList <String> options) {
		process("Question is: " + question);
		processAL("Answer Option", options);
		String output = options.get(c(options.size()));
		process("You chose: " + output);
		return output;
	}
	public ArrayList <String> getVocabulary() {
		return currentVocabulary.v;
	}
	public void addVocabularyEntry(String in) {
		currentVocabulary.v.add(in);
	}
	public void deleteVocabularyEntry(String toDelete) {
		currentVocabulary.v.remove(toDelete);
	}
	public void setCurrentVocabulary(int current) {
		currentVocabulary = vocabulary.get(current);
	}
	public void addVocabulary(String name) {
		Vocabulary v = new Vocabulary();
		v.name = name;
		vocabulary.add(v);
	}
	public void deleteVocabulary(int toDelete) {
		vocabulary.remove(toDelete);
		currentVocabulary = vocabulary.get(toDelete-1);
	}
	public String getResponse(String input) {
		process("User said: " + input);
		if (speaking) {
			return null;
		}
		process("Your choices of response are: ");
		processAL("Choice", currentVocabulary.v);
		String output = "";
		if (phraseConstruction) {
			if (c(2) == 0) {
				output = g.construct();
			} else {
				output = currentVocabulary.v.get(c(currentVocabulary.v.size()));
			}
		} else {
			output = currentVocabulary.v.get(c(currentVocabulary.v.size()));
		}
		process("Your output: " + output);
		return output;
	}
	public void process(String input) {
		sendInputToBrain(divideInput(convertPhrase(input)));
	}
	public void processAL(String message, ArrayList <String> input) {
		for (int i = 0; i < input.size(); i++) {
			process(message + ":  " + i + " " + input.get(i));
		}
	}
	public void sendInputToBrain(ArrayList <double[]> input) {
		Neuron[][] neurons = b.getNeurons();
		for (int i = 0; i < input.size(); i++) {
			for (int j = 0; j < 10; j++) {
				for (int k = 0; k < 10; k++) {
					neurons[j][k].sendInputToNeuron(input.get(i)[j*10 + k]);
				}
			}
		}
	}
	public ArrayList <double[]> divideInput(double[] total) {
		ArrayList <double[]> myAl = new ArrayList <double[]> ();
		int cycles = (int)Math.floor(total.length/100);
		int top = total.length%100;
		for (int i = 0; i < cycles; i++) {
			myAl.add(new double[100]);
			for (int j = 0; j < 100; j++) {
				myAl.get(i)[j] = total[i*100 + j];
			}
		}
		myAl.add(new double[100]);
		for (int i = 0; i < top; i++) {
			myAl.get(cycles)[i] = total[cycles*100 + i];
		}
		return myAl;
	}
	public double[] convertPhrase(String in) {
		double[] list = new double[in.length()];
		for (int i = 0; i < in.length(); i++) {
			list[i] = compressChar(in.charAt(i));
		}
		return list;
	}
	
	public double compressChar(char in) {
		return ((double)in)/65536.0;
	}
	public int c(int length) {
		double a = b.neurons[9][9].nucleus;
		String a0 = String.valueOf(a);
		if (a0.contains("E")) {
			a0 = a0.replaceAll("E-", "");
			a0 = a0.replaceAll("E", "");
		}
		String l0 = String.valueOf(length);
		int l = l0.length();
		int power = l;
		int outOf = (int)Math.pow(10.0, power);
		int selection = Integer.parseInt(a0.substring(a0.length() - l - 1, a0.length() - 1));
		return (int)Math.floor(((double)selection/(double)outOf)*(double)length);
	}
	public void setMonitor(boolean monitor) {
		h.setMonitor(monitor);
	}
	public void setSpeaking(boolean speaking) {
		this.speaking = speaking;
	}
	public void showForms() {
		FormEditor fe = new FormEditor(g);
	}
	public void setRunning(boolean running) {
		this.running = running;
	}
	public void begin() {
		b.begin();
		h = new Hearing(b);
		h.start();
		s = new Screen(b);
		s.start();
	}
	public void halt() {
		h.setRunning(false);
		s.setRunning(false);
		b.halt();
	}
	public void save() {
		halt();
		fo.save(b, "./brain.txt");
		fo.save(vocabulary, "./vocabulary.txt");
		g.save();
	}
}
