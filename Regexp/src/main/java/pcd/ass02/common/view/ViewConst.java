package pcd.ass02.common.view;

/**
 * This enumeration represents an utility for the management of the
 * view stages titles.
 *
 */
public enum ViewConst {

	GENERAL_TITLE("Giacomo Frisoni & Marcin Pabich"),
	EXECUTOR_TITLE(GENERAL_TITLE.getTitle() + " - Executors"),
	VERTX_TITLE(GENERAL_TITLE.getTitle() + " - Vert.x"),
	RX_TITLE(GENERAL_TITLE.getTitle() + " - Executors + RxJava");
	
	private final String title;
	
	private ViewConst(final String title) {
		this.title = title;
	}
	
	/**
	 * @return the title to display
	 */
	public String getTitle() {
		return this.title;
	}
	
}
