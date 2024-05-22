package Math3D;
import java.util.Vector;

public class Matriz4x4 {
	double amatriz[][] = new double[4][4];
	
	public void setIdentity() {
		amatriz[0][0] = 1;
		amatriz[0][1] = 0;
		amatriz[0][2] = 0;
		amatriz[0][3] = 0;
		
		amatriz[1][0] = 0;
		amatriz[1][1] = 1;
		amatriz[1][2] = 0;
		amatriz[1][3] = 0;
		
		amatriz[2][0] = 0;
		amatriz[2][1] = 0;
		amatriz[2][2] = 1;
		amatriz[2][3] = 0;
		
		amatriz[3][0] = 0;
		amatriz[3][1] = 0;
		amatriz[3][2] = 0;
		amatriz[3][3] = 1;
	}
	
	public void setTranslate(double a,double b,double c) {
		amatriz[0][0] = 1;
		amatriz[0][1] = 0;
		amatriz[0][2] = 0;
		amatriz[0][3] = a;
		
		amatriz[1][0] = 0;
		amatriz[1][1] = 1;
		amatriz[1][2] = 0;
		amatriz[1][3] = b;
		
		amatriz[2][0] = 0;
		amatriz[2][1] = 0;
		amatriz[2][2] = 1;
		amatriz[2][3] = c;
		
		amatriz[3][0] = 0;
		amatriz[3][1] = 0;
		amatriz[3][2] = 0;
		amatriz[3][3] = 1;
	}
	
	public void setRotateX(double ang) {
		double angrad = Math.toRadians(ang);
		double sin = Math.sin(angrad);
		double cos = Math.cos(angrad);
		
		amatriz[0][0] = 1;
		amatriz[0][1] = 0;
		amatriz[0][2] = 0;
		amatriz[0][3] = 0;
		
		amatriz[1][0] = 0;
		amatriz[1][1] = cos;
		amatriz[1][2] = -sin;
		amatriz[1][3] = 0;
		
		amatriz[2][0] = 0;
		amatriz[2][1] = sin;
		amatriz[2][2] = cos;
		amatriz[2][3] = 0;
		
		amatriz[3][0] = 0;
		amatriz[3][1] = 0;
		amatriz[3][2] = 0;
		amatriz[3][3] = 1;
	}
	
	public void setRotateY(double ang) {
		double angrad = Math.toRadians(ang);
		double sin = Math.sin(angrad);
		double cos = Math.cos(angrad);
		
		amatriz[0][0] = cos;
		amatriz[0][1] = 0;
		amatriz[0][2] = -sin;
		amatriz[0][3] = 0;
		
		amatriz[1][0] = 0;
		amatriz[1][1] = 1;
		amatriz[1][2] = 0;
		amatriz[1][3] = 0;
		
		amatriz[2][0] = sin;
		amatriz[2][1] = 0;
		amatriz[2][2] = cos;
		amatriz[2][3] = 0;
		
		amatriz[3][0] = 0;
		amatriz[3][1] = 0;
		amatriz[3][2] = 0;
		amatriz[3][3] = 1;
	}	
	
	public void setProjectionD(double d) {
		amatriz[0][0] = 1;
		amatriz[0][1] = 0;
		amatriz[0][2] = 0;
		amatriz[0][3] = 0;
		
		amatriz[1][0] = 0;
		amatriz[1][1] = 1;
		amatriz[1][2] = 0;
		amatriz[1][3] = 0;
		
		amatriz[2][0] = 0;
		amatriz[2][1] = 0;
		amatriz[2][2] = 1;
		amatriz[2][3] = 0;
		
		amatriz[3][0] = 0;
		amatriz[3][1] = 0;
		amatriz[3][2] = 1/d;
		amatriz[3][3] = 1;
	}	
	
	public Vetor3D multiplicaVetor(Vetor3D v) {
		Vetor3D saida = new Vetor3D(1,1,1,1);
		
		saida.x = v.x*amatriz[0][0] + v.y*amatriz[0][1] + v.z*amatriz[0][2] + v.w*amatriz[0][3];
		saida.y = v.x*amatriz[1][0] + v.y*amatriz[1][1] + v.z*amatriz[1][2] + v.w*amatriz[1][3];
		saida.z = v.x*amatriz[2][0] + v.y*amatriz[2][1] + v.z*amatriz[2][2] + v.w*amatriz[2][3];
		saida.w = v.x*amatriz[3][0] + v.y*amatriz[3][1] + v.z*amatriz[3][2] + v.w*amatriz[3][3];
		
		saida.x = saida.x/saida.w;
		saida.y = saida.y/saida.w;
		saida.z = saida.z/saida.w;
		saida.w = 1;
		
		return saida;
	}
	
	public Matriz4x4 multiplicaMatrix(Matriz4x4 m){

		Matriz4x4 result = new Matriz4x4();

	    for (int l = 0; l < 4; l++) {
	        for (int i = 0; i < 4; i++) {
	            for (int h = 0; h < 4; h++) {
	                result.amatriz[l][i] += amatriz[l][h] *  m.amatriz[h][i];
	            }
	        }
	    }

	    return result;
	}
	
	public void setScale(double a,double b,double c) {
		amatriz[0][0] = a;
		amatriz[0][1] = 0;
		amatriz[0][2] = 0;
		amatriz[0][3] = 0;
		
		amatriz[1][0] = 0;
		amatriz[1][1] = b;
		amatriz[1][2] = 0;
		amatriz[1][3] = 0;
		
		amatriz[2][0] = 0;
		amatriz[2][1] = 0;
		amatriz[2][2] = c;
		amatriz[2][3] = 0;
		
		amatriz[3][0] = 0;
		amatriz[3][1] = 0;
		amatriz[3][2] = 0;
		amatriz[3][3] = 1;
	}	
}
