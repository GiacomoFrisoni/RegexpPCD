package pcd.ass02.ex1.view;

import java.io.File;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import pcd.ass02.ex1.controller.RegexpController;

public class MainFrame extends VBox {
	
	private static final String CHOOSE_FOLDER_TITLE = "Choose folder to scan";
	
	private static final int WIDTH = 700;
	private static final int HEIGHT = 300;
	
	private final RegexpController controller;
	private final Window window;
	private int totalFilesToScan = 0;
	private ObservableList<RowType> rows = FXCollections.observableArrayList();	//TODO move to controller
	
	@FXML
	private TextField path, regularExpression, depth;
	
	@FXML
	private Button choosePath, start;
	
	@FXML
	private Label statusLabel, meanPercentage, currentScanned, totalToScan;
	
	@FXML
	private CheckBox maxDepth;
	
	@FXML
	private ProgressIndicator progress;
	
	@FXML
	private ProgressBar progressBar;
	
	@FXML
	private TableView<RowType> tableView;
	
	
	
	public MainFrame(final RegexpController controller, final Window window) {
		//Set some references
		this.controller = controller;
		this.window = window;
		
    	//Load the FXML definition
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainFrame.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
        try {
            fxmlLoader.load();
            this.setDimensions();
            this.setEventHandlers();
            this.setTableColumns();
            this.setIdle();
        } catch (Exception exception) {
        	MessageUtils.showFXMLException(this.toString(), exception);
        }
        
        
	}
	
	
	private void setDimensions() {
		/*final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    	this.setWidth((gd.getDisplayMode().getWidth() * FRAME_SCALE) / 100);
    	this.setHeight((gd.getDisplayMode().getHeight() * FRAME_SCALE) / 100);
    	this.setMinWidth((gd.getDisplayMode().getWidth() * FRAME_SCALE) / 100);
    	this.setMinHeight((gd.getDisplayMode().getHeight() * FRAME_SCALE) / 100);*/
		
		this.setWidth(WIDTH);
    	this.setHeight(HEIGHT);
    	this.setMinWidth(WIDTH);
    	this.setMinHeight(HEIGHT);
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
        		Platform.runLater(() -> {
        			this.choosePath.setDisable(true);
        			this.path.setDisable(true);
        			this.regularExpression.setDisable(true);
        			this.depth.setDisable(true);
        			this.start.setDisable(true);
        		});
	
        		//Tell controller to start
        		this.controller.setStartPath(this.choosePath.getText());
        		this.controller.setPattern(this.regularExpression.getText());	
        		this.controller.setMaxDepthNavigation(this.maxDepth.isSelected() ? 
        											  Integer.MAX_VALUE : 
        											  Integer.parseInt(this.depth.getText()));
        		
        		//Set to searching
        		this.setSearching();
        	}
		
		});
        
        //Checkbox for max depth
        this.maxDepth.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				depth.setDisable(newValue);
			}
		});
    }
	

	
	/**
	 * Set the view to the idle state
	 */
	public void setIdle() {
		this.changeStatus("Idle", false, false);
	}
	
	/**
	 * Set the view to the searching
	 */
	public void setSearching() {
		this.changeStatus("Searching...", true, false);
	}
	
	/**
	 * Set the view to show an error message
	 */
	public void setError(final String message) {
		this.changeStatus(message, false, true);
	}
	
	/**
	 * Set the label to show percentage
	 */
	public void setMeanPercentage(final double percentage) {
		Platform.runLater(() -> {
			this.meanPercentage.setText(percentage + "%");
		});
	}
	
	/**
	 * Set the label and progress showing files to scan
	 */
	public void setTotalFilesToScan(final int total) {
		this.totalFilesToScan = total;
		
		Platform.runLater(() -> {
			this.totalToScan.setText("" + this.totalFilesToScan);
			this.progressBar.setProgress(0);
		});
	}
	
	/**
	 * Set the number of scanned files, updating the progress bar
	 */
	public void setNumberOfScannedFiles(final int scanned) {
		Platform.runLater(() -> {
			this.currentScanned.setText("" + scanned);
			this.progressBar.setProgress(scanned/this.totalFilesToScan);
		});
	}
	
	//TODO move to controller
	public void addResult(String path, int value) {
		rows.add(new RowType(path, "" + value));
	}
	
	//TODO move to controller
	public void addResult(String path, String value) {
		rows.add(new RowType(path, value));
	}

	
	
	private void changeStatus(String message, boolean isSearching, boolean isError) {
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
	}
	
	private boolean checkInputs() {
		this.setIdle();
		
		//Path is empty
		if (this.path.getText().isEmpty()) {
			this.setError("Path shouldn't be empty");
			return false;
		}
		
		//Regular Expression is empty
		if (this.regularExpression.getText().isEmpty()) {
			this.setError("Regular Expression shouldn't be empty");
			return false;
		}
		
		//I have not selected the default max depth
		if (!this.maxDepth.isSelected()) {
			//Depth is empty
			if (this.depth.getText().isEmpty()) {
				this.setError("Max depth should be default or non empty");
				return false;
			} else {
				//Depth is not a number
				try {
					Integer.parseInt(this.depth.getText());
				} catch (Exception e) {
					this.setError("Depth is not a number");
					return false;
				}
			}
		}
		
		return true;
	}
	
	private void setTableColumns() {
		//Create two columns
		final TableColumn<RowType, String> tcPath = new TableColumn<>("Path");
		final TableColumn<RowType, String> tcValue = new TableColumn<>("Value");
		
		//First is not resizable
		tcValue.setResizable(false);
		
		//Preferred width
		tcPath.setPrefWidth(300.0d );  	
		tcValue.setPrefWidth(150.0d);
		
		//What they'll contain
		tcPath.setCellValueFactory(new PropertyValueFactory<RowType, String>("path"));
		tcValue.setCellValueFactory(new PropertyValueFactory<RowType, String>("value"));
		
		//Stretch the first column
		tcPath.prefWidthProperty().bind(
                this.tableView.widthProperty()
                .subtract(tcValue.widthProperty())
             );
		
		//Add all columns
		this.tableView.getColumns().add(tcPath);
		this.tableView.getColumns().add(tcValue);
		
		//Bind to observable collection
		this.tableView.itemsProperty().bind(new SimpleObjectProperty<>(rows));
	}
}
