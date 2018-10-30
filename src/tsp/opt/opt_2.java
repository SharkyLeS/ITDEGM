package tsp.opt;

import java.util.ArrayList;

import tsp.Instance;
import tsp.Solution;
import tsp.metaheuristic.AMetaheuristic;

public class opt_2 extends AMetaheuristic {

	public opt_2(Instance instance) throws Exception {
		super(instance, "Opt_2");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Solution solve(Solution sol, long time) throws Exception {	
		long startTime = System.currentTimeMillis();
		long spentTime=0;
		boolean ameliore;
		do {
		ameliore=false;
		int best_i=0;
		int best_j=0;
		Solution s_best=sol.copy();
		double best = sol.evaluate();
		for(int i=1;i<sol.getInstance().getNbCities()-2;i++) {
			for(int j=i+1;j<sol.getInstance().getNbCities()-1;j++) {
				double newDist=sol.getInstance().getDistances(sol.getCity(i),sol.getCity(j))+sol.getInstance().getDistances(sol.getCity(i+1), sol.getCity(j+1));
				double oldDist=sol.getInstance().getDistances(sol.getCity(i), sol.getCity(i+1))+sol.getInstance().getDistances(sol.getCity(j), sol.getCity(j+1));
				if(oldDist>newDist) {
					Solution s = sol.copy();
					s.setCityPosition(sol.getCity(j), i+1);
					s.setCityPosition(sol.getCity(i+1), j);
					if(s.evaluate()<best) {
						best=s.evaluate();
						s_best=s;
						best_i=i;
						best_j=j;
					}
				}
			}
		}
		if(best<sol.evaluate()) {
			ameliore=true;
			/*
			Solution solu = sol.copy();
			int debut = Math.min(best_i+1,best_j+1);
			int fin = Math.max(best_i+1,best_j+1);
			int taille = Math.abs(best_j-best_i);
			int milieu = taille/2;
			for(int k=0;k<milieu;k++) {
				int a = sol.getCity(debut+k);
				int b = sol.getCity(fin-1-k);
				solu.setCityPosition(b, debut+k);
				solu.setCityPosition(a, fin-k-1);
			}
			*/
			sol=s_best.copy(); // sol=solu.copy();
			sol.print(System.err);
		}
		spentTime=System.currentTimeMillis()-startTime;
		}
		while(ameliore && (spentTime<time*1000-100));
		System.err.println(spentTime);
		sol.evaluate();
		return sol;
	}


	/*
	 * Trouve si une amélioration est possible autour du sommet j
	 * si oui, la fait et retourne true 
	 * sinon, retourne false
	 */
	public Solution BestForI(Solution sol, int i) throws Exception {
		if((i<1)||(i>sol.getInstance().getNbCities()-2)) {
			throw new Exception("i doit appartenir à l'intervalle : [1;"+(sol.getInstance().getNbCities()-2)+"]");
		}
		else {
			boolean best =false;
			Solution s = sol;
			for(int j=1;j<s.getInstance().getNbCities()-1;j++) {
				if((j!=i-1)&&(j!=i)&&(j!=i+1)) {
					double newDist=sol.getInstance().getDistances(sol.getCity(i),sol.getCity(j))+sol.getInstance().getDistances(sol.getCity(i+1), sol.getCity(j+1));
					double oldDist=sol.getInstance().getDistances(sol.getCity(i), sol.getCity(i+1))+sol.getInstance().getDistances(sol.getCity(j), sol.getCity(j+1));
					if(oldDist>newDist) {
						int x = sol.getCity(i+1);
						s.setCityPosition(sol.getCity(j), i+1);
						s.setCityPosition(x, j);
						return s;
					}
				}
			}
			return s;
	}
	}
	
		public ArrayList<Integer> BestForI2(Solution sol, int i) throws Exception {
			if((i<1)||(i>sol.getInstance().getNbCities()-2)) {
				throw new Exception("i doit appartenir à l'intervalle : [1;"+(sol.getInstance().getNbCities()-2)+"]");
			}
			else {
				boolean best_trouve = false;
				Solution s_best = sol;
				int best = (int) sol.evaluate();
				int best_j = 0;
				for(int j=i+2;j<sol.getInstance().getNbCities()-1;j++) {
					double newDist=sol.getInstance().getDistances(s_best.getCity(i),s_best.getCity(j))+sol.getInstance().getDistances(s_best.getCity(i+1), s_best.getCity(j+1));
					double oldDist=sol.getInstance().getDistances(s_best.getCity(i), s_best.getCity(i+1))+sol.getInstance().getDistances(s_best.getCity(j), s_best.getCity(j+1));
					if(oldDist>newDist) {
						Solution s = s_best.copy();
						s.setCityPosition(s_best.getCity(j), i+1);
						s.setCityPosition(s_best.getCity(i+1), j);
						if(s.evaluate()<best) {
							s_best=s;
							best=(int) s.evaluate();
							best_trouve=true;
							best_j=j;
						}
						
					}
				}
				ArrayList<Integer> sortie = new ArrayList<Integer>();
				sortie.add(best_j);
				sortie.add(best);
				return sortie;
			}
	}
}


