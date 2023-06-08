package fishSwarmOwn;

import java.util.ArrayList;

public class fishmain{
	public static void main(String[] args) {
		int dim = 2;
		int population = 10;
		ArrayList<Fish> GroupFish = new ArrayList<Fish>();
		int trytimes = 3;
		//Fish params
		double visual = 0.2;
		double step = 0.3;
		//params
		int i = 0;
		int iteration = 10;

		ArrayList<Fish> StoreBest = new ArrayList<Fish>();
		
		//init Fish 
		Fish.initialize(dim, population, GroupFish);
		
		Fish B = Fish.getBestFish(GroupFish);
		
		StoreBest.add(new Fish(B.getPosition(), B.getFitness()));
		
		while (i < iteration) {
			int j = 0;
			while (j < population) {
				int k = 0;
				while (k < trytimes) {
					Fish temp_position = Fish.makeTemp(GroupFish.get(j), visual);
					if(GroupFish.get(j).getFitness() < temp_position.getFitness()) {
						Fish.prey(GroupFish.get(j), temp_position, B, dim, step, population, visual, GroupFish, j);
						break;
					}
					k=k+1;
				}
				Fish.moveRandomly(GroupFish.get(j), visual);
				j = j + 1;
			}
			i+=1;
			B = Fish.getBestFish(GroupFish);
			StoreBest.add(new Fish(B.getPosition(), B.getFitness()));
		}
		
		Fish BE = Fish.getBestFish(StoreBest);
		System.out.print(BE.getFitness());
		
		System.gc();
	}
	
	
}



