package evaluation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

public class Evaluation {

	File file = new File("C:\\Wahl\\Wahl_Ergebnisse.txt");
	BufferedReader reader;
	BufferedWriter writer;
	
	static String[] keys = new String[49];
	static int[] content = new int[49];
	
	public static void main(String[] args) {
		
		for(int i = 0; i < 10; i++) {
			
			keys[i] = "party" + ";";
		}
		
		
	}
}
