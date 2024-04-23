package primitivas3d;

import java.awt.Graphics2D;

public class Linha3D {
	public Ponto3D a;
	public Ponto3D b;
	public Linha3D(Ponto3D a, Ponto3D b) {
		super();
		this.a = a;
		this.b = b;
	}
	public Linha3D(double x1, double y1,double z1, double x2, double y2,double z2) {
		super();
		a = new Ponto3D(x1, y1, z1, 1);
		b = new Ponto3D(x2, y2, z2, 1);
	}
	
	public void desenhase(Graphics2D g2d) {
		g2d.drawLine((int)a.x,(int)a.y,(int)b.x,(int)b.y);
	}
	
	public void desenhase(Graphics2D g2d,Mat4x4 perspectiva) {
		Ponto3D an = a.multiplicaMatNP(perspectiva);
		Ponto3D bn = b.multiplicaMatNP(perspectiva);
		
		an.ajusteW();
		bn.ajusteW();
		
		//System.out.println("an.w "+an.w+" bn.w "+bn.w);
		
		
		g2d.drawLine((int)an.x+500,(int)an.y+400,(int)bn.x+500,(int)bn.y+400);
	}
}
