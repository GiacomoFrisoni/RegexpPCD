package pcd.ass02.ex1.view;

import java.io.File;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
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
	
	@FXML
	private TextField path, regularExpression, depth;
	
	@FXML
	private Button choosePath, startStop;
	
	@FXML
	private Label statusLabel;
	
	@FXML
	private ProgressIndicator progress;
	
	
	
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
        	setDimensions();
        	setEventHandlers();
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
        choosePath.setOnMouseClicked(e -> {
			final DirectoryChooser chooser = new DirectoryChooser();
			chooser.setTitle(CHOOSE_FOLDER_TITLE);
			final File pathFile = chooser.showDialog(this.window);
						
			if (pathFile != null) {
				this.path.setText(pathFile.getAbsolutePath());
			}		
			setError("ERrore");
		});
        
        startStop.setOnMouseClicked(e -> {
			this.choosePath.setDisable(true);
			this.path.setDisable(true);
			this.regularExpression.setDisable(true);
			this.depth.setDisable(true);
			this.startStop.setDisable(true);
			setLoading();
		});
    }
	
	private void setIdle() {
		this.statusLabel.getStyleClass().remove("errorLabel");
		this.statusLabel.setText("Idle");
		
		this.progress.setVisible(false);
		this.progress.setManaged(false);
	}
	
	private void setLoading() {
		this.statusLabel.getStyleClass().remove("errorLabel");
		this.statusLabel.setText("Searching...");
		
		this.progress.setVisible(true);
		this.progress.setManaged(true);
	}
	
	private void setError(String error) {
		this.statusLabel.getStyleClass().add("errorLabel");
		this.statusLabel.setText(error);
		
		this.progress.setVisible(false);
		this.progress.setManaged(false);
	}

}
