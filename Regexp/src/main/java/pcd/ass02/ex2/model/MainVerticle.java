package pcd.ass02.ex2.model;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;
import pcd.ass02.ex1.controller.Chrono;
import pcd.ass02.ex1.view.RegexpView;

public class MainVerticle extends AbstractVerticle {

	private final RegexpView view;
	private final Path startingPath;
	private final Pattern pattern;
	private final int maxDepth;
	private int nLeastOneMatch;
	private double meanNumberOfMatches;
	private final Chrono cron;
	private int nVisitedFiles;
	private int nComputedFiles;

	public MainVerticle(final RegexpView view, final Path startingPath, final Pattern pattern, final int maxDepth) {
		Objects.requireNonNull(view);
		Objects.requireNonNull(startingPath);
		Objects.requireNonNull(pattern);
		this.view = view;
		this.startingPath = startingPath;
		this.pattern = pattern;
		this.maxDepth = maxDepth;
		this.nLeastOneMatch = 0;
		this.meanNumberOfMatches = 0;
		this.cron = new Chrono();
		this.nVisitedFiles = 0;
		this.nComputedFiles = 0;
	}

	@Override
	public void start(final Future<Void> done) throws Exception {
		searchPattern(this.startingPath.toString(), this.maxDepth, handler -> {
			if (handler.failed()) {
				// this.view.showThreadException(handler.cause().getMessage(), null);
				handler.cause().printStackTrace();
			} else {
				this.view.setFinish();
			}
		});
	}

	private void searchPattern(final String path, final int depth, final Handler<AsyncResult<Void>> handler) {
		final Future<Void> future = Future.<Void>future().setHandler(handler);
		analyzeFile(path, depth, future);
		future.complete();
	}
	
	private void analyzeFile(final String path, final int depth, final Future<Void> future) {
		final FileSystem fs = vertx.fileSystem();
		if (depth >= 0) {
			fs.exists(path, existsHandler -> {
				if (existsHandler.failed()) {
					future.fail(existsHandler.cause());
				} else {
					if (existsHandler.result()) {
						fs.lprops(path, propsHandler -> {
							if (propsHandler.failed()) {
								future.fail(propsHandler.cause());
							} else {
								final FileProps props = propsHandler.result();
								if (props.isDirectory()) {
									fs.readDir(path, readDirHandler -> {
										if (readDirHandler.failed()) {
											future.fail(readDirHandler.cause());
										} else {
											final List<String> pathFiles = readDirHandler.result();
											for (final String pathFile : pathFiles) {
												analyzeFile(pathFile, depth - 1, future);
											}
										}
									});
								} else {
									fs.readFile(path, readFileHandler -> {
										if (readFileHandler.failed()) {
											future.fail(readFileHandler.cause());
											this.view.showResult(path, readFileHandler.cause().getMessage());
										} else {
											this.nVisitedFiles++;
											this.view.setTotalFilesToScan(this.nVisitedFiles);
											cron.start();
											final Buffer buffer = readFileHandler.result();
											final Matcher matcher = this.pattern.matcher(buffer.toString());
											int nMatches = 0;
											while (matcher.find())
												nMatches++;
											cron.stop();
											if (nMatches > 0) {
												// Updates the number of files with least one match
												this.nLeastOneMatch++;
												// Updates the mean number of matches among files with matches
												double tmp = this.meanNumberOfMatches;
												this.meanNumberOfMatches += (nMatches - tmp) / this.nLeastOneMatch;
											}
											this.view.showResult(path, nMatches, cron.getTime());
											// Shows statistics on view
											this.view.showMeanNumberOfMatches(this.meanNumberOfMatches);
											this.view.showLeastOneMatchPercentage(
													(double) this.nLeastOneMatch / (double) nComputedFiles);
											// Shows analysis progress on view
											this.view.setNumberOfScannedFiles(++nComputedFiles);
										}
									});
								}
							}
						});
					} else {
						// non esiste
					}
				}
			});	
		}
	}
}
