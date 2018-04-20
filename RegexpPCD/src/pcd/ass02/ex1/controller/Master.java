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

public class Master {

	private final ExecutorService executor;
	private final BlockingQueue<SearchFileResult> queue;
	private final List<File> files;
	private final Pattern pattern;
	
	public Master(final List<File> files, final Pattern pattern, final BlockingQueue<SearchFileResult> queue) {
		Objects.requireNonNull(files);
		Objects.requireNonNull(pattern);
		this.files = files;
		this.pattern = pattern;
		// Calculates the pool size for tasks executor, according to the processors number
		final int poolSize = Runtime.getRuntime().availableProcessors() + 1;
		// Initializes the executor
		this.executor = Executors.newFixedThreadPool(poolSize);
		// Initializes the result queue
		this.queue = queue;
	}
	
	public void compute() {
		for (final File file : this.files) {
			this.executor.submit(new SearchMatchesInFileTask(file, this.pattern, this.queue));
		}
		this.executor.shutdown();
		try {
			this.executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// notify view
		}
	}
	
}
