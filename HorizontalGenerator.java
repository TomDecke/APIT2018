import java.util.*;

/**
 * generator that creates cars that are going horizontal
 * @author Tom
 *
 */
public class HorizontalGenerator extends CarGenerator{

	//the row the generator is applied to
	private int row;
	private String firstDirection;
	private String symbol = "-";

	//Constructor to create 
	/**
	 * constructor to create
	 * @param intersection grid on which the cars move
	 * @param row int referring to the row on which the generator is to be applied
	 * @param direction int for the first heading of cars; 1 = EAST, 3 = WEST
	 */
	public HorizontalGenerator(Intersection intersection, int row, int direction) {
		super(intersection);
		//TODO do I have to check for this? Could I do this with a flag
		this.firstDirection = dirMap.get(direction);
		this.row = row;
	}


	@Override
	public Car generateCar() {

		//determine the first move of the car
		MoveSet ms = new MoveSet(firstDirection);

		int col = 0;
		if(firstDirection.equals("WEST")) {
			col = maxCol-1;
		}
		
		//use the obtained information to create a new car
		Car newCar = new Car(ms, symbol, row, col,intersection);

		//add the created car to the list of cars created by this generator
		createdCars.add(newCar);

		return newCar;
	}

	//TODO do I really have to override this one?
	@Override
	public void run() {
		//create an space for the car threads
		ArrayList<Thread> threads = new ArrayList<>();
		while(active) {
			try {
				//slow down the car creation
				Thread.sleep(2000);
			} catch (InterruptedException e) {}
			Car cCar = generateCar();
			cCar.addCarToGrid();

			//add the car to the threads and start it 
			threads.add(new Thread(cCar));
			threads.get(threads.size()-1).start();
		}
	}
}