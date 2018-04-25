package pcd.ass02.ex1.model;

import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Pattern;

public interface RegexpResearchData {
	
	void setStartingPath(String path) throws IllegalArgumentException;
	
	void setPattern(String regex) throws IllegalArgumentException;
	
	void setMaxDepthNavigation(int maxDepth) throws IllegalArgumentException;
	
	Optional<Path> getStartingPath();
	
	Optional<Pattern> getPattern();
	
	int getMaxDepth();
	 
}
