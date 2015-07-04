
/*********************************************************

   Point3D.java
   Description:

   Used within Java based 3D engine.

   Programmer: Ben Coe.


**********************************************************/
package View;
public class Point3D{
	//data.
	private float x;
	private float y;
	private float z;

	//Constructor.
	public Point3D(float cx,float cy,float cz)
	{
		x=cx;
		y=cy;
		z=cz;
	}

	//Cross product of twho Point3D.
	public Point3D cross(Point3D tempv)
	{
		Point3D v=new Point3D(tempv.x,tempv.y,tempv.z);
		Point3D u=new Point3D(x,y,z);

		u.x=(y*v.z)-(z*v.y);
		u.y=(z*v.x)-(x*v.z);
		u.z=(x*v.y)-(y*v.x);

		float norm=(float)Math.sqrt((u.x*u.x)+(u.y*u.y)+(u.z));

		return(u);
	}

	//Dot product between two Point3D.
	public float dot(Point3D tempv)
	{
		Point3D v=new Point3D(tempv.x,tempv.y,tempv.z);
		float f;
		Point3D u=new Point3D(x,y,z);
		f=(u.x*v.x)+(u.y*v.y)+(u.z*v.z);
		return(f);
	}

	//Subtract a Point3D.
	public Point3D sub(Point3D tempv){
		Point3D v=new Point3D(tempv.x,tempv.y,tempv.z);
		Point3D u=new Point3D(x,y,z);
		u.x=u.x-v.x;
		u.y=u.y-v.y;
		u.z=u.z-v.z;
		return(u);
	}

	//Add a Point3D.
	public Point3D add(Point3D tempv){
		Point3D v=new Point3D(tempv.x,tempv.y,tempv.z);
		Point3D u=new Point3D(x,y,z);
		u.x=u.x+v.x;
		u.y=u.y+v.y;
		u.z=u.z+v.z;
		return(u);
	}

	//Scale Point3D by float.
	public Point3D scale(float s){
		Point3D u=new Point3D(x,y,z);
		u.x=u.x*s;
		u.y=u.y*s;
		u.z=u.z*s;
		return(u);
	}

	//Get and set values.
	public float getX(){
		return(x);
	}
	public void setX(float v){
		x=v;
	}

	public float getY(){
		return(y);
	}

	public void setY(float v){
		y=v;
	}

	public float getZ(){
		return(z);
	}
	public void setZ(float v){
		z=v;
	}
}
