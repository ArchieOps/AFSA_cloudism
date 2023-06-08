package fishSwarmAlgo;

import java.io.IOException;

public class mainTest {
	/**
	 * @param args
	 * @throws IOException
	 * @author sun 
	 */
	public static void main(String[] args) throws IOException {
		//int fishNum, int tryTime, int dim, double step, double delta, double visual	
		System.out.println("begin");
//		Jumlah ikan, try time, dimensi, step ikan, delta (?), jarak pandang ikan
//		Set constant yang digunakan pada AFSA
		AFSA run = new AFSA(10,5,2,5,0.2,10);
  		run.doAFSA(10);//Jumlah iterasi yang dilakukan
	}

}
