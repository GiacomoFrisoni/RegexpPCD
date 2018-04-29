package pcd.ass02.ex3.view;

import java.io.File;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import pcd.ass02.ex1.controller.RegexpController;
import pcd.ass02.ex1.view.MessageUtils;
import pcd.ass02.ex1.view.RowType;

public class MainBindingFrame extends GridPane {
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 400;
	
	private static final String CHOOSE_FOLDER_TITLE = "Choose folder to scan";
	private static final String IDLE_MESSAGE = "Idle";
	private static final String SEARCHING_MESSAGE = "Searching...";
	private static final String FINISH_MESSAGE = "Done!";
	private static final String PERCENTAGE_SYMBOL = "%";
	private static final String PATH_EMPTY_MESSAGE = "Path shouldn't be empty";
	private static final String REGEX_EMPTY_MESSAGE = "Regular Expression shouldn't be empty";
	private static final String DEPTH_EMPTY_MESSAGE = "Max depth should be default or non empty";
	private static final String DEPTH_ERROR_MESSAGE = "Depth is not a number";
	
	private static final String PATH_COLUMN_TITLE = "Path";
	private static final String VALUE_COLUMN_TITLE = "Value";
	private static final String TIME_COLUMN_TITLE = "Elapsed time";
	private static final String PATH_PROPERTY_NAME = "path";
	private static final String VALUE_PROPERTY_NAME = "value";
	private static final String TIME_PROPERTY_NAME = "elapsedTime";
	
	private static final String DOUBLE_FORMAT = "%.2f";
	
	private final RegexpController controller;
	private final Stage window;
	
	@FXML private Integer defaultValue;
	
	@FXML private TextField path, regularExpression, depth;
	@FXML private Button choosePath, start, reset;
	@FXML private Label statusLabel, leastOneMatchPercentage, meanNumberOfMatches, currentScanned, totalToScan;
	@FXML private CheckBox maxDepth;
	@FXML private ProgressIndicator progress;
	@FXML private ProgressBar progressBar;
	@FXML private TableView<RowType> tableView;
	
	
	/**
	 * Constructs a new MainFrame.
	 * 
	 * @param controller
	 * 		the controller of the application
	 * @param window
	 * 		the window in which display the frame
	 */
	public MainBindingFrame(final RegexpController controller, final Stage window) {
		//Set some references
		this.controller = controller;
		this.window = window;
		
    	//Load the FXML definition
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("..\\..\\ex1\\view\\MainFrame.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
        try {
            fxmlLoader.load();

        } catch (Exception exception) {
        	MessageUtils.showFXMLException(this.toString(), exception);
        	exception.printStackTrace();
        }
        
        //Set view
        this.getStylesheets().add(getClass().getResource("..\\..\\ex1\\view\\style.css").toExternalForm());
        this.setDimensions();
        this.setEventHandlers();
        this.setTableColumns();
        this.setIdle();
        
        //Set bindings
        this.totalToScan.textProperty().bind(ViewDataManager.getHandler().numberOfVisitedFilesProperty().asString());
        this.currentScanned.textProperty().bind(ViewDataManager.getHandler().numberOfComputedFilesProperty().asString());
        this.leastOneMatchPercentage.textProperty().bind(ViewDataManager.getHandler().leastOneMatchPercentageProperty().asString(DOUBLE_FORMAT));
        this.meanNumberOfMatches.textProperty().bind(ViewDataManager.getHandler().meanNumberOfMatchesProperty().asString(DOUBLE_FORMAT));
	}
	
	private void setDimensions() {
		this.setWidth(WIDTH);
    	this.setHeight(HEIGHT);
    	this.setMinWidth(WIDTH);
    	this.setMinHeight(HEIGHT);
    	this.window.setMinWidth(WIDTH);
    	this.window.setMinHeight(HEIGHT);
	}
	
	private void setEventHandlers() {
		//Choosing the right path
        this.choosePath.setOnMouseClicked(e -> {
			final DirectoryChooser chooser = new DirectoryChooser();
			chooser.setTitle(CHOOSE_FOLDER_TITLE);
			final File pathFile = chooser.showDialog(this.window);
			if (pathFile != null) {
				this.path.setText(pathFile.getAbsolutePath());
			}		
		});
        
        //Starting computation
        this.start.setOnMouseClicked(e -> {
        	//Check if inputs are not empty / correct
        	if (checkInputs()) {
        		//Disable some views
        		this.setIdle();
        		this.setInputDisabled(true);
	
        		//Tell controller to start
        		this.controller.setStartingPath(this.path.getText());
        		this.controller.setPattern(this.regularExpression.getText());
        		if (!this.maxDepth.isSelected()) {
        			this.controller.setMaxDepthNavigation(Integer.parseInt(this.depth.getText()));
        		}
        		
        		//Check if search can start
        		if (this.controller.search()) {
        			//Start searching
            		this.setSearching();
        		} else {
        			//Something is wrong
        			this.setInputDisabled(false);
        		} 	
        	}
		});
        
        //Checkbox for max depth
        this.maxDepth.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				depth.setDisable(newValue);
			}
		});
        
        //Reset handler
        this.reset.setOnMouseClicked(e -> {
        	this.controller.reset();
        	this.reset();
        });
    }
	
	/**
	 * Sets the view to the idle state.
	 */
	public void setIdle() {
		this.changeStatus(IDLE_MESSAGE, false, false);
		Platform.runLater(() -> this.reset.setDisable(true));
	}
	
	/**
	 * Sets the view to the searching.
	 */
	public void setSearching() {
		this.changeStatus(SEARCHING_MESSAGE, true, false);
	}
	
	/**
	 * Sets the view to show an error message.
	 * 
	 * @param message
	 * 		the message to display
	 */
	public void setError(final String message) {
		this.changeStatus(message, false, true);
	}
	
	/**
	 * Sets the view as done when computation finished
	 */
	public void setFinish() {
		this.changeStatus(FINISH_MESSAGE, false, false);
		Platform.runLater(() -> this.reset.setDisable(false));
	}
	
	/**
	 * Reset all the view
	 */
	public void reset() {
		this.changeStatus(IDLE_MESSAGE, false, false);
		Platform.runLater(() -> {		
			this.path.clear();
			this.regularExpression.clear();
			this.depth.setText("1");
			this.maxDepth.setSelected(false);
			this.leastOneMatchPercentage.setText(defaultValue + PERCENTAGE_SYMBOL);
			this.meanNumberOfMatches.setText("" + defaultValue);
			this.currentScanned.setText("" + defaultValue);
			this.totalToScan.setText("" + defaultValue);
			this.progressBar.setProgress(defaultValue);
		
			this.choosePath.setDisable(false);
			this.path.setDisable(false);
			this.regularExpression.setDisable(false);
			this.depth.setDisable(false);
			this.start.setDisable(false);
			this.maxDepth.setDisable(false);
			this.reset.setDisable(true);
			this.tableView.getItems().clear();
		});
	}
	

	/**
	 * Change the status of the window according to passed parameters
	 * @param message
	 * 		Message to show under the button (info, confirm, error...)
	 * @param isSearching
	 * 		TRUE = progress visible | FALSE = progress disabled
	 * @param isError
	 * 		TRUE = error label's style | FALSE = normal label's style
	 */
	private void changeStatus(final String message, final boolean isSearching, final boolean isError) {
		Platform.runLater(() -> {
			if (isError) {
				this.statusLabel.getStyleClass().add("errorLabel");
			} else {
				this.statusLabel.getStyleClass().remove("errorLabel");
			}
			
			this.progress.setVisible(isSearching);
			this.progress.setManaged(isSearching);
			this.progressBar.setVisible(isSearching);
			this.progressBar.setManaged(isSearching);
			
			this.statusLabel.setText(message);
		});
	}
	
	/**
	 * Check if inputs are not empty and valid
	 * @return
	 * 		TRUE if all inputs are valid
	 */
	private boolean checkInputs() {
		this.setIdle();
		
		//Path is empty
		if (this.path.getText().isEmpty()) {
			this.setError(PATH_EMPTY_MESSAGE);
			return false;
		}
		
		//Regular Expression is empty
		if (this.regularExpression.getText().isEmpty()) {
			this.setError(REGEX_EMPTY_MESSAGE);
			return false;
		}
		
		//I have not selected the default max depth
		if (!this.maxDepth.isSelected()) {
			//Depth is empty
			if (this.depth.getText().isEmpty()) {
				this.setError(DEPTH_EMPTY_MESSAGE);
				return false;
			} else {
				//Depth is not a number
				try {
					Integer.parseInt(this.depth.getText());
				} catch (Exception e) {
					this.setError(DEPTH_ERROR_MESSAGE);
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Set the right columns, including width and binding
	 */
	private void setTableColumns() {
		//Create two columns
		final TableColumn<RowType, String> tcPath = new TableColumn<>(PATH_COLUMN_TITLE);
		final TableColumn<RowType, String> tcValue = new TableColumn<>(VALUE_COLUMN_TITLE);
		final TableColumn<RowType, String> tcTime = new TableColumn<>(TIME_COLUMN_TITLE);
		
		
		//First is not resizable
		tcValue.setResizable(false);
		tcTime.setResizable(false);
		
		//Preferred width
		tcPath.setPrefWidth(300.0d);  	
		tcValue.setPrefWidth(150.0d);
		tcTime.setPrefWidth(150.0d);
		
		//What they'll contain
		tcPath.setCellValueFactory(new PropertyValueFactory<RowType, String>(PATH_PROPERTY_NAME));
		tcValue.setCellValueFactory(new PropertyValueFactory<RowType, String>(VALUE_PROPERTY_NAME));
		tcTime.setCellValueFactory(new PropertyValueFactory<RowType, String>(TIME_PROPERTY_NAME));
		
		//They can be both string or integers, I try to sort them
		tcValue.setComparator((string1, string2) -> {
			Integer val1 = null;
			Integer val2 = null;
			
			//Check if first is a string
			try { val1 = Integer.parseInt(string1); } 
			catch (Exception e) { return -1; }
			
			//Check if second is a string
			try { val2 = Integer.parseInt(string2); } 
			catch (Exception e) { return 1; }
			
			//Both are integers
			return val2 - val1;	
		});
		
		//Stretch the first column
		tcPath.prefWidthProperty().bind(
                this.tableView.widthProperty()
                .subtract(tcValue.widthProperty())
                .subtract(tcTime.widthProperty())
             );
		
		//Add all columns
		this.tableView.getColumns().add(tcPath);
		this.tableView.getColumns().add(tcValue);
		this.tableView.getColumns().add(tcTime);
		
		//Bind to observable collection
		this.tableView.itemsProperty().bind(new SimpleObjectProperty<>(ViewDataManager.getHandler().getResultList()));
	}
	
	
	private void setInputDisabled(final boolean isDisabled) {
		Platform.runLater(() -> {
			this.choosePath.setDisable(isDisabled);
			this.path.setDisable(isDisabled);
			this.regularExpression.setDisable(isDisabled);
			this.start.setDisable(isDisabled);
			this.maxDepth.setDisable(isDisabled);
			this.depth.setDisable(this.maxDepth.isSelected());
		});
	}
	
}
