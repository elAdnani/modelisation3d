package util;

public enum Theme {
	DEFAULT(""), SOLARIS("../styles/solaris.css"), DARK("../styles/darkmode.css");
	
	private String path;
	
	private Theme(String path) {
		this.path = path;
	}
	
	@Override
	public String toString() {
		return this.name().substring(0, 1).toUpperCase() + this.name().substring(1).toLowerCase();
	}

	public String getPath() {
		return this.path;
	}
}
