import hevs.graphics.FunGraphics

import java.awt.Color
import java.awt.event.{MouseEvent, MouseListener}

class Display {
  var f:FunGraphics = null
  var roadLength = 0
  var d: Double = 0


  def init(roadLength: Int):Unit = {
    this.roadLength = roadLength
    this.d = roadLength / math.Pi
    f = FunGraphics(roadLength+100, 700)
    f.drawLine(50, 150, roadLength+50, 150)
    f.displayFPS(true)

    f.addMouseListener(new MouseListener {
      override def mouseClicked(e: MouseEvent): Unit = {
        if(e.getButton == MouseEvent.BUTTON1) {
          Main.log.writeLogCSV()
          Main.nbrBraking = 1
          Main.log.reset()
        }
      }

      override def mousePressed(e: MouseEvent): Unit = null

      override def mouseReleased(e: MouseEvent): Unit = null

      override def mouseEntered(e: MouseEvent): Unit = null

      override def mouseExited(e: MouseEvent): Unit = null
    })
  }

  def drawCarsLine(cars: List[Car]): Unit = {
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
