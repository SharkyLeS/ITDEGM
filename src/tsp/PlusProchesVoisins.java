package tsp;

import java.util.ArrayList;

import tsp.heuristic.AHeuristic;
import tsp.metaheuristic.AMetaheuristic;

public class PlusProchesVoisins extends AHeuristic {
	private long timeLimit;

	public PlusProchesVoisins(Instance instance, String name, long timeLim) throws Exception {
		super(instance, name);
		this.timeLimit=timeLim; 
	}
	public long gettimeLimit() {
		return this.timeLimit;
	}
	
	public int CherchePlusProche(int idCity, ArrayList<Integer> idRestants) throws Exception {
		if((idCity<0)||(idCity>m_instance.getNbCities()-1)) {
			throw new Exception("Error : index " + idCity
					+ " is not valid, it should range between 0 and "
					+ (m_instance.getNbCities()-1));
		}
		else {
			double dmin=10000000;
			int idPlusProche=idCity;
			for(int j:idRestants) {
				if(m_instance.getDistances(idCity,j)<=dmin) {
					dmin=m_instance.getDistances(idCity,j);
					idPlusProche=j;
				}
			}
			return idPlusProche;
		}

	}

	public ArrayList<Integer> initialiseID(ArrayList<Integer> idRestants, int idDebut) {
		//for(int i=-1;i<m_instance.getNbCities()-2;i++) {
		for(int i=0;i<m_instance.getNbCities();i++) {
			//idRestants.add(i+2);
			if(i!=idDebut) idRestants.add(i);
		}
		return idRestants;
	}

	public void removeDisponibleCity(ArrayList<Integer> idRestants, int idCity) throws Exception{
		if((idCity<0)||(idCity>m_instance.getNbCities()-1)) {
			throw new Exception("Error : index " + idCity
					+ " is not valid, it should range between 0 and "
					+ (m_instance.getNbCities()-1));
		}
		else {
			for(int i=0;i<idRestants.size();i++) {
				if(idRestants.get(i)==idCity) {
					idRestants.remove(i);
					break;
				}
			}
		}
	}

	@Override
	public void solve() throws Exception {
		long startTime = System.currentTimeMillis();
		long spentTime = 0;
		Solution best_sol = this.Voisin(0);
		double best = this.Voisin(0).evaluate();
		for(int i=1;i<m_instance.getNbCities();i++) {
			Solution s = this.Voisin(i);
			if(s.evaluate()<best_sol.evaluate()) {
				best_sol=s.copy();
				best=s.evaluate();
			}
		}
		
		m_solution=best_sol.copy();
	}

	public Solution Voisin(int villeDebut) throws Exception {
		int i=1;
		Solution s = new Solution(m_instance);
		ArrayList<Integer> idRestants = new ArrayList<Integer>();
		idRestants=this.initialiseID(idRestants, villeDebut);
		int idCity=this.CherchePlusProche(villeDebut, idRestants);
		do
		{
			//Stupid Heuristic
			s.setCityPosition(idCity, i);
			this.removeDisponibleCity(idRestants, idCity);
			idCity=this.CherchePlusProche(idCity, idRestants);
			i++;

		}while(i<m_instance.getNbCities());
		s.setCityPosition(villeDebut, 0);
		s.setCityPosition(villeDebut, m_instance.getNbCities());
		
		return s;
	}
	
}
