import java.awt.event.{MouseEvent, MouseListener}
import scala.util.Random

object Main {
  val MAX_SPEED: Double = 200 //Max speed of the car, in pixel/sec
  val ACCELERATION: Double = 20 //Acceleration of the car, in pixel/sec^2
  val BRAKING: Double = -1500 //Acceleration of the car when braking, in pixel/sec^2
  val TIME_STEP: Double = 1.0/60.0 //we want a 60 fps simulation, so we set the time step accordingly
  val ROAD_LENGTH: Int = 2000 //length of the road the cars are on, in pixels
  var nbrBraking: Int = 0
  var nbrCars: Int = 5 //Number of cars in the simulation
  var sim = new SimSetup(0) // create the simulation helper object
  var carList: List[Car] = createCars(nbrCars, ROAD_LENGTH)
  var brake1: Int = 0 // brake controls for sim
  var brake2: Int = 0
  def main(args: Array[String]): Unit = {
    val display = new Display
    //display.init(ROAD_LENGTH) //Uncomment to display the simulation

    //addListeners(display)

    while(true){
      var slowest = (MAX_SPEED, 0)

      try assert(nbrCars<=80) //We reached the end of a part of the sim
      catch {
        case _: AssertionError =>
          (brake1, brake2) match { //We can know in what part of the sim we are depending on the brake spots
            case (0,0) => //First go through, no braking
              sim.writeLogCSV()
              brake1 = 1 //set the first brake for next run
              sim = new SimSetup(1)
              nbrCars = 5
              reload(MAX_SPEED, nbrCars)
            case (1,0) => //Second go through, only the first braking
              sim.writeLogCSV()
              brake2 = 1 //Set the second brake for next run
              sim = new SimSetup(2)
              nbrCars = 5
              reload(MAX_SPEED, nbrCars)
            case _ => //3rd and last go through, just need to log and exit sim
              sim.writeLogCSV()
              System.exit(1)
          }
      }

      val t1: Long = System.currentTimeMillis()

      //display.drawCarsLine(carList)  //Uncomment to display the cars on the visualisation

      for(i <- carList.indices){
        if(i == carList.length-1)
          nextState(carList(i), carList.head)
        else
          nextState(carList(i), carList(i+1))

        if(carList(i).speed < slowest._1) //Finding the slowest car to display it in blue
          slowest = (carList(i).speed, i)
        carList(i).slowest = false
      }

      carList(slowest._2).slowest = true

      sim.getData(carList) match { //ask the sim manager what action to do
        case 0 => //Waiting for things to happen
        case 1 => nbrBraking = brake1; //We brake
        case 2 => //Finished, starting again with other settings
          println(s"finished for ${nbrCars} cars")
          nbrCars = nbrCars + //we add a car to the road
          reload(MAX_SPEED, nbrCars)
        case 3 => nbrBraking = brake2 //When we are in the 2 brake times setup
        case _ => println("what")
      }

      //Uncomment the following line to slow the simulation to "real time"
      //while(t1 + TIME_STEP*1000 > System.currentTimeMillis()){} //We wait for the time step in order to have 60fps
    }

  }

  // Function that creates a number of cars and puts them in a list
  private def createCars(nbr: Int, roadLength: Int): List[Car] = {

    //We place the cars at the same distance of each other in the beginning
    val distBetweenCars: Int = roadLength/nbr

    def helper(n: Int, pos: Int): List[Car] = {
      if(n == 0)
        Nil
      else {
        val rand: Random = new Random()
        //Cars are added to the List and we add a bit of random to the acceleration and speed values
        Car(pos, 0, ACCELERATION+rand.nextInt(8)-4, MAX_SPEED+rand.nextInt(30)-15, BRAKING)::helper(n-1, pos+distBetweenCars)
      }
    }

    helper(nbr, 0)
  }

  //Function that lets us compute the next state
  private def nextState(car: Car, nextCar: Car): Unit = {
    val rand = new Random()
    //Check if this car has to break
    if (nbrBraking != 0){
      nbrBraking -= 1
      car.leftToBrake = 30
    }
    //We compute the nex pos using the car current speed
    val nextPos: Double = car.pos + car.speed * TIME_STEP
    //We check the car ahead to see if we are within the braking distance.
    //The braking distance is weighted by the speed of the current car
    if ((nextCar.pos - car.pos > 50/(car.maxSpeed/(car.speed+1)) ||
      ((nextCar.pos - car.pos < 0) && (ROAD_LENGTH - car.pos) + nextCar.pos > 50/(car.maxSpeed/(car.speed+1)))) &&
      car.leftToBrake == 0 && car.waitToStart == 0) {

      //Check for end of the road, and compute the next speed with car acceleration
      if (nextPos > ROAD_LENGTH) car.pos = nextPos - ROAD_LENGTH else car.pos = nextPos

      if (car.speed < car.maxSpeed) {
        val nextSpeed: Double = car.speed + (car.accel * TIME_STEP)
        if (nextSpeed > car.maxSpeed) car.speed = car.maxSpeed else car.speed = nextSpeed
      }
    }
    else {
      //If we must break...
      if (nextPos > ROAD_LENGTH) car.pos = nextPos - ROAD_LENGTH else car.pos = nextPos

      //Compute the speed while braking, and check speed is not under 0
      val nextSpeed: Double = car.speed + (car.braking * TIME_STEP)
      if (nextSpeed <= 0) car.speed = 0 else car.speed = nextSpeed
      if (car.speed <= 0 && car.waitToStart == 0) car.waitToStart = 30

      if (car.leftToBrake>0) car.leftToBrake -= 1
      if (car.waitToStart>0) car.waitToStart -= 1
    }
  }

  //Reload function used to setup multiple sim in a single app launch
  def reload(maxSpeed: Double, nbrCars: Int): Unit = {
    //MAX_SPEED = maxSpeed
    carList = createCars(nbrCars, ROAD_LENGTH)
  }

  //Keyboard listener, was used to manually break at one point
  def addListeners(display: Display): Unit = {
    display.f.addMouseListener(new MouseListener {
      override def mouseClicked(e: MouseEvent): Unit = {
        if(e.getButton == MouseEvent.BUTTON1) {
          print("teehee")
        }
      }

      override def mousePressed(e: MouseEvent): Unit = null

      override def mouseReleased(e: MouseEvent): Unit = null

      override def mouseEntered(e: MouseEvent): Unit = null

      override def mouseExited(e: MouseEvent): Unit = null
    })
  }
}
