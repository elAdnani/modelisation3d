package views;





public class ModelFile {
		private String name,author,created,size;
		private int faces,points;
		private String path;
		public ModelFile(String path,String name,String size,int faces, int points, String created,String author) {
			super();
			this.path = path;
			this.name = name;
			this.size = size;
			this.author = author;
			this.faces = faces;
			this.points = points;
			this.created = created;
		}
		
		public String getName() {
			return name;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public void setName(String name) {
			this.name = name;
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
