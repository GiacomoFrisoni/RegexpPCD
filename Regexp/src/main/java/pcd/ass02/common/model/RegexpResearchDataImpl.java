package pcd.ass02.common.model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Implementation of {@link RegexpResearchData}.
 *
 */
public class RegexpResearchDataImpl implements RegexpResearchData {
	
	private Optional<Path> startingPath;
	private Optional<Pattern> pattern;
	private int maxDepth;
	
	/**
	 * Constructs a new data model.
	 */
	public RegexpResearchDataImpl() {
		this.startingPath = Optional.empty();
		this.pattern = Optional.empty();
		this.maxDepth = Integer.MAX_VALUE;
	}
	
	@Override
	public void setStartingPath(final String path) throws IllegalArgumentException {
		Objects.requireNonNull(path);
		final Path tempPath = Paths.get(path);
		if (Files.exists(tempPath)) {
			this.startingPath = Optional.of(tempPath);
		} else {
			throw new IllegalArgumentException("Invalid path");
		}
	}
	
	@Override
	public void setPattern(final String regex) throws IllegalArgumentException {
		Objects.requireNonNull(regex);
		try {
			final Pattern tmpPattern = Pattern.compile(regex);
			this.pattern = Optional.of(tmpPattern);
		} catch (final PatternSyntaxException e) {
			throw new IllegalArgumentException("Invalid regular expression syntax");
		}
	}
	
	@Override
	public void setMaxDepthNavigation(final int maxDepth) throws IllegalArgumentException {
		if (maxDepth >= 0) {
			this.maxDepth = maxDepth;
		} else {
			throw new IllegalArgumentException("Invalid max depth value");
		}
	}
	
	@Override
	public Optional<Path> getStartingPath() {
		return this.startingPath;
	}
	
	@Override
	public Optional<Pattern> getPattern() {
		return this.pattern;
	}
	
	@Override
	public int getMaxDepth() {
		return this.maxDepth;
	}
	
	@Override
	public void reset() {
		this.startingPath = Optional.empty();
		this.pattern = Optional.empty();
		this.maxDepth = Integer.MAX_VALUE;
	}
	
}
