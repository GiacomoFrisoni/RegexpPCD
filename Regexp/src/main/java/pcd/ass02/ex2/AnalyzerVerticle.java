package pcd.ass02.ex2;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;
import pcd.ass02.common.model.SearchFileErrorResult;
import pcd.ass02.common.view.MessageUtils.ExceptionType;
import pcd.ass02.ex1.view.RegexpView;

/**
 * This {@link AbstractVerticle} represents the event loop for the analysis of the files nested
 * in the specified starting path, on which research the pattern matches.
 * For each file founded, a message with the path is sent on the event bus.
 *
 */
public class AnalyzerVerticle extends AbstractVerticle {

	private static final long MAX_FILE_SIZE = 262144000;
	
	private static final String FILE_TOO_LARGE_MESSAGE = "Too large to analyze";
	private static final String FILE_NOT_EXISTS_MESSAGE = "The file does not exists";
	private static final String ANALYSIS_EXCEPTION_MESSAGE = "Un exception has occurred during the analysis of the subtree";
	
	private final RegexpView view;
	private final Path startingPath;
	private final int maxDepth;
	private int nVisitedFiles;
	
	/**
	 * Constructs a new analyzer verticle.
	 * 
	 * @param view
	 * 		the application view
	 * @param startingPath
	 * 		the starting path for the analysis
	 * @param maxDepth
	 * 		the max depth for the navigation
	 */
	public AnalyzerVerticle(final RegexpView view, final Path startingPath, final int maxDepth) {
		Objects.requireNonNull(view);
		Objects.requireNonNull(startingPath);
		this.view = view;
		this.startingPath = startingPath;
		this.maxDepth = maxDepth;
		this.nVisitedFiles = 0;
	}

	@Override
	public void start(final Future<Void> done) throws Exception {
		// Searches the files to analyze from the starting path
		searchFiles(this.startingPath.toString(), this.maxDepth, handler -> {
			if (handler.failed()) {
				this.view.setFinish();
				this.view.showException(ExceptionType.IO_EXCEPTION,
						ANALYSIS_EXCEPTION_MESSAGE,
						new Exception(handler.cause()));
			} else {
				this.vertx.eventBus().send("totalFiles", this.nVisitedFiles);
			}
		});
		
		// Destroys itself when the end message is received
		vertx.eventBus().consumer("end", message -> {
			vertx.undeploy(this.deploymentID());
		});
	}

	private void searchFiles(final String path, final int depth, final Handler<AsyncResult<Void>> handler) {
		analyzeFile(path, depth).setHandler(handler);
	}
	
	private Future<Void> analyzeFile(final String path, final int depth) {
		final FileSystem fs = vertx.fileSystem();
		
		// Declares the future for the analysis of the current file
		final Future<Void> future = Future.future();
		
		// Checks the base case
		if (depth >= 0) {
			
			final Future<Boolean> fExists = Future.future();
			final Future<FileProps> fProps = Future.future();
			final Future<List<String>> fReadDir = Future.future();
			
			fs.exists(path, fExists.completer());
			fExists.setHandler((AsyncResult<Boolean> existsHandler) -> {
				if (existsHandler.failed()) {
					future.fail(existsHandler.cause());
				} else {
					if (existsHandler.result()) {
						// If the file exists, checks its properties
						fs.lprops(path, fProps.completer());
					} else {
						// If the file does not exists...
						future.fail(FILE_NOT_EXISTS_MESSAGE);
					}
				}
			});
			fProps.setHandler((AsyncResult<FileProps> propsHandler) -> {
				// If the access to the file properties fails, the subtree is skipped
				if (propsHandler.succeeded()) {
					final FileProps props = propsHandler.result();
					// If the file is a directory, reads it
					if (props.isDirectory()) {
						fs.readDir(path, fReadDir.completer());
					} else {
						// Checks the file dimension
						if (props.size() < MAX_FILE_SIZE) {
							/*
							 * If the file is not a directory, updates the number of discovered
							 * files, sends a message and sets the future as successful completed.
							 */
							this.view.setTotalFilesToScan(++this.nVisitedFiles);
							vertx.eventBus().send("fileToAnalyze", path);
							future.complete();
						} else {
							vertx.eventBus().send("result", new SearchFileErrorResult(Paths.get(path),
									FILE_TOO_LARGE_MESSAGE));
							future.complete();
						}
					}
				} else {
					future.complete();
				}
			});
			fReadDir.setHandler((AsyncResult<List<String>> readDirHandler) -> {
				/*
				 * If the read operation of the directory has succeeded, goes on.
				 * So, if the visit of the file has encountered a failure, the subtree is skipped.
				 */
				if (readDirHandler.succeeded()) {
					@SuppressWarnings("rawtypes")
					final List<Future> futures = new ArrayList<Future>();
					// Makes the recursive calls on the children of the directory
					final List<String> pathFiles = readDirHandler.result();
					for (final String pathFile : pathFiles) {
						futures.add(analyzeFile(pathFile, depth - 1));
					}
					// Sets events for the completion of all directory children
					CompositeFuture.all(futures).setHandler((AsyncResult<CompositeFuture> res) -> {
						if (res.failed()) {
							// At least one children analysis failed
							future.fail(res.cause());
						} else {
							// All children analysis succeeded
							future.complete();
						}
					});
				} else {
					future.complete();
				}
			});
		}
		return future;
	}
	
}
