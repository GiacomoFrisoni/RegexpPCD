package pcd.ass02.ex3.view;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pcd.ass02.ex1.view.RowType;

public class ViewDataManager {

	private static volatile ViewDataManager singleton;
	
	private final SimpleIntegerProperty nVisitedFiles = new SimpleIntegerProperty();
	private final SimpleIntegerProperty nComputedFiles = new SimpleIntegerProperty();
	private final SimpleLongProperty totalElapsedTime = new SimpleLongProperty();
	private final SimpleDoubleProperty leastOneMatchPercentage = new SimpleDoubleProperty();
	private final SimpleDoubleProperty meanNumberOfMatches = new SimpleDoubleProperty();
	
	private final ObservableList<RowType> resultRows = FXCollections.observableArrayList();

	
    private ViewDataManager() {
		this.nVisitedFiles.set(0);
		this.nComputedFiles.set(0);
		this.totalElapsedTime.set(0);
		this.leastOneMatchPercentage.set(0.0);
		this.meanNumberOfMatches.set(0.0);
    };
    

    /**
     * This method returns the DataManager.
     * If the DataManager is null it creates a new one on the first call.
     * This way the resources are loaded only if necessary.
     * 
     * @return the data manager.
     */
    public static ViewDataManager getHandler() {
        if (singleton == null) {
            synchronized (ViewDataManager.class) {
                if (singleton == null) {
                    singleton = new ViewDataManager();
                }
            }
        }
        return singleton;
    }
    
    
	// Number of visited files handler
    
    /**
     * Set the number of visited files
     * @param value
     * 		number of visited files
     */
	public void setNumberOfVisitedFiles(final int value) {
		Platform.runLater(() -> this.nVisitedFiles.set(value));
	}
	
	/**
	 * Get the property of number of visited files ready to bind
	 * @return
	 * 		property ready to bind
	 */
	public IntegerProperty numberOfVisitedFilesProperty() {
		return this.nVisitedFiles;
	}
	

	// Number of computed files handler
	
    /**
     * Set the number of computed files (scanned and parsed)
     * @param value
     * 		number of visited files
     */
	public void setNumberOfComputedFiles(final int value) {
		Platform.runLater(() -> this.nComputedFiles.set(value));
	}
	
	/**
	 * Get the property of number of computed files ready to bind
	 * @return
	 * 		property ready to bind
	 */
	public IntegerProperty numberOfComputedFilesProperty() {
		return this.nComputedFiles;
	}
	

	// Mean number of matches handler
	
    /**
     * Set the mean number of matches
     * @param value
     * 		mean number of matches
     */
	public void setMeanNumberOfMatches(final double value) {
		Platform.runLater(() -> this.meanNumberOfMatches.set(value));
	}

	/**
	 * Get the property of mean number of matches ready to bind
	 * @return
	 * 		property ready to bind
	 */
	public DoubleProperty meanNumberOfMatchesProperty() {
		return this.meanNumberOfMatches;
	}
	
	
	// Least one match percentage

    /**
     * Set the percentage of files that have at least one match.
     * @param value
     * 		mean number of matches
     */
	public void setLeastOneMatchPercentage(final double value) {
		Platform.runLater(() -> this.leastOneMatchPercentage.set(value));
	}

	/**
	 * Get the percentage of files that have at least one match ready to bind
	 * @return
	 * 		property ready to bind
	 */
	public DoubleProperty leastOneMatchPercentageProperty() {
		return this.leastOneMatchPercentage;
	}
	
	// List of data
	/**
	 * Add a result to list
	 * @param path
	 * 		path of the computed file
	 * @param nMatches
	 * 		number of matches of this file
	 * @param time
	 * 		elapsed time to parse it
	 */
	public void addResult(final String path, final int nMatches, final long time) {
		this.resultRows.add(new RowType(path, "" + nMatches, time));
		Platform.runLater(() -> this.totalElapsedTime.set(this.totalElapsedTime.get() + time));
	}
	
	/**
	 * Add a result to list, when computation of the file was unsuccessful
	 * @param path
	 * 		path of the computed file
	 * @param message
	 * 		short message for the error
	 */
	public void addResult(final String path, final String message) {
		this.resultRows.add(new RowType(path, message, 0));
	}
	
	/**
	 * Return an observable collection of result items
	 * @return
	 * 		observable collection of result items
	 */
	public ObservableList<RowType> getResultList() {
		return this.resultRows;
	}
	
	
	//Total time
	
	/**
	 * Get the total elapsed time property ready to bind
	 * @return
	 * 		total elapsed time property
	 */
	public SimpleLongProperty getTotalElapsedTimeProperty() {
		return this.totalElapsedTime;
	}
	
}
