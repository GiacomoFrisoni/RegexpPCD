package pcd.ass02.ex1.model;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class models a document.
 *
 */
public class Document {
	
	private final String content;
	
	/**
	 * Constructs a new document.
	 * 
	 * @param filePath
	 * 		the file path from which extract the document
	 * @throws IOException
	 * 		a problem occurred during file reading
	 */
	public Document(final Path filePath) throws IOException {
		this.content = getFileContent(filePath, StandardCharsets.UTF_8);
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
	private static String getFileContent(final Path filePath, final Charset encoding) throws IOException {
		try {
			final byte[] encoded = Files.readAllBytes(filePath);
			return new String(encoded, encoding);
		} catch (Exception ex) {
			System.out.println(ex.getLocalizedMessage());
		}
		return null;
	}

}
