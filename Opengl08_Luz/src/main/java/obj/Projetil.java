package obj;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Model.Model;
import shaders.ShaderProgram;
import util.Utils3D;

public class Projetil extends Object3D {
	public Vector3f cor = new Vector3f();
	public Model model = null;
	
	FloatBuffer matrixBuffer = MemoryUtil.memAllocFloat(16);
	public float rotvel = 0;
	float ang = 0;
	
	long timervida = 0;
	
	public Projetil(float x, float y, float z) {
		super(x, y, z);
	}
	
	@Override
	public void DesenhaSe(ShaderProgram shader) {
		
		Matrix4f modelm = new Matrix4f();
		modelm.setIdentity();
		
		//System.out.println(""+x+" "+y+" "+z);
		
		modelm.translate(new Vector3f(x,y,z));
		modelm.rotate(ang,new Vector3f(0.0f,1.0f,0.0f));
		modelm.scale(new Vector3f(raio,raio,raio));
		
		int modellocation = glGetUniformLocation(shader.programID, "model");
		modelm.storeTranspose(matrixBuffer);
		matrixBuffer.flip();
		glUniformMatrix4fv(modellocation, false, matrixBuffer);	
		
		model.draw();
	}
	
	@Override
	public void SimulaSe(long diftime) {
		super.SimulaSe(diftime);
		
		timervida+=diftime;
		
		x += vx*diftime/1000.0f;
		y += vy*diftime/1000.0f;
		z += vz*diftime/1000.0f;
		
		if(timervida> 3000) {
			vivo = false;
		}
		
		//ang+=rotvel*diftime/1000.0f;
	}	

}
