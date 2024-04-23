package primitivas3d;

public class Vetor3D {
	public double x;
	public double y;
	public double z;
	public double w;
	
	public Vetor3D(double x, double y, double z, double w) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "("+x+","+y+","+z+","+w+")";
	}
	
	
}
