package ply;

public class PlyProperties {
	private String author;
	private String created;
	private String size;
	private int faces;
	private int points;
	private String path;
	private String name;

	public PlyProperties(String path, String name, String size, int faces, int points, String created, String author) {
		this.path = path;
		this.name=name;
		this.size = size;
		this.author = author;
		this.faces = faces;
		this.points = points;
		this.created = created;
	}
	public PlyProperties(String path) {
		this.path = path;
	}


	public String getPath() {
		return path;
	}

	/**
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int getFaces() {
		return faces;
	}

	public void setFaces(int faces) {
		this.faces = faces;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}
