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
import akka.actor.ActorRef
import scala.collection.mutable.MutableList
import it.unibo.pap.nbodies.model.BodyDetails
import akka.actor.ActorRef
import it.unibo.pap.nbodies.view.ViewConstants
import java.awt.Graphics2D
import it.unibo.pap.nbodies.model.ModelConstants

class NBodiesCanvas() extends Panel {

  var bodiesDetails = MutableList[(BodyDetails, ActorRef)]()
  var imageCenter = new Point2D.Double(0, 0)
  var tempClickPosition = new Point2D.Double(0, 0)
  var viewScale = ViewConstants.defaultScale
  var imageCenterUpdate = false

  override def paintComponent(g: Graphics2D) {

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

    println("bodiesDetails.lenght: " ++ bodiesDetails.length.toString())
    for (bodyDetails <- bodiesDetails) {
      //println("Print " ++ bodyDetails._1.coordinate.getX().toString() ++ ", " ++ bodyDetails._1.coordinate.getY().toString())
      drawCircle(g, bodyDetails._1.coordinate.getX(), bodyDetails._1.coordinate.getY(), bodyDetails._1.radius())
    }
  }

  private def drawCircle(drawer: scala.swing.Graphics2D, xCenter: Double, yCenter: Double, radius: Double) {
    val scaleX = (x: Double) => math.round(((x / viewScale) * ViewConstants.CanvasDimension.getWidth()))
    val scaleY = (x: Double) => math.round(((x / viewScale) * ViewConstants.CanvasDimension.getHeight()))

    println(radius)
    println(viewScale)

    var scaledRadius: Double = 1
    if (scaleX(radius) > 1) { scaledRadius = scaleX(radius) }
    if (!imageCenterUpdate) {
      imageCenterUpdate = true
      imageCenter.y = ViewConstants.CanvasDimension.getHeight() / 2
      imageCenter.x = ViewConstants.CanvasDimension.getWidth() / 2
    }

    val xCoordinate = scaleX(xCenter) + (imageCenter.getX() - (scaledRadius / 2))
    val yCoordinate = ViewConstants.CanvasDimension.getHeight() - (scaleY(yCenter) + (imageCenter.getY() - (scaledRadius / 2)))

    val ellipse = new Ellipse2D.Double(xCoordinate, yCoordinate, 2 * scaledRadius, 2 * scaledRadius)
    println("Print, drawed " ++ xCoordinate.toString ++ ", " ++ yCoordinate.toString ++ " radius: " ++ scaledRadius.toString)
    drawer.setColor(ViewConstants.BodyColor)
    drawer.fill(ellipse)
  }

  def setBodiesDetails(list: MutableList[(BodyDetails, ActorRef)]) = bodiesDetails = list

  def MouseDragged(x: Double, y: Double) = {
    if (tempClickPosition.getX() == 0 || tempClickPosition.getY() == 0) {
      tempClickPosition.x = x
      tempClickPosition.y = y
    } else {
      var distanceXAxe = tempClickPosition.getX() - x;
      var distanceYAxe = tempClickPosition.getY() - y;
      println(distanceXAxe.toString() ++ " " ++ distanceYAxe.toString())
      imageCenter.x += distanceXAxe
      imageCenter.y += distanceYAxe
      tempClickPosition.x = x
      tempClickPosition.y = y
    }
    this.repaint
  }

  def moveCenter(x: Int, y: Int) {
    imageCenterUpdate = true
    imageCenter.x -= x
    imageCenter.y += y
  }

  def clickPositionUpdated(x: Double, y: Double) {
    tempClickPosition.x = x
    tempClickPosition.y = y
  }

  def setViewScale(mouseWheel: Int) {
    println("test wheel")
    viewScale += (mouseWheel * (viewScale / 10))
    repaint
  }

  def resetView = {
    viewScale = ViewConstants.defaultScale
    imageCenterUpdate = false
  }
}