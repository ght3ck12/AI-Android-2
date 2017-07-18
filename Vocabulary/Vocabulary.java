package Vocabulary;

import java.io.Serializable;
import java.util.ArrayList;

import Utilities.FileOperations;

public class Vocabulary implements Serializable {
	public String name;
	public ArrayList <String> v = new ArrayList <String> ();
	public void load(FileOperations fo, String fileName) {
		v = (ArrayList <String>)fo.load(fileName);
	}
}
