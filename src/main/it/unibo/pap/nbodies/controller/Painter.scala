package it.unibo.pap.nbodies.controller

import akka.actor.Actor
import akka.actor.ActorDSL
import java.awt.geom.Point2D
import it.unibo.pap.nbodies.model.messages.Messages._
import scala.collection.mutable.ListBuffer
import it.unibo.pap.nbodies.model.BodyDetails
import scala.collection.mutable.MutableList
import it.unibo.pap.nbodies.view.nbodies.NBodiesCanvas
import akka.actor.ActorRef
/**
 * Actor used to manage the paint of the bodies
 */
class Painter(viewCanvas: NBodiesCanvas) extends Actor {

  val canvas = viewCanvas
  var bodiesDetails = scala.collection.mutable.MutableList[(BodyDetails, ActorRef)]()
  /**
   * TODO: Implement
   */
  override def receive = {
    case PaintBody(bodyDetails) => {
      println("NewX: " ++ bodyDetails.coordinate.getX.toString ++ " NewY: " ++ bodyDetails.coordinate.getX().toString ++ " newMass " ++ bodyDetails.mass.toString())
      bodiesDetails = bodiesDetails.filter(bodyDetails => bodyDetails._2 != sender)
      bodiesDetails.+=:((bodyDetails, sender))
    }
    case RemoveBody => {
      bodiesDetails = bodiesDetails.filter(bodyDetails => bodyDetails._2 != sender)
    }
    case SetBodiesDetails(list) => {
      bodiesDetails.clear()
      bodiesDetails = list.to[MutableList]
    }
    case DrawFrame => {
      canvas.setBodiesDetails(bodiesDetails)
      canvas.repaint()
    }
    case Reset => {
      canvas.resetClickPosition
      canvas.resetMouseDragged
      canvas.repaint
    }
  }
}