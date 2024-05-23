object Main {
  final val MAX_SPEED: Double = 500 //Max speed of the car, in pixel/sec
  final val ACCELERATION: Double = 50 //Acceleration of the car, in pixel/sec^2
  final val BRAKING: Double = -100 //Acceleration of the car when braking, in pixel/sec^2
  final val TIME_STEP: Double = 1.0/60.0 //we want a 60 fps simulation, so we set the time step accordingly (do that differently maybe)
  final val ROAD_LENGTH: Int = 500 //length of the road the cars are on, in pixels
  def main(args: Array[String]): Unit = {
    val testCarList: List[Car] = createCars(10, ROAD_LENGTH)
    val display = new Display
    display.init(ROAD_LENGTH)

    while(true){
      display.drawCars(testCarList)
      testCarList foreach ((x: Car) => nextState(x))
      Thread.sleep((TIME_STEP*1000).toLong)
    }
  }

  private def createCars(nbr: Int, roadLength: Int): List[Car] = {

    val distBetweenCars: Int = roadLength/nbr
    assert(distBetweenCars >= 50) // Lets say we must leave 50 pixels between each car at the start...

    def helper(n: Int, pos: Int): List[Car] = {
      if(n == 0)
        Nil
      else
        Car(pos, 0, ACCELERATION, MAX_SPEED)::helper(n-1, pos+distBetweenCars)
    }

    helper(nbr, 0)
  }

  private def nextState(car: Car): Unit = {
    val nextPos: Double = car.pos + car.speed * TIME_STEP
    if(nextPos > ROAD_LENGTH) car.pos = nextPos-ROAD_LENGTH else car.pos = nextPos

    if(car.speed<car.maxSpeed) {
      val nextSpeed: Double = car.speed + (car.accel * TIME_STEP)
      if(nextSpeed>MAX_SPEED) car.speed = MAX_SPEED else car.speed = nextSpeed
    }
  }

}
