package pcd.ass02.ex1.controller;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.function.Consumer;

public class RegularFileVisitor extends SimpleFileVisitor<Path> {

	private final Consumer<Path> pathHandler;
	private final Consumer<Integer> visitedFilesHandler;
	private int nVisitedFiles;
	
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
