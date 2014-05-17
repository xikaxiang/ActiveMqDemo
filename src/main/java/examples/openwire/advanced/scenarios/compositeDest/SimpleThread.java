package examples.openwire.advanced.scenarios.compositeDest;

public class SimpleThread extends Thread {
	private static final long TIMEOUT = 20000;
	private static final Boolean NON_TRANSACTED = false;

	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public SimpleThread(String name) {
		super(name);
		this.setCount(0);
	}

	@Override
	public void run() {
		
		
		
		while(true){
			this.count++;
			System.out.println(Thread.currentThread().getName() + " running ....."
					+ this.getCount());
			try {
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(this.getCount()==100){
				break;
			}
		}
		

	}
}
