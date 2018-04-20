package controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import view.RegexpView;

public class RegexpControllerImpl implements RegexpController {

	private final RegexpView view;
	private Optional<Path> startPath;
	private Optional<Pattern> pattern;
	private int maxDepth;

	public RegexpControllerImpl(final RegexpView view) {
		Objects.requireNonNull(view);
		this.maxDepth = Integer.MAX_VALUE;
		this.view = view;
	}

	@Override
	public void setStartPath(final String path) {
		Objects.requireNonNull(path);
		this.startPath = Optional.of(Paths.get(path));
	}

	@Override
	public void setPattern(final String regex) {
		this.pattern = Optional.of(Pattern.compile(regex));
	}
	
	@Override
	public void setMaxDepthNavigation(final int maxDepth) {
		this.maxDepth = maxDepth;
	}

	@Override
	public void search() {
		if (this.startPath.isPresent() && this.pattern.isPresent()) {
			new SearchMatchesService(this.startPath.get(), this.pattern.get(), this.maxDepth, this.view).start();
		} else {
			// view notify
		}
	}
	
}
