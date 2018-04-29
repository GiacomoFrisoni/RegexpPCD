package pcd.ass02.ex3.controller;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pcd.ass02.ex1.view.RowType;

public class DataManager {

	private static volatile DataManager singleton;
	
	private SimpleIntegerProperty nVisitedFiles = new SimpleIntegerProperty();
	private SimpleIntegerProperty nComputedFiles = new SimpleIntegerProperty();
	private SimpleDoubleProperty leastOneMatchPercentage = new SimpleDoubleProperty();
	private SimpleDoubleProperty meanNumberOfMatches = new SimpleDoubleProperty();
	private final ObservableList<RowType> resultRows = FXCollections.observableArrayList();

	
    private DataManager() {
		this.nVisitedFiles.set(0);
		this.nComputedFiles.set(0);
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
    public static DataManager getHandler() {
        if (singleton == null) {
            synchronized (DataManager.class) {
                if (singleton == null) {
                    singleton = new DataManager();
                }
            }
        }
        return singleton;
    }
    
    
	// Number of visited files handler
    
	public void setNumberOfVisitedFiles(final int value) {
		Platform.runLater(() -> this.nVisitedFiles.set(value));
	}
	
	public IntegerProperty numberOfVisitedFilesProperty() {
		return this.nVisitedFiles;
	}
	

	// Number of computed files handler
	
	public void setNumberOfComputedFiles(final int value) {
		Platform.runLater(() -> this.nComputedFiles.set(value));
	}
	
	public IntegerProperty numberOfComputedFilesProperty() {
		return this.nComputedFiles;
	}
	

	// Mean number of matches handler
	
	public void setMeanNumberOfMatches(final double value) {
		Platform.runLater(() -> this.meanNumberOfMatches.set(value));
	}

	public DoubleProperty meanNumberOfMatchesProperty() {
		return this.meanNumberOfMatches;
	}
	
	
	// Least one match percentage

	public void setLeastOneMatchPercentage(final double value) {
		Platform.runLater(() -> this.leastOneMatchPercentage.set(value));
	}

	public DoubleProperty leastOneMatchPercentageProperty() {
		return this.leastOneMatchPercentage;
	}
	
	//List of data
	
	public void addResult(final String path, final int nMatches, final long time) {
		this.resultRows.add(new RowType(path, "" + nMatches, time));
	}
	
	public void addResult(final String path, final String message) {
		this.resultRows.add(new RowType(path, message, 0));
	}
	
	public ObservableList<RowType> getResultList() {
		return FXCollections.unmodifiableObservableList(this.resultRows);
	}
	
}
