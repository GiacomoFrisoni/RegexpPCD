package pcd.ass02.ex2;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;
import pcd.ass02.ex1.view.RegexpView;

public class AnalyzerVerticle extends AbstractVerticle {

	private final RegexpView view;
	private final Path startingPath;
	private final int maxDepth;
	private int nVisitedFiles;
	private final List<Future> futures;
	
	public AnalyzerVerticle(final RegexpView view, final Path startingPath, final int maxDepth) {
		Objects.requireNonNull(view);
		Objects.requireNonNull(startingPath);
		this.view = view;
		this.startingPath = startingPath;
		this.maxDepth = maxDepth;
		this.nVisitedFiles = 0;
		this.futures = new ArrayList<Future>();
	}

	@Override
	public void start(final Future<Void> done) throws Exception {
		searchPattern(this.startingPath.toString(), this.maxDepth, handler -> {
			if (handler.failed()) {
				// this.view.showThreadException(handler.cause().getMessage(), null);
				handler.cause().printStackTrace();
			} else {
				// this.view.setFinish();
				this.vertx.eventBus().send("totalFiles", this.nVisitedFiles);
			}
		});
	}

	private void searchPattern(final String path, final int depth, final Handler<AsyncResult<Void>> handler) {
		analyzeFile(path, depth).setHandler(handler);
	}
	
	private Future analyzeFile(final String path, final int depth) {
		final Future<Void> future = Future.future();
		final FileSystem fs = vertx.fileSystem();
		
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
						fs.lprops(path, fProps.completer());
					} else {
						// non esiste
					}
				}
			});
			fProps.setHandler((AsyncResult<FileProps> propsHandler) -> {
				if (propsHandler.failed()) {
					future.fail(propsHandler.cause());
				} else {
					if (propsHandler.result().isDirectory()) {
						fs.readDir(path, fReadDir.completer());
					} else {
						this.view.setTotalFilesToScan(++this.nVisitedFiles);
						vertx.eventBus().send("fileToAnalyze", path);
						future.complete();
					}
				}
			});
			fReadDir.setHandler((AsyncResult<List<String>> readDirHandler) -> {
				if (readDirHandler.failed()) {
					future.fail(readDirHandler.cause());
				} else {
					final List<String> pathFiles = readDirHandler.result();
					for (final String pathFile : pathFiles) {
						futures.add(analyzeFile(pathFile, depth - 1));
					}
					CompositeFuture.all(this.futures).setHandler((AsyncResult<CompositeFuture> res) -> {
						if (res.failed()) {
							future.fail(res.cause());
						} else {
							future.complete();
						}
					});
				}
			});
		}
		return future;
	}
	
}
