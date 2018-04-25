package pcd.ass02.ex2.model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import io.vertx.core.Vertx;
import pcd.ass02.ex1.controller.RegexpController;
import pcd.ass02.ex1.view.RegexpView;

public class RegexpControllerVertexImpl implements RegexpController {

	private final RegexpView view;
	private Optional<Path> startingPath;
	private Optional<Pattern> pattern;
	private int maxDepth;

	/**
	 * Creates a new Controller.
	 * 
	 * @param view
	 * 		the application view
	 */
	public RegexpControllerVertexImpl(final RegexpView view) {
		Objects.requireNonNull(view);
		this.startingPath = Optional.empty();
		this.pattern = Optional.empty();
		this.maxDepth = Integer.MAX_VALUE;
		this.view = view;
	}

	@Override
	public void setStartingPath(final String path) {
		Objects.requireNonNull(path);
		final Path tempPath = Paths.get(path);
		if (Files.exists(tempPath)) {
			this.startingPath = Optional.of(tempPath);
		} else {
			this.view.showInputError("Invalid path");
		}
	}

	@Override
	public void setPattern(final String regex) {
		Objects.requireNonNull(regex);
		try {
			final Pattern tmpPattern = Pattern.compile(regex);
			this.pattern = Optional.of(tmpPattern);
		} catch (final PatternSyntaxException e) {
			this.view.showInputError("Invalid regular expression syntax");
		}
	}
	
	@Override
	public void setMaxDepthNavigation(final int maxDepth) {
		if (maxDepth >= 0) {
			this.maxDepth = maxDepth;
		} else {
			this.view.showInputError("Invalid max depth value");
		}
	}

	@Override
	public boolean search() {
		if (this.startingPath.isPresent()) {
			if (this.pattern.isPresent()) {
				final Vertx vertx = Vertx.vertx();
				final MainVerticle myVerticle = new MainVerticle(this.view, this.startingPath.get(), this.pattern.get(), this.maxDepth);
				vertx.deployVerticle(myVerticle);
				return true;
			} else {
				this.view.showInputError("The regular expression is not specified");
			}
		} else {
			this.view.showInputError("The starting path is not specified");
		}
		return false;
	}

	@Override
	public void reset() {
		this.view.reset();
	}

}
