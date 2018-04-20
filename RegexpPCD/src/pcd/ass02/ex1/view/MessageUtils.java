package pcd.ass02.ex1.view;

import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class MessageUtils {
	
	public static final String FXML_ERROR_LOADING_HEADER = "FXML Loading Exception";
	public static final String FXML_ERROR_LOADING_MESSAGE = "could not be loaded";
	
	/**
	 * Show a dialog. Thread-Safe (using Platform.runLater)
	 * @param type
	 * 		type of the dialog (info, warning, error)
	 * @param title
	 * 		title of the window when dialog is shown (on the top)
	 * @param header
	 * 		header of the dialog (bottom of the title, near the icon)
	 * @param message
	 * 		message of the dialog
	 * @param additional
	 * 		additional info of the dialog, under the main message
	 * @param action
	 * 		action performered on button pressed
	 */
	public static void showMessage(final AlertType type, final String title, final String header, final String message, final String additional, Consumer<ButtonType> action) {
		final Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		
		String msg = message;
		
		if (!additional.isEmpty()) {
			msg += "\n";
			msg += additional;
		}
		
		alert.setContentText(msg);
		
		Platform.runLater(() ->{
			if (action != null)
				alert.showAndWait().ifPresent(action);
			else
				alert.showAndWait();
		});

	}

	/**
	 * Simple method to show and error message and kill the app
	 * @param header
	 * 		main cause of the error (short!)
	 * @param message
	 * 		user-friendly message of the error
	 * @param exception
	 * 		real error message
	 */
	public static void showExceptionAndExit(final String header, final String message, final String exception) {
		showMessage(AlertType.ERROR, "ERROR", header, message, exception, r -> System.exit(0));		
	}
	
	/**
	 * Simple method to show a simple error message and kill the app
	 * @param header
	 * 		main cause of the error (short!)
	 * @param message
	 * 		user-friendly message of the error
	 */
	public static void showExceptionAndExit(final String header, final String message) {
		showExceptionAndExit(header, message, "");
	}
	
	public static void showFXMLException(final String root, Exception e) {
		showExceptionAndExit(FXML_ERROR_LOADING_HEADER, 
							 root + " " + FXML_ERROR_LOADING_MESSAGE, 
							 e.getMessage());
	}
}
