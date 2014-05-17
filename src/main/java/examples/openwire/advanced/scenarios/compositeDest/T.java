package examples.openwire.advanced.scenarios.compositeDest;

public class T {

	public static void main(String[] args) {
		new SimpleThread("TO").start();
		new SimpleThread("TH").start();
		new SimpleThread("TW").start();

	}

}
