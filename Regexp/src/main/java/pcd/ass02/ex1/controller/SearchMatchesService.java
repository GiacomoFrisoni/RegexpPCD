package pcd.ass02.ex1.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

import pcd.ass02.ex1.model.SearchFileResult;
import pcd.ass02.ex1.view.RegexpView;

/**
 * This class represents the main thread for the pattern matches research.
 *
 */
public class SearchMatchesService extends Thread {
	
	private final Path startingPath;
	private final Pattern pattern;
	private final int maxDepth;
	private final RegexpView view;
	private final BlockingQueue<SearchFileResult> queue;
	
	/**
	 * Constructs a new matches research service.
	 * 
	 * @param startPath
	 * 		the starter path of the research
	 * @param pattern
	 * 		the regex pattern
	 * @param maxDepth
	 * 		the max depth of the research
	 * @param view
	 * 		the application view
	 */
	public SearchMatchesService(final Path startingPath, final Pattern pattern, final int maxDepth, final RegexpView view) {
		Objects.requireNonNull(startingPath);
		Objects.requireNonNull(pattern);
		Objects.requireNonNull(view);
		if (!Files.exists(startingPath)) {
			throw new IllegalArgumentException("The starting path is not valid.");
		}
		if (maxDepth <= 0) {
			throw new IllegalArgumentException("The max depth must not be negative.");
		}
		this.startingPath = startingPath;
		this.pattern = pattern;
		this.maxDepth = maxDepth;
		this.view = view;
		this.queue = new LinkedBlockingQueue<>();
	}
	
	@Override
	public void run() {
		// Creates the collection of files in which to search for the pattern
		final List<File> files = new ArrayList<>();
		try {
			Files.walkFileTree(this.startingPath, Collections.emptySet(), this.maxDepth, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs) throws IOException {
					if(!attrs.isDirectory()) {
						files.add(path.toFile());
					}
					return FileVisitResult.CONTINUE;
				}
				
				@Override
				public FileVisitResult visitFileFailed(Path file, IOException io)
				{   
				    return FileVisitResult.SKIP_SUBTREE;
				}
			});
		} catch (final IOException e) {
			// notify view
		}
		
		if (!files.isEmpty()) {
			// Tells to the view the total number of files
			this.view.setTotalFilesToScan(files.size());
			// Starts the results consumer on a new thread
			new Consumer(this.queue, this.view, files.size()).start();
			// Starts master computation
			new Master(files, this.pattern, this.queue, this.view).compute();
		} else {
			this.view.setFinish();
		}
		
		
	}

}
