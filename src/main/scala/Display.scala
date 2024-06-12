import hevs.graphics.FunGraphics

import java.awt.Color
import java.awt.event.{MouseEvent, MouseListener}

class Display {
  var f:FunGraphics = null //I dont like this, felt so bad I had to put a try catch in display methods
  var roadLength = 0
  var d: Double = 0


  def init(roadLength: Int):Unit = {
    this.roadLength = roadLength
    this.d = roadLength / math.Pi
    f = FunGraphics(roadLength+100, 700)
    f.drawLine(50, 150, roadLength+50, 150)
    f.displayFPS(true)
  }

  def drawCarsLine(cars: List[Car]): Unit = {
    try {
      assert(f != null)
    }catch {
      case e: AssertionError =>
        println("Please use the init method before using the display")
        System.exit(-1)
    }
    f.frontBuffer.synchronized{
      f.clear
      f.setColor(Color.BLACK)
      f.drawFillRect(50, 148, roadLength, 4)

      for(i <- cars) {
        if (i.slowest) f.setColor(Color.BLUE) else f.setColor(Color.RED)
        f.drawFilledCircle(50 + i.pos.toInt - 10, 140, 20)
      }
    }
  }

  def drawCarsCircle(cars: List[Car]): Unit = { //If we want to display the line as a circle instead of a line (fancy)
    try {
      assert(f != null)
    }catch {
      case e: AssertionError =>
        println("Please use the init method before using the display")
        System.exit(-1)
    }
    f.frontBuffer.synchronized {
      f.clear
      f.setColor(Color.BLACK)
      f.drawCircle(100, 50, d.toInt)
      f.setColor(Color.RED)
      for(i <- cars){
        if (i.slowest)
          f.setColor(Color.BLUE)
        val theta: Double = i.pos / (d / 2)
        val x = (d/2) * math.cos(theta) -10
        val y = (d/2) * math.sin(theta) -10
        f.drawFilledCircle((100+d/2 + x).toInt, (50 + d/2 + y).toInt, 20)
        f.setColor(Color.RED)
      }
    }
  }


}
