package pcd.ass02.ex3.view;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pcd.ass02.common.view.RowType;

/**
 * This class handles the properties on which the view bindings its controls.
 * It uses the Singleton pattern.
 *
 */
public class ViewDataManager {

	private static volatile ViewDataManager singleton;
	
	private final SimpleIntegerProperty nVisitedFiles = new SimpleIntegerProperty();
	private final SimpleIntegerProperty nComputedFiles = new SimpleIntegerProperty();
	private final SimpleLongProperty totalElapsedTime = new SimpleLongProperty();
	private final SimpleDoubleProperty leastOneMatchPercentage = new SimpleDoubleProperty();
	private final SimpleDoubleProperty meanNumberOfMatches = new SimpleDoubleProperty();
	private final SimpleDoubleProperty progress = new SimpleDoubleProperty();
	
	private final ObservableList<RowType> resultRows = FXCollections.observableArrayList();

	
    private ViewDataManager() {
		this.nVisitedFiles.set(0);
		this.nComputedFiles.set(0);
		this.totalElapsedTime.set(0);
		this.leastOneMatchPercentage.set(0.0);
		this.meanNumberOfMatches.set(0.0);
		this.progress.set(0.0);
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
     * Sets the number of visited files.
     * 
     * @param value
     * 		number of visited files
     */
	public void setNumberOfVisitedFiles(final int value) {
		Platform.runLater(() -> {
			this.nVisitedFiles.set(value);
			
			if (this.nVisitedFiles.get() > 0) {
				this.progress.set(((double)this.nComputedFiles.get()) / ((double)this.nVisitedFiles.get()));
			}
		});
	}
	
	/**
	 * Gets the property associated to the number of visited files, ready to bind.
	 * 
	 * @return property ready to bind
	 */
	public IntegerProperty numberOfVisitedFilesProperty() {
		return this.nVisitedFiles;
	}
	

	// Number of computed files handler
	
    /**
     * Sets the number of computed files (scanned and parsed).
     * 
     * @param value
     * 		number of computed files
     */
	public void setNumberOfComputedFiles(final int value) {
		Platform.runLater(() -> {
			this.nComputedFiles.set(value);
			if (this.nVisitedFiles.get() > 0) {
				this.progress.set(((double)this.nComputedFiles.get()) / ((double)this.nVisitedFiles.get()));
			}
		});
	}
	
	/**
	 * Gets the property associated to the number of computed files, ready to bind.
	 * 
	 * @return property ready to bind
	 */
	public IntegerProperty numberOfComputedFilesProperty() {
		return this.nComputedFiles;
	}
	

	// Mean number of matches handler
	
    /**
     * Sets the mean number of matches.
     * 
     * @param value
     * 		mean number of matches
     */
	public void setMeanNumberOfMatches(final double value) {
		Platform.runLater(() -> this.meanNumberOfMatches.set(value));
	}

	/**
	 * Gets the property associated to the mean number of matches, ready to bind.
	 * 
	 * @return property ready to bind
	 */
	public DoubleProperty meanNumberOfMatchesProperty() {
		return this.meanNumberOfMatches;
	}
	
	
	// Least one match percentage handler

    /**
     * Sets the percentage of files that have at least one match.
     * 
     * @param value
     * 		mean number of matches
     */
	public void setLeastOneMatchPercentage(final double value) {
		Platform.runLater(() -> this.leastOneMatchPercentage.set(value));
	}

	/**
	 * Gets the property associated to the percentage of files that have at least one match, ready to bind.
	 * 
	 * @return property ready to bind
	 */
	public DoubleProperty leastOneMatchPercentageProperty() {
		return this.leastOneMatchPercentage;
	}
	
	
	// List of results data handler
	
	/**
	 * Adds a result to list.
	 * 
	 * @param path
	 * 		path of the computed file
	 * @param nMatches
	 * 		number of matches of this file
	 * @param time
	 * 		elapsed time to parse it
	 */
	public void addResult(final String path, final int nMatches, final long time) {
		Platform.runLater(() -> this.resultRows.add(new RowType(path, "" + nMatches, time)));
	}
	
	/**
	 * Adds a result to list, when computation of the file was unsuccessful.
	 * 
	 * @param path
	 * 		path of the computed file
	 * @param message
	 * 		short message for the error
	 */
	public void addResult(final String path, final String message) {
		Platform.runLater(() -> this.resultRows.add(new RowType(path, message, 0)));
	}
	
	/**
	 * @return an observable collection of result items.
	 */
	public ObservableList<RowType> getResultList() {
		return this.resultRows;
	}
	
	
	// Total elapsed time handler
	
	/**
     * Sets the mean number of matches.
     * 
     * @param value
     * 		mean number of matches
     */
	public void setTotalElapsedTime(final long value) {
		Platform.runLater(() -> this.totalElapsedTime.set(value));
	}

	/**
	 * Gets the property of mean number of matches, ready to bind.
	 * 
	 * @return property ready to bind
	 */
	public LongProperty totalElapsedTimeProperty() {
		return this.totalElapsedTime;
	}
	
	
	// Progress handler
	
	/**
	 * Gets the progress property ready to bind.
	 * 
	 * @return progress property
	 */
	public SimpleDoubleProperty progressProperty() {
		return this.progress;
	}
	
	
	// Reset
	
	public void reset() {
		Platform.runLater(() -> {
			this.nVisitedFiles.set(0);
			this.nComputedFiles.set(0);
			this.totalElapsedTime.set(0);
			this.leastOneMatchPercentage.set(0.0);
			this.meanNumberOfMatches.set(0.0);
			this.progress.set(0.0);
			this.resultRows.clear();
		});
	}
	
}
