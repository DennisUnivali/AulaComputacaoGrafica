package obj;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import obj.Vector2f;
import obj.Vector3f;
import primitivas3d.Mat4x4;
import primitivas3d.Triangulo3d;
import primitivas3d.Vetor3D;

public class ObjModel {
	public ArrayList<Vector3f> v;
	public ArrayList<Vector3f> vn;
	public ArrayList<Vector2f> vt;
	public ArrayList<Face3D> f;
	public ArrayList<GrupoFaces> g;
	public ConcurrentHashMap<String, GrupoFaces> gname;

	public ObjModel() {
		// TODO Auto-generated constructor stubn
		v = new ArrayList<Vector3f>();
		vn = new ArrayList<Vector3f>();
		vt = new ArrayList<Vector2f>();
		f = new ArrayList<Face3D>();
		g = new ArrayList<GrupoFaces>();
		gname = new ConcurrentHashMap<String, GrupoFaces>();
	}

	public void loadObj(String file) {
		// System.out.println(" "+file);
		// InputStream in = this.getClass().getResourceAsStream(file);
		InputStream in;
		try {
			in = new FileInputStream(file);

			// System.out.println(""+in);

			BufferedReader dados = new BufferedReader(new InputStreamReader(in));

			String str;

			try {

				while ((str = dados.readLine()) != null) {
					if (str.length() > 0) {
						if (str.contains("#")) {
							continue;
						}
						if (str.contains("v ")) {
							decodeVertice(str);
						}
						if (str.contains("vn ")) {
							decodeVerticeNormal(str);
						}
						if (str.contains("vt ")) {
							decodeTextureMapping(str);
						}
						if (str.contains("f ")) {
							decodeFace(str);
						}
						if (str.contains("g ")) {
							decodeGrupo(str);
						}
					}
				}

				if (g.size() > 0) {
					g.get(g.size() - 1).ffinal = f.size() - 1;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			//System.out.println(" v " + v.size() + " vn " + vn.size() + " vt " + vt.size());

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void decodeVertice(String str) {
		String s[] = str.split(" ");
		// System.out.print("V ");

		Vector3f vec = new Vector3f();

		int index = 0;
		for (int i = 0; i < s.length; i++) {
			// System.out.print(" s["+i+"] = "+s[i]);
			try {
				float numero = Float.parseFloat(s[i]);
				// System.out.print(" OK");

				if (index == 0) {
					vec.x = numero;
				}
				if (index == 1) {
					vec.y = numero;
				}
				if (index == 2) {
					vec.z = numero;
				}
				index++;
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		// System.out.println();
		v.add(vec);
	}

	public void decodeVerticeNormal(String str) {
		String s[] = str.split(" ");
		// System.out.print("Vn ");

		Vector3f vec = new Vector3f();

		int index = 0;
		for (int i = 0; i < s.length; i++) {
			// System.out.print(" s["+i+"] = "+s[i]);
			try {
				float numero = Float.parseFloat(s[i]);
				// System.out.print(" OK");

				if (index == 0) {
					vec.x = numero;
				}
				if (index == 1) {
					vec.y = numero;
				}
				if (index == 2) {
					vec.z = numero;
				}
				index++;
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		// System.out.println();
		vn.add(vec);
	}

	public void decodeTextureMapping(String str) {
		String s[] = str.split(" ");
		// System.out.print("Vt ");

		Vector2f vec = new Vector2f();

		int index = 0;
		for (int i = 0; i < s.length; i++) {
			// System.out.print(" s["+i+"] = "+s[i]);
			try {
				float numero = Float.parseFloat(s[i]);
				// System.out.print(" OK");

				if (index == 1) {
					vec.x = numero;
				}
				if (index == 0) {
					vec.y = numero;
				}

				index++;
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		// System.out.println();
		vt.add(vec);
	}

	int valorestmp[] = new int[3];

	public void decodeFace(String str) {
		String s[] = str.split(" ");
		// System.out.print("f ");

		Face3D face = new Face3D();

		int index = 0;

		for (int i = 0; i < s.length; i++) {
			// System.out.print(" s["+i+"] = "+s[i]);

			if (s[i].contains("/")) {
				String s2[] = s[i].split("/");

				valorestmp[0] = -1;
				valorestmp[1] = -1;
				valorestmp[2] = -1;

				for (int j = 0; j < s2.length; j++) {
					try {
						int numero = Integer.parseInt(s2[j]);
						valorestmp[j] = numero;
					} catch (Exception e) {
					}
				}

				face.v[index] = valorestmp[0];
				face.n[index] = valorestmp[2];
				face.t[index] = valorestmp[1];
				//System.out.println(" v " + valorestmp[0] + " n " + valorestmp[1] + " t " + valorestmp[2]);

				index++;
			}

		}

		face.nvertices = (byte) index;
		// System.out.println();
		f.add(face);
	}

	public void decodeGrupo(String str) {
		//System.out.println("GRUPO    _           __________--" + str);
		String sttmp[] = str.split(" ");
		String nome = "";
		if (sttmp.length >= 2) {
			nome = sttmp[1];
		}

		GrupoFaces gp = new GrupoFaces();
		gp.nome = nome;

		gp.finicial = f.size();

		if (g.size() > 0) {
			g.get(g.size() - 1).ffinal = f.size() - 1;
		}

		g.add(gp);
		gname.put(nome, gp);
		//System.out.println("nome" + nome + "--");

	}

	public void desenhaSe(Graphics2D dbg, Mat4x4 m_modelview, Mat4x4 m_perspectiva) {

		Mat4x4 modelviewprojection = m_perspectiva.multiplicaMatrix(m_modelview);
		//Mat4x4 modelviewprojection = m_modelview.multiplicaMatrix(m_perspectiva);
		
		for (int i = 0; i < f.size(); i++) {
			Face3D face = f.get(i);
			Vector3f oldvertex = null;
			/*for (int j = 0; j < face.nvertices; j++) {
				Vector3f vtmp = v.get(face.v[j] - 1);
				if (oldvertex != null) {
					Vetor3D p1l = modelviewprojection
							.multiplicaVetor(new Vetor3D(oldvertex.x, oldvertex.y, oldvertex.z, 1.0));
					Vetor3D p2l = modelviewprojection.multiplicaVetor(new Vetor3D(vtmp.x, vtmp.y, vtmp.z, 1.0));

					canvas.desenhaLinhaDennis((int) p1l.x, (int) p1l.y, (int) p2l.x, (int) p2l.y);
				}
				oldvertex = vtmp;
			}*/
			//for (int j = 0; j < face.nvertices; j++) {
			if(face.nvertices==3) {
				Vector3f v1 = v.get(face.v[0]-1);
				Vector3f v2 = v.get(face.v[1]-1);
				Vector3f v3 = v.get(face.v[2]-1);
				Vetor3D p1l = modelviewprojection.multiplicaVetor(new Vetor3D(v1.x, v1.y, v1.z, 1.0));
				Vetor3D p2l = modelviewprojection.multiplicaVetor(new Vetor3D(v2.x, v2.y, v2.z, 1.0));
				Vetor3D p3l = modelviewprojection.multiplicaVetor(new Vetor3D(v3.x, v3.y, v3.z, 1.0));
				
				//System.out.println(p1l.toString());
				
				Vector3f vn1 = v.get(face.n[0]-1);
				Vector3f vn2 = v.get(face.n[1]-1);
				Vector3f vn3 = v.get(face.n[2]-1);
				
				Vector3f vn = new Vector3f(vn1.x, vn1.y, vn1.z);
				vn.Normalize();
				
				Vetor3D vnl = modelviewprojection.multiplicaVetor(new Vetor3D(vn.x, vn.y, vn.z, 1.0));
				
				Vetor3D vzero = modelviewprojection.multiplicaVetor(new Vetor3D(0, 0, 0, 1.0));
				Vector3f vnll = new Vector3f((float)(vnl.x-vzero.x),(float)(vnl.y-vzero.y),(float)(vnl.z-vzero.z));
				vnll.Normalize();
				
				Vector3f vluz = new Vector3f(0,-1,0);
				float fluz = vluz.Dot(vnll);
				
				Vector3f camera = new Vector3f(0,0,-1);
				float fcamera = camera.Dot(vnll);
				
				int r = Math.min((int)(100+fluz*50), 255);
				
				//System.out.println(""+fluz);
//				if(fcamera<0) {
					dbg.setColor(new Color(r,r,r,255));
					Triangulo3d.desenhaStaticTriangle(dbg, p1l,p2l,p3l);
//				}
			}else if(face.nvertices==4) {
				Vector3f v1 = v.get(face.v[0]-1);
				Vector3f v2 = v.get(face.v[1]-1);
				Vector3f v3 = v.get(face.v[2]-1);
				Vector3f v4 = v.get(face.v[3]-1);
				Vetor3D p1l = modelviewprojection.multiplicaVetor(new Vetor3D(v1.x, v1.y, v1.z, 1.0));
				Vetor3D p2l = modelviewprojection.multiplicaVetor(new Vetor3D(v2.x, v2.y, v2.z, 1.0));
				Vetor3D p3l = modelviewprojection.multiplicaVetor(new Vetor3D(v3.x, v3.y, v3.z, 1.0));
				Vetor3D p4l = modelviewprojection.multiplicaVetor(new Vetor3D(v4.x, v4.y, v4.z, 1.0));
				
				int valorR = (int)Math.min(Math.abs(p1l.z)*0.2,255);
				
				Vector3f vn1 = v.get(face.n[0]-1);
				Vector3f vn2 = v.get(face.n[1]-1);
				Vector3f vn3 = v.get(face.n[2]-1);
				
				Vector3f vn = new Vector3f(vn1.x+vn2.x+vn3.x, vn1.y+vn2.y+vn3.y, vn1.z+vn2.z+vn3.z);
				vn.Normalize();
				
				Vetor3D vnl = modelviewprojection.multiplicaVetor(new Vetor3D(vn.x, vn.y, vn.z, 1.0));
				
				Vetor3D vzero = modelviewprojection.multiplicaVetor(new Vetor3D(0, 0, 0, 1.0));

				
				Vector3f vluz = new Vector3f(0,-1,0);
				Vector3f vnll = new Vector3f((float)(vnl.x-vzero.x),(float)(vnl.y-vzero.y),(float)(vnl.z-vzero.z));
				vnll.Normalize();
				float fluz = vluz.Dot(vnll);
				
				Vector3f camera = new Vector3f(0,0,-1);
				float fcamera = camera.Dot(vnll);
				
				//System.out.println(""+fluz);
				int r = Math.min((int)(100+fluz*50), 255);
				
//				if(fcamera<0) {
					//dbg.setColor(new Color(valorR,0,0,255));
					dbg.setColor(new Color(r,r,r,255));
					Triangulo3d.desenhaStaticTriangle(dbg, p1l,p2l,p3l);
					Triangulo3d.desenhaStaticTriangle(dbg, p3l,p4l,p1l);
//				}
			}
		}

		/*
		 * glPushMatrix ();
		 * 
		 * 
		 * for(int i = 0; i < f.size(); i++){ Face3D face = f.get(i);
		 * 
		 * 
		 * glBegin (GL_POLYGON); //System.out.println("nvertices  "+face.nvertices);
		 * for(int j = 0; j < face.nvertices;j++){ //System.out.println(" "+j); Vector3f
		 * vntmp = vn.get(face.n[j]-1); glNormal3d(vntmp.x, vntmp.y, vntmp.z); Vector2f
		 * vttmp = vt.get(face.t[j]-1); glTexCoord2f (vttmp.y,1.0f - vttmp.x); Vector3f
		 * vtmp = v.get(face.v[j]-1); glVertex3f (vtmp.x, vtmp.y, vtmp.z);
		 * 
		 * //System.out.println(" "+vtmp.x+" "+vtmp.y+" "+vtmp.z); }
		 * 
		 * glEnd (); }
		 * 
		 * 
		 * glPopMatrix();
		 * 
		 * 
		 * }
		 * 
		 * public void desenhaseGrupo(int indexgrupo){
		 * 
		 * glPushMatrix ();
		 * 
		 * int inicial = g.get(indexgrupo).finicial; int ffinal =
		 * g.get(indexgrupo).ffinal;
		 * 
		 * for(int i = inicial; i < ffinal; i++){ Face3D face = f.get(i);
		 * 
		 * 
		 * glBegin (GL_POLYGON); //System.out.println("nvertices  "+face.nvertices);
		 * for(int j = 0; j < face.nvertices;j++){ //System.out.println(" "+j); Vector3f
		 * vntmp = vn.get(face.n[j]-1); glNormal3d(vntmp.x, vntmp.y, vntmp.z); Vector2f
		 * vttmp = vt.get(face.t[j]-1); glTexCoord2f (vttmp.y,1.0f - vttmp.x); Vector3f
		 * vtmp = v.get(face.v[j]-1); glVertex3f (vtmp.x, vtmp.y, vtmp.z);
		 * 
		 * //System.out.println(" "+vtmp.x+" "+vtmp.y+" "+vtmp.z); }
		 * 
		 * glEnd (); }
		 * 
		 * 
		 * glPopMatrix();
		 */

	}

	public void desenhaseGrupo(String nome) {

		/*
		 * glPushMatrix ();
		 * 
		 * GrupoFaces gp = null; if(gname.containsKey(nome)==false){
		 * System.out.println(" nao rolo"); return; }
		 * 
		 * gp = gname.get(nome);
		 * 
		 * int inicial = gp.finicial; int ffinal = gp.ffinal;
		 * 
		 * for(int i = inicial; i < ffinal; i++){ Face3D face = f.get(i);
		 * 
		 * 
		 * glBegin (GL_POLYGON); //System.out.println("nvertices  "+face.nvertices);
		 * for(int j = 0; j < face.nvertices;j++){ //System.out.println(" "+j); Vector3f
		 * vntmp = vn.get(face.n[j]-1); glNormal3d(vntmp.x, vntmp.y, vntmp.z); Vector2f
		 * vttmp = vt.get(face.t[j]-1); glTexCoord2f (vttmp.y,1.0f - vttmp.x); Vector3f
		 * vtmp = v.get(face.v[j]-1); glVertex3f (vtmp.x, vtmp.y, vtmp.z);
		 * 
		 * //System.out.println(" "+vtmp.x+" "+vtmp.y+" "+vtmp.z); }
		 * 
		 * glEnd (); }
		 * 
		 * 
		 * glPopMatrix();
		 */

	}

}
