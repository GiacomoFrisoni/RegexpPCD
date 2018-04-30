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
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import pcd.ass02.common.controller.Chrono;
import pcd.ass02.common.model.SearchFileErrorResult;
import pcd.ass02.common.model.SearchFileResult;
import pcd.ass02.common.model.SearchFileSuccessfulResult;
import pcd.ass02.common.view.MessageUtils.ExceptionType;
import pcd.ass02.ex1.controller.SearchMatchesInFileTask;
import pcd.ass02.ex3.view.RegexpBindingView;
import pcd.ass02.ex3.view.ViewDataManager;

/**
 * This class represents the main thread for the pattern matches research.
 * It uses RxJava and the stream logic.
 *
 */
public class SearchMatchesService extends Thread {

	public static final Optional<SearchFileResult> POISON_PILL = Optional.empty();
	
	private final Path startingPath;
	private final Pattern pattern;
	private final int maxDepth;
	private final ExecutorService executor;
	private final RegexpBindingView view;
	private final BlockingQueue<Optional<SearchFileResult>> queue;
	
	private int nVisitedFiles;
	private int nComputedFiles;
	private double meanNumberOfMatches;
	private int nLeastOneMatch;
	
	
	/**
	 * Constructs a new matches research service.
	 * 
	 * @param startPath
	 * 		the starter path of the research
	 * @param pattern
	 * 		the regex pattern
	 * @param maxDepth
	 * 		the max depth of the research
	 * @param executor
	 * 		the executor service on which submits tasks
	 * @param view
	 * 		the application view
	 */
	public SearchMatchesService(final Path startingPath, final Pattern pattern, final int maxDepth,
			final ExecutorService executor, final RegexpBindingView view) {
		Objects.requireNonNull(startingPath);
		Objects.requireNonNull(pattern);
		Objects.requireNonNull(executor);
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
		this.executor = executor;
		this.view = view;
		this.queue = new LinkedBlockingQueue<>();
		this.nVisitedFiles = 0;
		this.nComputedFiles = 0;
		this.meanNumberOfMatches = 0;
		this.nLeastOneMatch = 0;
	}
	
	@Override
	public void run() {
		final Chrono totCron = new Chrono();
		final Collection<Future<Void>> results = new HashSet<Future<Void>>();
		
		totCron.start();
		
		/*
		 * Creates a stream that generates the paths of the files to analyze,
		 * searching them in the tree structure with the starting path as root.
		 */
		final Observable<Path> pathSource = Observable.create(emitter -> {
			new Thread(() -> {
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
					emitter.onComplete();
				} catch (final IOException e) {
					this.view.showException(ExceptionType.IO_EXCEPTION, "Error during the visiting of a file", e);
				}
			}).start();
		});
		
		// Defines a path observer
		pathSource.subscribe((path) -> {
			results.add(this.executor.submit(new SearchMatchesInFileTask(path, this.pattern, this.queue)));
			ViewDataManager.getHandler().setNumberOfVisitedFiles(++this.nVisitedFiles);
		}, (final Throwable t) -> {
			this.view.showException(ExceptionType.STREAM_EXCEPTION, "An error occurred in the path observable source",
					new Exception(t));
		}, () -> {
			// When all the tasks are submitted, waits them termination
			for (final Future<Void> result : results) {
				try {
					result.get();
				} catch (final Exception e) {
					this.view.showException(ExceptionType.THREAD_EXCEPTION, "Error during the await termination", e);
				}
			}
			// Stops the consumer with a poison pill at the end of the queue
			this.queue.add(POISON_PILL);
		});
		
		/*
		 * Creates a stream of search results.
		 * It uses a back pressure strategy in order to handle the potentially lower speed
		 * of the observer. 
		 */
		final Flowable<SearchFileResult> resultSource = Flowable.create(emitter -> {
			boolean foundedPoisonPill = false;
			Optional<SearchFileResult> element;
			while (!foundedPoisonPill) {
				element = this.queue.take();
				if (element.equals(POISON_PILL)) {
					foundedPoisonPill = true;
				} else {
					element.ifPresent(e -> emitter.onNext(e));
				}
			}
			emitter.onComplete();
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
				ViewDataManager.getHandler().addResult(successfulRes.getPath().toString(), nMatches,successfulRes.getElapsedTime());
			} else if (result instanceof SearchFileErrorResult) {
				final SearchFileErrorResult errorRes = (SearchFileErrorResult)result;
				// Shows file result on view
				ViewDataManager.getHandler().addResult(errorRes.getPath().toString(), errorRes.getErrorMessage());
			}
			// Shows statistics on view
			ViewDataManager.getHandler().setLeastOneMatchPercentage((double)this.nLeastOneMatch / (double)this.nComputedFiles);
		}, (final Throwable t) -> {
			this.view.showException(ExceptionType.STREAM_EXCEPTION, "An error occurred in the result observable source",
					new Exception(t));
		}, () -> {
			// When all results are shown, shows the total elapsed time and sets the view in the finished status
			totCron.stop();
			ViewDataManager.getHandler().setTotalElapsedTime(totCron.getTime());
			this.view.setFinish();
		});
	}

}
