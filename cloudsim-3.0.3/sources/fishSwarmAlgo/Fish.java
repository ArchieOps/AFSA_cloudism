package fishSwarmAlgo;

import java.io.IOException;

public class Fish {
	public int dim;                   //Dimensi ikan
	public int[] x;                //Koordinat titik ikan (x,y)
	public double fit;                //Nilai konsentrasi makanan dari ikan tersebut
	public int visual;             //Jarak pandang dari ikan
	public final double[] H = new double[256];
	public final double[] W = new double[256];
	

	public Fish(int dim, int visual) throws IOException {
		super();
		this.dim = dim;
		this.visual = visual;
		x = new int[dim];
		for(int i=0;i<dim;i++)
			x[i] = (int) Math.floor(256*Math.random());
		fit = 0;
		//init();
	}
	/*getfit = newfunction(this.x[0],this.x[1]);*/
	
//	Mencri jarak eucledian antara dua ikan
	public double distance(Fish f)
	{
		double a = 0;
		for(int i=0;i<dim;i++)
		{
			if(this.x[i]-f.x[i]==0)
				a = 0.00001;
			else 
				a += (this.x[i]-f.x[i])*(this.x[i]-f.x[i]);
		}
		return Math.sqrt(a);
	}
	
//	Menghitung nilai fit function untuk ikannya
	public  double newfunction(int[] coordinate) throws IOException {          
	
		return -(coordinate[0]*coordinate[0]-160*coordinate[0]+640+coordinate[1]*coordinate[1]-260*coordinate[1]+16900);
		
	}
}
