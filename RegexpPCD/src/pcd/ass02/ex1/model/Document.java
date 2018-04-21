package pcd.ass02.ex1.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Document {
	
	private final String content;
	
	public Document(final File file) throws FileNotFoundException {
		this.content = getFileContent(file);
	}
	
	public String getContent() {
		return this.content;
	}
	
	private static String getFileContent(final File file) throws FileNotFoundException {
		final Scanner scanner = new Scanner(file);
		final String content = scanner.useDelimiter("\\Z").next();
		scanner.close();
		return content;
	}

}
