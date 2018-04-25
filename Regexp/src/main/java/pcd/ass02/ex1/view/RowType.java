package pcd.ass02.ex1.view;

public class RowType {
	private String path;
	private String value;
	private long elapsedTime;
	
	public RowType() {
		 
	}
	
	public RowType(String path, String value, long elapsedTime) {
		this.path = path;
		this.value = value;
		this.elapsedTime = elapsedTime;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	
	
}
