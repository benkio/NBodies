package it.unibo.pap.nbodies.controller

import akka.actor.Actor
import akka.actor.ActorDSL
import scala.actors.ActorRef
import java.awt.geom.Point2D
import it.unibo.pap.nbodies.view.nbodies.NBodiesCanvas
/**
 * Actor used to manage the paint of the bodies
 */
class Painter(viewCanvas: NBodiesCanvas) extends Actor {

  val canvas = viewCanvas

  /**
   * TODO: Implement
   */
  override def receive = {
    case _ =>
  }

  def paint(bodies: List[(Point2D, Double)]) = {
    canvas.setBodiesList(bodies)
    canvas.repaint()
  }
}
