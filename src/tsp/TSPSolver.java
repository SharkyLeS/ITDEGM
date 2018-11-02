package tsp;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import tsp.AntColony.AntAlgorithm;
import tsp.GA.GA;
import tsp.heuristic.AHeuristic;
import tsp.metaheuristic.AMetaheuristic;
import tsp.metaheuristic.ThreadPerso;

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
		boolean GA = false;
		boolean ant = true;
		boolean thread3 = false;
		boolean thread4 = false;
		
		long startTime = System.currentTimeMillis();
		
		m_solution.print(System.err);
		AHeuristic ini = (new PlusProchesVoisins(m_instance,"PlusProchesVoisins",m_timeLimit));
		ini.solve();
		Solution solutionIni = ini.getSolution();
		
		Callable[] solvers = new Callable[2];
		if (ant) {
			solvers[0] = new ThreadPerso(new AntAlgorithm(m_instance,solutionIni),
			solutionIni, System.currentTimeMillis() - startTime , getTimeLimit());
		}
		if (GA) {
			solvers[1] = new ThreadPerso(new GA(solutionIni, m_instance,100,this.getTimeLimit()),
				solutionIni, System.currentTimeMillis() - startTime , getTimeLimit());
		}/*
		if (nbThread == 3) {
			solvers[2] = new ThreadPerso(new AntAlgorithm(m_instance,solutionIni),
					solutionIni, System.currentTimeMillis() - startTime , getTimeLimit());
		}
		if (nbThread == 4) {
			solvers[3] = new ThreadPerso(new AntAlgorithm(m_instance,solutionIni),
					solutionIni, System.currentTimeMillis() - startTime , getTimeLimit());
		}*/
		
		ExecutorService exe = Executors.newFixedThreadPool(2); 
				
		if (ant) {
			Future<Solution> fut0 = exe.submit(solvers[0]);
			m_solution = fut0.get();
		} else if (GA) {
			Future<Solution> fut1 = exe.submit(solvers[1]);
			Solution solutionGA = fut1.get();
			m_solution=solutionGA;
		} else if (ant&&GA) {
			Future<Solution> fut0 = exe.submit(solvers[0]);
			Future<Solution> fut1 = exe.submit(solvers[1]);
			m_solution = compareSolution(fut0.get(),fut1.get());
		} else {
			m_solution = solutionIni;
		}
		
		
		exe.shutdown(); //necessaire ?
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

	public Solution compareSolution(Solution s1, Solution s2) {
		if (s1.getObjectiveValue()>s2.getObjectiveValue()) {
			return s2;
		} else {
			return s1;
		}
	}
	
	public Solution compareSolution(Solution s1, Solution s2, Solution s3) {
		return compareSolution(s1,compareSolution(s2,s3));
	}
	public Solution compareSolution(Solution s1, Solution s2, Solution s3,Solution s4) {
		return compareSolution(compareSolution(s1,s2),compareSolution(s3,s4));
	}	
}