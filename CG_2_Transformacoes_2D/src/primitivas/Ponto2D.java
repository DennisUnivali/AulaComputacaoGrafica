package primitivas;

public class Ponto2D {
	public double x;
	public double y;
	public double w;
	
	public Ponto2D(double x, double y, double w) {
		super();
		this.x = x;
		this.y = y;
		this.w = w;
	}
	
	public void multiplicaMat(Mat3x3 mat) {
		double x1 = mat.mat[0][0]*x+mat.mat[0][1]*y+mat.mat[0][2]*w;
		double y1 = mat.mat[1][0]*x+mat.mat[1][1]*y+mat.mat[1][2]*w;
		double w1 = mat.mat[2][0]*x+mat.mat[2][1]*y+mat.mat[2][2]*w;
		
		x = x1;
		y = y1;
		w = w1;
	}
	
}
