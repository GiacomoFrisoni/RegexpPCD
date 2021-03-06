package pcd.ass02.ex2;

import java.util.Objects;

import com.google.gson.Gson;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import pcd.ass02.common.controller.RegexpController;
import pcd.ass02.common.model.RegexpResearchData;
import pcd.ass02.common.model.SearchFileErrorResult;
import pcd.ass02.common.model.SearchFileSuccessfulResult;
import pcd.ass02.ex1.view.RegexpView;

/**
 * Implementation of {@link RegexpController}.
 *
 */
public class RegexpControllerImpl implements RegexpController {

	private static final String INVALID_PATH_MESSAGE = "Invalid path";
	private static final String INVALID_REGEX_MESSAGE = "Invalid regular expression syntax";
	private static final String INVALID_DEPTH_MESSAGE = "Invalid max depth value";
	private static final String EMPTY_REGEX_MESSAGE = "The regular expression is not specified";
	private static final String EMPTY_PATH_MESSAGE = "The starting path is not specified";
	
	private final RegexpResearchData model;
	private final RegexpView view;

	/**
	 * Creates a new Controller.
	 * 
	 * @param view
	 * 		the application view
	 */
	public RegexpControllerImpl(final RegexpResearchData model, final RegexpView view) {
		Objects.requireNonNull(model);
		Objects.requireNonNull(view);
		this.model = model;
		this.view = view;
	}

	@Override
	public void setStartingPath(final String path) {
		try {
			this.model.setStartingPath(path);
		} catch (final IllegalArgumentException e) {
			this.view.showInputError(INVALID_PATH_MESSAGE);
		}
	}

	@Override
	public void setPattern(final String regex) {
		try {
			this.model.setPattern(regex);
		} catch (final IllegalArgumentException e) {
			this.view.showInputError(INVALID_REGEX_MESSAGE);
		}
	}

	@Override
	public void setMaxDepthNavigation(final int maxDepth) {
		try {
			this.model.setMaxDepthNavigation(maxDepth);
		} catch (final IllegalArgumentException e) {
			this.view.showInputError(INVALID_DEPTH_MESSAGE);
		}
	}

	@Override
	public boolean search() {
		if (this.model.getStartingPath().isPresent()) {
			if (this.model.getPattern().isPresent()) {
				final Vertx vertx = Vertx.vertx();
				
				// Registers the codecs on the event bus in order to send results objects
			    vertx.eventBus().registerDefaultCodec(SearchFileSuccessfulResult.class, new SuccessfulResultMessageCodec());
			    vertx.eventBus().registerDefaultCodec(SearchFileErrorResult.class, new ErrorResultMessageCodec());
				
				// Deploys matcher verticles
				final JsonObject config = new JsonObject().put("pattern", new Gson().toJson(this.model.getPattern().get()));
				final DeploymentOptions options = new DeploymentOptions()
						.setInstances(Runtime.getRuntime().availableProcessors() - 1)
						.setConfig(config);
				vertx.deployVerticle(MatcherVerticle.class.getCanonicalName(), options);
				
				// Deploys the results handler verticle
				final ResultsVerticle resultsVerticle = new ResultsVerticle(this.view);
				vertx.deployVerticle(resultsVerticle);
				
				// Deploys the analyzer verticle
				final AnalyzerVerticle analyzerVerticle = new AnalyzerVerticle(this.view, this.model.getStartingPath().get(),
						this.model.getMaxDepth());
				vertx.deployVerticle(analyzerVerticle);
				
				return true;
			} else {
				this.view.showInputError(EMPTY_REGEX_MESSAGE);
			}
		} else {
			this.view.showInputError(EMPTY_PATH_MESSAGE);
		}
		return false;
	}

	@Override
	public void reset() {
		this.model.reset();
	}

}
