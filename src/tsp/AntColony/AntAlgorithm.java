package tsp.AntColony;

import java.util.ArrayList;

import tsp.Instance;
import tsp.Solution;
import tsp.metaheuristic.AMetaheuristic;

public class AntAlgorithm extends AMetaheuristic {

	public static final double alpha = 2.0; //pondere les phéromones A DEFINIR
	public static final double beta = 3.0; //pondere la visibilité
	public static final double rho = 0.9; // doit être entre 0 et 1
	public static final double Q = 1.0;
	
	private Instance instance;
	private Solution bestSol;
	private Double[][] proba;
	private double[][] visibilite; // à voir si on garde
	private double[][] pheromones; // les tau (i,j)
	private ArrayList<Integer> villesRestantes; // pourquoi pas mettre dans solve, c'est pareil
	
	
	
	public AntAlgorithm(Instance instance, String name) throws Exception {
		super(instance, name);
	}

	
	
	
	public Solution solve(Solution sol) throws Exception {
		double meilleurDistance = sol.evaluate(); /*boucle présente ici, donc on mettre à jour 
		meilleurDistance si une meilleure solution est trouvée 
		Il va y avoir ici "Solution solActuelle = new Solution(instance)*/
		return null;
	}

}
