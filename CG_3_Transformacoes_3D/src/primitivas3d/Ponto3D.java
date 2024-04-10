package primitivas3d;

import primitivas3d.Mat4x4;

public class Ponto3D {
	public double x;
	public double y;
	public double z;
	public double w;
	
	public Ponto3D(double x, double y, double z,double w) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public void multiplicaMat(Mat4x4 mat) {
		double x1 = mat.mat[0][0]*x+mat.mat[0][1]*y+mat.mat[0][2]*z+mat.mat[0][3]*w;
		double y1 = mat.mat[1][0]*x+mat.mat[1][1]*y+mat.mat[1][2]*z+mat.mat[1][3]*w;
		double z1 = mat.mat[2][0]*x+mat.mat[2][1]*y+mat.mat[2][2]*z+mat.mat[2][3]*w;
		double w1 = mat.mat[3][0]*x+mat.mat[3][1]*y+mat.mat[3][2]*z+mat.mat[3][3]*w;
		
		x = x1;
		y = y1;
		z = z1;
		w = w1;
	}
	
	public Ponto3D multiplicaMatNP(Mat4x4 mat) {
		double x1 = mat.mat[0][0]*x+mat.mat[0][1]*y+mat.mat[0][2]*z+mat.mat[0][3]*w;
		double y1 = mat.mat[1][0]*x+mat.mat[1][1]*y+mat.mat[1][2]*z+mat.mat[1][3]*w;
		double z1 = mat.mat[2][0]*x+mat.mat[2][1]*y+mat.mat[2][2]*z+mat.mat[2][3]*w;
		double w1 = mat.mat[3][0]*x+mat.mat[3][1]*y+mat.mat[3][2]*z+mat.mat[3][3]*w;
		
		return new Ponto3D(x1, y1, z1, w1);
	}
	
	public void ajusteW() {
		x = x/w;
		y = y/w;
		z = z/w;
		w = 1;
	}
	
}
