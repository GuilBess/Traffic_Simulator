import hevs.graphics.FunGraphics
import java.awt.Color

class Display {
  var f:FunGraphics = null
  var roadLength = 0

  def init(roadLength: Int):Unit = {
    this.roadLength = roadLength
    f = FunGraphics(roadLength+100, 300)
    f.drawLine(50, 150, roadLength+50, 150)
  }

  def drawCars(cars: List[Car]): Unit = {
    f.frontBuffer.synchronized{
      f.clear
      f.setColor(Color.BLACK)
      f.drawFillRect(50, 148, roadLength, 4)
      f.setColor(Color.RED)
      cars foreach ((car: Car) => f.drawFilledCircle(50+car.pos.toInt-10, 140, 20))
    }

  }


}
