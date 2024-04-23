package primitivas2d;

public class Mat3x3 {
	double mat[][] = new double[3][3];
	
	public Mat3x3() {
		setIdentity();
	}
	public void setIdentity() {
		zera();
		mat[0][0] = 1;
		mat[1][1] = 1;
		mat[2][2] = 1;
	}
	
	public void zera() {
		for(int y = 0; y < 3;y++) {
			for(int x = 0; x < 3;x++) {
				mat[y][x] = 0;
			}
		}
	}
	
	public void setTranslate(double a,double b) {
		zera();
		mat[0][0] = 1;
		mat[1][1] = 1;
		mat[2][2] = 1;
		mat[0][2] = a;
		mat[1][2] = b;
	}
	
	public void setSacale(double a,double b) {
		zera();
		mat[0][0] = a;
		mat[1][1] = b;
		mat[2][2] = 1;
	}
}
