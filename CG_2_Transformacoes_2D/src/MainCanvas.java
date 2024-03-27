import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import primitivas.Linha2D;
import primitivas.Mat3x3;
import primitivas.Ponto2D;

public class MainCanvas extends JPanel implements Runnable{
	int W = 1000;
	int H = 800;
	
	Thread runner;
	boolean ativo = true;
	int paintcounter = 0;
	
	BufferedImage imageBuffer;
	byte bufferDeVideo[];
	
	Random rand = new Random();
	
	byte memoriaPlacaVideo[];
	short paleta[][];
	
	int framecount = 0;
	int fps = 0;
	
	Font f = new Font("", Font.PLAIN, 30);
	
	int clickX = 0;
	int clickY = 0;
	int mouseX = 0;
	int mouseY = 0;
	
	int pixelSize = 0;
	int Largura = 0;
	int Altura = 0;
	
	BufferedImage imgtmp = null;
	
	float posx = 00;
	float posy = 00;
	
	boolean LEFT = false;
	boolean RIGHT = false;
	boolean UP = false;
	boolean DOWN = false;
	
	Linha2D l1;
	Linha2D l2;
	Linha2D l3;
	
	Ponto2D p1;
	Ponto2D p2;
	Ponto2D p3;
	
	ArrayList<Ponto2D> listaDePontos = new ArrayList<>();
	ArrayList<Linha2D> listaDeLinhas = new ArrayList<>();
	
	Ponto2D pclik1 = null;
	Ponto2D pclik2 = null;
	
	public MainCanvas() {
		setSize(W,H);
		setFocusable(true);
		
		Largura = W;
		Altura = H;
		
		pixelSize = W*H;
		
		
		imgtmp = loadImage("fundo.jpg");
		
		imageBuffer = new BufferedImage(W,H, BufferedImage.TYPE_4BYTE_ABGR);
		//imageBuffer.getGraphics().drawImage(imgtmp, 0, 0, null);
		
		
		bufferDeVideo = ((DataBufferByte)imageBuffer.getRaster().getDataBuffer()).getData();
		
		System.out.println("Buffer SIZE "+bufferDeVideo.length );
		
//		p1 = new Ponto2D(100, 100, 1);
//		p2 = new Ponto2D(500, 100, 1);
//		p3 = new Ponto2D(300, 400, 1);
//		
//		l1 = new Linha2D(p1,p2);
//		l2 = new Linha2D(p2,p3);
//		l3 = new Linha2D(p3,p1);
		
		p1 = new Ponto2D(50, 50, 1);
		p2 = new Ponto2D(250, 50, 1);
		p3 = new Ponto2D(150, 200, 1);
		
		listaDePontos.add(p1);
		listaDePontos.add(p2);
		listaDePontos.add(p3);
		
		l1 = new Linha2D(p1,p2);
		l2 = new Linha2D(p2,p3);
		l3 = new Linha2D(p3,p1);
		
		listaDeLinhas.add(l1);
		listaDeLinhas.add(l2);
		listaDeLinhas.add(l3);

		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_W) {
					UP = false;
				}
				if(key == KeyEvent.VK_S) {
					DOWN = false;
				}
				if(key == KeyEvent.VK_A) {
					LEFT = false;
				}
				if(key == KeyEvent.VK_D) {
					RIGHT = false;
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				//System.out.println("CLICO "+key);
				if(key == KeyEvent.VK_W) {
					Mat3x3 mat = new Mat3x3();
					mat.setTranslate(0, 50);
					for(int i = 0;i < listaDePontos.size();i++) {
						listaDePontos.get(i).multiplicaMat(mat);
					}
				}
				if(key == KeyEvent.VK_S) {
					Mat3x3 mat = new Mat3x3();
					mat.setTranslate(0,-50);
					for(int i = 0;i < listaDePontos.size();i++) {
						listaDePontos.get(i).multiplicaMat(mat);
					}
				}
				if(key == KeyEvent.VK_A) {
					Mat3x3 mat = new Mat3x3();
					mat.setTranslate(50, 0);
					for(int i = 0;i < listaDePontos.size();i++) {
						listaDePontos.get(i).multiplicaMat(mat);
					}
				}
				if(key == KeyEvent.VK_D) {
					Mat3x3 mat = new Mat3x3();
					mat.setTranslate(-50, 0);
					for(int i = 0;i < listaDePontos.size();i++) {
						listaDePontos.get(i).multiplicaMat(mat);
					}
				}
				if(key == KeyEvent.VK_Z) {
					Mat3x3 mat = new Mat3x3();
					mat.setTranslate(-500, -400);
					for(int i = 0;i < listaDePontos.size();i++) {
						listaDePontos.get(i).multiplicaMat(mat);
					}
					mat.setSacale(1.1,1.1);
					for(int i = 0;i < listaDePontos.size();i++) {
						listaDePontos.get(i).multiplicaMat(mat);
					}
					mat.setTranslate(500, 400);
					for(int i = 0;i < listaDePontos.size();i++) {
						listaDePontos.get(i).multiplicaMat(mat);
					}
				}
				if(key == KeyEvent.VK_X) {
					Mat3x3 mat = new Mat3x3();
					mat.setTranslate(-500, -400);
					for(int i = 0;i < listaDePontos.size();i++) {
						listaDePontos.get(i).multiplicaMat(mat);
					}
					mat.setSacale(0.9,0.9);
					for(int i = 0;i < listaDePontos.size();i++) {
						listaDePontos.get(i).multiplicaMat(mat);
					}
					mat.setTranslate(500, 400);
					for(int i = 0;i < listaDePontos.size();i++) {
						listaDePontos.get(i).multiplicaMat(mat);
					}
				}
				if(key == KeyEvent.VK_F1) {
					try {
						BufferedWriter bfr = new BufferedWriter(new FileWriter("MeuDenhoLindo.txt"));
						for(int i = 0; i < listaDeLinhas.size();i++) {
							Linha2D l = listaDeLinhas.get(i);
							bfr.write(""+l.a.x+";"+l.a.y+";"+l.b.x+";"+l.b.y+";\n");
						}
						bfr.close();
						System.out.println("Salvou");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if(key == KeyEvent.VK_F2) {
					listaDeLinhas.clear();
					listaDePontos.clear();
					try {
						BufferedReader bfr = new BufferedReader(new FileReader("MeuDenhoLindo.txt"));
						String line = "";
						while((line = bfr.readLine())!=null) {
							String split[] = line.split(";");
							double x1 = Double.parseDouble(split[0]);
							double y1 = Double.parseDouble(split[1]);
							double x2 = Double.parseDouble(split[2]);
							double y2 = Double.parseDouble(split[3]);
							
							Ponto2D p1 = new Ponto2D(x1, y1, 1);
							Ponto2D p2 = new Ponto2D(x2, y2, 1);
							
							listaDePontos.add(p1);
							listaDePontos.add(p2);
							
							Linha2D l = new Linha2D(p1, p2);
							
							listaDeLinhas.add(l);
						}
						bfr.close();
						System.out.println("carregou");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});		
		
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				clickX = e.getX();
				clickY = e.getY();
				
				//System.out.println("CLICO ");
				if(pclik1==null) {
					pclik1 = new Ponto2D(clickX, clickY, 1);
				}else {
					pclik2 = new Ponto2D(clickX, clickY, 1);
					listaDePontos.add(pclik1);
					listaDePontos.add(pclik2);
					listaDeLinhas.add(new Linha2D(pclik1, pclik2));
					pclik1 = null;
					pclik2 = null;
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub
				mouseX = arg0.getX();
				mouseY = arg0.getY();
			}
			
			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		

		
	}
	private void drawImageToBuffer(BufferedImage image,int x,int y, float fr, float fg, float fb) {
		byte[] imgBuffer = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
		
		
		int iw = image.getWidth();
		int ih = image.getHeight();
		
		for(int yi = 0; yi < ih; yi++) {
			for(int xi = 0; xi < iw; xi++) {
				int pixi = yi*iw*4 + xi*4;
				int pixb = (yi+y)*W*4 + (xi+x)*4;
				bufferDeVideo[pixb] = imgBuffer[pixi];	
				
				int b = (imgBuffer[pixi+1]&0xff);
				int g =	(imgBuffer[pixi+2]&0xff);
				int r = (imgBuffer[pixi+3]&0xff);
				
				b = (int)(b*fb);
				g = (int)(g*fg);
				r = (int)(r*fr);
				
				b = Math.min(255, b);
				g = Math.min(255, g);
				r = Math.min(255, r);
				
				bufferDeVideo[pixb+1] = (byte)(b&0xff);
				bufferDeVideo[pixb+2] = (byte)(g&0xff);
				bufferDeVideo[pixb+3] = (byte)(r&0xff);
			}
		}
	}
	@Override
	public void paint(Graphics g) {
		
		for(int i = 0; i < bufferDeVideo.length; i++) {
			bufferDeVideo[i] = 0;
		}
		
		g.setColor(Color.white);
		g.fillRect(0, 0, W, H);

		g.drawImage(imageBuffer,0,0,null);
		
		g.setColor(Color.black);
		
		for(int i = 0;i < listaDeLinhas.size();i++) {
			listaDeLinhas.get(i).desenhase((Graphics2D)g);
		}
		
		g.setColor(Color.green);
		if(pclik1!=null) {
			g.drawLine((int)pclik1.x,(int)pclik1.y, mouseX, mouseY);
		}
		
		g.setColor(Color.black);
		g.drawString("FPS "+fps, 10, 25);
	}
	
	public void desenhaLinhaHorizontal(int x, int y,int w) {
		int pospix = y*(W*4)+x*4;
		
		for(int i = 0; i < w;i++) {
			
			bufferDeVideo[pospix] = (byte)255;
			bufferDeVideo[pospix+1] = (byte)0;
			bufferDeVideo[pospix+2] = (byte)0;
			bufferDeVideo[pospix+3] = (byte)0;
			pospix+=4;
		}
	}
	
	public void desenhaLinhaVertical(int x, int y,int h) {
		int pospix = y*(W*4)+x*4;
		
		for(int i = 0; i < h;i++) {
			
			bufferDeVideo[pospix] = (byte)255;
			bufferDeVideo[pospix+1] = (byte)0;
			bufferDeVideo[pospix+2] = (byte)0;
			bufferDeVideo[pospix+3] = (byte)255;
			pospix+=(W*4);
		}
	}
	
	public void desenhaPixel(int x, int y,int r,int g,int b) {
		int pospix = y*(W*4)+x*4;
			
		bufferDeVideo[pospix] = (byte)255;
		bufferDeVideo[pospix+1] = (byte)(b&0xff);
		bufferDeVideo[pospix+2] = (byte)(g&0xff);
		bufferDeVideo[pospix+3] = (byte)(r&0xff);
	
	}
	
	public void start(){
		runner = new Thread(this);
		runner.start();
	}
	
	int timer = 0;
	public void simulaMundo(long diftime){
		
		float difS = diftime/1000.0f;
		float vel = 50;
		
		timer+=diftime;
		
		if(UP) {
			posy -= vel*difS;
		}
		if(DOWN) {
			posy += vel*difS;
		}
		if(LEFT) {
			posx -= vel*difS;
		}
		if(RIGHT) {
			posx += vel*difS;
		}
		
	}
	
	
	@Override
	public void run() {
		long time = System.currentTimeMillis();
		long segundo = time/1000;
		long diftime = 0;
		while(ativo){
			simulaMundo(diftime);
			paintImmediately(0, 0, W, H);
			paintcounter+=100;
			
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long newtime = System.currentTimeMillis();
			long novoSegundo = newtime/1000;
			diftime = System.currentTimeMillis() - time;
			time = System.currentTimeMillis();
			framecount++;
			if(novoSegundo!=segundo) {	
				fps = framecount;
				framecount = 0;
				segundo = novoSegundo;
			}
		}
	}
	
	public BufferedImage loadImage(String filename) {
		try {
			imgtmp = ImageIO.read(new File(filename));
			
			BufferedImage imgout = new BufferedImage(imgtmp.getWidth(), imgtmp.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			
			imgout.getGraphics().drawImage(imgtmp, 0, 0, null);
			
			imgtmp = null;
			
			return imgout;
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
	}
}
