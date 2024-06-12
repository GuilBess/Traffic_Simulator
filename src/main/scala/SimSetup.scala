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
  var state: Int = 0 //0 => started, 1 => braked, 2 => ready for data collection
  var midBreak = 0

  def getData(cars: List[Car]): Int = { //we log the current average speed along with the time of the simulation

    currTime += 1.0/60
    //we wait a sufficient time for the traffic to stabilize, here it's 30s in each state
     state match{
       case 0 =>    //Start state, start of the sim, wait for cars to go up to speed and traffic to be stable
         if (currTime > wait4next) {wait4next += 30; state = 1; 1} else 0

       case 1 => //Braking state, we wait again to stabilize after braking to see if there is a jam or if traffic
         if (currTime > wait4next) {wait4next += 30; state = 2} //returns to normal
         if (currTime > wait4next-10 && midBreak == 0) {midBreak = 1; 3} else 0 //This sends the signal for the second
                                                                                //braking point if we use it
       case 2 => //Data capture state, we capture data for 30s and retrieve the average speed of all cars
         midBreak = 0
         var speedTot: Double = 0
         for (i <- cars) speedTot += i.speed/i.maxSpeed
         speedLog = speedLog ++ List((currTime, speedTot/cars.length)) //Logging the data, speed is the ratio btween
                                                                       //max speed and current speed of car
         if (currTime > wait4next) { //If we are at the end of this part of the sim,
           var tot = 0.0
           for(i <- speedLog)
             tot += i._2
           carsLogs = carsLogs ++ List((cars.length, tot/speedLog.length)) //Add the average to the log
           wait4next += 30
           state = 0 //Start state again
           reset() //reset the sim
           2 //signal to main that we are done
         } else 0

       case _ => println("wrong state"); 0 //whaaaaat? should not happen 🤨
     }

  }

  def writeLogCSV(): Unit = {
    //making csv compliant string
    carsLogs foreach ((x: (Int, Double)) => outCars += f"${x._1};${x._2}\n")
    //Writing the log is a csv file
    Files.write(Paths.get(s"carsLog${nbr}.csv"), outCars.getBytes(StandardCharsets.UTF_8))

    reset()
  }



  def reset(): Unit = {
    outSpeed = "time;avg speed/speedMax\n"
    speedLog = Nil
  }


}
