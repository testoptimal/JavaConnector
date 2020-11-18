package testoptimal.api;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class Util {
	public static StringBuffer readFile(String filePath_p) throws IOException {
		StringBuffer retBuf = new StringBuffer();
		try (
				DataInputStream in = new DataInputStream(new FileInputStream(filePath_p));
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
			) {
			String line = br.readLine();
			while (line != null) {
				retBuf.append(line).append("\n");
				line = br.readLine();
			}
		} 
		return retBuf;
	}

	public static void writeToFile (String filePath_p, String text_p) throws IOException {
		try (
			FileOutputStream outS = new FileOutputStream(filePath_p);) {
			outS.write(text_p.getBytes());
		}
	}
	
	public static String genUID () {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
