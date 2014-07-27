package main.it.unibo.pap.nbodies.view.nbodies

import scala.swing.Panel
import java.awt.{ Color, Graphics }
import java.awt.Dimension
import java.awt.geom.Ellipse2D
import java.awt.Graphics2D
import java.awt.geom.Point2D

class NBodiesCanvas() extends Panel {

  var bodiesDetails = List[(Point2D, Double)]()

  override def paintComponent(g: Graphics2D) {
    super.paintComponent(g)

    for (bodyDetails <- bodiesDetails) {
      drawCircle(g, bodyDetails._1.getX(), bodyDetails._1.getY(), bodyDetails._2)
    }
  }

  private def drawCircle(drawer: Graphics2D, xCenter: Double, yCenter: Double, radius: Double) {
    val ellipse = new Ellipse2D.Double(xCenter - radius, yCenter - radius, 2 * radius, 2 * radius);
    drawer.setColor(Color.BLACK)
    drawer.fill(ellipse);
  }

  def setBodiesDetails(bodiesDet: List[(Point2D, Double)]) = bodiesDetails = bodiesDet
}