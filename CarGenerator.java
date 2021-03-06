import java.util.*;

/**
 * Class to generate car objects and place them on the intersection
 * @author 2354160d
 *
 */
public class CarGenerator implements Runnable{

	//information about the intersection
	protected Intersection intersection;
	protected int maxRow;
	protected int maxCol;

	//generator specific information
	protected int delay;
	protected boolean active;
	protected Map<Integer, String> dirMap;

	//information about the travel time of the cars
	protected ArrayList<Car> createdCars;
	protected double totalTravelTime;

	//random generator for car-placement
	protected Random rand = new Random();

	/**
	 * Constructor to create a car generator
	 * @param intersection Intersection on which the cars are to be placed
	 */
	public CarGenerator(Intersection intersection) {
		//set up instance variables
		this.intersection = intersection;
		maxRow = intersection.getRows();
		maxCol = intersection.getColumns();
		active = true;

		//create a hashmap that includes the possible movement directions
		dirMap = new HashMap<Integer,String>();
		dirMap.put(0, "NORTH");
		dirMap.put(1, "EAST");
		dirMap.put(2, "SOUTH");
		dirMap.put(3, "WEST");

		//initialize delay and space for the cars
		delay = 200;
		createdCars = new ArrayList<>();
	}


	/**
	 * Generate a new car object and add it to the list of created cars
	 * @return Car the created car
	 */
	protected Car generateCar() {
		//get either EAST or SOUTH for the direction and use the information to set up a move-set
		String direction = dirMap.get(rand.nextInt(2)+1);
		MoveSet ms = new MoveSet(direction);

		//initialize start position randomly
		int startRow = rand.nextInt(maxRow);
		int startCol = rand.nextInt(maxCol);

		//reserve space for the car symbol
		String symbol = "x";

		//update either row or column to first position, depending on direction of the car
		switch (ms.getMoves()) {
		case "EAST":
			symbol = "-";
			startCol = 0;
			break;

		case "SOUTH":
			symbol = "o";
			startRow = 0;
			break;		
		}

		//use the obtained information to create a new car
		Car newCar = new Car(ms, symbol, startRow, startCol,intersection);

		//add the created car to the list of cars created by this generator
		createdCars.add(newCar);

		return newCar;
	}

	/**
	 * Run method for the thread. Creates cars and places them on the intersection
	 */
	@Override
	public void run() {
		//create an space for the car threads
		while(active) {
			try {
				//slow down the car creation
				Thread.sleep(delay);
			} catch (InterruptedException e) {}
			//create a car and add it to the grid
			Car cCar = generateCar();
			cCar.addCarToGrid();

			//create a new thread with the created car and start it
			new Thread(cCar).start();
		}
		//after being deactivated from the simulator, calculate the statistics
		accumulateTravelTime();
	}
	

	/**
	 * Deactivates the car generator
	 */
	protected void deactivateGenerator() {
		this.active = false;
	}

	/**
	 * Iterates through the created cars and adds up the travel time of cars that traversed the grid
	 */
	private void accumulateTravelTime() {
		
		//pass every car that made it through the grid to the log
		for(Car c : createdCars) {
			if(!c.isOnGrid()) {
				totalTravelTime+= c.getTravelTime();
			}
		}
	}
	
	/**
	 * Get the accumulated time of travel for all cars
	 * @return
	 */
	protected double reportTotalTravelTime() {
		return totalTravelTime;
	}
}
