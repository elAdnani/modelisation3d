package modele;

public class Trace {
	double x, y, z;

	public Trace() {
		double x = 1;
		double y = 1;
		double z = 0;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;

	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;

	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;

	}

	public Trace getPlan() {
		return this;
	}


}
