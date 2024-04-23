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
	public void desenhase(Graphics2D g2d, ClipingBounds cliping) {
		if(cliping==null) {
			g2d.drawLine((int)a.x,(int)a.y,(int)b.x,(int)b.y);
		}else {
			int xl1 = (int)a.x;
			int yl1 = (int)a.y;
			int xl2 = (int)b.x;
			int yl2 = (int)b.y;

			if(yl1 < cliping.y1 && yl2 < cliping.y1) {
				return;
			}
			if(yl1 > cliping.y2 && yl2 > cliping.y2) {
				return;
			}
			if(xl1<cliping.x1&&xl2<cliping.x1) {
				return;
			}
			if(xl1>cliping.x2&&xl2>cliping.x2) {
				return;
			}
			
			if(xl1>xl2) {
				int t = xl1;
				xl1 = xl2;
				xl2 = t;
				t = yl1;
				yl1 = yl2;
				yl2 = t;
			}
			
			
			if((xl1-xl2)==0) {
				if(yl1>yl2) {
					int t = yl1;
					yl1 = yl2;
					yl2 = t;
				}
				
				if(yl1<cliping.y1) {
					yl1 = cliping.y1;
				}
				if(yl2>cliping.y2) {
					yl2 = cliping.y2;
				}
			}else if((yl1-yl2)==0){
				if(xl1>xl2) {
					int t = xl1;
					xl1 = xl2;
					xl2 = t;
				}
				if(xl1<cliping.x1) {
					xl1 = cliping.x1;
				}
				if(xl2>cliping.x2) {
					xl2 = cliping.x2;
				}
			}else {
				float m = (yl2-yl1)/(float)(xl2-xl1);

				if(xl1<cliping.x1) {
					
					int nxl1 = cliping.x1;
					int nyl1 = (int)getyofx(m,nxl1,xl1,yl1);
					
					xl1 = nxl1;
					yl1 = nyl1;
				}
				if(xl2>cliping.x2) {
					int nxl2 = cliping.x2;
					int nyl2 = (int)getyofx(m,nxl2,xl1,yl1);
					
					xl2 = nxl2;
					yl2 = nyl2;
				}
				
				
				
				if(yl1<cliping.y1) {
					int nyl1 = cliping.y1;
					int nxl1 = (int)getxofy(m,nyl1,xl1,yl1);
					
					xl1 = nxl1;
					yl1 = nyl1;
				}
				if(yl2<cliping.y1) {
					int nyl2 = cliping.y1;
					int nxl2 = (int)getxofy(m,nyl2,xl1,yl1);
					
					xl2 = nxl2;
					yl2 = nyl2;
				}
				
				if(yl1>cliping.y2) {
					int nyl1 = cliping.y2;
					int nxl1 = (int)getxofy(m,nyl1,xl1,yl1);

					System.out.println("M a "+m+" "+nxl1+" "+nyl1+" ["+xl1+" "+yl1+"]["+xl2+" "+yl2+"]");
					
					xl1 = nxl1;
					yl1 = nyl1;
					
					
				}
				if(yl2>cliping.y2) {
					int nyl2 = cliping.y2;
					int nxl2 = (int)getxofy(m,nyl2,xl1,yl1);
					
					System.out.println("M a "+m+" "+nxl2+" "+nyl2+" ["+xl1+" "+yl1+"]["+xl2+" "+yl2+"]");
					
					xl2 = nxl2;
					yl2 = nyl2;
					
				}				
			}
			
			
			
			g2d.drawLine(xl1,yl1,xl2,yl2);
		}
	}
	public float getyofx(float m,float x, float x1, float y1) {
		return ((x-x1)*m)+y1;
	}
	public float getxofy(float m,float y, float x1, float y1) {
		return ((y-y1)/m)+x1;
	}
	
	public void desenhase(Graphics2D g2d) {
		desenhase(g2d,null);
	}
	
	
}
