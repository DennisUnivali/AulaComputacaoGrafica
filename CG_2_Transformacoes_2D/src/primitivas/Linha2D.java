package primitivas;

import java.awt.Graphics2D;

public class Linha2D {
	public Ponto2D a;
	public Ponto2D b;
	public Linha2D(Ponto2D a, Ponto2D b) {
		super();
		this.a = a;
		this.b = b;
	}
	public Linha2D(double x1, double y1, double x2, double y2) {
		super();
		a = new Ponto2D(x1, y1, 1);
		b = new Ponto2D(x2, y2, 1);
	}
	
	public void desenhase(Graphics2D g2d) {
		g2d.drawLine((int)a.x,(int)a.y,(int)b.x,(int)b.y);
	}
}
