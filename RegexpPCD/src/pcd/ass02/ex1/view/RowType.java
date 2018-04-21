package pcd.ass02.ex1.view;

public class RowType {
	private String path;
	private String value;
	
	public RowType() {
		 
	}
	
	public RowType(String path, String value) {
		this.path = path;
		this.value = value;
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

	
}
