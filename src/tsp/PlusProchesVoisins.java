package tsp;

import java.util.ArrayList;

import tsp.heuristic.AHeuristic;
import tsp.metaheuristic.AMetaheuristic;

public class PlusProchesVoisins extends AHeuristic {
	

	public PlusProchesVoisins(Instance instance, String name) throws Exception {
		super(instance, name);
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

	public ArrayList<Integer> initialiseID(ArrayList<Integer> idRestants) {
		//for(int i=-1;i<m_instance.getNbCities()-2;i++) {
		for(int i=1;i<m_instance.getNbCities();i++) {
			//idRestants.add(i+2);
			idRestants.add(i);
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
		m_solution.print(System.err);
		long startTime = System.currentTimeMillis();
		long spentTime = 0;
		int i=1;
		ArrayList<Integer> idRestants = new ArrayList<Integer>();
		idRestants=this.initialiseID(idRestants);
		int idCity=this.CherchePlusProche(0, idRestants);
		do
		{
			//Stupid Heuristic
			m_solution.setCityPosition(idCity, i);
			this.removeDisponibleCity(idRestants, idCity);
			idCity=this.CherchePlusProche(idCity, idRestants);
			i++;

			// Code a loop base on time here
			spentTime = System.currentTimeMillis() - startTime;
		}while((spentTime < (m_timeLimit * 1000 - 100) )&&(i<m_instance.getNbCities()));
		m_solution.setCityPosition(0, 0);
		m_solution.setCityPosition(0, m_instance.getNbCities());
		
	}

	
}
