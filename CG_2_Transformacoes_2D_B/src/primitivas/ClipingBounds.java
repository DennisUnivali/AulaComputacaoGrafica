package primitivas;

import java.awt.Graphics2D;

public class ClipingBounds {
	int x1 = 50;
	int y1 = 50;
	int x2 = 950;
	int y2 = 750;
	
	public void desenhaSe(Graphics2D g) {
		g.drawLine(x1, y1, x2, y1);
		g.drawLine(x1, y2, x2, y2);
		g.drawLine(x1, y1, x1, y2);
		g.drawLine(x2, y1, x2, y2);
	}
}
