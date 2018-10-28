package tsp;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestMultiThreading implements Runnable {
	
	Thread t1;
	public TestMultiThreading() {
		t1 = new Thread(this,"Thread 1");
		t1.start();
	}

	public void run() {
		while(true) {
			
			System.out.println("in Thread 1");
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		Callable test1 = (new Callable() {
			public Object call() throws Exception {
				return "Je suis le resultat de la thread 1";
			};
		});
		Callable test2 = (new Callable() {
			public Object call() throws Exception {
				return "Je suis le resultat de la thread 2";
			};
		});
		Callable test3 = (new Callable() {
			public Object call() throws Exception {
				return "Je suis le resultat de la thread 3";
			};
		});
		Callable test4 = (new Callable() {
			public Object call() throws Exception {
				return "Je suis le resultat de la thread 4";
			};
		});
		/*
		Callable[] solvers = new Callable[4];
		solvers[0] = new ThreadPerso(new AntAlgorithm(m_instance,solutionIni),
				solutionIni, System.currentTimeMillis() - startTime , getTimeLimit());
		solvers[1] = new ThreadPerso(new GA(solutionIni, m_instance,100,this.getTimeLimit()),
				solutionIni, System.currentTimeMillis() - startTime , getTimeLimit());*/
		//solvers[2] =
		//solvers[3] =
		ExecutorService exe = Executors.newFixedThreadPool(4); 
		/*for (int i=0; i<4; i++) {
			exe.execute(solvers[i]);
		}*/
		/*
		Future<Solution> fut0 = exe.submit(solvers[0]);
		Solution solutionAntColony = fut0.get();
		
		Future<Solution> fut1 = exe.submit(solvers[1]);
		Solution solutionGA = fut1.get();
		
		
		
		exe.shutdown(); //necessaire ? */
		
		
		Future<String> testfut1 = exe.submit(test1);
		System.out.println(testfut1.get());
		Future<String> testfut2 = exe.submit(test2);
		System.out.println(testfut2.get());
		Future<String> testfut3 = exe.submit(test3);
		System.out.println(testfut3.get());
		Future<String> testfut4 = exe.submit(test4);
		System.out.println(testfut4.get());
		
		exe.shutdown();
		/*new TestMultiThreading();
		while(true) {
			System.out.println("in main");
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
	}
	
}
