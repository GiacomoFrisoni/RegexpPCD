package pcd.ass02.ex1.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pcd.ass02.common.model.SearchFileResult;
import pcd.ass02.common.view.MessageUtils.ExceptionType;
import pcd.ass02.ex1.view.RegexpView;

/**
 * This class models the Regex Master.
 * It divides the workload in tasks and submit them.
 *
 */
public class Master {

	private final ExecutorService executor;
	private final Path startingPath;
	private final Pattern pattern;
	private final int maxDepth;
	private final BlockingQueue<Optional<SearchFileResult>> queue;
	private final RegexpView view;
	
	private final Logger logger;
	
	/**
	 * Creates a new Master.
	 * 
	 * @param startingPath
	 * 		the starting path
	 * @param pattern
	 * 		the regex pattern
	 * @param maxDepth
	 * 		the max depth navigation
	 * @param executor
	 * 		the executor service on which submit tasks
	 * @param queue
	 * 		the queue in which to enter the search results of the tasks
	 * @param view
	 * 		the application view
	 */
	public Master(final Path startingPath, final Pattern pattern, final int maxDepth,
			final ExecutorService executor, final BlockingQueue<Optional<SearchFileResult>> queue, final RegexpView view) {
		Objects.requireNonNull(startingPath);
		Objects.requireNonNull(pattern);
		Objects.requireNonNull(executor);
		Objects.requireNonNull(queue);
		Objects.requireNonNull(view);
		this.startingPath = startingPath;
		this.pattern = pattern;
		this.maxDepth = maxDepth;
		this.executor = executor;
		this.queue = queue;
		this.view = view;
		this.logger = LogManager.getLogger();
	}
	
	/**
	 * Submits the tasks for pattern matches research and awaits their termination.
	 */
	public void compute() {
		final Collection<Future<Void>> results = new HashSet<Future<Void>>();
		// Searches the paths to analyze and submits a task for each of them.
		try {
			Files.walkFileTree(this.startingPath, Collections.emptySet(), this.maxDepth,
					new RegularFileVisitor(
							(path) -> { results.add(this.executor.submit(new SearchMatchesInFileTask(path, this.pattern, this.queue))); },
							(nVisitedFiles) -> { this.view.setTotalFilesToScan(nVisitedFiles); }));
		} catch (final IOException e) {
			this.view.showException(ExceptionType.IO_EXCEPTION, "Error during the visiting of a file", e);
		}
		/*
		 * Awaits tasks termination.
		 * It uses a cycle of get in order to reuse the executor.
		 */
		for (final Future<Void> result : results) {
			try {
				result.get();
			} catch (final Exception e) {
				this.view.showException(ExceptionType.THREAD_EXCEPTION, "Error during the await termination", e);
			}
		}
		// Stops the consumer with a poison pill at the end of the queue
		try {
			this.queue.put(Consumer.POISON_PILL);
		} catch (final InterruptedException e) {
			this.logger.warn("Master interrupted while waiting for free space in the queue\n" + e.getMessage());
		}
	}
	
}
