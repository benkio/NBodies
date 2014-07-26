package it.unibo.pap.nbodies.model.body

import java.awt.geom.Point2D
import akka.actor._
import scala.util.Random
import java.awt.Point
import it.unibo.pap.nbodies.model.Messages.Messages._

class Body() extends Actor {
  var modified = true
  var mass = Random.nextDouble() * 10
  var radius = () => mass
  val coordinate = new Point2D.Double(Random.nextDouble() * 500, Random.nextDouble() * 500)

  /**
   * TODO: Implement the receive for the body
   */
  override def receive = {
    case GetXCoordinate => sender() ! coordinate.getX()
    case GetYCoordinate => sender() ! coordinate.getY()
    case GetRadius => sender ! radius()
  }
}