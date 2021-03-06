package tsp.GA;

import java.io.IOException;
import java.util.ArrayList;

import tsp.Instance;
import tsp.PlusProchesVoisins;
import tsp.Solution;
import tsp.SA.SA;
import tsp.gui.TSPGUI;
import tsp.heuristic.AHeuristic;
import tsp.opt.opt_2;

public class TesteurGA {
	
	public TesteurGA() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) throws Exception {
		
		String filename = null;
		long max_time = 30;
		boolean verbose = false;
		boolean graphical = false;
		int typeInstance = 0;

		// Parse commande line
		for (int i = 0; i < args.length; i++) {
			if (args[i].compareTo("-help") == 0) {
				System.err.println("The Traveling Salesman Problem");
				System.err.println("Program parameters:");
				System.err.println("command: java Main [options] dataFile");
				System.err.println("Options:");
				System.err.println("\t-help\t: prints this parameter description");
				System.err.println("\t-t\t\t: maximum number of seconds given to the algorithm (int)");
				System.err.println("\t-g\t\t: graphical output of the solution");
				System.err.println("\t-v\t\t: trace level");
				return;

			} else if (args[i].compareTo("-v") == 0) {
				verbose = true;
			} else if (args[i].compareTo("-g") == 0) {
				graphical = true;
			} else if (args[i].compareTo("-t") == 0) {
				try {
					max_time = Integer.parseInt(args[++i]);
				} catch (Exception e) {
					System.out.println("Error: The time given for -t is not a valid integer value.");
					System.exit(1);
				}
			} else if (args[i].compareTo("-i") == 0) {
				try {
					typeInstance = Integer.parseInt(args[++i]);
				} catch (Exception e) {
					System.out.println("error : the type of instance is not a valid type");
					System.exit(1);
				}
			} else {
				if (filename != null) {
					System.err.println("Error: There is a problem in the program parameters.");
					System.err.println("Value " + args[i] + " is not a valid parameter.");
					System.exit(1);
				}
				filename = args[i];
			}
		}
		
		// Initialisation paramètres test
		long startTime=System.currentTimeMillis();
		double lambda = 0.8;
		Instance i = new Instance(filename, typeInstance);
		AHeuristic ini = (new PlusProchesVoisins(i,"PlusProchesVoisins",max_time));
		ini.solve();
		Solution solutionIni = ini.getSolution();
		int taille_Monde = 300; //2500
		
		solutionIni.print(System.err);
		System.err.println(System.currentTimeMillis()-startTime);
		GA premier_GA = new GA(solutionIni, i, taille_Monde, max_time);
		opt_2 Opt_2 = new opt_2(i);
		// Test mutation()
		/* for(int j=0;j<20;j++) {
			Solution mutation = premier_GA.mutation(solutionIni);
			mutation.print(System.err);
			*/
		
		// Test getMondeSolutions()
		/* for(Solution s : premier_GA.getMonde_solutions()) {
			s.print(System.err);
		}
		System.err.print(premier_GA.getMonde_solutions().size());
		*/
		
		// Test addSolution()
		/*System.err.println(premier_GA.getTaille_Monde());
		premier_GA.addSolution_Monde(solutionIni);
		System.err.println(premier_GA.getTaille_Monde());
		premier_GA.getMonde_solutions().get(premier_GA.getTaille_Monde()-1).print(System.err);
		*/
		
		// Test getProba()
		
		 /* for(Solution s : premier_GA.getMonde_solutions()) {
			s.print(System.err);
			System.err.println("Probabilité d'apparition de cette solution dans la génération suivante : "+premier_GA.getProba(s));
		  }
		  */
		/* Attention, probas très proches : pour une instance à 10 ville,
		 * une solution de coût 177 a une proba de 0.112826 lorsqu'une de coût 230 a 
		 * une proba de 0.086827.
		 * EIL51 avec taillePop=30 : objectiveValue = 720/proba=0.0276
		 * 							 objectiveValue = 543/proba=0.0367 ...
		 */
		
		
		// Test choisirParents()
		
		/*
			for(int j=0;j<taille_Monde;j++) {
			ArrayList<Solution> parents = premier_GA.choisir_Parents();
			System.err.println("Parent 1 :");
			parents.get(0).print(System.err);
			System.err.println(parents.get(0).isFeasible());
			System.err.println("Parent 2 :");
			parents.get(1).print(System.err);
			System.err.println(parents.get(1).isFeasible());
		}
		*/
		
		// Attention, renvoie souvent deux solutions ayant juste subi une mutation
		
		// Test Crossover MPX
		
		/*
		ArrayList<Solution> parents = premier_GA.choisir_Parents();
		ArrayList<Solution> enfants=premier_GA.MPX(parents);
		parents.get(0).print(System.err);
		parents.get(1).print(System.err);
		enfants.get(0).print(System.err);
		System.err.println(enfants.get(0).isFeasible());
		enfants.get(1).print(System.err);
		System.err.println(enfants.get(1).isFeasible());
		*/
		
		// Test isElligible()
		
		/*
		for(int j=0;j<10;j++) {
			ArrayList<Solution> parents = premier_GA.choisir_Parents();
			ArrayList<Solution> enfants=premier_GA.MPX(parents);
			parents.get(0).print(System.err);
			parents.get(1).print(System.err);
			enfants.get(0).print(System.err);
			System.err.println("Solution admissible " + enfants.get(0).isFeasible());
			System.err.println("Solution elligible "  + premier_GA.isElligible(enfants.get(0), parents, 0));
			System.err.println(enfants.get(0).evaluate());
			enfants.get(1).print(System.err);
			System.err.println("Solution admissible " + enfants.get(1).isFeasible());
			System.err.println("Solution elligible " + premier_GA.isElligible(enfants.get(1), parents, 0));
			System.err.println(enfants.get(1).evaluate());
		}
		*/
		
		// Test offspringSelection
		
		/*
		long startTime = System.currentTimeMillis();
		int nb_Generations = 100; //800
		for(int j=0; j<nb_Generations; j++) {
			ArrayList<Solution> fils = premier_GA.offspring_Selection(lambda);
			premier_GA.setMonde_solutions(fils);
			//System.err.println(fils.size());
			//System.err.println(premier_GA.getTaille_Monde());
			// Taille du monde ne fait que d'augmenter !!
		
			Solution opti=premier_GA.getMonde_solutions().get(0);
			double best_sol = premier_GA.getMonde_solutions().get(0).evaluate();
			for(Solution s : premier_GA.getMonde_solutions()) {
				if(s.evaluate()<=best_sol) {
					opti=s;
					best_sol=s.evaluate();
				}
			}
			opti.print(System.err);
			//lambda += 1/nb_Generations;
		}
		System.err.println(System.currentTimeMillis()-startTime);
		*/
		
		
		// Test trouveOptimum()
		
		/*
		for(Solution s : premier_GA.getMonde_solutions()) {
			s.print(System.err);
		  }
		premier_GA.trouveOptimum().print(System.err);
		*/
		
		
		// Autres tests
		
		/*
		ArrayList<Integer> test = new ArrayList<Integer>();
		ArrayList<Integer> test2 = new ArrayList<Integer>();
		test.add(1);
		test.add(2);
		test2.add(3);
		test2.add(4);
		test=test2;
		String res = "[";
		for(int x : test) res+=x+" ,";
		System.out.println(res+"]");
		*/
		
		
		// Test solve 
		
		Solution solOpt_2 = Opt_2.solve(solutionIni, max_time);
		solOpt_2.print(System.err);
		
		
		/*
		GA compareGA = new GA(solOpt_2,i,taille_Monde,55);
		Solution solGA = premier_GA.solve(solOpt_2, 55);
		solGA.print(System.err);
		*/
		
		/*
		SA compareSA = new SA(i);
		Solution solSA = compareSA.solve(solutionIni, max_time);
		solSA.print(System.err);
		*/
		
		
		SA compareSA = new SA(i);
		ArrayList<Solution> sols = new ArrayList<Solution>();
		ArrayList<Double> probas = new ArrayList<Double>();
		Double p = 0.0;
		/*
		while(p<1) {
			probas.add(p);
			p+=0.1;
		}
		*/
		
		int k=0;
		long spentTime = System.currentTimeMillis()-startTime;
		while((spentTime<60*1000-500)&&(k<1000)) {
			/*
			for(int l=0;l<probas.size();l++) {
				compareSA.setProba(probas.get(l));
			}
			*/
			sols.add(compareSA.solve(solOpt_2, max_time));
			spentTime = System.currentTimeMillis()-startTime;
		}
		
		Solution best_sol = sols.get(0);
		double best = sols.get(0).evaluate();
		double avg =0;
		
		for(Solution s : sols) {
			if(s.evaluate()<best) {
				best=s.evaluate();
				best_sol=s.copy();
			}
			avg+=s.evaluate();
		}
		avg=avg/sols.size();
		
		System.err.println("Meilleure solution :");
		best_sol.print(System.err);
		
		System.err.println("Cout moyen : " + avg);
		
		
		
		TSPGUI gui = new TSPGUI(best_sol);
		
		System.err.println(System.currentTimeMillis()-startTime);
		
		
		
		// Test inverseRoute()
		
		/*
		solutionIni.print(System.err);
		Solution solImpair = Opt_2.inverseRoute(solutionIni, 0, 5);
		solImpair.print(System.err);
		Solution solPair = Opt_2.inverseRoute(solutionIni, 0, 6);
		solPair.print(System.err);
		*/
		
		
		// TESTS SA
		
		//SA sa = new SA(i);
		
		// Test swap 
		
		/*
		Solution swap = sa.swap(solutionIni, 3, 6);
		swap.print(System.err);
		*/
		
		// Test inverser
		
		/*
		Solution inverse = sa.inverse(solutionIni, 3, 7);
		inverse.print(System.err);
		*/
		
		// Test inserer
		
		/*
		Solution insere = sa.insert(solutionIni, 7, 3);
		insere.print(System.err);
		*/
	}
	}


