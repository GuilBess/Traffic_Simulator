import java.nio.file.{Paths, Files}
import java.nio.charset.StandardCharsets
class CustomLogger {
  private var speedLog: List[(Double, Double)] = Nil
  private var currTime: Double = 1.0/60
  private var outString: String = "avg speed;time\n"
  private var nbr: Int = 0
  def logSpeed(cars: List[Car]): Unit = { //we log the current average speed along with the time of the simulation
    var speedTot: Double = 0
    for (i <- cars) speedTot += i.speed
    speedLog = speedLog ++ List((speedTot/cars.length, currTime))
    currTime += 1.0/60
  }

  def writeLogCSV(): Unit = {
    speedLog foreach ((x: (Double, Double)) => outString += f"${x._1};${x._2}\n")
    Files.write(Paths.get(s"log${nbr}.csv"), outString.getBytes(StandardCharsets.UTF_8))
    nbr += 1
  }

  def reset(): Unit = {
    outString = "avg speed;time\n"
    speedLog = Nil
  }

}
