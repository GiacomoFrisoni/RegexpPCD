package pcd.ass02.ex1.controller;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * This class represents a custom visitor for regular files.
 * It specifies the operation to perform for each discovered file and for each update
 * of the number of visited files.
 *
 */
public class RegularFileVisitor extends SimpleFileVisitor<Path> {

	private final Consumer<Path> pathHandler;
	private final Consumer<Integer> visitedFilesHandler;
	private int nVisitedFiles;
	
	/**
	 * Constructs a new regular file visitor.
	 * 
	 * @param pathHandler
	 * 		the handler for each discovered path
	 * @param visitedFilesHandler
	 * 		the handler for the number of discovered files
	 */
	public RegularFileVisitor(final Consumer<Path> pathHandler, final Consumer<Integer> visitedFilesHandler) {
		Objects.requireNonNull(pathHandler);
		Objects.requireNonNull(visitedFilesHandler);
		this.pathHandler = pathHandler;
		this.visitedFilesHandler = visitedFilesHandler;
		this.nVisitedFiles = 0;
	}

	@Override
	public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs) throws IOException {
		if(!attrs.isDirectory()) {
			this.nVisitedFiles++;
			this.pathHandler.accept(path);
			this.visitedFilesHandler.accept(this.nVisitedFiles);
		}
		return FileVisitResult.CONTINUE;
	}
	
	@Override
	public FileVisitResult visitFileFailed(final Path file, final IOException io)
	{   
	    return FileVisitResult.SKIP_SUBTREE;
	}
	
}
