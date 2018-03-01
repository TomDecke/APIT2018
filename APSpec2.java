import java.util.ArrayList;

/**
 * Main class to start the execution of specification level 2
 * @author 2354160d
 *
 */
public class APSpec2 {
	
	public static void main(String[] args) {
		
		
		Intersection intersection = new Intersection(10, 20);
		ArrayList<CarGenerator> carGen = new ArrayList<CarGenerator>();
		
		//create int arrays to store the rows/columns on which generators are to be applied
		int[] rowsEast = {0,1,2,3,4};
		int[] rowsWest = {5,6,7,8,9};
		int[] colsNorth = new int[10];
		int[] colsSouth = new int[10];

		//fill remaining arrays with values 
		for(int i =0 ; i<10;i++) {
			colsNorth[i]=i;
			colsSouth[i]=i+10;
		}
		
		Log log = new Log();
		
		//create vehicle generators
		HorizontalGenerator eastGenerator = new HorizontalGenerator(intersection, log, rowsEast, 1, 750);
		HorizontalGenerator westGenerator = new HorizontalGenerator(intersection, log, rowsWest, 3, 800);
		VerticalGenerator northGenerator = new VerticalGenerator(intersection, log, colsNorth, 0);
		VerticalGenerator southGenerator = new VerticalGenerator(intersection, log, colsSouth, 2);

		//add generators to the array list
		carGen.add(eastGenerator);
		carGen.add(northGenerator);
		carGen.add(southGenerator);
		carGen.add(westGenerator);

		//create the simulator
		Simulator simulator = new Simulator(intersection, 600, carGen);

		//create threads for generators and simulator and start them
		Thread[] t = new Thread[5];
		t[0] = new Thread(simulator);
		t[1] = new Thread(eastGenerator);
		t[2] = new Thread(westGenerator);
		t[3] = new Thread(northGenerator);
		t[4] = new Thread(southGenerator);
		for(int i= 0 ; i < 5 ; i++) {
			t[i].start();;
		}
		
		//wait until all threads have finished, then print the log report
		for(int i= 0 ; i < 5 ; i++) {
			try {
				t[i].join();
			} catch (InterruptedException e) {};
		}
		System.out.println(log.getReport());
	}
}