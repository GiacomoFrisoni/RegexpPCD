package pcd.ass02.ex1.controller;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pcd.ass02.ex1.model.RegexpResearchData;
import pcd.ass02.ex1.view.RegexpView;

/**
 * Implementation of {@link RegexpController}.
 *
 */
public class RegexpControllerImpl implements RegexpController {

	private static final String INVALID_PATH = "Invalid path";
	private static final String INVALID_REGEX = "Invalid regular expression syntax";
	private static final String INVALID_DEPTH = "Invalid max depth value";
	private static final String EMPTY_REGEX_MESSAGE = "The regular expression is not specified";
	private static final String EMPTY_PATH_MESSAGE = "The starting path is not specified";
	
	private final RegexpResearchData model;
	private final RegexpView view;
	private final ExecutorService executor;

	/**
	 * Creates a new Controller.
	 * 
	 * @param model
	 * 		the application model
	 * @param view
	 * 		the application view
	 */
	public RegexpControllerImpl(final RegexpResearchData model, final RegexpView view) {
		Objects.requireNonNull(model);
		Objects.requireNonNull(view);
		this.model = model;
		this.view = view;
		// Calculates the pool size for tasks executor, according to the processors number
		final int poolSize = Runtime.getRuntime().availableProcessors() + 1;
		// Initializes the executor
		this.executor = Executors.newFixedThreadPool(poolSize);
	}

	@Override
	public void setStartingPath(final String path) {
		try {
			this.model.setStartingPath(path);
		} catch (final IllegalArgumentException e) {
			this.view.showInputError(INVALID_PATH);
		}
	}

	@Override
	public void setPattern(final String regex) {
		try {
			this.model.setPattern(regex);
		} catch (final IllegalArgumentException e) {
			this.view.showInputError(INVALID_REGEX);
		}
	}

	@Override
	public void setMaxDepthNavigation(final int maxDepth) {
		try {
			this.model.setMaxDepthNavigation(maxDepth);
		} catch (final IllegalArgumentException e) {
			this.view.showInputError(INVALID_DEPTH);
		}
	}

	@Override
	public boolean search() {
		if (this.model.getStartingPath().isPresent()) {
			if (this.model.getPattern().isPresent()) {
				new SearchMatchesService(this.model.getStartingPath().get(), this.model.getPattern().get(),
						this.model.getMaxDepth(), this.executor, this.view).start();
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
