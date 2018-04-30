package pcd.ass02.common.view;

/**
 * This class represents the structure of the table view elements.
 *
 */
public class RowType {
	
	private String path;
	private String value;
	private long elapsedTime;
	
	/**
	 * Constructs a new row type.
	 * 
	 * @param path
	 * 		the path to display
	 * @param value
	 * 		the value to display
	 * @param elapsedTime
	 * 		the elapsed time to display
	 */
	public RowType(final String path, final String value, final long elapsedTime) {
		this.path = path;
		this.value = value;
		this.elapsedTime = elapsedTime;
	}

	/**
	 * @return the path shown
	 */
	public String getPath() {
		return this.path;
	}

	/**
	 * Sets the path to display.
	 * 
	 * @param path
	 * 		file path to show
	 */
	public void setPath(final String path) {
		this.path = path;
	}

	/**
	 * @return the value shown
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value to display.
	 * 
	 * @param value
	 * 		value to show
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	/**
	 * @return the elapsed time shown
	 */
	public long getElapsedTime() {
		return elapsedTime;
	}

	/**
	 * Sets the elapsed time to display.
	 * 
	 * @param elapsedTime
	 * 		the elapsed time to show
	 */
	public void setElapsedTime(final long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	
}
