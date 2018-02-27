import java.util.ArrayList;

/**
 * Main class to start the execution
 * @author Tom
 *
 */
public class Main {

	public static void main(String[] args) {
		Intersection intersection = new Intersection(10, 20);
		ArrayList<CarGenerator> carGen = new ArrayList<CarGenerator>();
		carGen.add(new CarGenerator(intersection));
		Simulator simulator = new Simulator(intersection, 1000, carGen);
		
		Thread[] t = new Thread[2];
		t[0] = new Thread(simulator);
		t[1] = new Thread(carGen.get(0));
		
		t[0].start();
		t[1].start();
	}
}
