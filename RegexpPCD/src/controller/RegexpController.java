package controller;

/**
 * This class represents the controller of the application.
 *
 */
public interface RegexpController {
	void setStartPath(String path);
	void setMaxDepthNavigation(int maxDepth);
	void setPattern(String regex);
	void search();
}
