package tsp;

import java.util.ArrayList;

/**
 * 
 * This class is the place where you should enter your code and from which you can create your own objects.
 * 
 * The method you must implement is solve(). This method is called by the programmer after loading the data.
 * 
 * The TSPSolver object is created by the Main class.
 * The other objects that are created in Main can be accessed through the following TSPSolver attributes: 
 * 	- #m_instance :  the Instance object which contains the problem data
 * 	- #m_solution : the Solution object to modify. This object will store the result of the program.
 * 	- #m_timeLimit : the maximum time limit (in seconds) given to the program.
 *  
 * @author Damien Prot, Fabien Lehuede, Axel Grimault
 * @version 2017
 * 
 */
public class TSPSolver {

	// -----------------------------
	// ----- ATTRIBUTS -------------
	// -----------------------------

	/**
	 * The Solution that will be returned by the program.
	 */
	private Solution m_solution;

	/** The Instance of the problem. */
	private Instance m_instance;

	/** Time given to solve the problem. */
	private long m_timeLimit;


	// -----------------------------
	// ----- CONSTRUCTOR -----------
	// -----------------------------

	/**
	 * Creates an object of the class Solution for the problem data loaded in Instance
	 * @param instance the instance of the problem
	 * @param timeLimit the time limit in seconds
	 */
	public TSPSolver(Instance instance, long timeLimit) {
		m_instance = instance;
		m_solution = new Solution(m_instance);
		m_timeLimit = timeLimit;
	}

	// -----------------------------
	// ----- METHODS ---------------
	// -----------------------------

	/**
	 * **TODO** Modify this method to solve the problem.
	 * 
	 * Do not print text on the standard output (eg. using `System.out.print()` or `System.out.println()`).
	 * This output is dedicated to the result analyzer that will be used to evaluate your code on multiple instances.
	 * 
	 * You can print using the error output (`System.err.print()` or `System.err.println()`).
	 * 
	 * When your algorithm terminates, make sure the attribute #m_solution in this class points to the solution you want to return.
	 * 
	 * You have to make sure that your algorithm does not take more time than the time limit #m_timeLimit.
	 * 
	 * @throws Exception may return some error, in particular if some vertices index are wrong.
	 */
	public void solve() throws Exception
	{
		m_solution.print(System.err);
		long startTime = System.currentTimeMillis();
		long spentTime = 0;
		
		// INITIALISATION
		
		//
		/* 
		ArrayList<Long> l = new ArrayList<Long>(m_instance.getNbCities());
		for (int j=0;j<m_instance.getNbCities();j++) {
			int i=1;
			ArrayList<Integer> idRestants = new ArrayList<Integer>();
			idRestants=this.initialiseID(idRestants);
			int idCity=this.CherchePlusProche(j, idRestants);
			Colonie colo = new Colonie(this.m_instance,2.0,2.0,2.0,100);
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
			l.add(m_solution.getObjectiveValue());
		}
		Long m =MinimumListe(l);
		// */
	}
		
	// -----------------------------
	// ----- GETTERS / SETTERS -----
	// -----------------------------

	/** @return the problem Solution */
	public Solution getSolution() {
		return m_solution;
	}

	/** @return problem data */
	public Instance getInstance() {
		return m_instance;
	}

	/** @return Time given to solve the problem */
	public long getTimeLimit() {
		return m_timeLimit;
	}

	/**
	 * Initializes the problem solution with a new Solution object (the old one will be deleted).
	 * @param solution : new solution
	 */
	public void setSolution(Solution solution) {
		this.m_solution = solution;
	}

	/**
	 * Sets the problem data
	 * @param instance the Instance object which contains the data.
	 */
	public void setInstance(Instance instance) {
		this.m_instance = instance;
	}

	/**
	 * Sets the time limit (in seconds).
	 * @param time time given to solve the problem
	 */
	public void setTimeLimit(long time) {
		this.m_timeLimit = time;
	}



	// METHODES PERSO

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
	public long MinimumListe(ArrayList<Long> l) {
		int indicemin=0;
		for (int i=0;i<l.size();i++) {
			if (l.get(indicemin)<l.get(i)) {
				indicemin=i;
			}
		}
		return l.get(indicemin);
	}
}