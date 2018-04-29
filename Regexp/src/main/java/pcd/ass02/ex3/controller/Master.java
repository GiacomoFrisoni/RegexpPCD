package pcd.ass02.ex3.controller;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import pcd.ass02.ex1.model.SearchFileErrorResult;
import pcd.ass02.ex1.model.SearchFileResult;
import pcd.ass02.ex1.model.SearchFileSuccessfulResult;
import pcd.ass02.ex1.view.MessageUtils.ExceptionType;
import pcd.ass02.ex3.view.RegexpBindingView;
import pcd.ass02.ex3.view.ViewDataManager;

/**
 * This class models the Regex Master.
 * It divides the workload in tasks, submit them and handles the results.
 *
 */
public class Master {

	private final ExecutorService executor;
	private final Path startingPath;
	private final Pattern pattern;
	private final int maxDepth;
	private final RegexpBindingView view;
	private int nVisitedFiles;
	private int nComputedFiles;
	private double meanNumberOfMatches;
	private int nLeastOneMatch;
	
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
	 * @param view
	 * 		the application view
	 */
	public Master(final Path startingPath, final Pattern pattern, final int maxDepth,
			final ExecutorService executor, final RegexpBindingView view) {
		Objects.requireNonNull(startingPath);
		Objects.requireNonNull(pattern);
		Objects.requireNonNull(executor);
		Objects.requireNonNull(view);
		this.startingPath = startingPath;
		this.pattern = pattern;
		this.maxDepth = maxDepth;
		this.executor = executor;
		this.view = view;
		this.nVisitedFiles = 0;
		this.nComputedFiles = 0;
		this.meanNumberOfMatches = 0;
		this.nLeastOneMatch = 0;
	}
	
	/**
	 * Submits the tasks for pattern matches research and awaits their termination.
	 */
	public void compute() {
		final Collection<Future<SearchFileResult>> results = new HashSet<Future<SearchFileResult>>();
		
		final Observable<Path> pathSource = Observable.create(emitter -> {
			try {
				Files.walkFileTree(this.startingPath, Collections.emptySet(), this.maxDepth, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs) throws IOException {
						if(!attrs.isDirectory()) {
							emitter.onNext(path);
						}
						return FileVisitResult.CONTINUE;
					}
					
					@Override
					public FileVisitResult visitFileFailed(final Path file, final IOException io)
					{   
					    return FileVisitResult.SKIP_SUBTREE;
					}
				});
			} catch (final IOException e) {
				this.view.showException(ExceptionType.IO_EXCEPTION, "Error during the visiting of a file", e);
			}
		});
		
		pathSource.subscribe((path) -> {
			results.add(this.executor.submit(new SearchMatchesInFileTask(path, this.pattern)));
			ViewDataManager.getHandler().setNumberOfVisitedFiles(++this.nVisitedFiles);
		});
		
		/*
		 * Creates a stream of search results.
		 * It uses a back pressure strategy in order to handle the potentially lower speed
		 * of the observer. 
		 */
		final Flowable<SearchFileResult> resultSource = Flowable.create(emitter -> {
			new Thread(() -> {
				for (final Future<SearchFileResult> result : results) {
					try {
						// Generates an element when the current task is completed
						emitter.onNext(result.get());
					} catch (final Exception e) {
						this.view.showException(ExceptionType.THREAD_EXCEPTION, "Error during the await termination", e);
					}
				}
			}).start();
		}, BackpressureStrategy.BUFFER);
		
		// Defines a search results observer
		resultSource.subscribe((result) -> {
			ViewDataManager.getHandler().setNumberOfComputedFiles(++this.nComputedFiles);
			// Checks the type of the result
			if (result instanceof SearchFileSuccessfulResult) {
				final SearchFileSuccessfulResult successfulRes = (SearchFileSuccessfulResult)result;
				final int nMatches = successfulRes.getNumberOfMatches();
				if (nMatches > 0) {
					// Updates the number of files with least one match
					this.nLeastOneMatch++;
					// Updates the mean number of matches among files with matches
					final double oldMean = this.meanNumberOfMatches;
					this.meanNumberOfMatches += (nMatches - oldMean) / this.nLeastOneMatch;
					ViewDataManager.getHandler().setMeanNumberOfMatches(this.meanNumberOfMatches);
				}
				// Shows file result on view
				ViewDataManager.getHandler().addResult(successfulRes.getPath().toString(), nMatches, successfulRes.getElapsedTime());
			} else if (result instanceof SearchFileErrorResult) {
				final SearchFileErrorResult errorRes = (SearchFileErrorResult)result;
				// Shows file result on view
				ViewDataManager.getHandler().addResult(errorRes.getPath().toString(), errorRes.getErrorMessage());
			}
			// Shows statistics on view
			ViewDataManager.getHandler().setLeastOneMatchPercentage((double)this.nLeastOneMatch / (double)this.nComputedFiles);
		});
	}
	
}
