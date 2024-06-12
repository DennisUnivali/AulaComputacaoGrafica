
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Model.VboCube;
import obj.Cubo3D;
import obj.ObjHTGsrtm;
import obj.ObjModel;
import obj.Object3D;
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
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main3D {

	// The window handle
	private long window;
	
	float viewAngX = 0;
	float viewAngY = 0;
	float scale = 1.0f;
	
	public Random rnd = new Random();
	
	VboCube vboc;
	StaticShader shader;
	ArrayList<Object3D> listaObjetos = new ArrayList<>();
	
	
	Vector4f cameraPos = new  Vector4f(0.0f,0.0f, 0.0f,1.0f);
	Vector4f cameraVectorFront = new Vector4f(0.0f, 0.0f, -1.0f,1.0f);
	Vector4f cameraVectorUP = new Vector4f(0.0f, 1.0f, 0.0f,1.0f);
	Vector4f cameraVectorRight = new Vector4f(1.0f, 0.0f, 0.0f,1.0f);
	
	Matrix4f view = new Matrix4f();
	
	boolean UP = false;
	boolean DOWN = false;
	boolean LEFT = false;
	boolean RIGHT = false;
	
	boolean FORWARD = false;
	boolean BACKWARD = false;
	
	boolean QBu = false;
	boolean EBu = false;
	
	Matrix4f cameraMatrix = new Matrix4f();
	
	int tmult;
	int tgato;
	FloatBuffer matrixBuffer = MemoryUtil.memAllocFloat(16);
	Cubo3D mapa;
	Cubo3D umcubo;

	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
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
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(1500, 1000, "Hello World!", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated
		// or released.
		
		
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		
			if(action == GLFW_PRESS) {
				if ( key == GLFW_KEY_W) {
					UP = true;
				}
				if ( key == GLFW_KEY_S) {
					DOWN = true;
				}
				if ( key == GLFW_KEY_A) {
					RIGHT = true;
				}
				if ( key == GLFW_KEY_D) {
					LEFT = true;
				}
				if ( key == GLFW_KEY_Q) {
					QBu = true;
				}
				if ( key == GLFW_KEY_E) {
					EBu = true;
				}
				if ( key == GLFW_KEY_UP) {
					FORWARD = true;
				}
				if ( key == GLFW_KEY_DOWN) {
					BACKWARD = true;
				}
			}
			if(action == GLFW_RELEASE) {
				if ( key == GLFW_KEY_W) {
					UP = false;
				}
				if ( key == GLFW_KEY_S) {
					DOWN = false;
				}
				if ( key == GLFW_KEY_A) {
					RIGHT = false;
				}
				if ( key == GLFW_KEY_D) {
					LEFT = false;
				}
				if ( key == GLFW_KEY_Q) {
					QBu = false;
				}
				if ( key == GLFW_KEY_E) {
					EBu = false;
				}
				if ( key == GLFW_KEY_UP) {
					FORWARD = false;
				}
				if ( key == GLFW_KEY_DOWN) {
					BACKWARD = false;
				}				
			}
		});

		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
	}

	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.

		GL.createCapabilities();

		view.setIdentity();
		
		vboc = new VboCube();
		vboc.load();
		shader = new StaticShader();
		
		//Cubo3D cubo = new Cubo3D(0.0f, 0.0f, -1.0f, 0.2f);
		//cubo.vbocube = vboc;
		
		ObjModel x35 = new ObjModel();
		x35.loadObj("x-35_obj.obj");
		x35.load();
		
		ObjHTGsrtm model = new ObjHTGsrtm();
		model.load();
		
		mapa = new Cubo3D(0.0f, 0.0f, 0.0f, 1);
		mapa.model = model;
		
		umcubo = new Cubo3D(0.0f, 0.0f, 0.8f, 0.1f);
		umcubo.model = vboc;
		
		for(int i = 0; i < 100; i++) {
			//Cubo3D cubo = new Cubo3D(rnd.nextFloat()*2-1,rnd.nextFloat()*2-1, rnd.nextFloat()*2-1, rnd.nextFloat()*0.005f+0.0001f);
			//cubo.model = x35;
			Cubo3D cubo = new Cubo3D(rnd.nextFloat()*2-1,rnd.nextFloat()*2-1, rnd.nextFloat()*2-1, rnd.nextFloat()*0.1f+0.05f);
			cubo.model = vboc;
			cubo.vx = rnd.nextFloat()*0.4f-0.2f;
			cubo.vy = rnd.nextFloat()*0.4f-0.2f;
			cubo.vz = rnd.nextFloat()*0.4f-0.2f;
			cubo.rotvel = rnd.nextFloat()*9;
			listaObjetos.add(cubo);
		}
		
		//BufferedImage gatorgba = new BufferedImage(imggato.getWidth(), imggato.getHeight(), BufferedImage.TYPE_INT_ARGB);
		//gatorgba.getGraphics().drawImage(imggato, 0, 0, null);
		
		BufferedImage imggato = TextureLoader.loadImage("texturaGato.jpeg");
		tgato = TextureLoader.loadTexture(imggato);
		System.out.println("tgato "+tgato);
		
		BufferedImage imgmulttexture = TextureLoader.loadImage("multtexture.png");
		tmult = TextureLoader.loadTexture(imgmulttexture);
		System.out.println("tmult "+tmult);

		int frame = 0;
		long lasttime = System.currentTimeMillis();

		float angle = 0;
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(45, 600f / 800f, 0.5f, 100);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		
		long ultimoTempo = System.currentTimeMillis();
		while (!glfwWindowShouldClose(window)) {
			
			long diftime = System.currentTimeMillis()-ultimoTempo;
			ultimoTempo = System.currentTimeMillis();
			
			gameUpdate(diftime);
			gameRender();
			
			
			frame++;
			long actualTime = System.currentTimeMillis();
			if ((lasttime / 1000) != (actualTime / 1000)) {
				System.out.println("FPS " + frame);
				frame = 0;
				lasttime = actualTime;
			}

		}
	}

	private void gameUpdate(long diftime) {
		float vel = 1.0f;
		
		if(FORWARD) {
			cameraPos.x -= cameraVectorFront.x*vel*diftime/1000.0f;
			cameraPos.y -= cameraVectorFront.y*vel*diftime/1000.0f;
			cameraPos.z -= cameraVectorFront.z*vel*diftime/1000.0f;
			//System.out.println("UP "+diftime);
		}
		if(BACKWARD) {
			cameraPos.x += cameraVectorFront.x*vel*diftime/1000.0f;
			cameraPos.y += cameraVectorFront.y*vel*diftime/1000.0f;
			cameraPos.z += cameraVectorFront.z*vel*diftime/1000.0f;
			//System.out.println("UP "+diftime);
		}
//		
//		if(RIGHT) {
//			cameraPos.x += cameraVectorRight.x*vel*diftime/1000.0f;
//			cameraPos.y += cameraVectorRight.y*vel*diftime/1000.0f;
//			cameraPos.z += cameraVectorRight.z*vel*diftime/1000.0f;
//			//System.out.println("UP "+diftime);
//		}
//		if(LEFT) {
//			cameraPos.x -= cameraVectorRight.x*vel*diftime/1000.0f;
//			cameraPos.y -= cameraVectorRight.y*vel*diftime/1000.0f;
//			cameraPos.z -= cameraVectorRight.z*vel*diftime/1000.0f;
//			//System.out.println("UP "+diftime);
//		}	
		
		Matrix4f rotTmp = new Matrix4f();
		rotTmp.setIdentity();
		if(RIGHT) {
			rotTmp.rotate(1.0f*diftime/1000.0f, new Vector3f(cameraVectorUP.x, cameraVectorUP.y, cameraVectorUP.z));
		}
		if(LEFT) {
			rotTmp.rotate(-1.0f*diftime/1000.0f, new Vector3f(cameraVectorUP.x, cameraVectorUP.y, cameraVectorUP.z));
		}
		if(UP) {
			rotTmp.rotate(-1.0f*diftime/1000.0f, new Vector3f(cameraVectorRight.x, cameraVectorRight.y, cameraVectorRight.z));
		}
		if(DOWN) {
			rotTmp.rotate(1.0f*diftime/1000.0f, new Vector3f(cameraVectorRight.x, cameraVectorRight.y, cameraVectorRight.z));
		}
		
		rotTmp.transform(rotTmp,cameraVectorFront, cameraVectorFront);
		rotTmp.transform(rotTmp,cameraVectorRight, cameraVectorRight);
		rotTmp.transform(rotTmp,cameraVectorUP, cameraVectorUP);
		
		vec3dNormilize(cameraVectorFront);
		vec3dNormilize(cameraVectorRight);
		vec3dNormilize(cameraVectorUP);
		
		Vector4f t = new Vector4f(cameraPos.dot(cameraPos, cameraVectorRight),cameraPos.dot(cameraPos, cameraVectorUP),cameraPos.dot(cameraPos, cameraVectorFront),1.0f);
		
		view = setLookAtMatrix(t, cameraVectorFront, cameraVectorUP, cameraVectorRight);
		//view.mul(view, cameraMatrix, view);
		//view.translate(new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z));
		
		
		
		for(int i = 0; i < listaObjetos.size();i++) {
			listaObjetos.get(i).SimulaSe(diftime);
		}
	}

	private void gameRender() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

		glEnable(GL_LIGHTING);
		glShadeModel(GL_SMOOTH);

		glLoadIdentity();

//		float[] lightAmbient = { 0.1f, 0.1f, 0.1f, 0.5f };
//		float[] lightDiffuse = { 0.5f, 0.5f, 0.5f, 0.5f };
//		float[] lightPosition = { 0.0f, 0.0f, 0.0f, 1.0f };
//		//float[] lightPosition = { 0.0f, senoang * 600f, cosang * 600f, 1.0f };
//
//		glLightfv(GL_LIGHT0, GL_AMBIENT, lightAmbient);
//		glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuse);
//		glLightfv(GL_LIGHT0, GL_POSITION, lightPosition);
//
//		float[] lightAmbient2 = { 0.0f, 0.0f, 0.0f, 1.0f };
//		float[] lightDiffuse2 = { 1.0f, 1.0f, 1.0f, 1.0f };
//		float[] lightPosition2 = { -5f, -5f, 0f, 1.0f };

		shader.start();
		
		int projectionlocation = glGetUniformLocation(shader.programID, "projection");
		//Matrix4f projection = setFrustum(-1f,1f,-1f,1f,1f,100.0f);
		Matrix4f projection = setFrustum(-0.1f,0.1f,-0.1f,0.1f,0.1f,100.0f);
		projection.storeTranspose(matrixBuffer);
		matrixBuffer.flip();
		glUniformMatrix4fv(projectionlocation, false, matrixBuffer);
		
		int lightpos = glGetUniformLocation(shader.programID, "lightPosition");
		float vf[] = {0.0f,20.0f,0.0f,1.0f};
		glUniform4fv(lightpos, vf);
		
		glEnable(GL_DEPTH_TEST);
		
		glActiveTexture(GL_TEXTURE0);
		//glBindTexture(GL_TEXTURE_2D, tgato);
		glBindTexture(GL_TEXTURE_2D, tmult);
		
		
		int loctexture = glGetUniformLocation(shader.programID, "tex");
		glUniform1i(loctexture, 0);
		
		//view.setIdentity();
		//view.scale(new Vector3f(scale,scale,scale));
		//view.rotate(viewAngX*0.0174532f, new Vector3f(1,0,0));
		//view.rotate(viewAngY*0.0174532f, new Vector3f(0,1,0));
		//view.translate(new Vector3f(0,0,0));
		
		int viewlocation = glGetUniformLocation(shader.programID, "view");
		view.storeTranspose(matrixBuffer);
		matrixBuffer.flip();
		glUniformMatrix4fv(viewlocation, false, matrixBuffer);
		
		mapa.DesenhaSe(shader);
		umcubo.DesenhaSe(shader);
		
//			for(int i = 0; i < listaObjetos.size();i++) {
//				listaObjetos.get(i).DesenhaSe(shader);
//			}
		
		
		shader.stop();
		
		glfwSwapBuffers(window); // swap the color buffers

		// Poll for window events. The key callback above will only be
		// invoked during this call.
		glfwPollEvents();
	}


	public static void main(String[] args) {
		new Main3D().run();
	}

	public static void gluPerspective(float fovy, float aspect, float near, float far) {
		float bottom = -near * (float) Math.tan(fovy / 2);
		float top = -bottom;
		float left = aspect * bottom;
		float right = -left;
		glFrustum(left, right, bottom, top, near, far);
	}
	
	public static Matrix4f setLookAtMatrix(Vector4f pos,Vector4f front,Vector4f up,Vector4f right) {
		Matrix4f m = new Matrix4f();
		m.m00 = right.x;
		m.m01 = up.x;
		m.m02 = front.x;
		m.m03 = 0.0f;
		
		m.m10 = right.y;
		m.m11 = up.y;
		m.m12 = front.y;
		m.m13 = 0.0f;
		
		m.m20 = right.z;
		m.m21 = up.z;
		m.m22 = front.z;
		m.m23 = 0.0f;
		
		m.m30 = -pos.x;
		m.m31 = -pos.y;
		m.m32 = -pos.z;
		m.m33 = 1.0f;		
		
		return m;
	}
	
	public static Matrix4f setLookAtMatrixB(Vector4f pos,Vector4f front,Vector4f up,Vector4f right) {
		Matrix4f m = new Matrix4f();
		m.m00 = right.x;
		m.m01 = right.y;
		m.m02 = right.z;
		m.m03 = pos.x;
		
		m.m10 = up.x;
		m.m11 = up.y;
		m.m12 = up.z;
		m.m13 = pos.y;
		
		m.m20 = front.x;
		m.m21 = front.y;
		m.m22 = front.z;
		m.m23 = pos.z;
		
		m.m30 = 0.0f;
		m.m31 = 0.0f;
		m.m32 = 0.0f;
		m.m33 = 1.0f;		
		
		return m;
	}	
	
	public static double vecMag(Vector4f v) {
		return Math.sqrt(v.x*v.x+v.y*v.y+v.z*v.z);
	}
	
	public static void vec3dNormilize(Vector4f v) {
		double mag = vecMag(v);
		v.setX((float)(v.x/mag));
		v.setY((float)(v.y/mag));
		v.setZ((float)(v.z/mag));
	}
	
//////////////////////////////////////////////////////////////////////////////
//equivalent to glFrustum()
//PARAMS: (left, right, bottom, top, near, far)
///////////////////////////////////////////////////////////////////////////////
	Matrix4f setFrustum(float l, float r, float b, float t, float n, float f)
	{
		Matrix4f m = new Matrix4f();
		m.m00 = 2 * n / (r - l);
		m.m01 = 0.0f;
		m.m02 = 0.0f;
		m.m03 = 0.0f;
		
		m.m10 = 0.0f;
		m.m11 = 2 * n / (t - b);
		m.m12 = 0.0f;
		m.m13 = 0.0f;
		
		m.m20 = (r + l) / (r - l);
		m.m21 = (t + b) / (t - b);
		m.m22 = -(f + n) / (f - n);
		m.m23 = -1;
		
		m.m30 = 0.0f;
		m.m31 = 0.0f;
		m.m32 = -(2 * f * n) / (f - n);
		m.m33 = 0;

		return m;
	}

}
