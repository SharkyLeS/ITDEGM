package tsp.opt;

import java.util.ArrayList;

import tsp.Instance;
import tsp.Solution;
import tsp.gui.TSPGUI;
import tsp.heuristic.AHeuristic;
import tsp.metaheuristic.AMetaheuristic;

public class opt_2 extends AHeuristic {

	public opt_2(Instance instance) throws Exception {
		super(instance, "Opt_2");
		// TODO Auto-generated constructor stub
	}
	
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
			for(int j=i+2;j<sol.getInstance().getNbCities();j++) {
				double newDist=sol.getInstance().getDistances(sol.getCity(i),sol.getCity(j))+sol.getInstance().getDistances(sol.getCity(i+1), sol.getCity(j+1));
				double oldDist=sol.getInstance().getDistances(sol.getCity(i), sol.getCity(i+1))+sol.getInstance().getDistances(sol.getCity(j), sol.getCity(j+1));
				if(oldDist>newDist) {
					/*
					Solution s = sol.copy();
					s.setCityPosition(sol.getCity(j), i+1);
					s.setCityPosition(sol.getCity(i+1), j);
					*/
					int debut = Math.min(i+1, j);
					int fin = Math.max(i+1, j);
					Solution s=this.inverseRoute(sol, debut, fin);
					if(s.evaluate()<best) {
						best=s.evaluate();
						s_best=s.copy();
						best_i=i;
						best_j=j;
					}
				}
			}
		}
		if(best<sol.evaluate()) {
			ameliore=true;
			sol=s_best.copy(); // sol=solu.copy();
			// sol.print(System.err);
		}
		spentTime=System.currentTimeMillis()-startTime;
		}
		while(ameliore && (spentTime<time*1000-100));
		// if(!ameliore) System.err.println(ameliore);
		System.err.println(spentTime);
		return sol;
	}

	/**
	 * inverse dans la solution s le chemin entre les points debut et fin
	 * renvoie cette solution
	 * @param s
	 * @param debut
	 * @param fin
	 * @return
	 * @throws Exception
	 */
	public Solution inverseRoute(Solution s, int debut, int fin) throws Exception{
		if(fin<=debut) {
			throw new Exception("Il est nécessaire que debut<fin");
		}
		else {
			Solution newSol = s.copy();
			for(int k=0;k<(fin+1-debut)/2;k++) {
				int a = s.getCity(debut+k);
				int b = s.getCity(fin-k);
				newSol.setCityPosition(b, debut+k);
				newSol.setCityPosition(a, fin-k);
			}
			return newSol;
		}
	}
	
	
	/**
	 * Trouve si une amélioration est possible autour du sommet j
	 * si oui, la fait et retourne true 
	 * sinon, retourne false
	 * @param sol
	 * @param i
	 * @return
	 * @throws Exception
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

		@Override
		public void solve() throws Exception {
			// TODO Auto-generated method stub
			
		}
}


