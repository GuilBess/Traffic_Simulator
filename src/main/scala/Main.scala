object Main {
  final val MAX_SPEED: Double = 250 //Max speed of the car, in pixel/sec
  final val ACCELERATION: Double = 50 //Acceleration of the car, in pixel/sec^2
  final val BRAKING: Double = -500 //Acceleration of the car when braking, in pixel/sec^2
  final val TIME_STEP: Double = 1.0/60.0 //we want a 60 fps simulation, so we set the time step accordingly (do that differently maybe)
  final val ROAD_LENGTH: Int = 1500 //length of the road the cars are on, in pixels
  def main(args: Array[String]): Unit = {
    val testCarList: List[Car] = createCars(12, ROAD_LENGTH)
    val display = new Display
    display.init(ROAD_LENGTH)

    var cntTest = 0

    while(true){


      ///////////////////////////////////////////////////////////////////////////////////////////////
      cntTest += 1  //This is the way we introduce perturbation for now, not good, need to implement a better way
      if (cntTest == 200) {
        testCarList.head.speed = testCarList.head.speed - 50
        println("AAAAAAA IL FREINE")
      }
      //////////////////////////////////////////////////////////////////////////////////////////////


      display.drawCarsCircle(testCarList)
      for(i <- testCarList.indices){
        if(i == testCarList.length-1)
          nextState(testCarList(i), testCarList.head)
        else
          nextState(testCarList(i), testCarList(i+1))
      }
      Thread.sleep((TIME_STEP*1000).toLong) //We wait for the time needed to have 60fps
    }                   //This assumes the code is really fast. Not good, need to implement a better way

  }

  private def createCars(nbr: Int, roadLength: Int): List[Car] = {

    val distBetweenCars: Int = roadLength/nbr
    assert(distBetweenCars >= 50) // Lets say we must leave 50 pixels between each car at the start...

    def helper(n: Int, pos: Int): List[Car] = {
      if(n == 0)
        Nil
      else
        Car(pos, 0, ACCELERATION, MAX_SPEED, BRAKING)::helper(n-1, pos+distBetweenCars)
    }

    helper(nbr, 0)
  }

  private def nextState(car: Car, nextCar: Car): Unit = {
    if(nextCar.pos - car.pos > 100 || ((nextCar.pos - car.pos < 0) && (ROAD_LENGTH - car.pos) + nextCar.pos > 100)) {
      val nextPos: Double = car.pos + car.speed * TIME_STEP
      if (nextPos > ROAD_LENGTH) car.pos = nextPos - ROAD_LENGTH else car.pos = nextPos

      if (car.speed < car.maxSpeed) {
        val nextSpeed: Double = car.speed + (car.accel * TIME_STEP)
        if (nextSpeed > car.maxSpeed) car.speed = car.maxSpeed else car.speed = nextSpeed
      }
    }
    else{ //This is how we brake for now, would be nice to brake slowly first and the harder as we come closer to the
      val nextPos: Double = car.pos + car.speed * TIME_STEP                                  //car in front.
      if (nextPos > ROAD_LENGTH) car.pos = nextPos - ROAD_LENGTH else car.pos = nextPos      //we should never be able
                                                                                             //to go in front of it
      val nextSpeed: Double = car.speed + (car.braking * TIME_STEP)
      if (nextSpeed <= 0) car.speed = 0 else car.speed = nextSpeed
    }
  }

}
