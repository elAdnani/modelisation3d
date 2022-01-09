package util;
/**
 * 
 * Cette classe permet d'énumérer les thèmes existant sur la plateforme.<br>
 * Elle est définie par le chemin dans lequel il est placé.
 *
 */
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
