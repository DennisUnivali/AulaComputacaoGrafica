package obj;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Math3D.Matriz4x4;
import Math3D.Vetor3D;
import game.Constantes;
import shaders.ShaderProgram;

public class Personagem extends Object3D {
	Sphere sphere = new Sphere();
	
	ObjModel model = null;
	float vel = 2.0f;
	
	float rotxAngle = 0;
	float rotyAngle = 0;
	float rotzAngle = 0;
	
	Vector3f frente;
	
	float raio = 0.4f;
	FloatBuffer matrixBuffer = MemoryUtil.memAllocFloat(16);

	public Personagem(float x, float y, float z,ObjModel model) {
		super(x, y, z);
		this.model = model;
		frente = new Vector3f(0,0,1);
	}
	
	@Override
	public void SimulaSe(long diftime) {
		float oldx = x;
		float oldy = y;
		float oldz = z;
		
		x += frente.x*vel*diftime/1000.0f;
		y += frente.y*vel*diftime/1000.0f;
		z += frente.z*vel*diftime/1000.0f;
		
		//Testar Colisão
		
		for(int i = 0; i < Constantes.listaDeObjetos.size();i++) {
			Object3D obj = Constantes.listaDeObjetos.get(i);
			float dx = obj.x - x;
			float dy = obj.y - y;
			float dz = obj.z - z;
			
			float somaraio = raio+obj.raio;
			if(somaraio*somaraio>(dx*dx+dy*dy+dz*dz)) {
				//COLIDIU
				x = oldx;
				y = oldy;
				z = oldz;
				
				obj.vx = frente.x*vel*1.5f;
				obj.vy = frente.y*vel*1.5f;
				obj.vz = frente.z*vel*1.5f;
				
				continue;
			}
			
		}
		
	}
	
	@Override
	public void DesenhaSe(ShaderProgram shader) {
//		   glPushMatrix();
//		   
//		   glTranslatef(x, y, z);
//		   glRotatef(rotxAngle, 1.0f, 0.0f, 0.0f);
//		   glRotatef(rotyAngle, 0.0f, 1.0f, 0.0f);
//		   glRotatef(rotzAngle, 0.0f, 0.0f, 1.0f);
//		  
//
//		   glColor3f(1.0f,0.0f ,0.0f );
//		   sphere.draw(raio, 16, 16);
//		   
//		   glScalef(0.01f, 0.01f, 0.01f);
		
			Matrix4f modelm = new Matrix4f();
			modelm.setIdentity();
			
			System.out.println(""+x+" "+y+" "+z);
			modelm.translate(new Vector3f(x,y,z));
			
			modelm.rotate(rotxAngle, new Vector3f(1.0f, 0.0f, 0.0f));
			modelm.rotate(rotyAngle, new Vector3f(0.0f, 1.0f, 0.0f));
			modelm.rotate(rotzAngle, new Vector3f(0.0f, 0.0f, 1.0f));
			modelm.scale(new Vector3f(0.005f,0.005f,0.005f));
			
			
			
			int modellocation = glGetUniformLocation(shader.programID, "model");
			
			modelm.storeTranspose(matrixBuffer);
			matrixBuffer.flip();
			glUniformMatrix4fv(modellocation, false, matrixBuffer);	
		   		   
		    model.draw();//desenhaOPENGL();
		   
		   //glPopMatrix();
	}
	
	public void setRotY(float ang) {
		rotyAngle+=ang;
		
		Matriz4x4 mat = new Matriz4x4();
		mat.setIdentity();
		mat.setRotateY(ang);
		Vetor3D vec = mat.multiplicaVetor(new Vetor3D(frente.x, frente.y, frente.z, 1.0f));
		frente = new Vector3f((float)vec.x,(float)vec.y,(float)vec.z);
		

	}

}
