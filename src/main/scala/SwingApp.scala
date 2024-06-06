import com.cibo.evilplot._
import com.cibo.evilplot.geometry._

import com.cibo.evilplot._
import com.cibo.evilplot.plot._
import com.cibo.evilplot.plot.aesthetics.DefaultTheme._
import com.cibo.evilplot.numeric.Point
import scala.swing._
import scala.swing.event._
import java.awt.{Graphics2D, Color, geom}

object EvilPlot {
  val data = Seq.tabulate(100) { i =>
    Point(i.toDouble, scala.util.Random.nextDouble())
  }
  val bf = ScatterPlot(data).render().asBufferedImage
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