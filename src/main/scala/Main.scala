import hevs.graphics.FunGraphics

object Main {
  final val MAX_SPEED: Double = 15
  final val ACCELERATION: Double = 10
  final val TIME_STEP: Double = 1.0/60.0 //we want a 60 fps simulation, so we set the time step accordingly
  final val ROAD_LENGTH: Int = 50
  def main(args: Array[String]): Unit = {
    val testCarList: List[Car] = createCars(10, ROAD_LENGTH)
    testCarList foreach ((x: Car) => println(x.pos.toString))

    val f : FunGraphics = new FunGraphics(800,600)
    f.drawRect(10, 10, 100, 100)

    for(i <- 0 until 120){
      testCarList foreach ((x: Car) => nextState(x))
    }
  }

  private def createCars(nbr: Int, roadLength: Int): List[Car] = {

    val distBetweenCars: Int = roadLength/nbr
    assert(distBetweenCars >= 5) // Lets say we must leave 5 pixels between each car at the start...

    def helper(n: Int, pos: Int): List[Car] = {
      if(n == 0)
        return Nil
      else
        return new Car(pos, 0, ACCELERATION, MAX_SPEED)::helper(n-1, pos+distBetweenCars)
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
