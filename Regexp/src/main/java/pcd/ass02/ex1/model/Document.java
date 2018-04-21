package pcd.ass02.ex1.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class models a document.
 *
 */
public class Document {
	
	private final String content;
	
	/**
	 * Constructs a new document.
	 * 
	 * @param file
	 * 		the file from which extract the document
	 * @throws FileNotFoundException
	 */
	public Document(final File file) throws FileNotFoundException {
		this.content = getFileContent(file);
	}
	
	/**
	 * @return the content of the document
	 */
	public String getContent() {
		return this.content;
	}
	
	/*
	 * Gets the string content of the specified file.
	 */
	private static String getFileContent(final File file) throws FileNotFoundException {
		final Scanner scanner = new Scanner(file);
		final String content = scanner.useDelimiter("\\Z").next();
		scanner.close();
		return content;
	}

}
