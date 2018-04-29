package pcd.ass02.ex1.view;

public enum ViewConst {

	GENERAL_TITLE("Giacomo Frisoni & Marcin Pabich"),
	EXECUTOR_TITLE(GENERAL_TITLE.getTitle() + " - Executors"),
	VERTX_TITLE(GENERAL_TITLE.getTitle() + " - Vert.x"),
	RX_TITLE(GENERAL_TITLE.getTitle() + " - Executors + RxJava");
	
	private final String title;
	
	private ViewConst(final String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
}
