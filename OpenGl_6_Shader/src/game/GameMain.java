package game;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Model.VboCube;
import obj.Esfera3D;
import obj.ObjModel;
import obj.Personagem;
import shaders.ShaderProgram;
import shaders.StaticShader;
import util.TextureLoader;

import java.awt.image.BufferedImage;

//import com.sun.org.apache.xerces.internal.dom.DeepNodeListImpl;

import java.nio.*;
import java.util.ArrayList;
import java.util.Random;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;



public class GameMain {

	// The window handle
	private long window;
	ObjModel tankobj = null;

	Random rnd = new Random(); 

	
	int texturaDoGatinho = -1;
	int texturaDoDennis = -1;
	int windowW = 1200;
	int windowH = 1000;
	
	Personagem aviao;
	//AVIAO
//	float posX = 0;
//	float posY = -0.75f;
//	float posZ = -2.0f;
	
	float rotxAngle = 0;
	float rotyAngle = 100;
	float rotzAngle = 0;
	
	float vel = 0.5f;

	VboCube vbocube;
	
	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		vbocube = new VboCube();
		
		init();
		
		tankobj = new ObjModel();
		tankobj.loadObj("x-35_obj.obj");
		tankobj.createTextureBase("./x-35_obj.jpg", 1024, 1024);
		
		aviao = new Personagem(0, -0.75f, -2.0f, tankobj);
		
		
		
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(windowW, windowH, "Hello World!", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
			
			if ( key == GLFW_KEY_W) {
				//posZ-=0.1f;
			}
			if ( key == GLFW_KEY_S) {
//				posZ+=0.1f;
			}
			if ( key == GLFW_KEY_A) {
				aviao.setRotY(-0.1f);
			}
			if ( key == GLFW_KEY_D) {
				aviao.setRotY(0.1f);
			}
			if ( key == GLFW_KEY_Q) {
				rotxAngle+=15;
			}
			if ( key == GLFW_KEY_E) {
				rotxAngle-=15;
			}
			if ( key == GLFW_KEY_Z) {
				rotyAngle+=15;
			}
			if ( key == GLFW_KEY_X) {
				rotyAngle-=15;
			}
			if ( key == GLFW_KEY_N) {
				rotzAngle+=15;
			}
			if ( key == GLFW_KEY_M) {
				rotzAngle-=15;
			}
		});

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
		
		for(int i = 0; i < 20;i++) {
			for(int j = 0; j < 20;j++) {
				Esfera3D esf = new Esfera3D(i-10,rnd.nextFloat()*4-2,-j-4,rnd.nextFloat()*0.25f+0.01f);
				esf.cor = new Vector3f(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat());
				esf.vbocube = vbocube;
				
				Constantes.listaDeObjetos.add(esf);
			}
		}
		
		
	}

	long difTime = 0;
	long actualTime = 0;
	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
		
		ShaderProgram shader = new StaticShader();
		tankobj.load();
		vbocube.load();
		
		//BufferedImage bfm = TextureLoader.loadImage("gatinho01.jpg");
		BufferedImage bfm1 = TextureLoader.loadImage("gatinho01.jpg");
		texturaDoGatinho = TextureLoader.loadTexture(bfm1);
		BufferedImage bfm2 = TextureLoader.loadImage("x35_b.jpg");
		texturaDoDennis = TextureLoader.loadTexture(bfm2);

		// Set the clear color
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		//gluPerspective(45, windowH/(float)windowW,-1000.0f,1000f);
		float prop = windowH/(float)windowW;
		 glFrustum(-1f,1f,-1*prop,1*prop,1,1000);
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		
		int frame = 0;
		long lasttime  = System.currentTimeMillis();
		
		actualTime= System.currentTimeMillis();
		
		int angle = 0;
		FloatBuffer matrixBuffer = MemoryUtil.memAllocFloat(16);
		while ( !glfwWindowShouldClose(window) ) {
			
			aviao.SimulaSe(difTime);
			for(int i = 0; i < Constantes.listaDeObjetos.size();i++) {
				Constantes.listaDeObjetos.get(i).SimulaSe(difTime);
			}
			
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			
			
			glEnable(GL_LIGHTING);
			   glShadeModel(GL_SMOOTH);
			   
			   float senoang = (float)Math.sin(Math.toRadians(angle));
			   float cosang = (float)Math.cos(Math.toRadians(angle));

			   float[] lightAmbient = {0.1f, 0.1f, 0.1f, 1.0f}; 
		       float[] lightDiffuse = {0.1f, 0.1f, 0.1f, 1.0f};    
		       float[] lightPosition = {0.0f, senoang*600f, cosang*600f, 1.0f};
		       
		       glLightfv(GL_LIGHT0, GL_AMBIENT, lightAmbient);              
		       glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuse);            
		       glLightfv(GL_LIGHT0, GL_POSITION,lightPosition);
		       
			   float[] lightAmbient2 = {0.0f, 0.0f, 0.0f, 1.0f}; 
		       float[] lightDiffuse2 = {1.0f, 1.0f, 1.0f, 1.0f};    
		       float[] lightPosition2 = {-5f, -5f, 0f, 1.0f};
		       
		       glLightfv(GL_LIGHT1, GL_AMBIENT, lightAmbient2);              
		       glLightfv(GL_LIGHT1, GL_DIFFUSE, lightDiffuse2);            
		       glLightfv(GL_LIGHT1, GL_POSITION,lightPosition2);
		       
		       glEnable(GL_LIGHT0);		
		       //glEnable(GL_LIGHT1);	
			   glEnable(GL_COLOR_MATERIAL);
		       //glColorMaterial(GL_FRONT, GL_DIFFUSE);	
		       
		       glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);

		       //float[] mat_ambient = {0.1f, 0.1f, 0.1f, 1.0f};
		       //float[] mat_diffuse = { 0.1f, 0.1f, 0.1f, 1.0f };
		       //float[] mat_specular = { 0.0f, 0.0f, 0.0f, 0.0f };
		       //float[] mat_shininess = {0.0f,0,0,0 };

		       //glMaterialfv(GL_FRONT, GL_AMBIENT, mat_ambient);
		       //glMaterialfv(GL_FRONT, GL_DIFFUSE, mat_diffuse);
		       //glMaterialfv(GL_FRONT, GL_SPECULAR, mat_specular);
		       //glMaterialfv(GL_FRONT, GL_SHININESS, mat_shininess);
		       
		       //glEnable(GL_BLEND);
		    
		    shader.start();   

			angle++;
			glEnable(GL_DEPTH_TEST);
			
			
//			glLoadIdentity();
//			
//			glTranslated(-aviao.x,-(aviao.y+0.75f),-(aviao.z+2.0f));
			//GLU.gluLookAt(posX,posY+0.75f,posZ+2.0f,posX,posY+0.75f,posZ,0.0f,1.0f,0.0f);
			


		   //glEnable(GL_TEXTURE_2D);
		   //glBindTexture(GL_TEXTURE_2D, texturaDoDennis);
		   
		   
	        glActiveTexture(GL_TEXTURE0);
	        glBindTexture(GL_TEXTURE_2D, texturaDoGatinho);
	        
	        int loctexture = glGetUniformLocation(shader.programID, "tex");
	        glUniform1i(loctexture, 0);
			
	        //aviao.x = 0;
	        //aviao.y = 0;
	        //aviao.z = 0;
	        
			//Matrix4f projection = new Matrix4f();
			//projection.setIdentity();
			//projection.setPerspective((float) Math.PI / 2, (float) windowW / (float) windowH, 0.1f, 100f);
			
			//int viewlocation = glGetUniformLocation(shader.programID, "view");
			
			//view.storeTranspose(matrixBuffer);
			//matrixBuffer.flip();
			//glUniformMatrix4fv(viewlocation, false, matrixBuffer);	
			
	        
	        
			Matrix4f view = new Matrix4f();
			view.setIdentity();
			view.translate(new Vector3f(-aviao.x,-(aviao.y+0.75f),-(aviao.z+2.0f)));
			//view.translate(new Vector3f(0,0,0));
			
			int viewlocation = glGetUniformLocation(shader.programID, "view");
			
			view.storeTranspose(matrixBuffer);
			matrixBuffer.flip();
			glUniformMatrix4fv(viewlocation, false, matrixBuffer);	
		   
			for(int i = 0; i < Constantes.listaDeObjetos.size();i++) {
				Constantes.listaDeObjetos.get(i).DesenhaSe(shader);
			}
			
			aviao.DesenhaSe(shader);
		   //vbocube.draw();
		   
		   shader.stop();
		   
//		   glPushMatrix();
//		   
//		   glBindTexture(GL_TEXTURE_2D, texturaDoGatinho);
//		   glTranslated(0, 0.5, 0);
//		   glRotatef(90, 1.0f, 0.0f, 0.0f);
//		   glScalef(0.005f, 0.005f, 0.005f);   
//		   tankobj.desenhaOPENGL();
//		   
//		   glPopMatrix();
		   

			
			glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
			frame++;
			difTime = actualTime-System.currentTimeMillis();
			actualTime = System.currentTimeMillis();
			if((lasttime/1000)!=(actualTime/1000)) {
				System.out.println("FPS "+frame);
				frame=0;
				lasttime = actualTime;
			}
			
		}
	}

	public static void main(String[] args) {
		new GameMain().run();
	}
	
	public static void gluPerspective(float fovy, float aspect, float near, float far) {
	    float bottom = -near * (float) Math.tan(fovy / 2);
	    float top = -bottom;
	    float left = aspect * bottom;
	    float right = -left;
	    glFrustum(left, right, bottom, top, near, far);
	}

}

