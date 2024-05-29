import scala.util.Random

object Main {
  final val MAX_SPEED: Double = 350 //Max speed of the car, in pixel/sec
  final val ACCELERATION: Double = 200 //Acceleration of the car, in pixel/sec^2
  final val BRAKING: Double = -1000 //Acceleration of the car when braking, in pixel/sec^2
  final val TIME_STEP: Double = 1.0/60.0 //we want a 60 fps simulation, so we set the time step accordingly (do that differently maybe)
  final val ROAD_LENGTH: Int = 1000 //length of the road the cars are on, in pixels
  def main(args: Array[String]): Unit = {
    val testCarList: List[Car] = createCars(30, ROAD_LENGTH)
    val display = new Display
    display.init(ROAD_LENGTH)

    var cntTest = 0

    while(true){

      val t1: Long = System.currentTimeMillis()

      //Trouver la voiture la plus lente de toute, eq. à trouver le bouchon!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
      display.drawCarsCircle(testCarList)
      for(i <- testCarList.indices){
        if(i == testCarList.length-1)
          nextState(testCarList(i), testCarList.head)
        else
          nextState(testCarList(i), testCarList(i+1))
      }

      while(t1 + TIME_STEP*1000 > System.currentTimeMillis()){} //We wait for the time step in order to have 60fps
    }

  }

  private def createCars(nbr: Int, roadLength: Int): List[Car] = {

    val distBetweenCars: Int = roadLength/nbr

    def helper(n: Int, pos: Int): List[Car] = {
      if(n == 0)
        Nil
      else {
        val rand: Random = new Random()
        Car(pos, 0, ACCELERATION+rand.nextInt(20)-10, MAX_SPEED+rand.nextInt(60)-30, BRAKING+rand.nextInt(60)-30)::helper(n-1, pos+distBetweenCars)
      }
    }

    helper(nbr, 0)
  }

  private def nextState(car: Car, nextCar: Car): Unit = {
    val rand = new Random()
    if (rand.nextInt(10000)==1){
      car.leftToBrake = 30
    }
    if ((nextCar.pos - car.pos > 50/(car.maxSpeed/(car.speed+1)) ||
      ((nextCar.pos - car.pos < 0) && (ROAD_LENGTH - car.pos) + nextCar.pos > 50/(car.maxSpeed/(car.speed+1)))) &&
      car.leftToBrake == 0) {
      val nextPos: Double = car.pos + car.speed * TIME_STEP
      if (nextPos > ROAD_LENGTH) car.pos = nextPos - ROAD_LENGTH else car.pos = nextPos

      if (car.speed < car.maxSpeed) {
        val nextSpeed: Double = car.speed + (car.accel * TIME_STEP)
        if (nextSpeed > car.maxSpeed) car.speed = car.maxSpeed else car.speed = nextSpeed
      }
    }
    else {
      val nextPos: Double = car.pos + car.speed * TIME_STEP
      if (nextPos > ROAD_LENGTH) car.pos = nextPos - ROAD_LENGTH else car.pos = nextPos

      val nextSpeed: Double = car.speed + (car.braking * TIME_STEP)
      if (nextSpeed <= 0) car.speed = 0 else car.speed = nextSpeed

      if (car.leftToBrake>0) car.leftToBrake -= 1
    }
  }
}
