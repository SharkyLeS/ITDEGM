package tsp.GA;

import java.io.IOException;

import tsp.Instance;
import tsp.PlusProchesVoisins;
import tsp.Solution;
import tsp.heuristic.AHeuristic;

public class TesteurGA {

	public TesteurGA() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String filename = null;
		long max_time = 20;
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
		Instance i = new Instance(filename, typeInstance);
		AHeuristic ini = (new PlusProchesVoisins(i,"PlusProchesVoisins",max_time));
		ini.solve();
		Solution solutionIni = ini.getSolution();
		int taille_Monde = 10;
		
		solutionIni.print(System.err);
		GA premier_GA = new GA(solutionIni, i, taille_Monde, max_time);
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
		
		
		}
	}


