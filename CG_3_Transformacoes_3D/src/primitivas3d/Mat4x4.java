package primitivas3d;

public class Mat4x4 {
	double mat[][] = new double[4][4];
	
	public Mat4x4() {
		setIdentity();
	}
	public void setIdentity() {
		zera();
		mat[0][0] = 1;
		mat[1][1] = 1;
		mat[2][2] = 1;
		mat[3][3] = 1;
	}
	
	public void zera() {
		for(int y = 0; y < 4;y++) {
			for(int x = 0; x < 4;x++) {
				mat[y][x] = 0;
			}
		}
	}
	
	public void setTranslate(double a,double b,double c) {
		zera();
		mat[0][0] = 1;
		mat[1][1] = 1;
		mat[2][2] = 1;
		mat[3][3] = 1;
		mat[0][3] = a;
		mat[1][3] = b;
		mat[2][3] = c;
	}
	
	public void setSacale(double a,double b,double c) {
		zera();
		mat[0][0] = a;
		mat[1][1] = b;
		mat[2][2] = c;
		mat[3][3] = 1;
	}
	
	public void setRotY(double ang) {
		double angrad = ang*0.01745329;
		zera();
		mat[0][0] = Math.cos(angrad);
		mat[0][1] = 0;
		mat[0][2] = -Math.sin(angrad);
		
		mat[1][1] = 1;
		
		mat[2][0] = Math.sin(angrad);
		mat[2][1] = 0;
		mat[2][2] = Math.cos(angrad);
		
		mat[3][3] = 1;
	}
	
	public void setPerspectiva(double d) {

		zera();
		mat[0][0] = 1;
		mat[0][3] = 0;

		mat[1][1] = 1;
		mat[1][3] = 0;
		
		mat[2][0] = 0;
		mat[2][1] = 0;
		mat[2][2] = 0;
		mat[2][3] = 0;
		
		mat[3][2] = 1/d;
		mat[3][3] = 1;
	}
}
