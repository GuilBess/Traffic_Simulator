import java.nio.file.{Paths, Files}
import java.nio.charset.StandardCharsets
class SimSetup(n: Int) {
  private var speedLog: List[(Double, Double)] = Nil
  private var carsLogs: List[(Int, Double)] = Nil
  private var currTime: Double = 1.0/60
  private var outSpeed: String = "time;avg speed/speedMax\n"
  private var outCars: String = "nbrCars;avg speed/speedMax moy\n"
  private val nbr: Int = n
  var wait4next = 30
  var state: Int = 0 //0 => started, 1 => braked, 2 => ready
  var midBreak = 0

  def getData(cars: List[Car]): Int = { //we log the current average speed along with the time of the simulation

    currTime += 1.0/60
     state match{   //We wait for a bit of time for the traffic to stabilise before and after braking
       case 0 =>    //Then we log the speed
         if (currTime > wait4next) {wait4next += 30; state = 1; 1} else 0

       case 1 =>
         if (currTime > wait4next) {wait4next += 30; state = 2}
         if (currTime > wait4next-10 && midBreak == 0) {midBreak = 1; 3} else 0

       case 2 =>
         midBreak = 0
         var speedTot: Double = 0
         for (i <- cars) speedTot += i.speed/i.maxSpeed
         speedLog = speedLog ++ List((currTime, speedTot/cars.length))
         if (currTime > wait4next) {
           var tot = 0.0
           for(i <- speedLog)
             tot += i._2
           carsLogs = carsLogs ++ List((cars.length, tot/speedLog.length))
           wait4next += 30
           state = 0
           reset()
           2
         } else 0

       case _ => println("wrong state"); 0
     }

  }

  def writeLogCSV(): Unit = {
    //speedLog foreach ((x: (Double, Double)) => outSpeed += f"${x._1};${x._2}\n")
    //Files.write(Paths.get(s"speed_log${nbr}.csv"), outSpeed.getBytes(StandardCharsets.UTF_8))

    carsLogs foreach ((x: (Int, Double)) => outCars += f"${x._1};${x._2}\n")
    Files.write(Paths.get(s"carsLog${nbr}.csv"), outCars.getBytes(StandardCharsets.UTF_8))

    reset()
  }



  def reset(): Unit = {
    outSpeed = "time;avg speed/speedMax\n"
    speedLog = Nil
  }


}
