package pcd.ass02.ex1.model;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

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
	 * @throws IOException
	 * 		a problem occurred during file reading
	 */
	public Document(final File file) throws IOException {
		this.content = getFileContent(file, StandardCharsets.UTF_8);
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
	private static String getFileContent(final File file, final Charset encoding) throws IOException {
		final byte[] encoded = Files.readAllBytes(file.toPath());
		return new String(encoded, encoding);
	}

}
