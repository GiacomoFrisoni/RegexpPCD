package pcd.ass02.ex2;

import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import pcd.ass02.common.controller.Chrono;
import pcd.ass02.common.model.SearchFileErrorResult;
import pcd.ass02.common.model.SearchFileSuccessfulResult;

/**
 * This {@link AbstractVerticle} represents the event loop for the research of the regex
 * pattern in the files to analyze.
 * For each computed file, a message with the associated result is sent on the event bus.
 *
 */
public class MatcherVerticle extends AbstractVerticle {
	
	public MatcherVerticle() { }

	@Override
	public void start(final Future<Void> done) throws Exception {
		final Pattern pattern = new Gson().fromJson(config().getString("pattern"), Pattern.class);
		final FileSystem fs = vertx.fileSystem();
		final Chrono cron = new Chrono();
		
		// Search pattern matches
		vertx.eventBus().consumer("fileToAnalyze", message -> {
			final String path = (String) message.body();
			
			fs.readFile(path, readFileHandler -> {
				if (readFileHandler.failed()) {
					vertx.eventBus().send("result", new SearchFileErrorResult(Paths.get(path),
							readFileHandler.cause().getLocalizedMessage()));
				} else {
					final Buffer buffer = readFileHandler.result();
					cron.start();
					vertx.<Integer>executeBlocking(blockingHandler -> {
						final Matcher matcher = pattern.matcher(buffer.toString());
						int nMatches = 0;
						while (matcher.find())
							nMatches++;
						cron.stop();
						blockingHandler.complete(nMatches);
					},
					false,
					resultHandler -> {
						if (resultHandler.succeeded()) {
							vertx.eventBus().send("result", new SearchFileSuccessfulResult(Paths.get(path),
									resultHandler.result().intValue(), cron.getTime()));
						}
					});
				}
			});
		});
		
		// Destroys itself when the end message is received
		vertx.eventBus().consumer("end", message -> {
			vertx.undeploy(this.deploymentID());
		});
	}
	
}
