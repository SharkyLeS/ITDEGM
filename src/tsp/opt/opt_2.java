package tsp.opt;

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
		Solution s=sol;
		boolean ameliore;
		do {
			ameliore=false;
			for(int i=1;i<sol.getInstance().getNbCities()-3;i++) {
				ameliore = ameliore || (this.BestForI2(s, i).evaluate()<s.evaluate());
				s=this.BestForI2(s, i);
			}
			s.print(System.err);
			if(!ameliore) System.err.println(ameliore);
			spentTime=System.currentTimeMillis()-startTime;
		}
		while(ameliore && (spentTime<time*1000-100));
		System.err.println(spentTime);
		s.evaluate();
		return s;
	}

	/*
	 * Trouve si une amélioration est possible autour du sommet j
	 * si oui, la fait et retourne true 
	 * sinon, retourne false
	 */
	public boolean BestForI(Solution sol, int i) throws Exception {
		if((i<1)||(i>sol.getInstance().getNbCities()-2)) {
			throw new Exception("i doit appartenir à l'intervalle : [1;"+(sol.getInstance().getNbCities()-2)+"]");
		}
		else {
			boolean best =false;
			for(int j=i;j<sol.getInstance().getNbCities()-1;j++) {
				if((j!=i-1)&&(j!=i)&&(j!=i+1)) {
					double newDist=sol.getInstance().getDistances(sol.getCity(i),sol.getCity(j))+sol.getInstance().getDistances(sol.getCity(i+1), sol.getCity(j+1));
					double oldDist=sol.getInstance().getDistances(sol.getCity(i), sol.getCity(i+1))+sol.getInstance().getDistances(sol.getCity(j), sol.getCity(j+1));
					if(oldDist>newDist) {
						int x = sol.getCity(i+1);
						sol.setCityPosition(sol.getCity(j), i+1);
						sol.setCityPosition(x, j);
						best=true;
					}
				}
			}
			return best;
		}
	}
	
		public Solution BestForI2(Solution sol, int i) throws Exception {
			if((i<1)||(i>sol.getInstance().getNbCities()-2)) {
				throw new Exception("i doit appartenir à l'intervalle : [1;"+(sol.getInstance().getNbCities()-2)+"]");
			}
			else {
				boolean best_trouve = false;
				Solution s_best = sol;
				double best = sol.evaluate();
				int best_j = 0;
				for(int j=i+2;j<sol.getInstance().getNbCities()-1;j++) {
					double newDist=sol.getInstance().getDistances(sol.getCity(i),sol.getCity(j))+sol.getInstance().getDistances(sol.getCity(i+1), sol.getCity(j+1));
					double oldDist=sol.getInstance().getDistances(sol.getCity(i), sol.getCity(i+1))+sol.getInstance().getDistances(sol.getCity(j), sol.getCity(j+1));
					if(oldDist>newDist) {
						Solution s = sol.copy();
						s.setCityPosition(sol.getCity(j), i+1);
						s.setCityPosition(sol.getCity(i+1), j);
						if(s.evaluate()<best) {
							s_best=s;
							best=s.evaluate();
							best_j=j;
							best_trouve=true;
						}
					}
				}
				
				if(best_trouve) {
					return s_best.copy();
				}
				
				return sol;
			}
	}
}


