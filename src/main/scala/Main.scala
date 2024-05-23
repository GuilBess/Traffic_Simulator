object Main {
  final val MAX_SPEED: Double = 25
  final val ACCELERATION: Double = 10
  def main(args: Array[String]): Unit = {
    val testCarList: List[Car] = createCars(10, 100)

    testCarList foreach ((x: Car) => println(x.pos.toString))
  }

  def createCars(nbr: Int, roadLength: Int): List[Car] = {
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

}
