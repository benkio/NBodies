package it.unibo.pap.nbodies.model.body

import java.awt.geom.Point2D
import akka.actor._
import scala.util.Random

class Body(val coordinate: Point2D) extends Actor {
  var modified = true
  var mass = Random.nextInt(10)
  var radius = () => mass * 10
  
  /**
   * TODO: Implement the receive for the body
   */
  override def receive = {
    case _ =>
  }
}