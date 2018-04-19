package controller;

/**
 * This class represents the controller of the application.
 *
 */
public interface RegexpController {
	void setFilePath(String path);
	void setMaxDepthNavigation(int maxDepth);
	void search();
}
