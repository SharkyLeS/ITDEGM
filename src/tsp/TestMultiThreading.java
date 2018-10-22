package tsp;

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
	
	public static void main(String[] args) {
		new TestMultiThreading();
		while(true) {
			System.out.println("in main");
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
