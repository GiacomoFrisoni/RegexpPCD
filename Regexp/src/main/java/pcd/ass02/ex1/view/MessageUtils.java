package pcd.ass02.ex1.view;

import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class MessageUtils {
	
	public enum ExceptionType {
		
		FXML_EXCEPTION("FXML Loading Exception"),
		THREAD_EXCEPTION("Thread Exception"),
		IO_EXCEPTION("IO Exception");
		
		private final String header;
		
		private ExceptionType(String header) {
			this.header = header;
		}
		
		public String getHeader() {
			return this.header;
		}
	}
	
	public static final String ERROR_TITLE = "Error";
	public static final String FXML_ERROR_MESSAGE = "could not be loaded";
	
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
	 * 		action performed on button pressed
	 */
	public static void showMessage(final AlertType type, final String title, final String header, final String message, final String additional, Consumer<ButtonType> action) {
		Platform.runLater(() ->{
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
		
			if (action != null)
				alert.showAndWait().ifPresent(action);
			else
				alert.showAndWait();
		});
	}
	
	public static void showFXMLException(final String component, final Exception e) {
		if (e != null) {
			showMessage(AlertType.ERROR, ERROR_TITLE, ExceptionType.FXML_EXCEPTION.getHeader(), component + " " + FXML_ERROR_MESSAGE, e.getMessage(), null);
		} else {
			showMessage(AlertType.ERROR, ERROR_TITLE, ExceptionType.FXML_EXCEPTION.getHeader(), component + " " + FXML_ERROR_MESSAGE, "", null);
		}
	}
	
	public static void showExcpetion(final ExceptionType exceptionType, final String message, final Exception e) {
		if (e != null) {
			showMessage(AlertType.ERROR, ERROR_TITLE, exceptionType.getHeader(), message, e.getMessage(), null);
		} else {
			showMessage(AlertType.ERROR, ERROR_TITLE, exceptionType.getHeader(), message, "", null);
		}	
	}
}
