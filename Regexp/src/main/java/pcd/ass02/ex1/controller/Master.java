package pcd.ass02.ex1.controller;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import pcd.ass02.ex1.model.SearchFileResult;
import pcd.ass02.ex1.view.RegexpView;

/**
 * This class models the Regex Master.
 * It divides the workload in tasks and submit them.
 *
 */
public class Master {

	private final ExecutorService executor;
	private final BlockingQueue<SearchFileResult> queue;
	private final List<File> files;
	private final Pattern pattern;
	private final RegexpView view;
	
	/**
	 * Creates a new Master.
	 * 
	 * @param files
	 * 		the collection of file to compute
	 * @param pattern
	 * 		the regex pattern
	 * @param queue
	 * 		the queue in which to enter the search results of the tasks
	 */
	public Master(final List<File> files, final Pattern pattern, final BlockingQueue<SearchFileResult> queue, 
			final RegexpView view) {
		Objects.requireNonNull(files);
		Objects.requireNonNull(pattern);
		Objects.requireNonNull(queue);
		Objects.requireNonNull(view);
		this.files = files;
		this.pattern = pattern;
		this.queue = queue;
		this.view = view;
		// Calculates the pool size for tasks executor, according to the processors number
		final int poolSize = Runtime.getRuntime().availableProcessors() + 1;
		// Initializes the executor
		this.executor = Executors.newFixedThreadPool(poolSize);
	}
	
	/**
	 * Submits the tasks for pattern matches research and awaits their termination.
	 */
	public void compute() {
		for (final File file : this.files) {
			this.executor.submit(new SearchMatchesInFileTask(file, this.pattern, this.queue));
		}
		this.executor.shutdown();
		try {
			this.executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
			// notify view
			//System.out.println("Finish");
			this.view.setFinish();
		} catch (final InterruptedException e) {
			this.view.showThreadException("Someone interrupted the master during the await termination", e);
		}
	}
	
}
