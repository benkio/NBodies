package main.it.unibo.pap.nbodies.controller

import akka.actor.Actor
import akka.actor.ActorDSL
import scala.actors.ActorRef
import java.awt.geom.Point2D
import main.it.unibo.pap.nbodies.view.nbodies.NBodiesCanvas
import main.it.unibo.pap.nbodies.model.messages.Messages._
/**
 * Actor used to manage the paint of the bodies
 */
class Painter(viewCanvas: NBodiesCanvas) extends Actor {

  val canvas = viewCanvas

  /**
   * TODO: Implement
   */
  override def receive = {
    case PaintObj(bodiesDetails) => {
      canvas.setBodiesDetails(bodiesDetails)
      canvas.repaint();
    }
  }
}