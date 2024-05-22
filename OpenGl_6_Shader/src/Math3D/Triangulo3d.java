package Math3D;

import java.awt.Color;
import java.awt.Graphics2D;


public class Triangulo3d {
	Vetor3D p1;
	Vetor3D p2;
	Vetor3D p3;
	
	public Triangulo3d(Vetor3D p1, Vetor3D p2, Vetor3D p3) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}
	
	public void desenhaSe(Graphics2D dbg, Matriz4x4 m) {
		/*Matriz4x4 m2 = new Matriz4x4();
		m2.setTranslate(0, -250, 0);
		
		Matriz4x4 m3 = new Matriz4x4();
		m3.setTranslate(0, 250, 0);
		
		Vetor3D p1l = m2.multiplicaVetor(p1);
		Vetor3D p2l = m2.multiplicaVetor(p2);
		Vetor3D p3l = m2.multiplicaVetor(p3);
		
		p1l = m.multiplicaVetor(p1l);
		p2l = m.multiplicaVetor(p2l);
		p3l = m.multiplicaVetor(p3l);
		
		p1l = m3.multiplicaVetor(p1l);
		p2l = m3.multiplicaVetor(p2l);
		p3l = m3.multiplicaVetor(p3l);*/
		
		Vetor3D p1l = m.multiplicaVetor(p1);
		Vetor3D p2l = m.multiplicaVetor(p2);
		Vetor3D p3l = m.multiplicaVetor(p3);
		
		//System.out.println("p  "+p1.x+" "+(int)p1.y+" "+(int)p2.x+" "+(int)p2.y);
		//System.out.println("pl "+p1l.x+" "+(int)p1l.y+" "+(int)p2l.x+" "+(int)p2l.y);
		
		dbg.drawLine((int)p1l.x,(int)p1l.y,(int)p2l.x,(int)p2l.y);
		dbg.drawLine((int)p2l.x,(int)p2l.y,(int)p3l.x,(int)p3l.y);
		dbg.drawLine((int)p3l.x,(int)p3l.y,(int)p1l.x,(int)p1l.y);
	}
	
	public static void desenhaStaticTriangle(Graphics2D dbg, Vetor3D ps1,Vetor3D ps2,Vetor3D ps3) {
		
		/*dbg.drawLine((int)ps1.x,(int)ps1.y,(int)ps2.x,(int)ps2.y);
		dbg.drawLine((int)ps2.x,(int)ps2.y,(int)ps3.x,(int)ps3.y);
		dbg.drawLine((int)ps3.x,(int)ps3.y,(int)ps1.x,(int)ps1.y);*/
		
		
//		int xPoints[] = new int[3];
//		int yPoints[] = new int[3];
//		xPoints[0] = (int)ps1.x; 
//		xPoints[1] = (int)ps2.x;
//		xPoints[2] = (int)ps3.x;
//		yPoints[0] = (int)ps1.y; 
//		yPoints[1] = (int)ps2.y;
//		yPoints[2] = (int)ps3.y;
//		
//		dbg.fillPolygon(xPoints, yPoints, 3);
		
		//dbg.setColor(Color.DARK_GRAY);
		//dbg.fillPolygon(xPoints, yPoints, 3);
		
		//Color cc = dbg.getColor();
		//triangleRaste.Color c = new triangleRaste.Color(cc.getRed()/255.0f,cc.getGreen()/255.0f,cc.getBlue()/255.0f);
		
		//MainCanvas.raster.drawTriangle(c, (float)ps1.x, (float)ps1.y,(float)ps1.z, c, (float)ps2.x, (float)ps2.y,(float)ps2.z, c, (float)ps3.x, (float)ps3.y,(float)ps3.z);
	}	
}
