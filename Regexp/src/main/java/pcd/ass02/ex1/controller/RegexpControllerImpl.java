package pcd.ass02.ex1.controller;

import java.util.Objects;

import pcd.ass02.ex1.model.RegexpResearchData;
import pcd.ass02.ex1.view.RegexpView;

/**
 * Implementation of {@link RegexpController}.
 *
 */
public class RegexpControllerImpl implements RegexpController {

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
			this.view.showInputError("Invalid path");
		}
	}

	@Override
	public void setPattern(final String regex) {
		try {
			this.model.setPattern(regex);
		} catch (final IllegalArgumentException e) {
			this.view.showInputError("Invalid regular expression syntax");
		}
	}
	
	@Override
	public void setMaxDepthNavigation(final int maxDepth) {
		try {
			this.model.setMaxDepthNavigation(maxDepth);
		} catch (final IllegalArgumentException e) {
			this.view.showInputError("Invalid max depth value");
		}
	}

	@Override
	public boolean search() {
		if (this.model.getStartingPath().isPresent()) {
			if (this.model.getPattern().isPresent()) {
				new SearchMatchesService(this.model.getStartingPath().get(), this.model.getPattern().get(),
						this.model.getMaxDepth(), this.view).start();
				return true;
			} else {
				this.view.showInputError("The regular expression is not specified");
			}
		} else {
			this.view.showInputError("The starting path is not specified");
		}
		return false;
	}
	
}