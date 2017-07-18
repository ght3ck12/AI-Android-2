package Brain;

import java.io.Serializable;

public class Message implements Serializable {
	public double [] pattern = new double[6];
	public Range [] rangePattern = new Range[6];
	public int counter = 0;
	public boolean full = false;
	public Range getRange(double val) {
		if (val >= .5) {
			return Range.HIGH;
		} else {
			return Range.LOW;
		}
	}
	public void addToPattern(double x) {
		if (counter < 6) {
			pattern[counter] = x;
			rangePattern[counter] = getRange(x);
			counter++;
		} else {
			full = true;
		}
	}
	public boolean sharesPattern(Message m) {
		for (int i = 0; i < rangePattern.length; i++) {
			if (m.rangePattern[i] != rangePattern[i]) {
				return false;
			}
		}
		return true;
	}
	public boolean isFull() {
		return full;
	}
}

enum Range {
	HIGH, LOW;
}