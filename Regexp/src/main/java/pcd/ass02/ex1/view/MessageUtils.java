package pcd.ass02.ex1.view;

import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class MessageUtils {
	
	public static final String ERROR_TITLE = "Error";
	public static final String FXML_ERROR_HEADER = "FXML Loading Exception";
	public static final String FXML_ERROR_MESSAGE = "could not be loaded";
	
	public static final String THREAD_ERROR_HEADER = "Thread Exception";
	
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
		
		if (additional != null) {
			if (!additional.isEmpty()) {
				msg += "\n";
				msg += additional;
			}
		}
		
		alert.setContentText(msg);
		
		Platform.runLater(() ->{
			if (action != null)
				alert.showAndWait().ifPresent(action);
			else
				alert.showAndWait();
		});
	}
	
	public static void showFXMLException(final String component, Exception e) {
		showMessage(AlertType.ERROR, ERROR_TITLE, FXML_ERROR_HEADER, component + " " + FXML_ERROR_MESSAGE, e.getMessage(), null);
	}
	
	public static void showThreadExcpetion(final String message, Exception e) {
		showMessage(AlertType.ERROR, ERROR_TITLE, THREAD_ERROR_HEADER, message, e.getMessage(), null);
	}
}