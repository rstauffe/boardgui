package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class BadConfigFormatException extends RuntimeException {

	private String filename;
	private String log = "errorLog.txt";
	
	public BadConfigFormatException(String file) {
		super("The file you provided: " + file + " is not a valid config file");
		this.filename = file;
		writeToLog(log, filename);
	}
	
	@Override
	public String toString() {
		return this.getMessage();
	}
	
	/*
	 * Function to write error message to log
	 * TODO: add logic to determine particular error
	 * 		 add logic to ensure errorLog.txt exists, and if it doesn't, then create it
	 */
	private void writeToLog(String log, String errorFile) {
		
		PrintWriter out = null;
		try {
			out = new PrintWriter(log);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.write("The file you provided was invalid: " + errorFile);
		
	}
}
