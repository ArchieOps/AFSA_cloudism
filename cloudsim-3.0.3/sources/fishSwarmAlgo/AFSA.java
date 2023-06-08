package fishSwarmAlgo;

import java.io.IOException;
import java.util.Date;

public class AFSA {
//			Jumlah ikan
			private int fishNum;
//			Jumlah percobaan dilakukan
			private int tryTime;
//			Dimensi bidany yang digunakan
			private int dim;
//			Total step ikan
			private int step;
// 			Untuk menghitung nilai perbedaan antara fungsi Y 1 dengan fungsi Y 2
			private double delta;
//			Cakupan pandang ikan
			private int visual;
//			Kelompok ikan, ikan terbaiknya, dan ikan berikutnya dalam lintasan
			Fish[] fish;
//			Ikan terbaik
			Fish bestfish;
			
//			Kelompok nextfishnya
			Fish[] nextfish;
			int index;
			double[][] vector;
			private int[] choosed;
			
			//Jumlah ikan dalam jangkauan
			public int scopelength;

			public AFSA(int fishNum, int tryTime, int dim, int step, double delta, int visual) throws IOException {
				super();
				this.fishNum = fishNum;
				this.tryTime = tryTime;
				this.dim = dim;
				this.step = step;
				this.delta = delta;
				this.visual = visual;

//				Array of fish
				fish = new Fish[fishNum];
				nextfish = new Fish[3];
				vector = new double[fishNum][dim];
				choosed = new int[fishNum];
				index = 0;
				init();
			}
			
			public void doAFSA(int numberIter) throws IOException {  
				long startTime = new Date().getTime();
				double a = 0.0;
				int count = 1;             				//Jumlah pencarian yang dilakukan
				int len = 0;
				while(count<=numberIter) {
//				    i untuk manggil ikan sesuai indexnya. 
					for(int i=0; i<fishNum; i++) {
				    	 prey(i);
				         swarm(i);
				         follow(i);
				         bulletin(i);
				         System.out.println("Hasil iterasi ke " + count + ", untuk ikan ke "+i+ " adalah = " + bestfish.fit);
				     }	     
				     System.out.println(count+" Nilai optimal saat ini : ");
					 for(int i=0; i<dim; i++)
//					 1 = x; y = 2;
					 {
						 System.out.println("Lokasi"+(i+1)+":  "+bestfish.x[i]);  //Lokasi ikan terbaik saat ini
					 }
					 System.out.println();
					 count++;
//					 System.out.println("step:"+step+"    visual:"+visual);
				}
				
//				Setelah melakukan AFSA sebanyak n kali, printa hasilnya
				System.out.println("Nilai optimal keseluruhan : "+bestfish.fit);
				
				for(int i=0; i<dim; i++){
//					 1 = x; y = 2;
				    System.out.println("Lokasi"+(i+1)+":  "+bestfish.x[i]);  
				}
				long endTime = new Date().getTime();
				System.out.println("Program ini berjalan selama : "+(endTime-startTime)+" ms");
			}
				
//			Buat nentuin, aksi mana yang memiliki fit yang paling baik untuk state fish selanjutnya
			private void bulletin(int fishOrder) throws IOException {
				Fish maxfish = new Fish(dim,visual);
				maxfish = nextfish[0];
				for(int j=0; j<3; j++)
				{
					if(nextfish[j].fit>maxfish.fit && nextfish[j].x[0]!=0 && nextfish[j].x[1]!=0)
					{
						maxfish = nextfish[j];
					}
				}
				if(maxfish.fit<fish[fishOrder].fit)
				{
					return ;
				}
				fish[fishOrder] = maxfish;
				if(maxfish.fit>bestfish.fit)
					bestfish = maxfish;
			}
			
			
//			MinFish gw ubah jadi M
			private void follow(int fishOrder) throws IOException {
				nextfish[2] = new Fish(dim,visual);
				Fish maxfish = new Fish(dim,visual);                    // Ikan yang akan mungkin menfollow ikan lainnya
				maxfish = fish[fishOrder];
				Fish[] scope = getScopefish(fishOrder);
				int key = fishOrder;
//				Kalau ikan fishOrder punya tetangga ...
				if(scope!=null){
					for(int j=0;j<scope.length;j++) {
						if(scope[j].fit > maxfish.fit) {
							maxfish = scope[j];
							key = j;
						}
					}
					
//					--------------- INI SAYA UBAH ---------------
//					Kalau fit tetangganya kurang dari fishOrder, langsung jalankan prey. Kalau tidak, follow dulu ke sekelilingnya
					if(maxfish.fit<=fish[fishOrder].fit) // >= harusnya
						prey(fishOrder);
					else{
						Fish[] newScope = getScopefish(key);
						if(newScope!=null)
						{
//							Kalau fish yang di follow tersebut memiliki nilai fit yang masih kurang dari nilai delta fit fishOrder, maka fishOrder bergerak mendekati minFish
							if(newScope.length*maxfish.fit<delta*fish[fishOrder].fit) {
								double dis = fish[fishOrder].distance(maxfish);
								for(int k=0;k<dim;k++) {
									nextfish[2].x[k] = (int) (fish[fishOrder].x[k]+(maxfish.x[k]-fish[fishOrder].x[k])*step*Math.random()/dis);									
								}
								nextfish[2].fit = nextfish[2].newfunction(nextfish[2].x);
							}
							else prey(fishOrder);
						}
						else prey(fishOrder);
					}
				}
				else prey(fishOrder);
				
			}
			
//			Sifat Swarm
			private void swarm(int fishOrder) throws IOException {
				nextfish[1] = new Fish(dim,visual);
//				Variable untuk menyimpan titik pusat swarm
				int[] center = new int[dim];                    
				for(int j=0;j<dim;j++)
					center[j] = 0;
				Fish[] scope = getScopefish(fishOrder);
				if(scope!=null)
				{
					for(int j=0;j<scope.length;j++)                     // Titik tengah dari ikan ikan yang berkumpul, setelah ikan ditambahkan
					{
						for( int i=0; i<dim; ++i )
							center[i] += scope[j].x[i];
					}
					for( int i=0; i<dim; i++ )
						center[i] /= scope.length;                               // Nilai tengah dari ikan yang berkumpul
					
					double dis=0.0;
					Fish centerfish = new Fish(dim,visual);
					centerfish.x = center;
					centerfish.fit = centerfish.newfunction(centerfish.x);
					dis = fish[fishOrder].distance(centerfish);
//					Kalau di pusat swarm lebih baik, gerak ke arah centerfish
					if(centerfish.fit>fish[fishOrder].fit && scope.length*centerfish.fit<delta*fish[fishOrder].fit)
					{
						for(int j=0;j<dim;j++)
						{
							nextfish[1].x[j] = (int) (fish[fishOrder].x[j]+(centerfish.x[j]-fish[fishOrder].x[j])*step*Math.random()/dis);
							
						}
						nextfish[1].fit = nextfish[1].newfunction(nextfish[1].x);
					}
					else  prey(fishOrder); 
				}
				else  prey(fishOrder); 
			} 
// Swarm End
				
				
//			Prey Start
			private void prey(int fishOrder) throws IOException  {
			
				Fish newfish = new Fish(dim,visual);
				newfish.fit = 0;
				nextfish[0] = new Fish(dim,visual);
				for(int k=0; k<tryTime; k++ )           // 	Cek apakah ada konsentrasi makanan lebih tinggi, berdasarkan tryTime
				{
					for(int j=0; j<dim; j++ )
					{
						newfish.x[j] = (int) ((2*(Math.random())-1)*visual);
						
					}
					newfish.fit = newfish.newfunction(newfish.x);
					
//					Kalau ikan random saat ini fit nya lebih baik, ikan fishOrder bergerak ke posisi itu
					if( newfish.fit > fish[fishOrder].fit )
					{
						double dis = fish[fishOrder].distance(newfish);
						for(int j=0; j<dim; j++ )
						{
							
							nextfish[0].x[j] = (int) (fish[fishOrder].x[j]+(newfish.x[j]-fish[fishOrder].x[j])*step*Math.random()/dis);
								
						}
						nextfish[0].fit =nextfish[0].newfunction(nextfish[0].x); 
					}
					else
					{
//						Kalau gk ketemu nilai fit yang lebih baik, langsung di leap ke posisi random
						for(int j=0; j<dim; j++)
						{
							nextfish[0].x[j] = (int) (fish[fishOrder].x[j]+visual*(2*(Math.random())-1));
								
							nextfish[0].fit = nextfish[0].newfunction(nextfish[0].x);
			
						}
			
					}
				}
			}
						
//Prey  End
			
			
//			Mencari tetangga terdekat dari sebuah ikan
			private Fish[] getScopefish(int fishOrder) {
				int num = 0;
				for(int j=0; j<fishNum; j++)
				{
					choosed[j] = -1;
					if(fish[fishOrder].distance(fish[j])<visual)
					{
						choosed[j] = fishOrder;
						num++;
					}
				}
				if(num!=0){
					Fish[] scope = new Fish[num];
					int k = 0;
					for(int j=0;j<fishNum;j++)
					{
						if(choosed[j]!=-1)
							scope[k++] = fish[choosed[j]];
					}
//					Kembalikan data dari tetangga ikan tersebut
					return scope;
				}
				return null;
			}     

			
// Inisiasi proses AFSA		
			private void init() throws IOException {
				for(int i=0;i<fishNum;i++) {
//					Dim for looping variable in mentioning Fish position
					fish[i] = new Fish(dim,visual);
//					New function use to calculate fit
					fish[i].fit = fish[i].newfunction(fish[i].x);
				}
				bestfish = new Fish(dim,visual);
//				Set minimum value
				bestfish.fit = -999999;
			}
}
