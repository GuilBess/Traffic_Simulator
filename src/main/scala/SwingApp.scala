import com.cibo.evilplot._
import com.cibo.evilplot.geometry._
import com.cibo.evilplot._
import com.cibo.evilplot.plot._
import com.cibo.evilplot.plot.aesthetics.DefaultTheme._
import com.cibo.evilplot.numeric.Point

import scala.swing._
import scala.swing.event._
import java.awt.{Color, Graphics2D, geom}
import scala.io.Source

object EvilPlot {
  var data: List[Point] = Nil

  val src = Source.fromFile("carsLog2.csv")
  for (i<-src.getLines().drop(1)){
    val temp = i.split(";")
    data = data ++ List(new Point(temp(0).toInt, temp(1).toDouble))
  }

  val bf = ScatterPlot(data).xAxis().yAxis().frame().render().asBufferedImage
}

object SwingApp extends SimpleSwingApplication {


  lazy val ui: Panel = new Panel {
    background = Color.white
    preferredSize = new Dimension(1000, 1000)
    focusable = true

    override def paintComponent(g: Graphics2D): Unit = {
      super.paintComponent(g)
      g.drawImage(EvilPlot.bf, null, 0, 0)
    }
  }

  def top: Frame = new MainFrame {
    title = "Painting as you wish in Scala"
    centerOnScreen()
    contents = ui
  }

}