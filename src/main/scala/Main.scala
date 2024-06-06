import java.awt.event.{MouseEvent, MouseListener}
import scala.util.Random

object Main {
  var MAX_SPEED: Double = 200 //Max speed of the car, in pixel/sec
  val ACCELERATION: Double = 75 //Acceleration of the car, in pixel/sec^2
  val BRAKING: Double = -1500 //Acceleration of the car when braking, in pixel/sec^2
  val TIME_STEP: Double = 1.0/60.0 //we want a 60 fps simulation, so we set the time step accordingly (do that differently maybe)
  val ROAD_LENGTH: Int = 2000 //length of the road the cars are on, in pixels
  var nbrBraking: Int = 0
  var nbrCars: Int = 5
  var sim = new SimSetup(0)
  var carList: List[Car] = createCars(nbrCars, ROAD_LENGTH)
  var brake1: Int = 0
  var brake2: Int = 0
  def main(args: Array[String]): Unit = {
    val display = new Display
    //display.init(ROAD_LENGTH)

    //addListeners(display)

    while(true){
      var slowest = (MAX_SPEED, 0)

      try assert(nbrCars<=80)
      catch {
        case _: AssertionError =>
          (brake1, brake2) match {
            case (0,0) =>
              sim.writeLogCSV()
              brake1 = 1
              sim = new SimSetup(1)
              nbrCars = 5
              reload(MAX_SPEED, nbrCars)
            case (1,0) =>
              sim.writeLogCSV()
              brake1 = 2
              sim = new SimSetup(2)
              nbrCars = 5
              reload(MAX_SPEED, nbrCars)
            case _ =>
              sim.writeLogCSV()
              System.exit(1)
          }
      }

      val t1: Long = System.currentTimeMillis()

      //Trouver la voiture la plus lente de toute, eq. à trouver le bouchon!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
      //display.drawCarsLine(carList)

      for(i <- carList.indices){
        if(i == carList.length-1)
          nextState(carList(i), carList.head)
        else
          nextState(carList(i), carList(i+1))

        if(carList(i).speed < slowest._1)
          slowest = (carList(i).speed, i)
        carList(i).slowest = false
      }

      carList(slowest._2).slowest = true

      sim.getData(carList) match {
        case 0 =>
        case 1 => nbrBraking = brake1;
        case 2 =>
          println(s"finished for ${nbrCars} cars")
          nbrCars = nbrCars + 1
          reload(MAX_SPEED, nbrCars)
        case 3 => nbrBraking = brake2
        case _ => println("what")
      }

      //while(t1 + TIME_STEP*1000 > System.currentTimeMillis()){} //We wait for the time step in order to have 60fps
    }

  }

  private def createCars(nbr: Int, roadLength: Int): List[Car] = {

    val distBetweenCars: Int = roadLength/nbr

    def helper(n: Int, pos: Int): List[Car] = {
      if(n == 0)
        Nil
      else {
        val rand: Random = new Random()
        Car(pos, 0, ACCELERATION+rand.nextInt(8)-4, MAX_SPEED+rand.nextInt(30)-15, BRAKING)::helper(n-1, pos+distBetweenCars)
      }
    }

    helper(nbr, 0)
  }

  private def nextState(car: Car, nextCar: Car): Unit = {
    val rand = new Random()
    if (nbrBraking != 0){
      nbrBraking -= 1
      car.leftToBrake = 30
    }
    val nextPos: Double = car.pos + car.speed * TIME_STEP
    if ((nextCar.pos - car.pos > 50/(car.maxSpeed/(car.speed+1)) ||
      ((nextCar.pos - car.pos < 0) && (ROAD_LENGTH - car.pos) + nextCar.pos > 50/(car.maxSpeed/(car.speed+1)))) &&
      car.leftToBrake == 0 && car.waitToStart == 0) {

      if (nextPos > ROAD_LENGTH) car.pos = nextPos - ROAD_LENGTH else car.pos = nextPos

      if (car.speed < car.maxSpeed) {
        val nextSpeed: Double = car.speed + (car.accel * TIME_STEP)
        if (nextSpeed > car.maxSpeed) car.speed = car.maxSpeed else car.speed = nextSpeed
      }
    }
    else {
      if (nextPos > ROAD_LENGTH) car.pos = nextPos - ROAD_LENGTH else car.pos = nextPos

      val nextSpeed: Double = car.speed + (car.braking * TIME_STEP)
      if (nextSpeed <= 0) car.speed = 0 else car.speed = nextSpeed
      if (car.speed <= 0 && car.waitToStart == 0) car.waitToStart = 30

      if (car.leftToBrake>0) car.leftToBrake -= 1
      if (car.waitToStart>0) car.waitToStart -= 1
    }
  }

  def reload(maxSpeed: Double, nbrCars: Int): Unit = {
    MAX_SPEED = maxSpeed
    carList = createCars(nbrCars, ROAD_LENGTH)
  }

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
