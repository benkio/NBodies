package it.unibo.pap.nbodies.view.nbodies

import scala.swing.Panel
import java.awt.{ Color, Graphics }
import java.awt.Dimension
import java.awt.geom.Ellipse2D
import java.awt.Graphics2D
import java.awt.geom.Point2D

class NBodiesCanvas() extends Panel {

  var bodies = List[(Point2D, Double)]()

  override def paintComponent(g: Graphics2D) {

    // Start by erasing this Canvas
    g.clearRect(0, 0, size.width, size.height)

    // Draw things that change on top of background
    for (body <- bodies) {
      g.setColor(Color.BLACK)
      drawCircle(g, body._1.getX(), body._1.getY(), body._2)
    }
  }

  def drawCircle(cg: Graphics2D, xCenter: Double, yCenter: Double, radius: Double) {
    val ellipse = new Ellipse2D.Double(xCenter - radius, yCenter - radius, 2 * radius, 2 * radius);
    cg.draw(ellipse);
  }
  def setBodiesList(bodies: List[(Point2D, Double)]) = this.bodies = bodies
}