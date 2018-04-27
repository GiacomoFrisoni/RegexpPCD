package pcd.ass02.ex2;

import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import pcd.ass02.ex1.controller.Chrono;
import pcd.ass02.ex1.model.SearchFileErrorResult;
import pcd.ass02.ex1.model.SearchFileSuccessfulResult;

public class MatcherVerticle extends AbstractVerticle {
	
	public MatcherVerticle() {
	}

	@Override
	public void start(final Future<Void> done) throws Exception {
		final Pattern pattern = new Gson().fromJson(config().getString("pattern"), Pattern.class);
		final FileSystem fs = vertx.fileSystem();
		final Chrono cron = new Chrono();
		vertx.eventBus().consumer("fileToAnalyze", message -> {
			final String path = (String) message.body();
			cron.start();
			fs.readFile(path, readFileHandler -> {
				if (readFileHandler.failed()) {
					done.fail(readFileHandler.cause());
					vertx.eventBus().send("result", new SearchFileErrorResult(Paths.get(path),
							readFileHandler.cause().getLocalizedMessage()));
				} else {
					final Buffer buffer = readFileHandler.result();
					vertx.<Integer>executeBlocking(blockingHandler -> {
						final Matcher matcher = pattern.matcher(buffer.toString());
						int nMatches = 0;
						while (matcher.find())
							nMatches++;
						cron.stop();
						blockingHandler.complete(nMatches);
					}, resultHandler -> {
						if (resultHandler.succeeded()) {
							vertx.eventBus().send("result", new SearchFileSuccessfulResult(Paths.get(path),
									resultHandler.result().intValue(), cron.getTime()));
						}
					});
				}
			});
		});
	}
	
}
