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

import pcd.ass02.ex1.model.SearchFileResult;
import pcd.ass02.ex1.view.RegexpView;
import pcd.ass02.ex1.view.MessageUtils.ExceptionType;

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
	}
	
	/**
	 * Submits the tasks for pattern matches research and awaits their termination.
	 */
	public void compute() {
		final Collection<Future<Void>> results = new HashSet<Future<Void>>();
		try {
			Files.walkFileTree(this.startingPath, Collections.emptySet(), this.maxDepth,
					new RegularFileVisitor(
							(path) -> { results.add(this.executor.submit(new SearchMatchesInFileTask(path, this.pattern, this.queue))); },
							(nVisitedFiles) -> { this.view.setTotalFilesToScan(nVisitedFiles); }));
		} catch (final IOException e) {
			this.view.showException(ExceptionType.IO_EXCEPTION, "Error during the visiting of a file", e);
		}
		for (final Future<Void> result : results) {
			try {
				result.get();
			} catch (final Exception e) {
				this.view.showException(ExceptionType.THREAD_EXCEPTION, "Error during the await termination", e);
			}
		}
		this.queue.add(Consumer.POISON_PILL);
	}
	
}
