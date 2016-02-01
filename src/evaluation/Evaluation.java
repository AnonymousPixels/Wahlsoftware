package evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Evaluation {

	static File file = new File("C:\\Users\\Felix\\Desktop\\Backup2.txt");
	static BufferedReader reader;

	static String[] keys = new String[49];
	static int[] content = new int[49];

	public static void main(String[] args) throws IOException {

		for (int i = 0; i < content.length; i++) {
			keys[i] = "";			
			content[i] = 0;
		}

		keys[0] = "party-1";
		for (int i = 0; i < 9; i++) {
			keys[i+1] = "party" + (i);
		}

		keys[10] = "pres-1";
		for (int i = 0; i < 8; i++) {
			keys[i+11] = "pres" + (i);
		}

		keys[17] = "flag-1";
		for (int i = 0; i < 19; i++) {
			keys[i+18] = "flag" + (i);
		}

		keys[37] = "emblem-1";
		for (int i = 0; i < 11; i++) {
			keys[i+38] = "emblem" + (i);
		}

		reader = new BufferedReader(new FileReader(file));

		String s = reader.readLine();
		while (s != null) {
			
			for (int i = 0; i < keys.length; i++) {
				
				if (s.startsWith(keys[i]))
					content[i]++;
			}
			s = reader.readLine();
		}
		reader.close();
		
		for (int i = 0; i < 10; i++) {

			System.out.println(keys[i] + ": " + content[i]);
		}
	}
}
