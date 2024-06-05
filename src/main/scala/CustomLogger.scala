import java.nio.file.{Paths, Files}
import java.nio.charset.StandardCharsets
class CustomLogger {
  private var speedLog: List[(Double, Double)] = Nil
  private var jamPos: List[(Double, Double)] = List((0,0))
  private var currTime: Double = 1.0/60
  private var outSpeed: String = "time;avg speed\n"
  private var outJam: String = "time;jam pos\n"
  private var nbr: Int = 0
  def logSpeed(cars: List[Car]): Unit = { //we log the current average speed along with the time of the simulation
    var speedTot: Double = 0
    for (i <- cars) speedTot += i.speed
    speedLog = speedLog ++ List((currTime, speedTot/cars.length))
    cars foreach ((x: Car) => {
      if (x.slowest) {
        jamPos = jamPos ++ List((currTime, x.pos))
      }
    })
    currTime += 1.0/60
  }

  def writeLogCSV(): Unit = {
    speedLog foreach ((x: (Double, Double)) => outSpeed += f"${x._1};${x._2}\n")
    jamPos foreach ((x: (Double, Double)) => outJam += f"${x._1};${x._2}\n")
    Files.write(Paths.get(s"speed_log${nbr}.csv"), outSpeed.getBytes(StandardCharsets.UTF_8))
    Files.write(Paths.get(s"jam_log${nbr}.csv"), outJam.getBytes(StandardCharsets.UTF_8))
    nbr += 1
  }

  def reset(): Unit = {
    outSpeed = "time;avg speed\n"
    outJam = "time;jam pos\n"
    speedLog = Nil
    jamPos = List((currTime, 0))
  }

}
