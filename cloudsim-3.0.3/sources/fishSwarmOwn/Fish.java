package fishSwarmOwn;

import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

public class Fish implements Cloneable {
	public ArrayList<Double> position;
	public double fitness;
	
	public ArrayList<Double> getPosition(){
		return this.position;
	}
	
	public void setPosition(ArrayList<Double> position){
		this.position = position;
	}
	
	public double getFitness(){
		return this.fitness;
	}
	
	public void setFitness(double fitness){
		this.fitness = fitness;
	}
	
	
	public Fish(ArrayList<Double> position, double fitness) {
		this.position = position;
		this.fitness = fitness;
	}
//	
//	public double getFitness() {
//		return this.fitness;
//	}
//	
	
//	Inisiasi ikan
	public static void initialize(int dim, int pop, ArrayList<Fish> GroupFish) {
	
		ArrayList<Double> s = new ArrayList<Double>();
				
		for (int i = 0; i < pop; i++) {
			s.clear();
			for(int j = 0; j < dim; j++) {
				s.add(Math.random());
			}
			double fit = calcFitness(s);
			Fish f = new Fish (s, fit);
			GroupFish.add(f);
		}
	}
	
	
//	Hitung nilai fitness
	public static double calcFitness(ArrayList<Double> s) {
		double sum = 0.0;
		int i = 0;
		while (i < (s.size() - 1)) {			
			double r = 100 * Math.pow( (s.get(i+1)-Math.pow(s.get(i),2)) , 2) + Math.pow((s.get(i)-1), 2);
			sum += r;
			i += 1;
		}
		return sum;
	}
	
	
//	Cek kondisi bergerak
	public static Fish makeTemp(Fish ind, double visual) {
		ArrayList<Double> temp_pos = new ArrayList<Double>();
		double temp = 0.0;
		for(int i = 0; i<ind.position.size() ; i++) {
			temp = ind.position.get(i) + (visual * Math.random());
			temp_pos.add(temp);
		}
		double temp_fit=calcFitness(temp_pos);
		Fish F = new Fish (temp_pos, temp_fit);
		return F;
	}
	
//	Mencari jarak eucledian antara dua titik
	public static double euclideanDistance(ArrayList<Double> TF, ArrayList<Double> ind, int dim) {
		double distance = 0.0;
		double temp = 0.0;
		double sum = 0.0;
		for (int i = 0; i < dim; i++) {
			temp = Math.pow(TF.get(i) - ind.get(i), 2);
			sum += temp;
			temp = 0;
		}
		distance = Math.sqrt(sum);
		return distance;
	}

	
//  Perilaku Prey dari Ikan
	public static void prey (Fish ind, Fish TF, Fish BestFish, int dim, double step, int n, double visual, ArrayList<Fish> GroupFish, int j){
		Random random = new Random();
		double maxRandom = 1.0;
		double minRandom = 0.5;
		double crowdFactor =  (random.nextDouble() * (maxRandom - minRandom)) + minRandom;
		ArrayList<Double> new_state = new ArrayList<Double>();
		for(int i = 0; i < dim; i++) {
			double m = ind.position.get(i) + (((TF.position.get(i)-ind.position.get(i)) / euclideanDistance(TF.position, ind.position, dim))*step*Math.random());
			new_state.add(m);		
		}
		double new_fit = calcFitness(ind.position);
		double nf = visual*n;
		
		
		ArrayList<Double> Xc = new ArrayList<Double>();
//		Perilaku Swarm
		for (int i = 0; i < (int) Math.round(nf) ; i++) {	
			if (j != 0 && j != n-1) {
				System.out.println(j);
				for(int x = 0;  x < dim; x++) {
					double element = GroupFish.get(j-1).position.get(x) + GroupFish.get(j).position.get(x) + GroupFish.get(j+1).position.get(x);
					Xc.add(element);
				}
			}
		}
		
		
		double XcFitness = calcFitness(Xc);
	
//		Follow behavior
		if(BestFish.fitness > ind.fitness && (nf/n)<crowdFactor) {
			follow(ind, BestFish, dim, step);
		}
		
		else if(XcFitness>ind.fitness && (nf/n)<crowdFactor) {
			swarm(ind, TF, dim, step);
		}
		
		else {
			ind.position = new_state;
			ind.fitness = new_fit;
		}
	}
	
	public static void moveRandomly(Fish ind, double visual) {
		ArrayList<Double> new_state = new ArrayList<Double>();
		for(int i = 0; i < ind.position.size(); i++) {
			double n = ind.position.get(i) + (visual * Math.random());
			new_state.add(n);
		}
		ind.position = new_state;
		ind.fitness = calcFitness(new_state);
	}
	

	public static Fish getBestFish(ArrayList<Fish> GroupFish) {
		ArrayList<Fish> tempFish = new ArrayList<Fish>(GroupFish);
		tempFish.sort((f1, f2) -> Double.compare(f1.fitness, f2.fitness));
		return tempFish.get(tempFish.size()-1);
	}
	
	
	public static void follow(Fish ind, Fish TF, int dim, double step) {
		ArrayList<Double> new_state = new ArrayList<Double>();
		for(int i = 0; i < dim; i++) {
			double o = ind.position.get(i) + (((TF.position.get(i)-ind.position.get(i)) / euclideanDistance(TF.position, ind.position, dim))*step*Math.random());
			new_state.add(o);
		}
		ind.position = new_state;
		ind.fitness = calcFitness(new_state);
	}
	
	public static void swarm(Fish ind, Fish B, int dim, double step) {
		ArrayList<Double> new_state = new ArrayList<Double>();
		for(int i = 0; i < dim; i++) {
			double m = ind.position.get(i) + (((B.position.get(i)-ind.position.get(i)) / euclideanDistance(B.position, ind.position, dim))*step*Math.random());
			new_state.add(m);
		}
		ind.position = new_state;
		ind.fitness = calcFitness(new_state);
	}
}
	
