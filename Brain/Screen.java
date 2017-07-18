package Brain;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Screen extends Thread {
	private Brain b;
	private boolean running = true;
	public Screen(Brain b) {
		this.b = b;
		setRunning(true);
	}
	public void run() {
		while (running) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException ie) {
				
			}
			try {
				BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
				ArrayList <Double> inputs = new ArrayList <Double> ();
				for (int i = 0; i < image.getWidth(); i++) {
					for (int j = 0; j < image.getHeight(); j++) {
						inputs.add(convertColorToDouble(new Color(image.getRGB(i, j)).getRed()));
						inputs.add(convertColorToDouble(new Color(image.getRGB(i, j)).getGreen()));
						inputs.add(convertColorToDouble(new Color(image.getRGB(i, j)).getBlue()));
					}
				}
				sendInputToBrain(inputs);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	}
	public void sendInputToBrain(ArrayList <Double> input) {
		Neuron[][] neurons = b.getNeurons();
		for (int i = 0; i < input.size(); i++) {
			for (int j = 0; j < 10; j++) {
				if (i*10 + j >= input.size() - 1)
					break;
				neurons[8][j].sendInputToNeuron(input.get(i*10 + j));
			}
		}
	}
	public double convertColorToDouble(int color) {
		return (color + 1)/257.0;
	}
	public void setRunning(boolean running) {
		this.running = running;
	}
}
