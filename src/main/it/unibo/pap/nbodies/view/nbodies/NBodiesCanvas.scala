package it.unibo.pap.nbodies.view.nbodies

import scala.swing.Panel
import java.awt.{ Color, Graphics }
import java.awt.Dimension
import java.awt.geom.Ellipse2D
import java.awt.geom.Point2D
import scala.collection.mutable.ListBuffer
import akka.actor.Actor
import it.unibo.pap.nbodies.model.messages.Messages._
import java.awt.RenderingHints
import scala.swing.Graphics2D

class NBodiesCanvas() extends Panel {

  var bodiesDetails = ListBuffer[(Point2D, Double)]()

  override def paintComponent(g: scala.swing.Graphics2D) {
    super.paintComponent(g)
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    println("bodiesDetails.lenght: " ++ bodiesDetails.length.toString())
    for (bodyDetails <- bodiesDetails) {
      println("Print " ++ bodyDetails._1.getX().toString() ++ ", " ++ bodyDetails._1.getY().toString())
      drawCircle(g, bodyDetails._1.getX(), bodyDetails._1.getY(), bodyDetails._2)
    }
    bodiesDetails.clear()
  }

  private def drawCircle(drawer: scala.swing.Graphics2D, xCenter: Double, yCenter: Double, radius: Double) {
    val ellipse = new Ellipse2D.Double(xCenter - radius, yCenter - radius, 2 * radius, 2 * radius);
    drawer.setColor(Color.BLACK)
    drawer.fill(ellipse);
  }

  def setBodiesDetailsAndPaint(bodiesDet: ListBuffer[(Point2D, Double)]) = {
    bodiesDetails = bodiesDet
    this.peer.repaint()
  }
}